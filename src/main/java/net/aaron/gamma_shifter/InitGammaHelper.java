package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

/**
 * Helper class that temporarily stores the gamma value read by {@link GameOptions#load()} before an instance of
 * {@link MinecraftClient} is available, and sets the value when the title screen loads for the first time.
 */
public class InitGammaHelper {

    /**
     * The gamma value found from options.txt.
     */
    public Double gammaFromFile;

    /**
     * Stores whether the value has been set already, since it should be set when the title screen displays
     * for the first time.
     */
    public boolean alreadyDone;

    /**
     * Default constructor.
     */
    public InitGammaHelper(){
        this.gammaFromFile = 1.0;
        this.alreadyDone = false;
    }

    /**
     * Constructor to set custom initial gamma value.
     * @param value The value to store.
     */
    public InitGammaHelper(Double value){
        this.gammaFromFile = value;
        this.alreadyDone = false;
    }

    /**
     * Stores a gamma value.
     * @param value The value to store.
     */
    public void storeGamma(Double value){
        this.gammaFromFile = value;
    }

    /**
     * Get the stored gamma value (does not read file).
     * @return The stored gamma value.
     */
    public Double getGammaFromFile(){
        return this.gammaFromFile;
    }

    /**
     * Check if the gamma value has already been saved to the settings.
     * @return true if the value has already been stored, false otherwise.
     */
    public boolean alreadyDone(){
        return this.alreadyDone;
    }

    /**
     * Sets the stored gamma value to the game settings.
     * <p>The caller is responsible for checking {@link InitGammaHelper#alreadyDone} before calling.</p>
     */
    public void setInitialGamma(){
        try {
            this.alreadyDone = true;
            if(GammaShifter.alwaysStartEnabled()) {
                MinecraftClient.getInstance().options.getGamma().setValue(this.gammaFromFile);
            }else{
                MinecraftClient.getInstance().options.getGamma().setValue(1.0);
            }
            MinecraftClient.getInstance().options.write();
            GammaHandler.setCurrentCustomGamma(gammaFromFile);
        }catch(Exception e){
            GammaShifter.LOGGER.error("Couldn't load gamma from file: " + e);
        }
    }
}
