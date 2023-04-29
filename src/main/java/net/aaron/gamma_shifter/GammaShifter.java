package net.aaron.gamma_shifter;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client-side FabricMC mod that restore the ability to set the gamma (brightness) setting beyond 100% (1.0) in
 * Minecraft 1.19-1.19.4.
 * <p>This file acts as an entry point for the mod.</p>
 */
public class GammaShifter implements ModInitializer {
	/**
	 * The internal name of the mod.
	 */
	public static final String GAMMA_MOD = "Gamma Shifter";

	/**
	 * The Logger for log output from all other mod classes.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(GAMMA_MOD);

	/**
	 * The game version.
	 */
	private static final String version = "1.19.4";

	/**
	 * Records whether the mod is currently enabled/toggled.
	 */
	private static boolean toggled = true;

	/**
	 * Empty method run when the mod is initialized.
	 */
	@Override
	public void onInitialize() {
	}

	/**
	 * Returns whether the mod is toggled on.
	 * @return true if the mod is toggled on, false otherwise.
	 */
	public static boolean isToggled(){
		return toggled;
	}

	/**
	 * Toggles the mod.
	 */
	public static void toggle(){
		toggled = !toggled;
	}

	/**
	 * Returns the current game version as a String.
	 * @return The current game version.
	 */
	public static String getVersion(){ return version; }
}
