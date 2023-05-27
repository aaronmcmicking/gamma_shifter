package net.aaron.gamma_shifter.event;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.HUD.HUD;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
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
    public static final String KEY_CATEGORY_GAMMA_SHIFTER = "key.category.gamma_shifter.gamma_shifter";
    public static final String KEY_INCREASE_GAMMA = "key.gamma_shifter.increase_gamma";
    public static final String KEY_DECREASE_GAMMA = "key.gamma_shifter.decrease_gamma";
    public static final String KEY_TOGGLE_MOD = "key.gamma_shifter.toggle_mod";
    public static final String KEY_MAX_GAMMA = "key.gamma_shifter.max_gamma";
    public static final String KEY_DEFAULT_GAMMA = "key.gamma_shifter.default_gamma";
    public static final String KEY_PRESET_ONE = "key.gamma_shifter.preset_one";
    public static final String KEY_PRESET_TWO = "key.gamma_shifter.preset_two";
    public static final String KEY_SHOW_GAMMA = "key.gamma_shifter.show_gamma";

    /**
     * Default keybinds, initialized in {@link KeyInputHandler#registerKeyBinds()}.
     */
    public static KeyBinding increaseGammaKey;
    public static KeyBinding decreaseGammaKey;
    public static KeyBinding toggleModKey;
    public static KeyBinding maxGammaKey;
    public static KeyBinding defaultGammaKey;
    public static KeyBinding presetOneKey;
    public static KeyBinding presetTwoKey;
    public static KeyBinding showGammaKey;

    /**
     * Checks every tick for relevant key presses and calls the appropriate handler/helper method.
     */
    public static void registerKeyInputs(){
        // Detect keypresses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(GammaShifter.isEnabled()){
                // if the mod is enabled, process keybinds
                if(increaseGammaKey.wasPressed()){
                    GammaHandler.increaseGamma();
                }

                if(decreaseGammaKey.wasPressed()){
                    GammaHandler.decreaseGamma();
                }

                if(maxGammaKey.wasPressed()){
                    GammaHandler.applyMaxGamma();
                }

                if(defaultGammaKey.wasPressed()){
                    GammaHandler.applyVanillaMaxGamma();
                }

                if(presetOneKey.wasPressed()){
                    GammaHandler.applyPresetOne();
                }

                if(presetTwoKey.wasPressed()){
                    GammaHandler.applyPresetTwo();
                }

                if(showGammaKey.wasPressed()){
                    HUD.displayGammaMessage(true);
                    // debug (remove for any release)
                    if(MinecraftClient.getInstance().world != null) { // remove before any releases
//                        GammaShifter.LOGGER.info("getTimeOfDay == " + MinecraftClient.getInstance().world.getLevelProperties().getTimeOfDay());
                        GammaShifter.LOGGER.info("isActive == " + AutoNight.isActive());
                    }
                }
            }

            if(toggleModKey.wasPressed()){
                if(!GammaShifter.isEnabled()){
                    // flush buffered inputs when re-enabling mod
                    flushBufferedInputs();
                }
                GammaHandler.toggle();
            }
        });
    }

    /**
     * Creates the default keybinds on mod initialization.
     */
    public static void registerKeyBinds(){
        // set the default keybind to increase gamma to '='
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

        // set the max gamma keybind to ']'
        maxGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_MAX_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the default gamma keybind to '['
        defaultGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DEFAULT_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the default Preset 1 key to be unbound
        presetOneKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_PRESET_ONE,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the default Preset 2 key to be unbound
        presetTwoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_PRESET_TWO,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        // set the show gamma key to be unbound
        showGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SHOW_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        registerKeyInputs();
    }

    /**
     * Flushes any buffered inputs from gamma-modifying keys. Buffered inputs are accrued when a keybind is pressed
     * while the mod is disabled.
     */
    public static void flushBufferedInputs(){
        for(KeyBinding keybinding: new KeyBinding[]{increaseGammaKey, decreaseGammaKey, maxGammaKey, defaultGammaKey, presetOneKey, presetTwoKey, showGammaKey}){
            keybinding.reset();
        }
    }
}
