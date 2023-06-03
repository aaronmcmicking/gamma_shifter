package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.HUD.HUD;
import net.aaron.gamma_shifter.event.AutoNight;
import net.aaron.gamma_shifter.event.Darkness;
import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.jetbrains.annotations.NotNull;

/**
 * Handles calculating and applying gamma values. Also stores default values, such as {@link GammaHandler#MAX_GAMMA} and
 * {@link GammaHandler#MIN_GAMMA}, and setting fields such as {@link GammaHandler#snappingEnabled}. Does not handle
 * saving or loading of config file, keypresses, etc.
 */
public class GammaHandler {

    /**
     * The current instance of MinecraftClient, wrapped to improve readability.
     */
    private static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * The maximum gamma value the mod will set.
     */
    public static final Double MAX_GAMMA = 20.0;

    /**
     * The minimum gamma value the mod will set.
     */
    public static final Double MIN_GAMMA = 0.0;

    /**
     * The amount to change the gamma value by per input received. Can be modified by user in ModMenu settings.
     */
    public static Double changePerInput = 0.25;

    /**
     * Stores the current gamma value for when the mod is toggled off. Initially set when the gamma value read from
     * options.txt is read.
     * <p>This value will  not always be the same as {@link GameOptions#getGamma()} when the mod is enabled.</p>
     * <p>NOTE: Does not track gamma values gamma values set in AutoNight Mode (ANM). In ANM, a local value is set
     * instead, but the previous custom gamma is still saved here.</p>
     * @see net.aaron.gamma_shifter.event.AutoNight
     */
    public static Double currentCustomGamma = 20.0;

    /**
     * Stores whether changes in gamma value will be snapped to the nearest multiple of {@link GammaHandler#changePerInput}.
     */
    public static boolean snappingEnabled = true;

    /**
     * Whether gamma values should be clamped to {@link GammaHandler#MIN_GAMMA} and {@link GammaHandler#MAX_GAMMA}.
     */
    public static boolean shouldEnforceBounds = true;

    /**
     * The values of the custom preset keys. Initially these keys are unbound. They are initialized
     * by {@link net.aaron.gamma_shifter.config.ConfigLoader}.
     */
    public static double presetOne = 2.5;
    public static double presetTwo = 5.0;

    /**
     * Handles increasing the gamma. Behaves as a wrapper for helper methods to calculate and set gamma and display
     * information to the user.
     * @see GammaHandler#calculateGamma(Double, boolean)
     * @see GammaHandler#set(Double)
     * @see HUD#displayGammaMessage()
     */
    public static void increaseGamma(){
        double newGamma = calculateGamma(client.options.getGamma().getValue(), true);
        set(newGamma);
        HUD.displayGammaMessage();
    }

    /**
     * Handles decreasing the gamma. Behaves as a wrapper for helper methods to calculate and set gamma and display
     * information to the user.
     * @see GammaHandler#calculateGamma(Double, boolean)
     * @see GammaHandler#set(Double)
     * @see HUD#displayGammaMessage()
     */
    public static void decreaseGamma(){
        double newGamma = calculateGamma(client.options.getGamma().getValue(), false);
        set(newGamma);
        HUD.displayGammaMessage();
    }

    /**
     * Determines how the gamma will be calculated based off the current gamma, the current step value, and other settings.
     * Wraps other calculating methods so that the caller need not be concerned with how the gamma is calculated.
     * @param oldGamma The previous gamma value.
     * @param increasing True if the gamma should be increased, false otherwise.
     * @return The new gamma value.
     */
    public static Double calculateGamma(Double oldGamma, boolean increasing){
        if(shouldEnforceBounds()) {
            if (increasing && (MAX_GAMMA - oldGamma) <= changePerInput) { // if increasing would exceed max gamma
                KeyInputHandler.flushBufferedInputs();
                return MAX_GAMMA;
            } else if (!increasing && oldGamma <= changePerInput) { // if decreasing would be lower that min gamma
                KeyInputHandler.flushBufferedInputs();
                return MIN_GAMMA;
            }
        }
        return isSnappingEnabled() ? calculateGammaWithSnapping(oldGamma, increasing) : calculateGammaSimple(oldGamma, increasing);
    }

