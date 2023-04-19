package net.aaron.gamma_shifter;

//import net.aaron.gamma_shifter.HUD.HUD;
import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class GammaShifterClient implements ClientModInitializer {

//    public static HUD hud = new HUD();

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}
