package net.aaron.gamma_shifter.HUD;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

/**
 * The HUD elements of the mod. Handles rendering overlays and stores option fields related to HUD elements.
 */
public class HUD {

    /**
     * The instance of the Minecraft client.
     */
    private static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * The current location that the persistent overlay is displayed at (if enabled).
     */
    private static Locations currentLocation = Locations.TOP_LEFT;

    /**
     * Enum of locations that the persistent gamma overlay can be rendered at.
     */
    public enum Locations{
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    /**
     * Whether the persistent display should be shown.
     */
    public static boolean showCurrentGammaOverlay = false;

    /**
     * The text colour used in HUD elements.
     */
    private static int textColour = 0xFFFFFF;

    /**
     * Display a HUD message to the user telling them the current gamma value.
     */
    public static void displayGammaMessage(){
        if(client.player != null && !GammaShifter.isSilentModeEnabled()) {
            MutableText messageText = getDisplayGammaMessage();
            client.player.sendMessage(messageText.fillStyle(Style.EMPTY.withColor(textColour)), true);
        }
    }

    /**
     * Constructs the translatable message that displays the gamma to the player.
     * @return The gamma mesage as a translatable string.
     */
    public static MutableText getDisplayGammaMessage(){
        return Text.translatable("message.gamma_shifter.display_current_gamma", Math.round(client.options.getGamma().getValue()*100));
    }

    /**
     * Renders a persistent overlay that shows the gamma in a screen corner at all times (if enabled).
     * <p>Renders every tick.</p>
     * @param matrices The matrix stacked used in by the renderer for Minecraft HUD elements.
     * @see net.aaron.gamma_shifter.mixin.InGameHudMixin
     */
    public static void renderPersistentOverlay(MatrixStack matrices){
        float x, y;
        Window window = client.getWindow();
        float scaledWindowHeight = (float) window.getScaledHeight();
        float scaledWindowWidth = (float) window.getScaledWidth();
        MutableText overlayText = getDisplayGammaMessage(); // translatable
        TextRenderer textRenderer = client.textRenderer;
        float overlayTextWidth = (float) textRenderer.getWidth(overlayText);


        // get the x, y values for the text
        switch (currentLocation) {
            case TOP_LEFT -> {
                x = 2.0f;
                y = 2.0f;
            }
            case TOP_RIGHT -> {
                x = scaledWindowWidth - overlayTextWidth - 2.0f;
                y = 2.0f;
            }
            case BOTTOM_LEFT -> {
                x = 2.0f;
                y = scaledWindowHeight - 10.0f;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWindowWidth - overlayTextWidth - 2.0f;
                y = scaledWindowHeight - 10.0f;
            }
            default -> {
                GammaShifter.LOGGER.error("[GammaShifter] Encountered unexpected value when setting HUD location... disabling HUD");
                setShowCurrentGammaOverlay(false);
                return;
            }
        }

        // render the text
        matrices.push();
        textRenderer.drawWithShadow(matrices, overlayText, x, y, textColour);
        matrices.pop();
    }

    /**
     * Gets the current location of the persistent gamma overlay.
     * @return The current overlay location.
     */
    public static Locations getCurrentLocation(){
        return currentLocation;
    }

    /**
     * Sets the current location for the persistent gamma overlay.
     * @param location The new location.
     */
    public static void setCurrentLocation(Locations location){
        currentLocation = location;
    }

    /**
     * Converts a {@link Locations} value to a user-presentable string.
     * @param location The location to convert.
     * @return The converted string.
     */
    public static String getLocationDisplayString(Locations location){
            switch(location){
                case TOP_RIGHT -> { return "Top Right"; }
                case BOTTOM_LEFT -> { return "Bottom Left"; }
                case BOTTOM_RIGHT -> { return "Bottom Right"; }
                default -> { return "Top Left"; } // TOP_LEFT or unexpected value (default value)
            }
    }

    /**
     * Gets whether the persistent gamma overlay should be shown or not.
     * @return True if the overlay should be rendered, false otherwise.
     */
    public static boolean shouldShowCurrentGammaOverlay(){
        return showCurrentGammaOverlay;
    }

    /**
     * Sets whether the persistent gamma overlay should be shown or not.
     * @param value The new value. True if the overlay should be shown, false otherwise.
     */
    public static void setShowCurrentGammaOverlay(boolean value){
        showCurrentGammaOverlay = value;
    }

    /**
     * Gets the text colour for HUD elements.
     */
    public static int getTextColour(){ return textColour; }

    /**
     * Sets the text colour for HUD elements.
     */
    public static void setTextColour(int colour){
        textColour = colour;
    }

}
