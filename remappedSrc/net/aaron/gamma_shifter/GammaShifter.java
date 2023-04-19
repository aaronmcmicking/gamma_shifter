package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GammaShifter implements ModInitializer {
	public static final String GAMMA_MOD = "gamma_shifter";
	public static final Logger LOGGER = LoggerFactory.getLogger(GAMMA_MOD);

	private static MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitialize() {
	}
}
