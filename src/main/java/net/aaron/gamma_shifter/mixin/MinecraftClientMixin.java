package net.aaron.gamma_shifter.mixin;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaInitializer;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.config.ConfigLoader;
import net.aaron.gamma_shifter.event.AutoNight;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * <p>1. Signals to {@link GammaInitializer} to set the gamma file read from options.txt when the title screen loads for the first time.</p>
 * <p>2. Saves the options to disk when the pause menu is opened while the player is in a world.</p>
 * <p>3. Saves the options when the game closes.</p>
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    public Screen currentScreen;

    /**
     * This injection either signals to set the gamma value read from options.txt on initialization when the title
     * screen loads for the first time, saves the options and config to disk when the pause menu is opened, or
     * sets AutoNight to initialize when the pause menu is closed.</p>
     * <p>Also updates {@link GammaHandler#setCurrentCustomGamma(Double)} to reflect changes made to settings
     * in the vanilla options menu.</p>
     *
     * @param screen The Screen to change to (nullable).
     * @param ci     CallbackInfo to return.
     * @see GammaInitializer
     * @see AutoNight#initializeAutoNightStatus()
     */
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreenGammaInject(Screen screen, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!GammaInitializer.alreadyDone()) { // if setting the initial value from options.txt
            GammaInitializer.setInitialGamma();
            return;
        }

        // if saving options while player is playing
        if ((screen instanceof GameMenuScreen) && (client != null)  &&  (client.player != null)) {
            client.options.write();
            ConfigLoader.save();
            // if the mod is enabled, make sure GammaHandler gets the updated gamma value from the vanilla settings menu
            if(GammaShifter.isEnabled() && !AutoNight.isActive()){ // changes are ignored if AutoNight is active
                GammaHandler.setCurrentCustomGamma(client.options.getGamma().getValue());
            }
        // if AutoNight needs to be initialized
        } else if ( screen == null
                && client != null
                && client.player != null
                && client.world != null ) {
            AutoNight.initializeAutoNightStatus();
        }
    }

    /**
     * Saves the options when the game closes.
     * @param ci CallbackInfo to be returned.
     */
    @Inject(method = "stop", at = @At("HEAD"))
    public void saveOnClose(CallbackInfo ci){
        ConfigLoader.save();
        MinecraftClient.getInstance().options.write();
        GammaShifter.LOGGER.info(("Saved options"));
    }

    /**
     * Resets AutoNight's initialization status when the player leaves the world.
     * @param screen The screen to change to (unused in mixin).
     * @param ci CallbackInfo to be returned.
     * @see AutoNight
     */
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V" , at = @At("HEAD"))
    public void updateAutoNightStatusOnDisconnect(Screen screen, CallbackInfo ci){
        AutoNight.setAlreadyInitialized(false);
    }
}