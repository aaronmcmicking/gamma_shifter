package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
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
	public static final String GAMMA_MOD = "gamma_shifter";

	/**
	 * The Logger for log output from all other mod classes.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(GAMMA_MOD);

	/**
	 * Records whether the mod is currently enabled/toggled.
	 */
	public static boolean toggled = true;

	/**
	 * Empty method run when the mod is initialized.
	 */
	@Override
	public void onInitialize() {
	}

	public static boolean isToggled(){
		return toggled;
	}

	public static void toggle(){
		toggled = !toggled;
	}
}
