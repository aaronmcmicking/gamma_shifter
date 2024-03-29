package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.config.ConfigLoader;
import net.aaron.gamma_shifter.event.Darkness;
import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.aaron.gamma_shifter.event.AutoNight;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% (1.0) in
 * Minecraft 1.19-1.19.4.
 * <p>This file acts as an entry point for the mod and stores mod settings such as {@link GammaShifter#enabled}.</p>
 */
public class GammaShifter implements ClientModInitializer {

    /**
     * The mod ID.
     */
    public static final String GAMMA_MOD = "gamma_shifter";

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
     * True if the custom gamma should be set regardless of if the mod is enabled, false otherwise.
     */
    private static boolean alwaysSaveCustomGamma = true;

    /**
     * Returns whether the mod is toggled on.
     * @return true if the mod is toggled on, false otherwise.
     */
    public static boolean isEnabled(){
        return enabled;
    }

    /**
     * Toggles the mod.
     * <p>Does not configure any other mod properties and therefore should only be called by GammaHandler.</p>
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
     * Sets whether the custom gamma should be saved regardless of whether the mod is enabled or not.
     * @param value True if the custom gamma should always be saved, false otherwise.
     */
    public static void setAlwaysSaveCustomGamma(boolean value){
        alwaysSaveCustomGamma = value;
    }

    /**
     * Gets whether the custom gamma should be saved regardless of whether the mod is enabled or not.
     * @return True if the custom gamma should always be saved, false otherwise.
     */
    public static boolean getAlwaysSaveCustomGamma(){
        return alwaysSaveCustomGamma;
    }

    @Override
    public void onInitializeClient() {
        ConfigLoader.load();
        KeyInputHandler.registerKeyBinds();
        AutoNight.registerAutoNight();
        Darkness.detectDarknessEffect();
    }

}
