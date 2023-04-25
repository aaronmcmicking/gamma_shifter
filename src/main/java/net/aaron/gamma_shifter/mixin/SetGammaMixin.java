package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/*
    SetGammaMixin

    Fabric mixin that bypasses the validation process for changes to the gamma setting, allowing values higher than 1.0
    Injects into SimpleOption.setValue() and uses an Accessor to manually set the gamma value
 */
@Mixin(SimpleOption.class)
public abstract class SetGammaMixin {
//    @SuppressWarnings("unchecked") // removes warning for unchecked typecast of 'this' to 'SimpleOptionAccessor<>'
    @Inject(method = "setValue(Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    public void setValueInjected(Object value, CallbackInfo ci){
        SimpleOption<?> gamma = (SimpleOption<?>) (Object) this;
        MinecraftClient mc = MinecraftClient.getInstance();

//        if(gamma == null){
//            GammaShifter.LOGGER.error("( (SimpleOption<?>) (Object) this ) == null");
//            return;
//        }else if(mc == null){
//            GammaShifter.LOGGER.error("MinecraftClient.getInstance() returned null");
//            return;
//        }else if(mc.options == null){
//            GammaShifter.LOGGER.error("MinecraftClient.getInstance().options == null");
//            return;
//        }else if(mc.options.getGamma() == null){
//            GammaShifter.LOGGER.error("MinecraftClient.getInstance().options.getGamma() returned null");
//            return;
//        }

        try {
            if(mc.options != null) {
                if (gamma.equals(mc.options.getGamma())) {
                    try {
                        ((SimpleOptionAccessor<Object>) this).setValue(value); // setValue method created with accessor mixin
                        ci.cancel();
                        GammaShifter.LOGGER.info("Cancelling setValue to override value checking");
                    } catch (NullPointerException e) {
                        GammaShifter.LOGGER.error("((SimpleOptionAccessor<Object>) this).setValue(value) threw NullPointerException");
                    }
                }
            }
        }catch(NullPointerException e){
            GammaShifter.LOGGER.error("gamma.equals(mc.options.getGamma()) threw NullPointerException");
        }
    }

}