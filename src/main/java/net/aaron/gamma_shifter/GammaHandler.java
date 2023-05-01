package net.aaron.gamma_shifter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
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

    /**
     * The current instance of MinecraftClient, wrapped to improve readability.
     */
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    /**
     * The amount to change the gamma value by per input received. It is currently hardcoded but it is planned to be
     * made customizable by the user in future updates.
     */
    public static Double changePerInput = 0.5;

    /**
     * The maximum gamma value the mod will set.
     */
    public static final Double MAX_GAMMA = 10.0;

    /**
     * Stores the current gamma value for when the mod is toggled off. Initially set when the gamma value read from
     * options.txt is read.
     * <p>Should always be the same as the value stored {@link GameOptions#getGamma()}.</p>
     */
    private static Double currentCustomGamma = 1.0;

    /**
     * Define the decimal format for Doubles when the current gamma is displayed to the user.
     */
    private final static DecimalFormat decFor = new DecimalFormat("0");

    /**
     * Increases the gamma value by the {@link GammaHandler#changePerInput}. Rounds values to whole percents and clamps values to
     * {@link GammaHandler#MAX_GAMMA}.
     */
    public static void increaseGamma(){
        double newGamma, oldGamma = mc.options.getGamma().getValue();
        if(oldGamma <= (MAX_GAMMA - changePerInput)) {
            newGamma = Math.round((oldGamma + changePerInput) * 100) / 100.0; // fix round errors with double arithmetic
        }else{
            newGamma = 10.0;
        }
        set(newGamma);

        // display a message on-screen telling the player the current gamma value
        if(mc.player != null) {
            String msg = "Gamma = " + decFor.format(mc.options.getGamma().getValue()*100) + "%";
            mc.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
        }
    }

    /**
     * Decreases the gamma value by the {@link GammaHandler#changePerInput}. Rounds values to whole percents and clamps
     * values to 0.0.
     */
    public static void decreaseGamma(){
        double newGamma, oldGamma = mc.options.getGamma().getValue();
        // decrease gamma
        if(oldGamma > changePerInput) {
            newGamma = Math.round((mc.options.getGamma().getValue() - changePerInput)*100)/100.0; // fix round errors with double arithmetic
        }else{
            newGamma = 0.0;
        }
        set(newGamma);

        // display a message on-screen telling the player the current gamma value
        if(mc.player != null) {
            String msg = "Gamma = " + decFor.format(newGamma*100) + "%";
            mc.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
        }
    }

    /**
     * Wrapper method for setting gamma in the Minecraft settings and in {@link GammaHandler#currentCustomGamma}.
     * @param value The gamma value to set.
     */
    private static void set(Double value){
        mc.options.getGamma().setValue(value);
        currentCustomGamma = value;
    }

    /**
     * Toggles the mod effects on/off by setting gamma to either 0.0 or {@link GammaHandler#currentCustomGamma} and
     * toggling {@link GammaShifter}.
     */
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

    /**
     * Sets {@link GammaHandler#currentCustomGamma} manually. Should only be called when the game is initializing.
     * <p>Otherwise, {@link GammaHandler#currentCustomGamma} should only be set in conjunction with a call to
     * {@link net.minecraft.client.option.SimpleOption#setValue(Object)}.</p>
     * @param value The value to set.
     */
    public static void setCurrentCustomGamma(Double value){
        currentCustomGamma = value;
    }

    /**
     * Get the amount to change the gamma by per input.
     * @return The change-per-input.
     */
    public static Double getChangePerInput(){
        return changePerInput;
    }

    /**
     * Sets the amount to change the gamma by per input.
     * @param value The new change-per-input to set.
     */
    public static void setChangePerInput(Double value){
        changePerInput = value;
    }
}
