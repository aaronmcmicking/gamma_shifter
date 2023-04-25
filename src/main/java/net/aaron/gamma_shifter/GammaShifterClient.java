package net.aaron.gamma_shifter;

//import net.aaron.gamma_shifter.HUD.HUD;
import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

/*
	GammaShifterClient

    ClientModInitializer for GammaShifter:
        A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19
*/

public class GammaShifterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}
