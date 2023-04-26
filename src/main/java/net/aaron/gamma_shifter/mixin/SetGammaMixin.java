package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
    SetGammaMixin

    Spongepowered mixin that bypasses the validation process for changes to the gamma setting, allowing values higher than 1.0
    Injects into SimpleOption.setValue() and uses an Accessor to manually set the gamma value
 */
@Mixin(SimpleOption.class)
public abstract class SetGammaMixin {
    @SuppressWarnings("unchecked") // removes warning for unchecked typecast of 'this' to 'SimpleOptionAccessor<>'
    @Inject(method = "setValue(Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    public void setValueInjected(Object value, CallbackInfo ci){
        SimpleOption<?> gamma = (SimpleOption<?>) (Object) this; // cast this to SimpleOption since this will be injected into SimpleOption.class
        MinecraftClient mc = MinecraftClient.getInstance();

        try {
            if (mc.options != null && gamma.equals(mc.options.getGamma())) {
                ((SimpleOptionAccessor<Object>) this).setValue(value); // setValue method created with accessor mixin
                ci.cancel(); // cancel the targeted method to prevent it overwriting the value
            }
        }catch(NullPointerException e){
            GammaShifter.LOGGER.error("Couldn't set new value for gamma: " + e);
        }
    }

}