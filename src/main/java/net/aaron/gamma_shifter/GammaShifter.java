package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
	GammaShifter

	ModInitializer for GammaShifter:
		A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19.2
 */

public class GammaShifter implements ModInitializer {
	public static final String GAMMA_MOD = "gamma_shifter";
	public static final Logger LOGGER = LoggerFactory.getLogger(GAMMA_MOD);

	@Override
	public void onInitialize() {
	}
}
