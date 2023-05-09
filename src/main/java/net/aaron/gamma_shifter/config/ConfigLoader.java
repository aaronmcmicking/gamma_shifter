package net.aaron.gamma_shifter.config;

import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.fabricmc.loader.api.FabricLoader;

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
    private static double presetOneValue = 1.0;
    private static double presetTwoValue = 1.0;

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

            enabled = Boolean.parseBoolean((String) properties.get("enabled"));
            step = Double.parseDouble((String) properties.get("step"));
            alwaysStartEnabled = Boolean.parseBoolean((String) properties.get("alwaysStartEnabled"));
            snappingEnabled = Boolean.parseBoolean((String) properties.get("snappingEnabled"));
            alwaysSaveCustomGamma = Boolean.parseBoolean((String) properties.get("alwaysSaveCustomGamma"));
            presetOneValue = Double.parseDouble((String) properties.get("presetOneValue"));
            presetTwoValue = Double.parseDouble((String) properties.get("presetTwoValue"));


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
        alwaysSaveCustomGamma = GammaHandler.getAlwaysSaveCustomGamma();
        presetOneValue = GammaHandler.getPresetOneValue();
        presetTwoValue = GammaHandler.getPresetTwoValue();

        // write to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            properties.put("enabled", String.valueOf(enabled));
            properties.put("alwaysStartEnabled", String.valueOf(alwaysStartEnabled));
            properties.put("step", String.valueOf(step));
            properties.put("snappingEnabled", String.valueOf(snappingEnabled));
            properties.put("alwaysSaveCustomGamma", String.valueOf(alwaysSaveCustomGamma));
            properties.put("presetOneValue", String.valueOf(presetOneValue));
            properties.put("presetTwoValue", String.valueOf(presetTwoValue));

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
        GammaHandler.setAlwaysSaveCustomGamma(alwaysSaveCustomGamma);
        GammaHandler.setPresetOneValue( clamp(presetOneValue) );
        GammaHandler.setPresetTwoValue( clamp(presetTwoValue) );
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
}
