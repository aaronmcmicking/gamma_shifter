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

/*
    DataResultErrorMixin

    Fabric mixin to cancel error messages sent by Minecraft after it refuses to save gamma values > 1.0
    Suppresses warnings by returning an empty Optional object from DataResult.error() instead of the intended Optional<PartialResult<R>>
        This prevents the game from knowing that an error has actually occurred
 */

@SuppressWarnings("unchecked") // suppressing mixin typecast-warnings
@Mixin(DataResult.class)
public abstract class DataResultErrorMixin {
    @Inject(method = "error()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true, remap = false)
    public <R, T> void DataResultErrorInject(CallbackInfoReturnable<Optional<DataResult.PartialResult<R>>> cir){
        DataResult<R> dataResult = (DataResult<R>) (Object) this;
        try {
            Optional<DataResult.PartialResult<R>> partialResult = ((DataResultAccessor<R>) dataResult).getResult().right(); // get the partial result
            if(partialResult.isPresent()) {
                // if the partial result message contains info about the gamma values > 1.0
                if (partialResult.get().toString().contains(MinecraftClient.getInstance().options.getGamma().getValue().toString()) && partialResult.get().toString().contains("outside of range")) {
//                    GammaShifter.LOGGER.info("Cancelled high-brightness error message");
                    cir.setReturnValue(Optional.empty()); // return an empty Optional object, signaling that the client need not produce an error message
                }
            }
        }catch(NoSuchElementException e){
            GammaShifter.LOGGER.error("NoSuchElementException in DataResultErrorMixin"); // Optional.get() error
        }
    }
}