    /**
     * Calculates a new gamma value and snaps it to the nearest multiple of {@link GammaHandler#changePerInput}.
     * <p>The caller is responsible for ensuring that adding/subtracting {@link GammaHandler#changePerInput} from
     * the current gamma will not result in a value outside {@link GammaHandler#MIN_GAMMA} and
     * {@link GammaHandler#MAX_GAMMA}.</p>
     * @param oldGamma The previous gamma value.
     * @param increasing True if the gamma should be increased, false otherwise.
     * @return The new gamma value.
     */
    private static Double calculateGammaWithSnapping(Double oldGamma, boolean increasing){
        double newGamma;
        oldGamma *= 100;
        double changePerInput = GammaHandler.changePerInput * 100;
        int oldGammaModChangePerInput = (int)Math.round((oldGamma)) % (int)Math.round((changePerInput));
        if(increasing){
            newGamma = Math.round( oldGamma + changePerInput - oldGammaModChangePerInput ) / 100.0;
        }else{
            newGamma = oldGammaModChangePerInput==0 ? (oldGamma)-(changePerInput) : (oldGamma)-oldGammaModChangePerInput;
            newGamma = Math.round( newGamma ) / 100.0;
        }
        return newGamma;
    }

    /**
     * Calculates the new gamma value without snapping it to a particular value.
     * <p>The caller is responsible for ensuring that adding/subtracting {@link GammaHandler#changePerInput} from
     * the current gamma will not result in a value outside {@link GammaHandler#MIN_GAMMA} and
     * {@link GammaHandler#MAX_GAMMA}.</p>
     * @param oldGamma The previous gamma value.
     * @param increasing True if the gamma should be increased, false otherwise.
     * @return The new gamma value.
     */
    public static Double calculateGammaSimple(Double oldGamma, boolean increasing){
        oldGamma = Math.round(oldGamma * 100) / 100.0;
        oldGamma += increasing ? changePerInput : -changePerInput;
        return Math.round(oldGamma * 100) / 100.0;
    }

    /**
     * Wrapper method for setting gamma in the Minecraft settings and in {@link GammaHandler#currentCustomGamma}.
     * @param value The gamma value to set.
     */
    public static void set(Double value){
        AutoNight.setIsActive(false);
        client.options.getGamma().setValue(value);
        currentCustomGamma = value;
    }

    /**
     * Toggles the mod effects on/off by setting gamma to either 0.0 or {@link GammaHandler#currentCustomGamma} and
     * toggling {@link GammaShifter}.
     * <p>Also stores the latest gamma value locally (fixes compatibility issue with Sodium), unless Auto Night Mode
     * is active.</p>
     * @see AutoNight
     */
    public static void toggle(){
        // make sure the latest gamma value is stored locally before overwriting it
        if(GammaShifter.isEnabled()) {
            if (AutoNight.isActive()) {
                AutoNight.setIsActive(false);
            } else {
                currentCustomGamma = client.options.getGamma().getValue();
            }
        }else{
            Darkness.cancel();
        }
        double gamma = GammaShifter.isEnabled() ? 1.0 : currentCustomGamma;
        client.options.getGamma().setValue(gamma);
        GammaShifter.toggle();
    }

    /**
     * Toggling method that accepts a new gamma value to apply from a GammaPacket. Uses caller information from the
     * packet to control setting the applied gamma and {@link GammaHandler#currentCustomGamma}, as well as modifying
     * the state of {@link AutoNight}.
     * @param packet The GammaPacket containing the gamma value and sender information.
     * @see GammaPacket
     */
    public static void toggle(@NotNull GammaPacket packet){
        if(GammaShifter.isEnabled()){
            if(packet.sender().isPresent()){
                if(packet.sender().get() != GammaPacket.Sender.AUTO_NIGHT){
                    currentCustomGamma = client.options.getGamma().getValue();
                }
            }
            client.options.getGamma().setValue(1.0);
        }else{
            if(packet.sender().isPresent() && packet.sender().get() == GammaPacket.Sender.AUTO_NIGHT){
                AutoNight.setIsActive(true);
            }
            client.options.getGamma().setValue(packet.value);
        }
        GammaShifter.toggle();
    }

