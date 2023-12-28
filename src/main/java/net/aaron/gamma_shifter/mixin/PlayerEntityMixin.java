package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.event.AutoNight;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow
    private int sleepTimer;

    @Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"))
    public void wakeUpInject(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
        if(sleepTimer == 100){ // 100 means the night has been skipped
            AutoNight.disableOnWakeUp();
        }
    }
}
