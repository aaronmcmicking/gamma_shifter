package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;

/**
 * Spongepowered mixin to read/load gamma values from options.txt when the client launches, bypassing clamping to 0.0:1.0.
 *
 * <p>After being read, the gamma value is saved in {@link GammaHandler#initHelper}, which sets the value when
 * the title screen is displayed. This helper class is used since {@link MinecraftClient#getInstance()} returns null
 * while the game is initializing, so {@link SimpleOption#setValue(Object)} cannot be called yet.</p>
 */
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    /**
     * The gamma found in options.txt. Set to 1.0 by default (max vanilla brightness).
     */
    private Double foundGamma = 1.0;

    /**
     * Shadow placeholder for {@link GameOptions#getOptionsFile()}.
     * @return The options file as type 'File'.
     */
    @Shadow public abstract File getOptionsFile();

    /**
     * Injects into {@link GameOptions#load()} to retrieve and stores the gamma value before it is overwritten by the game.
     * @param ci CallbackInfo to be returned after injection finishes.
     */
    @Inject(method = "load", at = @At("HEAD"))
    public void retrieveGammaInject(CallbackInfo ci){
//        GameOptions options = (GameOptions) (Object) this; // cast 'this' to a GameOption, so we can reference it (replaced by @Shadow)
        boolean found = false, missingFile = false, malformed = false; // records: 1. if gamma value was found 2. if options file was malformed 3. if options file was missing
        String curKey = "", curVal = ""; // key-value pair
        int i = 0; // iterating variable
        @Nullable String line = ""; // line from options.txt

        // find the key 'gamma' in the file
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getOptionsFile().getName()));
            while (line != null && !curKey.equals("gamma")) { // while line != null && !curKey.equals("gamma")
                line = br.readLine();
                if (line != null) {
                    curKey = "";
                    curVal = "";
                    i = 0;
                    while (i < line.length() && line.charAt(i) != ':') {   // read the key
                        curKey += line.charAt(i++);
                    } // while
                } // if
            } // while
        } catch (IOException e) {
            GammaShifter.LOGGER.error("Couldn't read options file... does it exist?\n\t" + e);
            missingFile = true;
        }

        // if the key was found, retrieve the associated value as a Double parsed from a String
        if (curKey.equals("gamma")) {
            i++;
            try {
                while (i < 13 && i < line.length()) { // read the value (stops after reading 7 characters or at Exception)
                    curVal += line.charAt(i++);
                }
            } catch (IndexOutOfBoundsException e) { /* empty catch block */ }
            try {
                this.foundGamma = Math.min(Double.parseDouble(curVal), GammaHandler.MAX_GAMMA);  // clamp to the maximum allowed gamma
                found = true;
            } catch (NumberFormatException |
                     NullPointerException e) { // possible exceptions from Double.parseDouble()
                GammaShifter.LOGGER.error("Couldn't parse value from file... was the options file malformed?\n\t" + e);
                this.foundGamma = 1.0; // in case of exception, set gamma to a default value
                malformed = true;
            }
        }   // if(curKey.equals("gamma"))

        if(found){
            GammaShifter.LOGGER.info("Read gamma value of " + foundGamma + " from options file");
            GammaHandler.initHelper.storeGamma(foundGamma); // InitGammaHelper handles setting the value
        }else if(!malformed && !missingFile){
            GammaShifter.LOGGER.error("Couldn't find an existing gamma setting... did the options file include one?");
        }

    }
}
