package net.aaron.gamma_shifter.HUD;

import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class HUD {

    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static Locations currentLocation = Locations.TOP_LEFT;

    public enum Locations{
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

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

    public static MutableText getDisplayGammaMessage(){
        return Text.translatable("message.gamma_shifter.display_current_gamma", Math.round(client.options.getGamma().getValue()*100));
    }

    public static void renderPersistentOverlay(MatrixStack matrices){
        float x, y;
        Window window = client.getWindow();
        float scaledHeight = (float) window.getScaledHeight();
        float scaledWidth = (float) window.getScaledWidth();
        MutableText overlayText = getDisplayGammaMessage();
        TextRenderer textRenderer = client.textRenderer;

        // get the x, y values for the text
        switch (currentLocation) {
            case TOP_LEFT -> {
                x = 2.0f;
                y = 2.0f;
            }
            case TOP_RIGHT -> {
                x = scaledWidth - (float) textRenderer.getWidth(overlayText) - 2.0f;
                y = 2.0f;
            }
            case BOTTOM_LEFT -> {
                x = 2.0f;
                y = scaledHeight - 10.0f;
            }
            case BOTTOM_RIGHT -> {
                x = scaledWidth - (float) textRenderer.getWidth(overlayText) - 2.0f;
                y = scaledHeight - 10.0f;
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

    public static Locations getCurrentLocation(){
        return currentLocation;
    }

    public static void setCurrentLocation(Locations location){
        currentLocation = location;
    }

    public static String getLocationString(Locations location){
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
