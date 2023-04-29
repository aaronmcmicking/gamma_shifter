package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.GammaShifterClient;
import net.aaron.gamma_shifter.initGammaHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Spongepowered mixin signals to {@link initGammaHelper} to set the gamma file read from options.txt when the title screen loads for the first time.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    /**
     * Signals to set the gamma value read from options.txt on initialization when the title screen loads for the
     * first time.
     * @see initGammaHelper
     * @param screen The Screen to change to (nullable).
     * @param ci     CallbackInfo to return.
     */
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreenGammaInject(Screen screen, CallbackInfo ci) {
        if (!GammaShifterClient.gammaHelper.alreadyDone()) {
            GammaShifterClient.gammaHelper.setInitialGamma();
        }
//        else{
//            if(screen.equals(new GameMenuScreen()));
//        }
    }

    @Shadow
    public Screen currentScreen;

    /**
     * Saves the game options to options.txt whenever the pause menu is opened. The player must not be null (ie. must be
     * in a singleplayer/multiplayer world) to avoid running continuously on other menus.
     * @param ci CallbackInfo to be returned.
     */
    @Inject(method = "openPauseMenu", at = @At("HEAD"))
    public void saveOptionsOnPauseMenu(boolean pause, CallbackInfo ci){
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc != null && mc.options != null && mc.player != null && this.currentScreen == null){
            mc.options.write();
            GammaShifter.LOGGER.info("Saved options");
        }
    }
}
