package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

/**
 * Temporarily stores the gamma value read by {@link GameOptions#load()} before an instance of
 * {@link MinecraftClient} is available, and sets the value when the title screen loads for the first time.
 */
public class GammaInitializer {

    /**
     * The gamma value found from options.txt.
     */
    public static Double gammaFromFile = 1.0;

    /**
     * Stores whether the value has been set already, since it should be set when the title screen displays
     * for the first time.
     */
    public static boolean alreadyDone = false;

    /**
     * Stores a gamma value.
     * @param value The value to store.
     */
    public static void storeGamma(Double value){
        gammaFromFile = value;
    }

    /**
     * Get the stored gamma value (does not read file).
     * @return The stored gamma value.
     */
    public static Double getGammaFromFile(){
        return gammaFromFile;
    }

    /**
     * Check if the gamma value has already been saved to the settings.
     * @return true if the value has already been stored, false otherwise.
     */
    public static boolean alreadyDone(){
        return alreadyDone;
    }

    /**
     * Sets the stored gamma value to the game settings.
     * <p>The caller is responsible for checking {@link GammaInitializer#alreadyDone} before calling.</p>
     */
    public static void setInitialGamma(){
        try {
            gammaFromFile = clamp(gammaFromFile);
            alreadyDone = true;
            if(GammaShifter.isEnabled() || GammaShifter.alwaysStartEnabled()) {
                MinecraftClient.getInstance().options.getGamma().setValue(gammaFromFile);
            }else{
                MinecraftClient.getInstance().options.getGamma().setValue(1.0);
            }
            MinecraftClient.getInstance().options.write();
            GammaHandler.setCurrentCustomGamma(gammaFromFile);
        }catch(Exception e){
            GammaShifter.LOGGER.error("Couldn't load gamma from file: " + e);
        }
    }

    /**
     * Clamps a given value to {@link GammaHandler#MIN_GAMMA} and {@link GammaHandler#MAX_GAMMA} if bound
     * {@link GammaHandler#shouldEnforceBounds} is set to 'true'.
     * @param value The value to clamp.
     * @return The clamped value. This value is only ever the value passed into the method,
     * {@link GammaHandler#MIN_GAMMA}, or {@link GammaHandler#MAX_GAMMA}.
     */
    private static double clamp(double value){
        if(GammaHandler.shouldEnforceBounds()){
            value = Math.max(GammaHandler.MIN_GAMMA, value);
            value = Math.min(GammaHandler.MAX_GAMMA, value);
        }
        return value;
    }
}
