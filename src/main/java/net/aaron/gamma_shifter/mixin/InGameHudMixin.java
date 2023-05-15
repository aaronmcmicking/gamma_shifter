package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("HEAD"))
    public void renderPersistentGammaOverlay(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        if(GammaHandler.shouldShowCurrentGammaOverlay() && !GammaShifter.isSilentModeEnabled()) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            matrices.push();
            textRenderer.drawWithShadow(matrices, GammaHandler.getDisplayGammaMessage(), 2.0f, 2.0f, GammaShifter.getTextColour());
            matrices.pop();
        }
    }
}