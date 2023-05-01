package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.InitGammaHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Spongepowered mixin that:
 * <p>1. Signals to {@link InitGammaHelper} to set the gamma file read from options.txt when the title screen loads for the first time.</p>
 * <p>2. Saves the options to disk when the pause menu is opened while the player is in a world.</p>
 * <p>3. Saves the options when the game closes.</p>
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    /**
     * This injection either signals to set the gamma value read from options.txt on initialization when the title screen loads for the
     * first time, or saves the options to disk when the pause menu is opened.</p>
     *
     * @param screen The Screen to change to (nullable).
     * @param ci     CallbackInfo to return.
     * @see InitGammaHelper
     */
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreenGammaInject(Screen screen, CallbackInfo ci) {
        if (!GammaHandler.initHelper.alreadyDone()) { // if setting the initial value from options.txt
            GammaHandler.initHelper.setInitialGamma();
        } else {
            // if saving options while player is playing
            if ((screen instanceof GameMenuScreen) && (MinecraftClient.getInstance() != null)  &&  (MinecraftClient.getInstance().player != null)) {
                MinecraftClient.getInstance().options.write();
                GammaShifter.LOGGER.info(("Saved options"));
            }
        }
    }

    /**
     * Saves the options when the game closes.
     * @param ci CallbackInfo to be returned.
     */
    @Inject(method = "stop", at = @At("HEAD"))
    public void saveOnClose(CallbackInfo ci){
        MinecraftClient.getInstance().options.write();
        GammaShifter.LOGGER.info(("Saved options"));
    }
}