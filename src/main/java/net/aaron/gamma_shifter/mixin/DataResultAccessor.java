package net.aaron.gamma_shifter.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 *  A Spongepowered mixin that expands field access to {@link DataResult} for use in other mixins.
 * @param <R>
 */
@Mixin(DataResult.class)
public interface DataResultAccessor<R> {
    /**
     * Creates a getter for the 'result' field of {@link DataResult}.
     * @return The 'result' field from {@link DataResult}.
     */
    @Accessor(remap = false)
    Either<R, DataResult.PartialResult<R>> getResult();
}