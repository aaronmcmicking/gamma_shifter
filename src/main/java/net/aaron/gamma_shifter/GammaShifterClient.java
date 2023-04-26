package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

/*
	GammaShifterClient

    ClientModInitializer for GammaShifter:
        A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19
*/

public class GammaShifterClient implements ClientModInitializer {

    public static initGammaHelper gammaHelper = new initGammaHelper();

    @Override
    public void onInitializeClient() {
//        gammaHelper.setValue();
        KeyInputHandler.register();
    }

}
