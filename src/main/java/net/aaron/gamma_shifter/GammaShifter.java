package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.config.Config;
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

    private static boolean alwaysStartEnabled = true;

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
     * @see Config
     */
    public static Boolean alwaysStartEnabled(){
        return alwaysStartEnabled;
    }

    /**
     * Sets whether the mod always starts enabled. Modified in the config menu.
     * @param value Whether the mod should always start enabled.
     * @see Config
     */
    public static void setAlwaysStartEnabled(Boolean value){
        alwaysStartEnabled = value;
    }

    /**
     * Starts mod activity once the game loads.
     */
    @Override
    public void onInitializeClient() {
        Config.load();
        KeyInputHandler.registerKeyBinds();
    }

}
