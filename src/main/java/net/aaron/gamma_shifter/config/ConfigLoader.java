package net.aaron.gamma_shifter.config;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.HUD.HUD;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

/**
 * Reads and writes from the config file ("gamma-shifter.properties"). Loads and sets options upon mod initialization,
 * and writes options when the 'save' button is clicked in ModMenu. If a config file cannot be found, a new one is
 * created with default values.
 * @see ConfigScreenBuilder
 */
public class ConfigLoader {
    /**
     * Initialize and set default values for options saved in the config file.
     */
    private static boolean enabled = true;
    private static boolean alwaysStartEnabled = true;
    private static double step = 0.25;
    private static boolean snappingEnabled = true;
    private static boolean alwaysSaveCustomGamma = true;
    private static double presetOne = 2.5;
    private static double presetTwo = 5.0;
    private static boolean showCurrentGammaOverlay = false;
    private static boolean silentMode = false;
    private static int textColour = 0xFFFFFF;
    private static HUD.Locations location = HUD.Locations.TOP_LEFT;
    private static boolean showMessageOnGammaChange = true;

    /**
     * Initialize the file properties.
     */
    private static final String CONFIG_FILE_NAME = "gamma_shifter.properties";
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME).toFile();

    /**
     * Loads options from the config file using the {@link Properties} class. If a config file cannot be found then
     * a new config file is created and the default config values are saved to it.
     */
    public static void load(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
            properties.load(br);

            // retrieve all the values from the config file
            enabled = Boolean.parseBoolean((String) properties.get("enabled"));
            step = Double.parseDouble((String) properties.get("step"));
            alwaysStartEnabled = Boolean.parseBoolean((String) properties.get("alwaysStartEnabled"));
            snappingEnabled = Boolean.parseBoolean((String) properties.get("snappingEnabled"));
            alwaysSaveCustomGamma = Boolean.parseBoolean((String) properties.get("alwaysSaveCustomGamma"));
            presetOne = Double.parseDouble((String) properties.get("presetOne"));
            presetTwo = Double.parseDouble((String) properties.get("presetTwo"));
            showCurrentGammaOverlay = Boolean.parseBoolean((String) properties.get("showCurrentGammaOverlay"));
            silentMode = Boolean.parseBoolean((String) properties.get("silentMode"));
            textColour = Integer.parseInt((String) properties.get("textColour"));
            location = parseLocation((String) properties.get("location"));
            showMessageOnGammaChange = Boolean.parseBoolean((String) properties.get("showMessageOnGammaChange"));

            // adjust default values for malformed strings (not "true" or "false") that should default to "true"
            fixMalformedBooleans(properties);

            // apply the values in their respective spots
            set();
        }catch(FileNotFoundException e){
            // attempt to create a new config file and save the default values into it
            if(createNewConfigFile()) {
//                GammaShifter.LOGGER.info("[GammaShifterBeta] Couldn't find config file, creating a new one");
                save();
            }else{
                GammaShifter.LOGGER.error("[GammaShifter] Couldn't find config and couldn't create a new one:\n\t" + e);
            }
        }catch(IOException e){ // non-FileNotFoundException IOExceptions
            /* empty catch block */
//            GammaShifter.LOGGER.error("[GammaShifterBeta] Couldn't read config file:\n\t" + e);
        }catch(NullPointerException | NumberFormatException e){ // Parsing exceptions
            /* empty catch block */
            GammaShifter.LOGGER.error("[GammaShifter] Couldn't parse config file... was it malformed?\n\t" + e);
        }
    }

    /**
     * Saves the current settings into the config file. If the config file cannot be found, then a new one is created
     * by {@link FileWriter}.
     */
    public static void save(){
        Properties properties = new Properties();

        // get the current options
        enabled = GammaShifter.isEnabled();
        alwaysStartEnabled = GammaShifter.alwaysStartEnabled();
        step = GammaHandler.getChangePerInput();
        snappingEnabled = GammaHandler.isSnappingEnabled();
        alwaysSaveCustomGamma = GammaShifter.getAlwaysSaveCustomGamma();
        presetOne = GammaHandler.getPresetOne();
        presetTwo = GammaHandler.getPresetTwo();
        showCurrentGammaOverlay = HUD.shouldShowCurrentGammaOverlay();
        silentMode = GammaShifter.isSilentModeEnabled();
        textColour = HUD.getTextColour();
        location = HUD.getCurrentLocation();
        showMessageOnGammaChange = HUD.getShowMessageOnGammaChange();

        // write to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            properties.put("enabled", String.valueOf(enabled));
            properties.put("alwaysStartEnabled", String.valueOf(alwaysStartEnabled));
            properties.put("step", String.valueOf(step));
            properties.put("snappingEnabled", String.valueOf(snappingEnabled));
            properties.put("alwaysSaveCustomGamma", String.valueOf(alwaysSaveCustomGamma));
            properties.put("presetOne", String.valueOf(presetOne));
            properties.put("presetTwo", String.valueOf(presetTwo));
            properties.put("showCurrentGammaOverlay", String.valueOf(showCurrentGammaOverlay));
            properties.put("silentMode", String.valueOf(silentMode));
            properties.put("textColour", String.valueOf(textColour));
            properties.put("location", getLocationString(location));
            properties.put("showMessageOnGammaChange", String.valueOf(showMessageOnGammaChange));

            properties.store(bw, "Gamma Shifter Config");
        }
        catch(IOException e){
            /* empty catch block */
//            GammaShifter.LOGGER.error("[GammaShifterBeta] Couldn't write config file:\n\t" + e);
        }
    }

    /**
     * Attempts to create a new config file.
     * @return True if the file was successfully created, false otherwise.
     */
    private static Boolean createNewConfigFile(){
        try {
            return (new File(CONFIG_FILE.getPath())).createNewFile();
        }catch(Exception e){
//            GammaShifter.LOGGER.error("[GammaShifterBeta] Couldn't create new config file:\n\t" + e);
            return false;
        }
    }

    /**
     * Applies the stored values elsewhere in the mod.
     */
    private static void set(){
        if (alwaysStartEnabled) {
            GammaShifter.setEnabled(true);
        } else {
            GammaShifter.setEnabled(enabled);
        }
        GammaShifter.setAlwaysStartEnabled(alwaysStartEnabled);
        GammaHandler.setChangePerInput( clamp(step, 0.01, GammaHandler.MAX_GAMMA) );
        GammaHandler.setSnappingEnabled(snappingEnabled);
        GammaShifter.setAlwaysSaveCustomGamma(alwaysSaveCustomGamma);
        GammaHandler.setPresetOne( clamp(presetOne) );
        GammaHandler.setPresetTwo( clamp(presetTwo) );
        HUD.setShowCurrentGammaOverlay(showCurrentGammaOverlay);
        GammaShifter.setSilentModeEnabled(silentMode);
        HUD.setTextColour(textColour);
        HUD.setCurrentLocation(location);
        HUD.setShowMessageOnGammaChange(showMessageOnGammaChange);
    }

    /**
     * Parses a {@link net.aaron.gamma_shifter.HUD.HUD.Locations} value from a string.
     * @param str The string containing the location.
     * @return The enumerated location. If a value cannot be parsed,
     * {@link net.aaron.gamma_shifter.HUD.HUD.Locations#TOP_LEFT} is returned by default.
     */
    private static @NotNull HUD.Locations parseLocation(String str){
        if(str == null) return HUD.Locations.TOP_LEFT;
        return switch(str){
            case "TOP_RIGHT" -> HUD.Locations.TOP_RIGHT;
            case "BOTTOM_LEFT" -> HUD.Locations.BOTTOM_LEFT;
            case "BOTTOM_RIGHT" -> HUD.Locations.BOTTOM_RIGHT;
            default -> HUD.Locations.TOP_LEFT; // "TOP_LEFT" or non-parsable entry
        };
    }

    /**
     * Converts a {@link net.aaron.gamma_shifter.HUD.HUD.Locations} value to an equivalent string. If an invalid value is
     * passed into the method, "TOP_LEFT" is returned by default.
     * @param location The value to convert.
     * @return The string containing the converted value.
     */
    private static @NotNull String getLocationString(HUD.Locations location){
        if(location == null) return "TOP_LEFT";
        return switch(location){
            case TOP_RIGHT -> "TOP_RIGHT";
            case BOTTOM_RIGHT -> "BOTTOM_RIGHT";
            case BOTTOM_LEFT -> "BOTTOM_LEFT";
            default -> "TOP_LEFT"; // top left and non-parsable option
        };
    }

    /**
     * Clamps double values to {@link GammaHandler#MIN_GAMMA} and {@link GammaHandler#MAX_GAMMA}.
     * @param value The value to be clamped.
     * @return The clamped value.
     */
    private static double clamp(double value){
        if(value < GammaHandler.MIN_GAMMA) return GammaHandler.MIN_GAMMA;
        if(value > GammaHandler.MAX_GAMMA) return GammaHandler.MAX_GAMMA;
        return value;
    }

    /**
     * Clamps double values to a given minimum/maximum interval.
     * @param value The value to be clamped.
     * @return The clamped value.
     */
    private static double clamp(double value, double min, double max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    /**
     * Missing or malformed options are automatically set to 'false' when {@link Boolean#parseBoolean(String)} fails
     * to parse a valid value. This method checks for that case and sets the corresponding values to 'true' for options
     * which should always default to such.
     * @param properties The properties from the file.
     */
    private static void fixMalformedBooleans(Properties properties){
        if(!"true".equalsIgnoreCase((String) properties.get("enabled")) && !"false".equalsIgnoreCase((String) properties.get("enabled"))){
            enabled = true;
        }
        if(!"true".equalsIgnoreCase((String) properties.get("alwaysStartEnabled")) && !"false".equalsIgnoreCase((String) properties.get("alwaysStartEnabled"))){
            alwaysStartEnabled = true;
        }
        if(!"true".equalsIgnoreCase((String) properties.get("snappingEnabled")) && !"false".equalsIgnoreCase((String) properties.get("snappingEnabled"))){
            snappingEnabled = true;
        }
        if(!"true".equalsIgnoreCase((String) properties.get("alwaysSaveCustomGamma")) && !"false".equalsIgnoreCase((String) properties.get("alwaysSaveCustomGamma"))){
            alwaysSaveCustomGamma = true;
        }
        if(!"true".equalsIgnoreCase((String) properties.get("showMessageOnGammaChange")) && !"false".equalsIgnoreCase((String) properties.get("showMessageOnGammaChange"))){
            showMessageOnGammaChange = true;
        }
    }
}
