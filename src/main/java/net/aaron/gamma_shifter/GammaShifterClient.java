package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

/**
 * A client-side FabricMC mod that restore the ability to set the gamma (brightness) setting beyond 100% (1.0) in
 * Minecraft 1.19-1.19.4.
 * <p>Acts as an entry point for the client-side operations of the mod.</p>
 */
public class GammaShifterClient implements ClientModInitializer {

    /**
     * A helper-class instance that stores and sets gamma values read from options.txt.
     * <p>See also: net.aaron.gamma_shifter.mixin.initGammaMixin</p>
     */
    public static initGammaHelper gammaHelper = new initGammaHelper();

    /**
     * Begins client-side operations once the is initialized.
     */
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }

}
