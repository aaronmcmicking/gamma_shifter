package net.aaron.gamma_shifter.mixin;

import com.google.common.base.Splitter;
import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaInitializer;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.event.AutoNight;
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
import java.util.Iterator;

/**
 * Spongepowered mixin to read/load gamma values from options.txt when the client launches, bypassing clamping to 0.0:1.0.
 *
 * <p>After being read, the gamma value is saved in {@link GammaInitializer}, which sets the value when
 * the title screen is displayed. This helper class is used since {@link MinecraftClient#getInstance()} returns null
 * while the game is initializing, so {@link SimpleOption#setValue(Object)} cannot be called yet.</p>
 */
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    /**
     * Storage variable to save a set gamma while another one is substituted when saving to options.txt.
     * @see GameOptionsMixin#substituteCustomGammaForSave(CallbackInfo)
     */
    private Double tempGamma = 1.0;

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
        @Nullable String line = ""; // line from options.txt

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getOptionsFile().getName()));
            while (line != null && !line.contains("gamma")) {
                line = br.readLine();
            }
        } catch (IOException e) {
            GammaShifter.LOGGER.warn("Couldn't read options file... is it missing?\n\t" + e);
            line = null; // prevents attempts to parse line below
        }

        // now, line is either null or contains the line with "gamma"
        try {
            if (line != null && line.contains("gamma")) {
                Iterator<String> iter = Splitter.on(':').split(line).iterator();
                iter.next(); // "gamma" can be thrown away
                Double gamma = Double.parseDouble( iter.next() ); // the value

                GammaShifter.LOGGER.info("Read gamma value of " + gamma + " from options file");
                GammaInitializer.storeGamma(gamma); // GammaInitializer handles setting the value
            } else {
                GammaShifter.LOGGER.warn("Couldn't find an existing gamma setting... did the options file include one?");
            }
        }catch(Exception e){ // exception from Double.parseDouble(String)
            GammaShifter.LOGGER.error("Couldn't parse gamma value from options.txt... was the options file malformed?\n\t" + e);
        }
    }

    /**
     * Substitutes the current gamma with a custom gamma value if the mod is disabled or if AutoNight is active,
     * so that the custom value is still saved in options.txt. Stores the current shown gamma value in {@link GameOptionsMixin#tempGamma} to
     * be restored after {@link GameOptions#write()} finishes.
     * <p>Only modifies values if mod is currently disabled.</p>
     * @see GameOptionsMixin#restoreCurrentGammaAfterSave(CallbackInfo)
     * @param ci CallbackInfo to be returned.
     */
    @Inject(method = "write", at = @At("HEAD"))
    public void substituteCustomGammaForSave(CallbackInfo ci){
        if(GammaShifter.getAlwaysSaveCustomGamma() && (!GammaShifter.isEnabled() || AutoNight.isActive()) ) {
            GameOptions options = MinecraftClient.getInstance().options;
            tempGamma = options.getGamma().getValue();
            options.getGamma().setValue(GammaHandler.currentCustomGamma);
        }
    }

    /**
     * Restores the current gamma after options.txt has been saved with the custom gamma.
     * <p>Only modifies values if mod is currently disabled.</p>
     * @param ci CallbackInfo to be returned.
     */
    @Inject(method = "write", at = @At("TAIL"))
    public void restoreCurrentGammaAfterSave(CallbackInfo ci){
        if(GammaShifter.getAlwaysSaveCustomGamma() && (!GammaShifter.isEnabled() || AutoNight.isActive()) ){
            MinecraftClient.getInstance().options.getGamma().setValue(tempGamma);
        }
    }
}
