package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Mixin(GameOptions.class)
public class SaveGammaMixin {
    @Inject(method = "write", at = @At("RETURN"), cancellable = false)
    public void writeInject(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this;
        if(options.getGamma().getValue() > 1.0){
            try {
                String str = "gamma:" + options.getGamma().getValue() + "\n";
                Files.write(Paths.get(options.getOptionsFile().getName()), str.getBytes(), StandardOpenOption.APPEND);
//                GammaShifter.LOGGER.info("[GammaShifter] Manually wrote gamma value (" + options.getGamma().getValue() + ") to options file at " + options.getOptionsFile().toString());
            }catch(IOException e){
                GammaShifter.LOGGER.info("[GammaShifter] Unable to open options file for manual writing (IOException)");
            }
        }
    }
}
