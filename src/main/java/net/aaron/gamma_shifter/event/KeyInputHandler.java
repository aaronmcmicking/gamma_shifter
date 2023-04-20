package net.aaron.gamma_shifter.event;

//import net.aaron.gamma_shifter.HUD.HUD;
import net.aaron.gamma_shifter.GammaShifter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.Optional;

/*
    KeyInputHandler

    Sets default keybinds and detects key presses
    Calculates and sets new gamma settings on keypress (runs every tick)
 */


public class KeyInputHandler {

    public static final String KEY_CATEGORY_GAMMA_SHIFTER = "Gamma Shifter";
    public static final String KEY_INCREASE_GAMMA = "Increase Gamma";
    public static final String KEY_DECREASE_GAMMA = "Decrease Gamma";
    static DecimalFormat decFor = new DecimalFormat("0.0");

    public static KeyBinding increaseGammaKey;
    public static KeyBinding decreaseGammaKey;

    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(increaseGammaKey.wasPressed()){
                // fix round errors with double arithmetic and set the new value
                double new_gamma = Math.round((client.options.getGamma().getValue() + .2)*100)/100.0;
                client.options.getGamma().setValue(new_gamma);

                // display a message on-screen telling the player the current gamma value
                String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                if(client.player != null) {
                    client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
                }
                GammaShifter.LOGGER.info("[GammaShifter] Set gamma to " + client.options.getGamma().getValue());
                MinecraftClient.getInstance().options.write(); // write the settings to the options file
              }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(decreaseGammaKey.wasPressed()){
                // decrease gamma
                if(client.options.getGamma().getValue() > .2) {
                    // fix round errors with double arithmetic and set the new value
                    double new_gamma = Math.round((client.options.getGamma().getValue() - .2)*100)/100.0;
                    client.options.getGamma().setValue(new_gamma);
                    GammaShifter.LOGGER.info("[GammaShifter] Set gamma to " + client.options.getGamma().getValue());
                }else{
                    GammaShifter.LOGGER.info("[GammaShifter] Set gamma to " + client.options.getGamma().getValue());
                    client.options.getGamma().setValue(0.0);
                }

                MinecraftClient.getInstance().options.write(); // write the settings to the options file
                // display a message on-screen telling the player the current gamma value
                String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                if(client.player != null) {
                    client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
                }
            }
        });
    }

    public static void register(){
        // set the default keybind to increase gamma to ']'
        increaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_INCREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the default keybind to decrease gamma to '['
        decreaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DECREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        registerKeyInputs();
    }
}
