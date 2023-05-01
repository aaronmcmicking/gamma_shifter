package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;

/**
 * Helper class that temporarily stores the gamma value read by initGammaMixin before the client formally begins running
 * and sets it when the title screen loads.
 */
public class InitGammaHelper {

    /**
     * The gamma value found from options.txt.
     */
    public Double gammaFromFile;

    /**
     * Boolean to store whether the value has been set already, since it should be set when the title screen displays
     * for the first time.
     */
    public boolean alreadyDone;

    /**
     * Default constructor that sets default values.
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
     * Get the stored gamma value.
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
            MinecraftClient.getInstance().options.getGamma().setValue(this.gammaFromFile);
            MinecraftClient.getInstance().options.write();
            GammaHandler.setCurrentCustomGamma(gammaFromFile);
        }catch(Exception e){
            GammaShifter.LOGGER.error("Couldn't load gamma from file: " + e);
        }
    }
}
