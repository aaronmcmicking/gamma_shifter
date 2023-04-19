package net.aaron.gamma_shifter.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleOption.class)

public interface SimpleOptionAccessor<Object> {
    @Accessor
    void setValue(Object value);
}
