package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.HUD.HUD;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects into HUD rendering to call renderer for mod HUD.
 * @see HUD
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("HEAD"))
    public void renderPersistentGammaOverlay(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        if(HUD.shouldShowCurrentGammaOverlay() && !GammaShifter.isSilentModeEnabled()) {
            HUD.renderPersistentOverlay(matrices); // HUD class handles actual rendering
        }
    }
}