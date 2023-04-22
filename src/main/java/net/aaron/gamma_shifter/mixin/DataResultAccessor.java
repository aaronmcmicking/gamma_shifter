package net.aaron.gamma_shifter.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DataResult.class)
public interface DataResultAccessor<R> {
    @Accessor(remap = false)
    Either<R, DataResult.PartialResult<R>> getResult();
}