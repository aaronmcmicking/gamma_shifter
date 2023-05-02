package net.aaron.gamma_shifter;

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
     * Starts mod activity once the game loads.
     */
    @Override
    public void onInitializeClient() {
        KeyInputHandler.registerKeyBinds();
    }

}
