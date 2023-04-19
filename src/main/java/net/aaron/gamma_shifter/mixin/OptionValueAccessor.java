package net.aaron.gamma_shifter.mixin;

import com.mojang.serialization.Decoder;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleOption.class)

public interface OptionValueAccessor{
    @Accessor("value")
    public void setOptionValue(Object value);
}
