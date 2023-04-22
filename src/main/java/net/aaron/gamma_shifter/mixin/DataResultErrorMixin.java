package net.aaron.gamma_shifter.mixin;

import com.mojang.serialization.DataResult;
import net.aaron.gamma_shifter.GammaShifter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(DataResult.class)
public abstract class DataResultErrorMixin {

    @Shadow public abstract <K, V> Function<K, DataResult<V>> partialGet(Function<K, V> partialGet, Supplier<String> errorPrefix);

    @Inject(method = "error()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true, remap = false)
    public <R, T> void DataResultErrorInject(CallbackInfoReturnable<Optional<DataResult.PartialResult<R>>> cir){
//        if(MinecraftClient.getInstance() != null)
//            if(MinecraftClient.getInstance().player != null)
//                GammaShifter.LOGGER.info("[GammaShifter] Injected into DataResult.error() at HEAD");

        DataResult<R> dataResult = (DataResult<R>) (Object) this;
        try {
            Optional<DataResult.PartialResult<R>> partialResult = ((DataResultAccessor<R>) dataResult).getResult().right();
            if(partialResult.isPresent()) {
                GammaShifter.LOGGER.info("[GammaShifter] PartialResult: " + partialResult.get().message());
                if (partialResult.get().toString().contains("Brightness") && partialResult.get().toString().contains("outside of range")) {
                    GammaShifter.LOGGER.info("[GammaShifter] Found Brightness in DataResult injection");
                    Optional<T> parRes = ( (PartialResultAccessor<T>) ( (DataResultAccessor<R>) dataResult ).getResult()).getPartialResult();
                    ( (OptionalAccessor<T>) parRes).setValue(null);
//                    cir.setReturnValue(new Optional<>(null));
                }
            }
        }catch(NoSuchElementException e){
            // empty catch
            GammaShifter.LOGGER.info("[GammaShifter] NoSuchElementException in DataResultErrorMixin");
        }
    }
}
