package net.aaron.gamma_shifter.event;


import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;

public class Darkness {
    private static boolean enabled = true;
    private static boolean waiting = false;
    private static boolean override = false;

    // stores previous darkness states
    private static class DarknessStatus {
        private static boolean previouslyEnabled = false;
        private static boolean currentlyEnabled = false;
    }

    public static void detectDarknessEffect(){
        ClientTickEvents.END_CLIENT_TICK.register(listener -> {
            if(!enabled || MinecraftClient.getInstance() == null) return; // depending on behaviour of ClientTickEvents, MC.getInstance() may always be non-null here
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if(player == null){
                return;
            }
            boolean hasDarkness = player.hasStatusEffect(StatusEffects.DARKNESS);
            boolean GSEnabled = GammaShifter.isEnabled();
            DarknessStatus.previouslyEnabled = DarknessStatus.currentlyEnabled;
            DarknessStatus.currentlyEnabled = hasDarkness;
            GammaShifter.LOGGER.info("prev_en = " + DarknessStatus.previouslyEnabled + ", hasDarkness = " + hasDarkness + ", waiting = " + waiting + ", GSEnabled = " + GSEnabled);
            if(!waiting && !override && hasDarkness && GSEnabled){
                GammaShifter.LOGGER.info("setting waiting to false");
                waiting = true;
                GammaHandler.toggle(); // disabling
            }else if(waiting && !GSEnabled && !hasDarkness){
                waiting = false;
                override = false;
                GammaHandler.toggle(); // enabling
            }else if(!hasDarkness && (waiting || override)){
                waiting = false;
                override = false;
            }
            if(DarknessStatus.previouslyEnabled && !hasDarkness){
                override = false;
            }
        });
    }

    public static void cancel(){
        override = true;
        waiting = false;
    }

    public static boolean isEnabled(){
        return enabled;
    }

    public static void setEnabled(boolean value){
        enabled = value;
    }
}

