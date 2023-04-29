package net.aaron.gamma_shifter.event;

import net.aaron.gamma_shifter.GammaShifter;
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
    public static final String KEY_TOGGLE_MOD = "Toggle Mod";

    /**
     * The keybinds themselves, initialized in register().
     */
    public static KeyBinding increaseGammaKey;
    public static KeyBinding decreaseGammaKey;
    public static KeyBinding toggleModKey;

    /**
     * The amount to change the gamma value by per input received. It is currently hardcoded but it is planned to be
     * made customizable by the user in future updates.
     */
    public static Double changePerInput = 0.5;

    /**
     * Stores the current gamma value for when the mod is toggled off
     */
    private static Double currentCustomGamma = 1.0;

    /**
     * Define the decimal format for Doubles when the current gamma is displayed to the user.
     */
    private final static DecimalFormat decFor = new DecimalFormat("0");

    /**
     * Checks every tick if the keybinds to increase/decrease gamma have been pressed. If they have, the gamma is incremented/decremented accordingly. Displays a message above the hotbar to the user.
     */
    public static void registerKeyInputs(){
        // Detect keypresses for increasing the gamma
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(increaseGammaKey.wasPressed() && GammaShifter.isToggled()){
                // fix round errors with double arithmetic and set the new value
                boolean set = false; // records whether a new value was set
                double new_gamma;
                if(client.options.getGamma().getValue() <= 9.81) {
                    new_gamma = Math.round((client.options.getGamma().getValue() + changePerInput) * 100) / 100.0;
                    set = true;
                }else{
                    new_gamma = 10.0;
                    if(client.options.getGamma().getValue() < 10.0) {
                        set = true;
                    }
                }
                client.options.getGamma().setValue(new_gamma);

                // display a message on-screen telling the player the current gamma value
                String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                if(client.player != null) {
                    client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
                }
                if(set) {
//                    GammaShifter.LOGGER.info("Set gamma to " + client.options.getGamma().getValue());
                    currentCustomGamma = new_gamma;
                }
              }
        });

        // Detect keypresses for decreasing the gamma
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(decreaseGammaKey.wasPressed() && GammaShifter.isToggled()){
                boolean set = false; // records whether a new value was set
                double new_gamma = 1.0;
                // decrease gamma
                if(client.options.getGamma().getValue() > changePerInput) {
                    // fix round errors with double arithmetic and set the new value
                    new_gamma = Math.round((client.options.getGamma().getValue() - changePerInput)*100)/100.0;
                    client.options.getGamma().setValue(new_gamma);
                    set = true;
                }else{
                    if(client.options.getGamma().getValue() > 0.0) {
                        set = true;
                    }
                    new_gamma = 0.0;
                    client.options.getGamma().setValue(new_gamma);
                }

                if(set) {
//                    GammaShifter.LOGGER.info("Set gamma to " + client.options.getGamma().getValue());
                    currentCustomGamma = new_gamma;
                }
                // display a message on-screen telling the player the current gamma value
                if(client.player != null) {
                    String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                    client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
                }
            }
        });

        // Detect keypresses for toggle mod effects
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(toggleModKey.wasPressed()){
                if(GammaShifter.isToggled()){
                    MinecraftClient.getInstance().options.getGamma().setValue(1.0);
                }else{
                    MinecraftClient.getInstance().options.getGamma().setValue(currentCustomGamma);
                }
                GammaShifter.toggle();
            }
        });
    }

    /**
     * Creates the default keybinds on mod initialization.
     */
    public static void registerKeyBinds(){
        // set the default keybind to increase gamma to '+'
        increaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_INCREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the default keybind to decrease gamma to '-'
        decreaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DECREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_MINUS,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the default keybind to toggle mod effects to 'G'
        toggleModKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_TOGGLE_MOD,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        registerKeyInputs();
    }

    /**
     * Gets the current custom gamma.
     * @return The current custom gamma.
     */
    public static Double getCurrentCustomGamma() {
        return currentCustomGamma;
    }

    /**
     * Get the gamma change-per-input.
     * @return The change-per-input.
     */
    public static Double getChangePerInput(){
        return changePerInput;
    }

    /**
     * Sets the gamma change-per-input.
     * @param value The new change-per-input to set.
     */
    public static void setChangePerInput(Double value){
        changePerInput = value;
    }
}
