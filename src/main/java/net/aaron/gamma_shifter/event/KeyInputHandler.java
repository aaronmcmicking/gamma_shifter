package net.aaron.gamma_shifter.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;

/**
 *   Sets default keybinds, detects key presses, calculates, and sets new gamma settings on keypress (runs every tick).
 **/

public class KeyInputHandler {

    /**
     * Names for the mod category and keybinds in the settings menu.
     **/
    public static final String KEY_CATEGORY_GAMMA_SHIFTER = "Gamma Shifter";
    public static final String KEY_INCREASE_GAMMA = "Increase Gamma";
    public static final String KEY_DECREASE_GAMMA = "Decrease Gamma";

    /**
     * The keybinds themselves, initialized in register().
     */
    public static KeyBinding increaseGammaKey;
    public static KeyBinding decreaseGammaKey;

    /**
     * Define the decimal format for Doubles when the current gamma is displayed to the user.
     */
    private final static DecimalFormat decFor = new DecimalFormat("0");

    /**
     * Checks every tick if the keybinds to increase/decrease gamma have been pressed. If they have, the gamma is incremented/decremented accordingly. Displays a message above the hotbar to the user.
     * <p>
     * Hard-coded to change the value by 0.2, or 20%, per input.
     * </p>
     */
    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(increaseGammaKey.wasPressed()){
                // fix round errors with double arithmetic and set the new value
                boolean set = false; // records whether a new value was set
                double new_gamma = 1.0;
                if(client.options.getGamma().getValue() <= 9.81) {
                    new_gamma = Math.round((client.options.getGamma().getValue() + .2) * 100) / 100.0;
                    set = true;
                }else{
                    new_gamma = 10.0;
                    if(client.options.getGamma().getValue() < 10.0)
                        set = true;
                }
                client.options.getGamma().setValue(new_gamma);

                // display a message on-screen telling the player the current gamma value
                String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                if(client.player != null) {
                    client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
                }
                if(set) {
//                    GammaShifter.LOGGER.info("Set gamma to " + client.options.getGamma().getValue());
                    MinecraftClient.getInstance().options.write(); // write the settings to the options file (if a new value was set)
                }
              }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(decreaseGammaKey.wasPressed()){
                boolean set = false; // records whether a new value was set
                // decrease gamma
                if(client.options.getGamma().getValue() > .2) {
                    // fix round errors with double arithmetic and set the new value
                    double new_gamma = Math.round((client.options.getGamma().getValue() - .2)*100)/100.0;
                    client.options.getGamma().setValue(new_gamma);
                    set = true;
                }else{
                    client.options.getGamma().setValue(0.0);
                    if(client.options.getGamma().getValue() > 0.0)
                        set = true;
                }

                if(set) {
//                    GammaShifter.LOGGER.info("Set gamma to " + client.options.getGamma().getValue());
                    MinecraftClient.getInstance().options.write(); // write the settings to the options file
                }
                // display a message on-screen telling the player the current gamma value
                if(client.player != null) {
                    String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                    client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
                }
            }
        });
    }

    /**
     * Creates the default keybinds on mod initialization.
     */
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
