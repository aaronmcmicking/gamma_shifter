package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;

public class initGammaHelper {

    public Double gamma;
    public boolean done;

    public initGammaHelper(){
        this.gamma = 1.0;
        this.done = false;
    }

    public initGammaHelper(Double value){
        this.gamma = value;
        this.done = false;
    }

    public void storeGamma(Double value){
        this.gamma = value;
    }

    public Double getGamma(){
        return this.gamma;
    }

    public boolean alreadyDone(){
        return this.done;
    }

    public void setGamma(){
        try {
//            GammaShifter.LOGGER.info("Setting gamma (value = " + this.gamma + ") from initGammaHelper.setValue()");
            this.done = true;
            MinecraftClient.getInstance().options.getGamma().setValue(this.gamma);
        }catch(Exception e){
            GammaShifter.LOGGER.error("Failed to load gamma from file: " + e);
        }
    }
}
