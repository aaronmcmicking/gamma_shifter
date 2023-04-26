package net.aaron.gamma_shifter.mixin;

import com.mojang.serialization.DataResult;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Spongepowered mixin to cancel error messages sent by Minecraft after it refuses to save out-of-range gamma values (> 1.0).
 * <p>Injects into {@link DataResult#error()} and returns an empty Optional instance, preventing the caller from learning the submitted option value couldn't be validated</p>.
 * @see Optional
 * @see net.minecraft.client.option.SimpleOption#setValue(Object)
 */

@SuppressWarnings("unchecked") // suppressing mixin typecast-warnings
@Mixin(DataResult.class)
public abstract class DataResultErrorMixin {
    /**
     * Injects into {@link DataResult#error()} and reads the error message to find references to an out-of-range gamma value. Upon finding them, the injection returns an
     * empty {@link Optional} instance, which the caller expects to receive if no error was encountered when validating option values.
     * @param cir The returnable callback that returns an empty Optional and cancels the calling method.
     * @param <R> The DataResult type.
     */
    @Inject(method = "error()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true, remap = false)
    public <R> void DataResultErrorInject(CallbackInfoReturnable<Optional<DataResult.PartialResult<R>>> cir){
        DataResult<R> dataResult = (DataResult<R>) (Object) this;
        try {
            Optional<DataResult.PartialResult<R>> partialResult = ((DataResultAccessor<R>) dataResult).getResult().right(); // get the partial result
            if(partialResult.isPresent()) {
                // if the partial result message contains info about the gamma values > 1.0
                if (partialResult.get().toString().contains(MinecraftClient.getInstance().options.getGamma().getValue().toString()) && partialResult.get().toString().contains("outside of range")) {
                    cir.setReturnValue(Optional.empty()); // return an empty Optional object, signaling that the client need not produce an error message
                }
            }
        }catch(NoSuchElementException e){
            GammaShifter.LOGGER.error("NoSuchElementException in DataResultErrorMixin"); // Optional.get() error
        }
    }
}