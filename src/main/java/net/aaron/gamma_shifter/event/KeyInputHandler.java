package net.aaron.gamma_shifter.event;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 *   Sets default keybinds and detects key presses.
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
     * Default keybinds, initialized in {@link KeyInputHandler#registerKeyBinds()}.
     */
    public static KeyBinding increaseGammaKey;
    public static KeyBinding decreaseGammaKey;
    public static KeyBinding toggleModKey;

    /**
     * Checks every tick if the keybinds to increase/decrease gamma have been pressed and calls the appropriate handler/helper method.
     */
    public static void registerKeyInputs(){
        // Detect keypresses for increasing the gamma
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(increaseGammaKey.wasPressed() && GammaShifter.isEnabled()){
                GammaHandler.increaseGamma();
              }
        });

        // Detect keypresses for decreasing the gamma
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(decreaseGammaKey.wasPressed() && GammaShifter.isEnabled()){
                GammaHandler.decreaseGamma();
            }
        });

        // Detect keypresses for toggle mod effects
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(toggleModKey.wasPressed()){
                GammaHandler.toggle();
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
}
