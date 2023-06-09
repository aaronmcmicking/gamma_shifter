package net.aaron.gamma_shifter.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Spongepowered mixin that enables out-of-range gamma values to be set by bypassing range checks.
 */
@Mixin(SimpleOption.class)
public class SimpleOptionMixin {

    /**
     * Shadow placeholder for {@link SimpleOption#text}.
     */
    @Final
    @Shadow
    Text text;

    /**
     * Replaces the existing codec for gamma with a valid codec so that the game does not realize gamma values are
     * out-of-range. Enables {@link GameOptions#load()} to save gamma values >1.0 to options.txt and suppresses error messages.
     */
    @Inject(method = "getCodec", at = @At("HEAD"), cancellable = true)
    public void replaceCodec(CallbackInfoReturnable<Codec<Double>> cir){
        String gammaTranslation = Language.getInstance().get("options.gamma"); // gets translated name
        if(this.text.getString().equals(gammaTranslation)){
            cir.setReturnValue(Codec.DOUBLE);
        }
    }

    /**
     * Replaces the empty Optional for out-of-range gamma values with an existing Optional of that value.
     * Enables {@link SimpleOption#setValue(Object)} to set gamma values >1.0.
     * <p>Currently cannot discriminate between gamma and other options, and so any option using
     * {@link net.minecraft.client.option.SimpleOption.DoubleSliderCallbacks} will have it's validation replaced
     * (since 0.1.0)</p>
     */
    @Mixin(SimpleOption.DoubleSliderCallbacks.class)
    public static class DoubleSliderCallbacksMixin {
        @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
        public void replaceValidator(Double value, CallbackInfoReturnable<Optional<Double>> cir){
            cir.setReturnValue(Optional.of(value));
        }
    }
}
