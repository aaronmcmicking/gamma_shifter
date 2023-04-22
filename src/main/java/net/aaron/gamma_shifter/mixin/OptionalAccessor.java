package net.aaron.gamma_shifter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(Optional.class)
public interface OptionalAccessor<T>{
    @Accessor
    void setValue(T value);
}
