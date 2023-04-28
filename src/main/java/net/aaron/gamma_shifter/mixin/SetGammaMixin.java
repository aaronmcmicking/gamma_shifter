package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
    SetGammaMixin

    Spongepowered mixin that bypasses the validation process for changes to the gamma setting, allowing values higher than 1.0
    Injects into SimpleOption.setValue() and uses an Accessor to manually set the gamma value
 */

/**
 * Spongepowered mixin that manually sets gamma values > 1.0, bypassing vanilla clamping to 0.0:1.0 and cancelling the
 * caller.
 */
@Mixin(SimpleOption.class)
public abstract class SetGammaMixin {

    /**
     * Shadow private 'value' in {@link SimpleOption}.
     */
    @Shadow
    Object value;

    /**
     * Injects into start of {@link SimpleOption#setValue(Object)} and manually sets the gamma value to bypass vanilla
     * clamping to 0.0:1.0. Cancels caller method if a value is manually set.
     * @param value The new gamma value. If working on the gamma option, then the type will be Double.
     * @param ci CallbackInfo to be returned.
     */
    @Inject(method = "setValue(Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    public void setValueInjected(Object value, CallbackInfo ci){
        SimpleOption<?> gamma = (SimpleOption<?>) (Object) this; // cast this to SimpleOption since this will be injected into SimpleOption.class
        MinecraftClient mc = MinecraftClient.getInstance();
//        GammaShifter.LOGGER.info("[HEAD: SetGammaMixin] MC.getInstance().getGameVersion() = " + MinecraftClient.getInstance().getGameVersion());
        try {
            if (mc.options != null && gamma.equals(mc.options.getGamma())) {
//                GammaShifter.LOGGER.info("[SetGammaMixin] Setting gamma (" + value + ")");
                this.value = value;
                ci.cancel(); // cancel the targeted method to prevent it overwriting the value
            }
        }catch(NullPointerException e){
            GammaShifter.LOGGER.error("Couldn't set new value for gamma: " + e);
        }
    }

}