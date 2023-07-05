package net.aaron.gamma_shifter.HUD;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

/**
 * The HUD elements of the mod. Handles rendering overlays and stores option fields related to HUD elements.
 */
public class HUD {

    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static Locations currentOverlayLocation = Locations.TOP_LEFT;

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
    private static boolean showCurrentGammaOverlay = false;

    private static int textColour = 0xFFFFFF; // default = white

    /**
     * True if a message should be shown above the hotbar when the gamma is changed, false otherwise.
     */
    private static boolean showMessageOnGammaChange = true;

    /**
     * Overloaded wrapper for {@link HUD#displayGammaMessage(boolean)} to exclude need to pass a boolean in (as this
     * parameter is 'false' in the majority of cases.
     */
    public static void displayGammaMessage(){
        displayGammaMessage(false);
    }

    /**
     * Display a HUD message to the user telling them the current gamma value.
     * @param force Overrides {@link HUD#showMessageOnGammaChange} being set to 'false' to allow a message to still be
     *              displayed if desired. Cannot override {@link GammaShifter#isSilentModeEnabled()} being 'true'.
     */
    public static void displayGammaMessage(boolean force){
        if((client.player != null && !GammaShifter.isSilentModeEnabled()) && (showMessageOnGammaChange || force)) {
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
     * @param context The DrawContext used in by the renderer for Minecraft HUD elements.
     * @see net.aaron.gamma_shifter.mixin.InGameHudMixin
     */
    public static void renderPersistentOverlay(DrawContext context){
        float x, y;
        Window window = client.getWindow();
        float scaledWindowHeight = (float) window.getScaledHeight();
        float scaledWindowWidth = (float) window.getScaledWidth();
        MutableText overlayText = getDisplayGammaMessage(); // translatable
        TextRenderer textRenderer = client.textRenderer;
        float overlayTextWidth = (float) textRenderer.getWidth(overlayText);

        final float OFFSET = 2.0f;
        final float LARGE_OFFSET = 10.0f;

        // get the x, y values for the text
        switch (currentOverlayLocation) {
            case TOP_LEFT -> {
                x = OFFSET;
                y = OFFSET;
            }
            case TOP_RIGHT -> {
                x = scaledWindowWidth - overlayTextWidth - OFFSET;
                y = OFFSET;
            }
            case BOTTOM_LEFT -> {
                x = OFFSET;
                y = scaledWindowHeight - LARGE_OFFSET;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWindowWidth - overlayTextWidth - OFFSET;
                y = scaledWindowHeight - LARGE_OFFSET;
            }
            default -> {
                GammaShifter.LOGGER.error("[GammaShifter] Encountered unexpected value when setting overlay location... disabling overlay");
                setShowCurrentGammaOverlay(false);
                return;
            }
        }

        // render the text
        context.drawText(textRenderer, overlayText, (int)x, (int)y, textColour, false);
    }

    /**
     * Gets the current location of the persistent gamma overlay.
     * @return The current overlay location.
     */
    public static Locations getCurrentOverlayLocation(){
        return currentOverlayLocation;
    }

    /**
     * Sets the current location for the persistent gamma overlay.
     * @param location The new location.
     */
    public static void setCurrentOverlayLocation(Locations location){
        currentOverlayLocation = location;
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

    public static boolean shouldShowCurrentGammaOverlay(){
        return showCurrentGammaOverlay;
    }

    public static void setShowCurrentGammaOverlay(boolean value){
        showCurrentGammaOverlay = value;
    }

    public static int getTextColour(){ return textColour; }

    public static void setTextColour(int colour){
        textColour = colour;
    }

    public static void setShowMessageOnGammaChange(boolean value){
        showMessageOnGammaChange = value;
    }

    public static boolean getShowMessageOnGammaChange(){
        return showMessageOnGammaChange;
    }

}
