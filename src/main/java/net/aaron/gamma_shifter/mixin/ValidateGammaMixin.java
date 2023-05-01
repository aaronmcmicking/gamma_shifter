package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class ValidateGammaMixin {
        @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
        public void overrideValidation(Double value, CallbackInfoReturnable<Optional<Double>> cir){
            GammaShifter.LOGGER.info("Overriding validation (value = " + value + ")");
            cir.setReturnValue(Optional.of(value));
        }
}
