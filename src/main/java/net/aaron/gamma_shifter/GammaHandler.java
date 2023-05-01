package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;

public class GammaHandler {
    /**
     * A helper-class instance that stores and sets gamma values read from options.txt.
     * <p>See also: net.aaron.gamma_shifter.mixin.initGammaMixin</p>
     */
    public static InitGammaHelper initHelper = new InitGammaHelper();

    static MinecraftClient mc = MinecraftClient.getInstance();

    /**
     * The amount to change the gamma value by per input received. It is currently hardcoded but it is planned to be
     * made customizable by the user in future updates.
     */
    public static Double changePerInput = 0.5;

    public static final Double MAX_GAMMA = 10.0;

    /**
     * Stores the current gamma value for when the mod is toggled off
     */
    private static Double currentCustomGamma = 1.0;

    /**
     * Define the decimal format for Doubles when the current gamma is displayed to the user.
     */
    private final static DecimalFormat decFor = new DecimalFormat("0");


    public static void increaseGamma(){
        boolean set = false; // records whether a new value was set
        double new_gamma;
        if(mc.options.getGamma().getValue() <= (MAX_GAMMA - changePerInput)) {
            new_gamma = Math.round((mc.options.getGamma().getValue() + changePerInput) * 100) / 100.0;
            set = true;
        }else{
            new_gamma = 10.0;
            if(mc.options.getGamma().getValue() < 10.0) {
                set = true;
            }
        }
        mc.options.getGamma().setValue(new_gamma);

        // display a message on-screen telling the player the current gamma value
        String msg = "Gamma = " + decFor.format(mc.options.getGamma().getValue()*100) + "%";
        if(mc.player != null) {
            mc.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
        }
        if(set) {
//                    GammaShifter.LOGGER.info("Set gamma to " + mc.options.getGamma().getValue());
            currentCustomGamma = new_gamma;
        }
    }

    public static void decreaseGamma(){
        boolean set = false; // records whether a new value was set
        double new_gamma = 1.0;
        // decrease gamma
        if(mc.options.getGamma().getValue() > changePerInput) {
            // fix round errors with double arithmetic and set the new value
            new_gamma = Math.round((mc.options.getGamma().getValue() - changePerInput)*100)/100.0;
            mc.options.getGamma().setValue(new_gamma);
            set = true;
        }else{
            if(mc.options.getGamma().getValue() > 0.0) {
                set = true;
            }
            new_gamma = 0.0;
            mc.options.getGamma().setValue(new_gamma);
        }

        if(set) {
//                    GammaShifter.LOGGER.info("Set gamma to " + mc.options.getGamma().getValue());
            currentCustomGamma = new_gamma;
        }
        // display a message on-screen telling the player the current gamma value
        if(mc.player != null) {
            String msg = "Gamma = " + decFor.format(mc.options.getGamma().getValue()*100) + "%";
            mc.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
        }
    }

    public static void toggle(){
        mc.options.getGamma().setValue(GammaShifter.isEnabled() ? 1.0 : currentCustomGamma);
        GammaShifter.toggle();
    }


    /**
     * Gets the current custom gamma.
     * @return The current custom gamma.
     */
    public static Double getCurrentCustomGamma() {
        return currentCustomGamma;
    }

    public static void setCurrentCustomGamma(Double value){
        currentCustomGamma = value;
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
//
//    private MinecraftClient mc(){
//        return MinecraftClient.getInstance();
//    }

}
