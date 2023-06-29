package net.aaron.gamma_shifter.event;


import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;

/**
 * This code is very poorly written, and I am not entirely sure how it works or if it exhibits unintended behaviour. I
 * am somewhat ashamed that it appears on my public GitHub. However, by some miracle it appears to work as intended and
 * so instead of re-writing it, I have decided not to touch it. If you happen to be a potential employer please know that
 * I would never add code like this to your codebase.
 * <br><br>
 * Setting the gamma beyond 100% causes visual anomalies during the vanilla Minecraft Darkness effect, such as odd
 * colourations. This class detects when the Darkness effect has been applied and temporarily sets the gamma to 100%, and
 * then re-applies the gamma value to the previous value.
 * <br><br>
 * When this class lowers the gamma, the player has the option to re-enable it manually before the Darkness effect wears
 * off. In this case, this class should abandon its plan and not toggle the gamma value when the effect wears off. This
 * is also true if the player manually re-enables and then disables it again before the Darkness effect wears off.
 */
public class Darkness {
    private static boolean shouldDisableDuringDarkness = true;
    /**
     * True if the mod has temporarily been disabled and is waiting for the Darkness effect to wear off, false otherwise
     */
    private static boolean waiting = false;
    /**
     * Prevents fullbright from being re-enabled when the player manuals turns it back on while the Darkness effect is still active.
     * <br><br>
     * This is tracked explicitly/separately since if the player turns on and then turns off the mod all before the
     * Darkness effect has worn off this class should still not re-enable the mod when the effect ends.
     * <br><br> True if enabling will be prevented, and false if fullbright will be re-enabled as normal.
     */
    private static boolean override = false;

    /**
     * Stores whether the Darkness effect is currently/was previously applied. Is updated every tick by {@link Darkness#detectDarknessEffect()}.
     */
    private static class DarknessStatus {
        private static boolean previouslyEnabled = false;
        private static boolean currentlyEnabled = false;
    }

    /**
     * Executes behaviour described in the documentation for {@link Darkness}. Runs every tick.
     */
    public static void detectDarknessEffect(){
        ClientTickEvents.END_CLIENT_TICK.register(listener -> {
            if(!shouldDisableDuringDarkness || MinecraftClient.getInstance() == null) return; // depending on behaviour of ClientTickEvents, MC.getInstance() may always be non-null here
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if(player == null){
                return;
            }
            boolean hasDarkness = player.hasStatusEffect(StatusEffects.DARKNESS); // whether the player has Darkness effect
            boolean GSEnabled = GammaShifter.isEnabled();

            // shift the previous values in DarknessStatus and update the current one
            DarknessStatus.previouslyEnabled = DarknessStatus.currentlyEnabled;
            DarknessStatus.currentlyEnabled = hasDarkness;

            // if this is the first tick that the Darkness effect was applied and the mod is currently turned on
            if(!waiting && !override && hasDarkness && GSEnabled){
                waiting = true;
                GammaHandler.toggle(); // disabling

            // if we were waiting for Darkness to end, and it has ended, and the player has not manually turned the mod back on
            }else if(waiting && !GSEnabled && !hasDarkness){
                waiting = false;
                override = false;
                GammaHandler.toggle(); // enabling

            // if the Darkness has worn off and EITHER we were waiting for it to do so or the player manually re-enabled the mod
            }else if(!hasDarkness && (waiting || override)){
                waiting = false;
                override = false;
            }

            // if Darkness was applied last tick and it is not this tick
            if(DarknessStatus.previouslyEnabled && !hasDarkness){
                override = false;
            }
        });
    }

    /**
     * Signals that the mod should not be re-enabled when the Darkness effect wears off if this class was currently
     * waiting for it to do so.
     */
    public static void cancel(){
        override = true;
        waiting = false;
    }

    public static boolean shouldDisableDuringDarkness(){
        return shouldDisableDuringDarkness;
    }

    public static void setShouldDisableDuringDarkness(boolean value){
        shouldDisableDuringDarkness = value;
    }
}

