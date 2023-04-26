package net.aaron.gamma_shifter.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/*
    SimpleOptionAccessor

    Spongepowered mixin that expands field/method access to SimpleOptions.class
 */

@Mixin(SimpleOption.class)
public interface SimpleOptionAccessor<Object> {
    // create setter for private field 'value'
    @Accessor
    void setValue(Object value);
}
