package net.aaron.gamma_shifter.event;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaPacket;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.config.ConfigScreenBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Handles logic to enable/disable Night Mode during the day/night cycle of Minecraft. Stores a unique custom gamma
 * value and sets it as desired. Class activity is largely disabled if {@link AutoNight#enabled} or {@link AutoNight#isActive}
 * (which are controlled by the config menu and {@link GammaHandler}) are set to false.
 * <p>Unlike other mod features, AutoNight uses pseudo-packets to set gamma values. These allow other classes to distinguish
 * which class they are being called by and act accordingly. </p>
 * <p>AutoNight should never overwrite other classes' fields directly. Instead, whether any functional changes are made is
 * at the discretion methods called by AutoNight. In this way, AutoNight must request changes from other methods and
 * cannot make forceful changes (<b>except as described below</b>), and therefore bugs/unexpected behaviour will often
 * result in AutoNight being disabled by other mod features. </p>
 * <p>The only authority AutoNight exerts is through calls to {@link AutoNight#isActive()}, some other mod features
 * check. However, AutoNight does not control if this is checked or how the result is interpreted, and
 * {@link AutoNight#isActive} can be publicly set at any time.</p>
 * @see GammaPacket
 * @see ConfigScreenBuilder
 * @see net.aaron.gamma_shifter.config.ConfigLoader
 * @see GammaHandler#toggle(GammaPacket)
 */
public class AutoNight {
    /**
     * The gamma value set when AutoNight activates.
     */
    private static double nightGammaValue = 3.0;
    /**
     * Whether the feature is 'enabled' in settings and will activate during night. Enabled does not indicate whether
     * the nightGammaValue is currently applied.
     */
    private static boolean enabled = false;
    /**
     * Whether the feature is currently 'active', ie. whether nightGammaValue is currently applied.
     */
    private static boolean isActive = false;
    /**
     * When the player enters the world or leaves the settings menu are modifying mod settings, the feature checks if
     * night mode needs to be turned on. This field indicates that this has already be completed to avoid repetitive
     * actions whenever the game menu is closed.
     */
    private static boolean alreadyInitialized = false;
    /**
     * Constants for the time when night and morning start (in ticks).
     */
    private static final long NIGHT_START_TIME = 12400;
    private static final long MORNING_START_TIME = 23500;

    /**
     * Enums to indicate whether it is currently day or night in the world.
     */
    private enum TimeState{
        DAY,
        NIGHT
    }

    /**
     * Checks every tick if auto night mode should be enabled/disabled and sends appropriate GammaPackets to
     * GammaHandler as needed. Only checks for hardcoded day-night times ({@link AutoNight#MORNING_START_TIME},
     * {@link AutoNight#NIGHT_START_TIME}), and so this method will not change state if these values are not passed.
     * In missed cases, such as the player joining the world in the middle of the night, other methods are responsible
     * for updating state.
     * <p>The GammaPacket includes info that AutoNight has sent the packet, and therefore the
     * receiver is allowed to discriminate against/ignore the enclosed value.</p>
     * @see AutoNight#initializeAutoNightStatus()
     * @see GammaPacket
     */
    public static void registerAutoNight(){
        ClientTickEvents.END_CLIENT_TICK.register(listener -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(enabled && client != null && client.world != null){ // autoNight is enabled and player is in world
                long time = client.world.getTimeOfDay() % 24000; // mod by ticks-per-day to normalize time
                RegistryKey<World> dimension = client.world.getRegistryKey();

                if(time == NIGHT_START_TIME && dimension.equals(World.OVERWORLD)) { // night start
                    if(!GammaShifter.isEnabled()){
                        isActive = true;
                        GammaHandler.toggle(new GammaPacket(nightGammaValue, GammaPacket.Sender.AUTO_NIGHT));
                    }
                }else if(time == MORNING_START_TIME || !dimension.equals(World.OVERWORLD)){ // day start
                    if(GammaShifter.isEnabled() && AutoNight.isActive()){
                        isActive = false;
                        GammaHandler.toggle(new GammaPacket(1.0, GammaPacket.Sender.AUTO_NIGHT)); // I think this packet gets ignored
                    }
                }
            }
        });
    }

    public static void disableOnWakeUp(){
        if(AutoNight.isActive() && GammaShifter.isEnabled()){
            GammaHandler.toggle();
            isActive = false; // must come after toggle() call since toggle() would otherwise overwrite GammaHandler.currentCustomGamma
        }
    }

    /**
     * Checks if auto night mode should be enabled when the player 1) joins the world from the main menu, 2) closes
     * the pause menu after changing settings in ModMenu.
     * <p>Only runs if {@link AutoNight#alreadyInitialized} is false and always sets it to true after making any
     * functional changes (log output may still be generated).</p>
     */
    public static void initializeAutoNightStatus(){
        // preconditions
        if(!enabled || alreadyInitialized){
            return;
        }

        // more preconditions
        MinecraftClient client = MinecraftClient.getInstance();
        if(client == null || client.world == null){
            GammaShifter.LOGGER.error("[GammaShifter] Incorrectly trying to initialize AutoNight state when the client instance or world was null");
            return;
        }

        TimeState timeState = getTimeState(client.world);

        // turn on when night begins
        if(timeState == TimeState.NIGHT && client.world.getRegistryKey().equals(World.OVERWORLD)){
            if(!GammaShifter.isEnabled()){
                isActive = true;
                GammaHandler.toggle(new GammaPacket(nightGammaValue, GammaPacket.Sender.AUTO_NIGHT));
            }
        }
        alreadyInitialized = true; // set to true regardless of if a new gamma value was set
    }

    /**
     * Checks whether the current world is in nighttime or daytime, as defined by {@link AutoNight#MORNING_START_TIME}
     * and {@link AutoNight#NIGHT_START_TIME} and returns the value as an enum.
     * @param world The world to check the time on.
     * @return The current state of the time (NIGHT, DAY, etc).
     * @see TimeState
     */
    private static TimeState getTimeState(@NotNull ClientWorld world){
        long time = world.getLevelProperties().getTimeOfDay() % 24000; // mod by ticks-per-day to normalize time
        if(time > NIGHT_START_TIME && time < MORNING_START_TIME){
            return TimeState.NIGHT;
        }
        return TimeState.DAY;
    }

    /**
     * Updates the applied auto night mode gamma value by toggling night mode off and on. The caller is responsible
     * for ensuring that the night mode is active before attempting to update the applied value.
     */
    public static void update(){
        GammaHandler.toggle();
        GammaHandler.toggle(new GammaPacket(nightGammaValue, GammaPacket.Sender.AUTO_NIGHT));
    }

    /**
     * Gets the current gamma value applied when auto night mode activates.
     * @return The current night mode gamma value.
     */
    public static double getNightGammaValue(){
        return nightGammaValue;
    }

    /**
     * Sets the gamma value that will be applied when night mode turns on.
     * @param value The new gamma value to apply.
     */
    public static void setNightGammaValue(double value){
        // clamp to max/min gamma
        if(value <= GammaHandler.MAX_GAMMA && value >= GammaHandler.MIN_GAMMA){
            nightGammaValue = value;
        }else{
            nightGammaValue = 2.5;
        }
    }

    /**
     * Whether auto night mode will turn itself on/off at dawn/dusk. Does not indicate whether the night mode gamma
     * is currently applied.
     * @return True if the feature is enabled, false otherwise.
     */
    public static boolean isEnabled(){
        return enabled;
    }

    /**
     * Sets whether auto night mode will turn itself on/off at dawn/dusk. Does not directly cause any change the applied
     * gamma value.
     * @param value True if the feature should be enabled, false otherwise.
     */
    public static void setEnabled(boolean value){
        enabled = value;
    }

    /**
     * Whether AutoNight is currently controlling the applied gamma value.
     * @return True if AutoNight is currently activated, false otherwise.
     */
    public static boolean isActive(){
        return isActive;
    }

    /**
     * Sets whether AutoNight should be controlling the applied gamma value.
     * @param value True if AutoNight should be activated, false otherwise.
     */
    public static void setIsActive(boolean value){
        isActive = value;
    }

    /**
     * Returns whether AutoNight has updated it's state after 1) the player enters a world from the main menu 2)
     * the pause menu is closed after settings are changed in ModMenu.
     * @return True if NightMode has initialized itself, false otherwise.
     */
    public static boolean alreadyInitialized(){
        return alreadyInitialized;
    }

    public static void setAlreadyInitialized(boolean value){
        alreadyInitialized = value;
    }
}
