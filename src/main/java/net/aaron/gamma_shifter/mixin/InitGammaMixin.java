package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
/*
    InitGammaMixin

    Fabric mixin to read gamma settings from options.txt when the game client starts
    Retrieves and stores the raw value from options.txt when GameOptions.load() is called and manually sets the gamma value when the method ends
    Bypasses default clamping of gamma values to 0.0:1.0
 */

@Mixin(GameOptions.class)
public class InitGammaMixin {
    private Double found_gamma = 1.0;

    // retrieves the gamma setting from options.txt before it is modified by the game
    @Inject(method = "load", at = @At("HEAD"), cancellable = false)
    public void retrieveGammaInject(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this; // cast 'this' to a GameOption, so we can reference it
        boolean found = false; // records if a gamma value was found in the file
        boolean malformed = false, missing = false; // records if the options file appeared to be malformed (controls logging), or was missing
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
                        while(i < 13){ // read the value (stops after reading 7 characters or at Exception)
                            cur_val += line.charAt(i++);
                        }
                    }catch(IndexOutOfBoundsException e){
//                        GammaShifter.LOGGER.warn("IndexOutOfBoundsException in InitGammaMixin.retrieveGammaInject: cur_val = " + cur_val);
                    }
                    try {
                        found_gamma = Math.min(Double.parseDouble(cur_val), 10.0);  // clamp to 1000%
                        found = true;
                    }catch(NumberFormatException e){
                        GammaShifter.LOGGER.error("Couldn't parse value from file... was the options file malformed?");
                        found_gamma = 1.0;
                        malformed = true;
                    }
                    break;
                }   // if(cur_key.equals("gamma"))
                line = br.readLine();
            }   // while (line != null)
        }catch(IOException e){
            GammaShifter.LOGGER.error("Caught IOException while trying to load options... does the options file exist?");
            missing = true;
        }

        if(found){
            GammaShifter.LOGGER.info("Read gamma value of " + found_gamma + " from options file");
        }else if(!malformed && !missing){
            GammaShifter.LOGGER.warn("Couldn't find an existing gamma setting... did the options file include one?");
        }
    }

    // set the gamma to the value found in options.txt, bypassing clamping
    @Inject(method = "load", at = @At("RETURN"), cancellable = false)
    public void setGammaInject(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this;
        GammaShifter.LOGGER.info("[GammaShifter] About to set gamma");
        options.getGamma().setValue(found_gamma); // setting values higher than 1.0 with setValue enabled by setGammaMixin
        GammaShifter.LOGGER.info("[GammaShifter] Just set gamma");
    }
}
