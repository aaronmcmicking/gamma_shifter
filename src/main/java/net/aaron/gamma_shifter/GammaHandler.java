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
     * The minimum gamma value the mod will set.
     */
    public static final Double MIN_GAMMA = 0.0;

    /**
     * Stores the current gamma value for when the mod is toggled off. Initially set when the gamma value read from
     * options.txt is read.
     * <p>Should always be the same as the value stored {@link GameOptions#getGamma()}.</p>
     */
    private static Double currentCustomGamma = 1.0;

    /**
     * Handles increasing the gamma. Behaves as a wrapper for helper methods to calculate and set gamma and display
     * information to the user.
     * @see GammaHandler#calculateNewGamma(Double, boolean)
     * @see GammaHandler#set(Double)
     * @see GammaHandler#displayGammaMessage()
     */
    public static void increaseGamma(){
        double newGamma = calculateNewGamma(mc.options.getGamma().getValue(), true);
        set(newGamma);
        displayGammaMessage();
    }

    /**
     * Decreases the gamma value by the {@link GammaHandler#changePerInput}. Rounds values to whole percents and clamps
     * values to 0.0.
     */
    public static void decreaseGamma(){
        double newGamma = calculateNewGamma(mc.options.getGamma().getValue(), false);
        set(newGamma);
        displayGammaMessage();
    }

    /**
     * Calculates the new gamma value and snaps it to the nearest 0.5. Values are clamped to {@link GammaHandler#MIN_GAMMA}
     * and {@link GammaHandler#MAX_GAMMA}.
     * <p>Does not support custom step values (ie. {@link GammaHandler#changePerInput} must be 0.5) and does not support
     * the ability to set gamma values less than 0.0.</p>
     * @param oldGamma The previous gamma.
     * @param increasing Whether the gamma should be increased or decreased.
     * @return The new gamma value.
     */
    public static Double calculateNewGamma(Double oldGamma, boolean increasing){
        double newGamma;
        oldGamma = Math.round(oldGamma * 100) / 100.0;
        
        if(increasing) {
            if((MAX_GAMMA - oldGamma) <= changePerInput){ return MAX_GAMMA; }
            if (oldGamma % 0.5 >= 0.25) {
                oldGamma -= 0.25;
            }
            newGamma = Math.round((oldGamma + changePerInput) * 100) / 100.0;
        }else{ // else if decreasing
            if(oldGamma <= changePerInput) { return MIN_GAMMA; }
            if(oldGamma % 0.5 < 0.25 && oldGamma % 0.5 != 0.0){
                oldGamma += 0.25;
            }
            newGamma = Math.round((oldGamma - changePerInput) * 100) / 100.0;
        }
        newGamma = roundToHalf(newGamma);
        return newGamma;
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
     * Display a HUD message to the user telling them the current gamma value.
     */
    public static void displayGammaMessage(){
        if(mc.player != null) {
            DecimalFormat decFor = new DecimalFormat("0"); // define the Double decimal format (no decimals)
            String msg = "Gamma = " + decFor.format(mc.options.getGamma().getValue()*100) + "%"; // build string
            mc.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
        }
    }

    /**
     * Toggles the mod effects on/off by setting gamma to either 0.0 or {@link GammaHandler#currentCustomGamma} and
     * toggling {@link GammaShifter}.
     * <p>Also stores the latest gamma value locally (fixes compatibility issue with Sodium)</p>
     */
    public static void toggle(){
        // make sure the latest gamma value is stored locally before overwriting it
        if(GammaShifter.isEnabled()){
            currentCustomGamma = mc.options.getGamma().getValue();
        }
        mc.options.getGamma().setValue(GammaShifter.isEnabled() ? 1.0 : currentCustomGamma);
        GammaShifter.toggle();
    }

    /**
     * Sets the gamma to the maxiumum allowed value.
     */
    public static void setMaxGamma(){
        set(MAX_GAMMA);
        displayGammaMessage();
    }

    /**
     * Sets the gamma to the default maximum value (100%).
     */
    public static void setDefaultGamma(){
        set(1.0); // Default MC max gamma
        displayGammaMessage();
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

    /**
     * Rounds a Double to the nearest 0.5. For example:
     * <p>1.1 -> 1.0</p><p>1.4 -> 1.5</p><p>1.75 -> 2.0</p>
     * @param x The value to be rounded.
     * @return The rounded value.
     */
    private static Double roundToHalf(Double x){
        return Math.round(x * 2) / 2.0;
    }
}
