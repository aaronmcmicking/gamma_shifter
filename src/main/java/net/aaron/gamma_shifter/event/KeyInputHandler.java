package net.aaron.gamma_shifter.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_GAMMA_SHIFTER = "key.gamma_shifter.category.gamma_shifter";
    public static final String KEY_INCREASE_GAMMA = "key.gamma_shifter.increase_gamma";
    public static final String KEY_DECREASE_GAMMA = "key.gamma_shifter.decrease_gamma";

    public static KeyBinding increaseGammaKey;
    public static KeyBinding decreaseGammaKey;

    public static void registerKeyInputs(){

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(increaseGammaKey.wasPressed()){
                // increase gamma
                client.player.sendChatMessage("Increase key pressed", null);
                if(client.options.getGamma().getValue()  <= 4.8) {
                    client.options.getGamma().setValue(client.options.getGamma().getValue() + .2);
                }
                int gamma_val = (int)Math.round(client.options.getGamma().getValue()*100);
                client.player.sendChatMessage("Gamma = " + gamma_val + "%", null);
              }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(decreaseGammaKey.wasPressed()){
                // increase gamma
//                client.player.sendChatMessage("Decrease key pressed", null);

                if(client.options.getGamma().getValue() > .2) {
                    client.options.getGamma().setValue(client.options.getGamma().getValue() - .2);
                }
                int gamma_val = (int)Math.round(client.options.getGamma().getValue()*100);
                client.player.sendChatMessage("Gamma = " + gamma_val + "%", null);

            }
        });
    }

    public static void register(){
        increaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_INCREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        decreaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DECREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        registerKeyInputs();
    }
}