    /**
     * Sets the gamma to the maxiumum allowed value.
     */
    public static void applyMaxGamma(){
        set(MAX_GAMMA);
        HUD.displayGammaMessage();
    }

    /**
     * Sets the gamma to the default maximum value (100%).
     */
    public static void applyVanillaMaxGamma() {
        set(1.0); // Default MC max gamma
        HUD.displayGammaMessage();
    }

    /**
     * Applies the value of {@link GammaHandler#presetOne} to the game.
     */
    public static void applyPresetOne(){
        set(presetOne);
        HUD.displayGammaMessage();
    }

    /**
     * Applies the value of {@link GammaHandler#presetTwo} to the game.
     */
    public static void applyPresetTwo(){
        set(presetTwo);
        HUD.displayGammaMessage();
    }

    /**
     * Gets the current custom gamma.
     * @return The current custom gamma.
     */
    public static Double getCurrentCustomGamma() {
        return currentCustomGamma;
    }

    /**
     * Sets {@link GammaHandler#currentCustomGamma} manually. Should only be called when the game is initializing.
     * <p>Otherwise, {@link GammaHandler#currentCustomGamma} should only be set in conjunction with a call to
     * {@link net.minecraft.client.option.SimpleOption#setValue(Object)}, OR when being updated by ModMenu.</p>
     * @param value The value to set.
     */
    public static void setCurrentCustomGamma(Double value){
        currentCustomGamma = value;
    }

    /**
     * Get the amount to change the gamma by per input.
     * @return The change-per-input.
     */
    public static Double getChangePerInput(){
        return changePerInput;
    }

    /**
     * Sets the amount to change the gamma by per input.
     * @param value The new change-per-input to set.
     */
    public static void setChangePerInput(Double value){
        changePerInput = value;
    }

    /**
     * Gets whether new gamma values should be snapped to the nearest multiple of {@link GammaHandler#changePerInput}.
     * @return True if snapping is enabled, false otherwise.
     */
    public static boolean isSnappingEnabled() {
        return snappingEnabled;
    }

    /**
     * Sets whether new gamma values should be snapped to the nearest multiple of {@link GammaHandler#changePerInput}.
     * <p>Set true if values should be snapped, false otherwise.</p>
     */
    public static void setSnappingEnabled(boolean enabled) {
        snappingEnabled = enabled;
    }

    /**
     * Gets the value of {@link GammaHandler#presetOne}.
     * @return The value of {@link GammaHandler#presetOne}.
     */
    public static double getPresetOne(){
        return presetOne;
    }

    /**
     * Sets the value of {@link GammaHandler#presetOne}.
     * @param value The new value.
     */
    public static void setPresetOne(double value){
        presetOne = value;
    }

    /**
     * Gets the value of {@link GammaHandler#presetTwo}.
     * @return The value of {@link GammaHandler#presetTwo}.
     */
    public static double getPresetTwo(){
        return presetTwo;
    }

    /**
     * Sets the value of {@link GammaHandler#presetTwo}.
     * @param value The new value.
     */
    public static void setPresetTwo(double value){
        presetTwo = value;
    }

    /**
     * Get the value of {@link GammaHandler#shouldEnforceBounds}.
     * @return True if {@link GammaHandler#MIN_GAMMA} and {@link GammaHandler#MAX_GAMMA} should be enforced, false otherwise.
     */
    public static boolean shouldEnforceBounds(){
        return shouldEnforceBounds;
    }

    /**
     * Sets the value of {@link GammaHandler#shouldEnforceBounds}.
     * @param value True if {@link GammaHandler#MIN_GAMMA} and {@link GammaHandler#MAX_GAMMA} should be enforced, false otherwise.
     */
    public static void setShouldEnforceBounds(boolean value){
        shouldEnforceBounds = value;
    }

}
