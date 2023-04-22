package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import org.slf4j.event.EventRecodingLogger;
import org.slf4j.helpers.SubstituteLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubstituteLogger.class)
public class LoggerInterruptMixin {
    @Inject(method = "error(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true, remap = false)
    public void LoggerInterruptInject(String msg, CallbackInfo ci){
//        GammaShifter.LOGGER.info("[GammaShifter] Injected into EventRecodingLogger.error at HEAD");
        GammaShifter.LOGGER.info("[GammaShifter] Injected into SubstitutionLogger.error at HEAD");
//        if(msg.contains("Brightness") && msg.contains("outside of range")){
//            ci.cancel();
//        }
    }
}
