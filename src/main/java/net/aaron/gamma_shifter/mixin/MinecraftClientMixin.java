package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifterClient;
import net.aaron.gamma_shifter.initGammaHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Spongepowered mixin signals to {@link initGammaHelper} to set the gamma file read from options.txt when the title screen loads for the first time.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    /**
     * Injects into {@link MinecraftClient#setScreen(Screen)} and signals to set the initial gamma value.
     * @param screen The Screen to change to (nullable).
     * @param ci CallbackInfo to return.
     */
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreenGammaInject(Screen screen, CallbackInfo ci){
            if (!GammaShifterClient.gammaHelper.alreadyDone()) {
                GammaShifterClient.gammaHelper.setGamma();
        }
    }
}
