package net.aaron.gamma_shifter;

import net.aaron.gamma_shifter.event.KeyInputHandler;
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
    private static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * The amount to change the gamma value by per input received. Can be modified by user in ModMenu settings.
     */
    public static Double changePerInput = 0.25;

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
     * <p>If the mod is enabled, this should be the same as the value stored {@link GameOptions#getGamma()}.</p>
     */
    public static Double currentCustomGamma = 1.0;

    /**
     * Stores whether changes in gamma value will be snapped to the nearest multiple of {@link GammaHandler#changePerInput}.
     */
    private static boolean snappingEnabled = true;

    private static boolean alwaysSaveCustomGamma = true;

    /**
     * Handles increasing the gamma. Behaves as a wrapper for helper methods to calculate and set gamma and display
     * information to the user.
     * @see GammaHandler#calculateGammaWithoutSnapping(Double, boolean)
     * @see GammaHandler#set(Double)
     * @see GammaHandler#displayGammaMessage()
     */
    public static void increaseGamma(){
        double newGamma = calculateGamma(client.options.getGamma().getValue(), true);
        set(newGamma);
        displayGammaMessage();
    }

    /**
     * Handles decreasing the gamma. Behaves as a wrapper for helper methods to calculate and set gamma and display
     * information to the user.
     * @see GammaHandler#calculateGammaWithoutSnapping(Double, boolean)
     * @see GammaHandler#set(Double)
     * @see GammaHandler#displayGammaMessage()
     */
    public static void decreaseGamma(){
        double newGamma = calculateGamma(client.options.getGamma().getValue(), false);
        set(newGamma);
        displayGammaMessage();
    }

    /**
     * Determines how the gamma will be calculated based off the current gamma, the current step value, and other settings.
     * Wraps other calculating methods so that the caller need not be concerned with how the gamma is calculated.
     * @param oldGamma The previous gamma value.
     * @param increasing True if the gamma should be increased, false otherwise.
     * @return The new gamma value.
     */
    public static Double calculateGamma(Double oldGamma, boolean increasing){
        double newGamma;

        if(increasing){
            if((MAX_GAMMA - oldGamma) <= changePerInput){
                KeyInputHandler.flushBufferedInputs();
                return MAX_GAMMA;
            }
            if(isSnappingEnabled()){
                newGamma = calculateGammaWithSnapping(oldGamma, true);
            }else{
                newGamma = calculateGammaWithoutSnapping(oldGamma, true);
            }
        }else{
            if(oldGamma <= changePerInput) {
                KeyInputHandler.flushBufferedInputs();
                return MIN_GAMMA;
            }
            if(isSnappingEnabled()){
                newGamma = calculateGammaWithSnapping(oldGamma, false);
            }else{
                newGamma = calculateGammaWithoutSnapping(oldGamma, false);
            }
        }
        return newGamma;
    }

    /**
     * Calculates a new gamma value and snaps it to the nearest multiple of {@link GammaHandler#changePerInput}.
     * <p>The caller is responsible for ensuring that adding/subtracting {@link GammaHandler#changePerInput} from
     * the current gamma will not result in a value outside {@link GammaHandler#MIN_GAMMA} and
     * {@link GammaHandler#MAX_GAMMA}.</p>
     * @param oldGamma The previous gamma value.
     * @param increasing True if the gamma should be increased, false otherwise.
     * @return The new gamma value.
     */
    private static Double calculateGammaWithSnapping(Double oldGamma, boolean increasing){
        double newGamma;
        int oldGammaModChangePerInput = (int)Math.round((oldGamma*100)) % (int)Math.round((changePerInput*100));
//        GammaShifter.LOGGER.info((int)Math.round((oldGamma*100)) + " % " + (int)Math.round((changePerInput*100)) + " = " + oldGammaModChangePerInput);
        if(increasing){
            newGamma = Math.round( oldGamma*100 + changePerInput*100 - oldGammaModChangePerInput ) / 100.0;
        }else{
            newGamma = oldGammaModChangePerInput==0 ? (oldGamma*100)-(changePerInput*100) : (oldGamma*100)-oldGammaModChangePerInput;
            newGamma = Math.round( newGamma ) / 100.0;
        }
        return newGamma;
    }

    /**
     * Calculates the new gamma value without snapping it to a particular value.
     * <p>The caller is responsible for ensuring that adding/subtracting {@link GammaHandler#changePerInput} from
     * the current gamma will not result in a value outside {@link GammaHandler#MIN_GAMMA} and
     * {@link GammaHandler#MAX_GAMMA}.</p>
     * @param oldGamma The previous gamma value.
     * @param increasing True if the gamma should be increased, false otherwise.
     * @return The new gamma value.
     */
    public static Double calculateGammaWithoutSnapping(Double oldGamma, boolean increasing){
        double newGamma;
        oldGamma = Math.round(oldGamma * 100) / 100.0;

        if(increasing) {
            newGamma = Math.round((oldGamma + changePerInput) * 100) / 100.0;
        }else{ // else if decreasing
            newGamma = Math.round((oldGamma - changePerInput) * 100) / 100.0;
        }
        return newGamma;
    }

    /**
     * Wrapper method for setting gamma in the Minecraft settings and in {@link GammaHandler#currentCustomGamma}.
     * @param value The gamma value to set.
     */
    public static void set(Double value){
        client.options.getGamma().setValue(value);
        currentCustomGamma = value;
    }

    /**
     * Display a HUD message to the user telling them the current gamma value.
     */
    public static void displayGammaMessage(){
        if(client.player != null) {
            DecimalFormat decFor = new DecimalFormat("0"); // define the Double decimal format (no decimals)
            String msg = "Gamma = " + decFor.format(client.options.getGamma().getValue()*100) + "%"; // build string
            client.player.sendMessage(Text.literal(msg).fillStyle(Style.EMPTY.withColor(Formatting.WHITE)), true);
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
            currentCustomGamma = client.options.getGamma().getValue();
        }
        client.options.getGamma().setValue(GammaShifter.isEnabled() ? 1.0 : currentCustomGamma);
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
    public static void setDefaultVanillaGamma(){
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
     * {@link net.minecraft.client.option.SimpleOption#setValue(Object)}, OR when being updated by ModMenu.</p>
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
     * Gets whether new gamma values should be snapped to the nearest multiple of {@link GammaHandler#changePerInput}.
     * @return True if snapping is enabled, false otherwise.
     */
    public static boolean isSnappingEnabled() {
        return snappingEnabled;
    }

    /**
     * Sets whether new gamma values should be snapped to the nearest multiple of {@link GammaHandler#changePerInput}.
     * <p>Set true if values should be snapped, false otherwise.</p>
     */
    public static void setSnappingEnabled(boolean enabled) {
        snappingEnabled = enabled;
    }

    public static void setAlwaysSaveCustomGamma(boolean value){
            alwaysSaveCustomGamma = value;
    }

    public static boolean getAlwaysSaveCustomGamma(){
        return alwaysSaveCustomGamma;
    }
}
