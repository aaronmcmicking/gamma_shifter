package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifterClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
    MinecraftClientMixin

    Spongepowered mixin detects when the title screen first loads and signals to write an initial gamma value
 */

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreenGammaInject(Screen screen, CallbackInfo ci){
            if (!GammaShifterClient.gammaHelper.alreadyDone()) {
                GammaShifterClient.gammaHelper.setGamma();
//                GammaShifter.LOGGER.info("calling GammaShifterClient.gammaHelper.setValue()");
        }
    }
}
