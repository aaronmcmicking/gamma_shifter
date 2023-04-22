package net.aaron.gamma_shifter.mixin;

import com.mojang.serialization.DataResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(DataResult.PartialResult.class)
public interface PartialResultAccessor<R> {
    @Accessor
    Optional<R> getPartialResult();
}

