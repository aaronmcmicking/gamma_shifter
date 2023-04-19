package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class GammaShifterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}
