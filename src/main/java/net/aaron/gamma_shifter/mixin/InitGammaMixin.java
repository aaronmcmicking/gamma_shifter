package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;

/**
 * Spongepowered mixin to read/load gamma values from options.txt when the client launches, bypassing clamping to 0.0:1.0.
 *
 * <p>After being read, the gamma value is saved in {@link GammaShifter#gammaHelper}, which saves the value when
 * the title screen is displayed. Setting the value uses a helper class as the game client is not formally running yet
 * ({@link MinecraftClient#getInstance()} returns null), and so setGammaMixin cannot be used.</p>
 */
@Mixin(GameOptions.class)
public abstract class InitGammaMixin {
    @Shadow public abstract SimpleOption<Double> getGamma();

    /**
     * The gamma found in options.txt. Set to 1.0 by default (max vanilla brightness).
     */
    private Double foundGamma = 1.0;

    /**
     * Injects into {@link GameOptions#load} to retrieve and store the gamma value before it is deleted by the game.
     * @param ci CallbackInfo to be returned after injection finishes.
     */
    @Inject(method = "load", at = @At("HEAD"))
    public void retrieveGammaInject(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this; // cast 'this' to a GameOption, so we can reference it
        boolean found = false; // records if a gamma value was found in the file
        boolean malformed = false; // records if the options file appeared to be malformed (controls logging)
        boolean missing_file = false;  // records if the options file was missing
        try {
            BufferedReader br = new BufferedReader(new FileReader(options.getOptionsFile().getName()));
            String line = br.readLine();
            while(line != null){
                // initialize key-value pair
                String cur_key = "", cur_val = "";
                int i = 0;
                while(line.charAt(i) != ':'){   // read the key
                    cur_key += line.charAt(i++);
                }
                if(cur_key.equals("gamma")){
                    i++;
                    try{
                        while(i < 13 && i < line.length()){ // read the value (stops after reading 7 characters or at Exception)
                            cur_val += line.charAt(i++);
                        }
                    }catch(IndexOutOfBoundsException e){ /* empty catch block */ }
                    try {
                        this.foundGamma = Math.min(Double.parseDouble(cur_val), 10.0);  // clamp to 1000%
                        found = true;
                    }catch(NumberFormatException | NullPointerException e){ // possible exceptions from Double.parseDouble()
                        GammaShifter.LOGGER.error("Couldn't parse value from file... was the options file malformed?\n\t" + e);
                        this.foundGamma = 1.0;
                        malformed = true;
                    }
                    break;
                }   // if(cur_key.equals("gamma"))
                line = br.readLine();
            }   // while (line != null)
        }catch(IOException e){
            GammaShifter.LOGGER.error("Caught IOException while trying to load options... does the options file exist?\n\t" + e);
            missing_file = true;
        }

        if(found){
            GammaShifter.LOGGER.info("Read gamma value of " + foundGamma + " from options file");
            GammaHandler.initHelper.storeGamma(foundGamma); // initGammaHelper handles setting the value
        }else if(!malformed && !missing_file){
            GammaShifter.LOGGER.warn("Couldn't find an existing gamma setting... did the options file include one?");
        }
    }
}
