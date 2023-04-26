package net.aaron.gamma_shifter.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/*
    DataResultAccessor

    Spongepowered mixin that expands field/method access to DataResult.class
 */

@Mixin(DataResult.class)
public interface DataResultAccessor<R> {
    // create getter for private field 'result'
    @Accessor(remap = false)
    Either<R, DataResult.PartialResult<R>> getResult();

}