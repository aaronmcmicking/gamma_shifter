package net.aaron.gamma_shifter.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Spongepowered mixin that expands field access to {@link SimpleOption}.
 * @param <Object>
 */
@Mixin(SimpleOption.class)
public interface SimpleOptionAccessor<Object> {
    /**
     * Create setter for 'value' in {@link SimpleOption}.
     * @param value The value to be set.
     */
    @Accessor
    void setValue(Object value);
}
