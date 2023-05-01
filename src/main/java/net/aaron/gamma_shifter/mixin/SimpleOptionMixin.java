package net.aaron.gamma_shifter.mixin;

import com.mojang.serialization.Codec;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin {

    @Final
    @Shadow
    Text text;

    @Inject(method = "getCodec", at = @At("HEAD"), cancellable = true)
    public void falsifyCodec(CallbackInfoReturnable<Codec<Double>> cir){
        if(this.text.getString().equalsIgnoreCase("brightness")){
            cir.setReturnValue(Codec.DOUBLE);
        }
    }
    @Mixin(SimpleOption.DoubleSliderCallbacks.class)
    public static class DoubleSliderCallbacksMixin {
        @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
        public void falsifyValidation(Double value, CallbackInfoReturnable<Optional<Double>> cir){
            GammaShifter.LOGGER.info("Overriding validation (value = " + value + ")");
            cir.setReturnValue(Optional.of(value));
        }
    }


}
