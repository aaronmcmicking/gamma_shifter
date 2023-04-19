package net.aaron.gamma_shifter.event;

//import net.aaron.gamma_shifter.HUD.HUD;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.Optional;


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
                // set gamma to max
                client.options.getGamma().setValue(client.options.getGamma().getValue() + .2);

                String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
              }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(decreaseGammaKey.wasPressed()){
                // decrease gamma
                if(client.options.getGamma().getValue() > .2) {
                    client.options.getGamma().setValue(client.options.getGamma().getValue() - .2);
                }else{
                    client.options.getGamma().setValue(0.0);
                }
                String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%";
                client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);

            }
        });
    }

    public static void register(){
        increaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_INCREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        decreaseGammaKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DECREASE_GAMMA,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                KEY_CATEGORY_GAMMA_SHIFTER
        ));

        registerKeyInputs();
    }
}
