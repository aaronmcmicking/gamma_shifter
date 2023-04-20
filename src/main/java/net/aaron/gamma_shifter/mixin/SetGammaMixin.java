package net.aaron.gamma_shifter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Mixin injection that bypasses the validation process for changes to the gamma setting, allowing values higher than the game normally allows

Injects into the setValue method of the SimpleOption class.
 */
@Mixin(SimpleOption.class)
public abstract class SetGammaMixin {
    @SuppressWarnings("unchecked") // removes warning for unchecked typecast of 'this' to 'SimpleOptionAccessor<>'
    @Inject(method = "setValue(Ljava/lang/Object;)V", at = @At("RETURN"), cancellable = false)
    public void setValueInjected(Object value, CallbackInfo ci){
        SimpleOption<?> gamma = (SimpleOption<?>) (Object) this;
        MinecraftClient mc = MinecraftClient.getInstance();

        if(gamma.equals(mc.options.getGamma())){
            ((SimpleOptionAccessor<Object>) this).setValue(value); // setValue method created with accessor mixin
        }
    }

}