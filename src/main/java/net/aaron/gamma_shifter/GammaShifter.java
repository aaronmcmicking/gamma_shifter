package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.config.ConfigLoader;
import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% (1.0) in
 * Minecraft 1.19-1.19.4.
 * <p>This file acts as an entry point for the mod.</p>
 */
public class GammaShifter implements ClientModInitializer {

    /**
     * The mod ID.
     */
    public static final String GAMMA_MOD = "gamma_shifter";

    /**
     * The Logger for log output from all other mod classes.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(GAMMA_MOD);

    /**
     * Records whether the mod is currently enabled/toggled.
     */
    private static boolean enabled = true;

    /**
     * Whether the mod should always start enabled or if it should start in the state it was previously closed in.
     */
    private static boolean alwaysStartEnabled = true;

    /**
     * Silent mode hides all HUD elements. True if silent mode is enabled, false otherwise.
     */
    private static boolean silentModeEnabled = false;

    /**
     * The text colour used in HUD elements.
     */
    private static int textColour = 0xFFFFFF;

    /**
     * Returns whether the mod is toggled on.
     * @return true if the mod is toggled on, false otherwise.
     */
    public static boolean isEnabled(){
        return enabled;
    }

    /**
     * Toggles the mod.
     */
    public static void toggle(){
        enabled = !enabled;
    }

    /**
     * Initializes the mod to be either enabled or disabled.
     * @param value The initial state.
     */
    public static void setEnabled(Boolean value){
        enabled = value;
    }

    /**
     * Returns whether the mod is currently set to always start enabled. Can be edited in the config settings.
     * @return True if the mod always starts enabled, false otherwise.
     * @see ConfigLoader
     */
    public static Boolean alwaysStartEnabled(){
        return alwaysStartEnabled;
    }

    /**
     * Sets whether the mod always starts enabled. Modified in the config menu.
     * @param value Whether the mod should always start enabled.
     * @see ConfigLoader
     */
    public static void setAlwaysStartEnabled(Boolean value){
        alwaysStartEnabled = value;
    }

    /**
     * Sets whether silent mode is enabled or disabled. Silent mode hides all HUD elements of the mod.
     * @param value The new value.
     */
    public static void setSilentModeEnabled(boolean value){
        silentModeEnabled = value;
    }

    /**
     * Gets the current value of silentModeEnabled. Silent mode hides all mod HUD elements.
     * @return True if silent mode is enabled, false otherwise.
     */
    public static boolean isSilentModeEnabled(){
        return silentModeEnabled;
    }

    /**
     * Gets the text colour for HUD elements.
     */
    public static int getTextColour(){ return textColour; }

    /**
     * Sets the text colour for HUD elements.
     */
    public static void setTextColour(int colour){
        textColour = colour;
    }

    /**
     * Starts mod activity once the game loads.
     */
    @Override
    public void onInitializeClient() {
        ConfigLoader.load();
        KeyInputHandler.registerKeyBinds();
    }

}
