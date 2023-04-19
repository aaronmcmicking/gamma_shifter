package net.aaron.gamma_shifter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SimpleOption.class)

public class GammaMixin {
    MinecraftClient mc = MinecraftClient.getInstance();

//    @Inject(method = "Lnet/minecraft/client/option/SimpleOption;setValue(Ljava/lang/Object;)V", at = @At("TAIL"), cancellable = false)
//    public void setValue(Object new_value, CallbackInfo ci){
//
//        GammaShifter.LOGGER.info("setValue() called");
//        SimpleOption<?> gamma = (SimpleOption<?>) (Object) this;
//
//        if(gamma.equals(mc.options.getGamma())){
//            ((OptionValueAccessor)gamma).setOptionValue(new_value);
//            GammaShifter.LOGGER.info("modified gamma value");
//        }
//    }

    @ModifyVariable(method = "setValue(Ljava/lang/Object;)V", at = @At("STORE"), ordinal = 0, argsOnly = true)
    public Object setGammaValue(Object object){
        SimpleOption<?> gamma = (SimpleOption<?>) (Object) this;
        if(gamma.equals(mc.options.getGamma())){
            return (Double)object + .2;
        }
        return object;
    }


}
