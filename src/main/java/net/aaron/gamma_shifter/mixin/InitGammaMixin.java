package net.aaron.gamma_shifter.mixin;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;


@Mixin(GameOptions.class)
public class InitGammaMixin {
    private Double found_gamma = 1.0;
    private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);

    // retrieves the gamma setting from options.txt before it is modified by the game
    @Inject(method = "load", at = @At("HEAD"), cancellable = false)
    public void retrieveGammaInject(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this;
//        GammaShifter.LOGGER.info("Injected into GameOptions.load() successfully (optionsFile = " + options.getOptionsFile().toString());
        AtomicBoolean found = new AtomicBoolean(false);

        // read every line of the file
        try (BufferedReader bufferedReader = Files.newReader(options.getOptionsFile(), Charsets.UTF_8);){
            bufferedReader.lines().forEach(line -> {
                try {
                    Iterator<String> iterator = COLON_SPLITTER.split(line).iterator();
                    // retrieve key-value pairs
                    String cur_key = iterator.next();
                    String cur_val = iterator.next();

                    if(cur_key.equals("gamma")){
                        found_gamma = Double.parseDouble(cur_val);
                        found.set(true);
                    }
                } catch (Exception exception) {
                    GammaShifter.LOGGER.warn("[GammaShifter] Skipping bad option: {} (encountered while retrieving gamma)", line);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(found.get()){
            GammaShifter.LOGGER.info("[GammaShifter] Found existing gamma setting of " + found_gamma);
        }else{
            GammaShifter.LOGGER.warn("[GammaShifter] Couldn't find an existing gamma setting... did " + options.getOptionsFile().getName() +  " specify a gamma setting?");
        }
    }

    // set the gamma to the value found in options.txt, bypassing clamping
    @Inject(method = "load", at = @At("RETURN"), cancellable = false)
    public void setGammaInject(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this;
        options.getGamma().setValue(found_gamma); // setting values higher than 1.0 with setValue enabled by setGammaMixin
    }

}
