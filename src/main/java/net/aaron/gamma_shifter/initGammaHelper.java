package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class initGammaHelper {

    public static void setValue(Double value){
        int i = 0; int i_max = 5;
        while(!MinecraftClient.getInstance().isRunning() && i < i_max) {
            try {
                Thread.sleep(3000);
                i++;
            } catch (InterruptedException e) {
                GammaShifter.LOGGER.error("[GammaShifter] encountered error while sleeping: " + e);
//            throw new RuntimeException(e);
            }
        }
        GammaShifter.LOGGER.info("[GammaShifter] Left waiting loop, waited " + i + "/" + i_max + " times, MinecraftClient.getInstance().isRunning() = " + MinecraftClient.getInstance().isRunning());
        try {
            MinecraftClient.getInstance().options.getGamma().setValue(value);
        }catch(Exception e){
            GammaShifter.LOGGER.error("Exception in initGammaHelper: " + e);
            GammaShifter.LOGGER.error("MinecraftClient.getInstance().isRunning = " + MinecraftClient.getInstance().isRunning());

            if(MinecraftClient.getInstance() == null){
                GammaShifter.LOGGER.error("MinecraftClient.getInstance() returned null");
            }else if(MinecraftClient.getInstance().options == null){
                GammaShifter.LOGGER.error("MinecraftClient.getInstance().options == null");
            }else if(MinecraftClient.getInstance().options.getGamma() == null){
                GammaShifter.LOGGER.error("MinecraftClient.getInstance().options.getGamma() returned null");
            }
        }
    }

}
