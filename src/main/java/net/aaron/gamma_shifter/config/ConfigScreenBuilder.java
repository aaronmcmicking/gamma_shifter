package net.aaron.gamma_shifter.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.aaron.gamma_shifter.HUD.HUD;
import net.aaron.gamma_shifter.event.AutoNight;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * A custom config screen builder that uses the Cloth Config API to create a custom {@link Screen} object using
 * Cloth Config helper methods. This screen is then displayed by ModMenu when requested.
 */
public class ConfigScreenBuilder {
    /**
     * The config builder used to create other Cloth Config objects and builders.
     */
    ConfigBuilder builder = ConfigBuilder.create()
                                        .setParentScreen(MinecraftClient.getInstance().currentScreen)
                                        .setTitle(Text.translatable("title.gamma_shifter.config"))
                                        .setDoesConfirmSave(false)
                                        .setSavingRunnable(ConfigLoader::save);

    /**
     * Config menu categories.
     */
    ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.gamma_shifter.general"));
    ConfigCategory HUDCategory = builder.getOrCreateCategory(Text.translatable("category.gamma_shifter.HUD"));
    ConfigCategory autoNightMode = builder.getOrCreateCategory(Text.translatable("category.gamma_shifter.auto_night"));
    ConfigCategory presets = builder.getOrCreateCategory(Text.translatable("category.gamma_shifter.presets"));

    
    /**
     * A builder used to create new "entries"/modifiable options on the options screen.
     */
    ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    /**
     * Constructs a new options screen.
     * <p>As per Cloth Config specifications, a new screen must be build every time the config menu is opened.</p>
     * @return The options screen.
     */
    public Screen getGammaShifterOptionsMenu() {

        // GENERAL OPTIONS

        // toggle mod (boolean button)
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.toggle_mod"), GammaShifter.isEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.gamma_shifter.toggle_mod.tooltip"))
                .setSaveConsumer(newValue -> {
                    if(newValue != GammaShifter.isEnabled()) {
                        GammaHandler.toggle();
                    }
                })
                .build());

        // set brightness (field)
        general.addEntry(entryBuilder.startIntField(Text.translatable("config.gamma_shifter.gamma"), (int)Math.round(GammaHandler.getCurrentCustomGamma()*100))
                .setMin(GammaHandler.shouldEnforceBounds () ? (int)(GammaHandler.MIN_GAMMA*100) : Integer.MIN_VALUE)
                .setMax(GammaHandler.shouldEnforceBounds() ? (int)(GammaHandler.MAX_GAMMA*100) : Integer.MAX_VALUE)
                .setDefaultValue((int)(GammaHandler.MAX_GAMMA*100))
                .setTooltip(Text.translatable("config.gamma_shifter.gamma.tooltip"))
                .setSaveConsumer(newValue -> {
                    if(GammaShifter.isEnabled()) {
                        if(AutoNight.isActive() && newValue != (int)Math.round(GammaHandler.getCurrentCustomGamma()*100)){
                            GammaShifter.LOGGER.info("newValue = " + newValue + ", rounded CCG = " + (int)Math.round(GammaHandler.getCurrentCustomGamma()*100));
//                            AutoNight.setIsActive(false);
                            GammaHandler.set((Math.round(newValue)) / 100.0);
                        }
                    }else{
                        GammaHandler.setCurrentCustomGamma((Math.round(newValue)) / 100.0);
                    }
                } )
                .build()
        );

        // set step (field)
        general.addEntry(entryBuilder.startIntField(Text.translatable("config.gamma_shifter.change_per_input"),
                        (int)Math.round(GammaHandler.getChangePerInput()*100))
                .setMin(1)
                .setMax((int)(GammaHandler.MAX_GAMMA*100))
                .setDefaultValue(25)
                .setTooltip(Text.translatable("config.gamma_shifter.change_per_input.tooltip"))
                .setSaveConsumer(newValue -> GammaHandler.setChangePerInput(Math.round(newValue)/100.0))
                .build()
        );

        // set alwaysStartEnabled (boolean button)
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.always_start_enabled"), GammaShifter.alwaysStartEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.gamma_shifter.always_start_enabled.tooltip"))
                .setSaveConsumer(GammaShifter::setAlwaysStartEnabled)
                .build()
        );

        // set enableSnapping (boolean button)
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.snap"), GammaHandler.isSnappingEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.gamma_shifter.snap.tooltip"))
                .setSaveConsumer(GammaHandler::setSnappingEnabled)
                .build()
        );

        // set alwaysSaveCustomGamma
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.always_save_custom_brightness"), GammaShifter.getAlwaysSaveCustomGamma())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.gamma_shifter.always_save_custom_brightness.tooltip"))
                .setSaveConsumer(GammaShifter::setAlwaysSaveCustomGamma)
                .build()
        );

        // toggle bounds enforcing (boolean)
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.enforce_bounds"), GammaHandler.shouldEnforceBounds())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.gamma_shifter.enforce_bounds.tooltip"))
                .setSaveConsumer(GammaHandler::setShouldEnforceBounds)
                .build()
        );


        // HUD OPTIONS

        // toggle silent mode (boolean)
        HUDCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.silent_mode"), GammaShifter.isSilentModeEnabled())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.gamma_shifter.silent_mode.tooltip"))
                .setSaveConsumer(GammaShifter::setSilentModeEnabled)
                .build()
        );

        // toggle persistent gamma display (boolean)
        HUDCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.show_current_gamma_overlay"), HUD.shouldShowCurrentGammaOverlay())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.gamma_shifter.show_current_gamma_overlay.tooltip"))
                .setSaveConsumer(HUD::setShowCurrentGammaOverlay)
                .build()
        );

        // set the text colour for HUD elements (colour field)
        HUDCategory.addEntry(entryBuilder.startColorField(Text.translatable("config.gamma_shifter.textColour"), HUD.getTextColour())
                .setDefaultValue(0xFFFFFF)
                .setTooltip(Text.translatable("config.gamma_shifter.textColour.tooltip"))
                .setSaveConsumer(HUD::setTextColour)
                .build()
        );

        // Set overlay location
        // Does not currently support translations for button text
        String[] list = {"Top Left", "Top Right", "Bottom Right", "Bottom Left"};
        HUDCategory.addEntry(entryBuilder.startSelector(Text.translatable("config.gamma_shifter.persistent_overlay_location"), list, HUD.getLocationDisplayString(HUD.getCurrentLocation()))
                .setDefaultValue("Top Left")
                .setTooltip(Text.translatable("config.gamma_shifter.persistent_overlay_location.tooltip"))
                .setSaveConsumer(newValue -> {
                    HUD.setCurrentLocation(
                            switch (newValue) {
                                case "Bottom Left" -> HUD.Locations.BOTTOM_LEFT;
                                case "Bottom Right" -> HUD.Locations.BOTTOM_RIGHT;
                                case "Top Right" -> HUD.Locations.TOP_RIGHT;
                                default -> HUD.Locations.TOP_LEFT;
                    });
                })
                .build()
        );

        HUDCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.show_message_on_gamma_change"), HUD.getShowMessageOnGammaChange())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.gamma_shifter.show_message_on_gamma_change.tooltip"))
                .setSaveConsumer(HUD::setShowMessageOnGammaChange)
                .build()
        );

        // AUTO NIGHT OPTIONS

        // toggle auto night mode (boolean)
        autoNightMode.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.gamma_shifter.auto_night"), AutoNight.isEnabled())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.gamma_shifter.auto_night.tooltip"))
                .setSaveConsumer(newValue -> {
                    if(newValue && !AutoNight.isEnabled()) {
                        AutoNight.setAlreadyInitialized(false);
                    }
                    AutoNight.setEnabled(newValue);
                })
                .build()
        );

        // set auto night mode gamma value (int field)
        autoNightMode.addEntry(entryBuilder.startIntField(Text.translatable("config.gamma_shifter.auto_night_value"), (int)(AutoNight.getNightGammaValue()*100))
                .setDefaultValue(300)
                .setMin((int)(GammaHandler.MIN_GAMMA*100))
                .setMax((int)(GammaHandler.MAX_GAMMA*100))
                .setTooltip(Text.translatable("config.gamma_shifter.auto_night_value.tooltip"))
                .setSaveConsumer(newValue -> {
                    AutoNight.setNightGammaValue(Math.round(newValue) / 100.0);
                    if(AutoNight.isActive()){
                        AutoNight.update();
                    }
                })
                .build()
        );


        // PRESET OPTIONS

        // set presetOneValue (int field)
        presets.addEntry(entryBuilder.startIntField(Text.translatable("config.gamma_shifter.preset_one"), (int)Math.round(GammaHandler.getPresetOne()*100))
                .setDefaultValue(250)
                .setMin((int)(GammaHandler.MIN_GAMMA*100))
                .setMax((int)(GammaHandler.MAX_GAMMA*100))
                .setTooltip(Text.translatable("config.gamma_shifter.preset_one.tooltip"))
                .setSaveConsumer(newValue -> GammaHandler.setPresetOne(Math.round(newValue) / 100.0))
                .build()
        );

        // set presetTwoValue (int field)
        presets.addEntry(entryBuilder.startIntField(Text.translatable("config.gamma_shifter.preset_two"), (int)Math.round(GammaHandler.getPresetTwo()*100))
                .setDefaultValue(500)
                .setMin((int)(GammaHandler.MIN_GAMMA*100))
                .setMax((int)(GammaHandler.MAX_GAMMA*100))
                .setTooltip(Text.translatable("config.gamma_shifter.preset_two.tooltip"))
                .setSaveConsumer(newValue -> GammaHandler.setPresetTwo(Math.round(newValue) / 100.0))
                .build()
        );


        // return the built screen
        return builder.build();
    }
}
