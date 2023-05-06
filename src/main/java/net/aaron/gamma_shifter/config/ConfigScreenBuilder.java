package net.aaron.gamma_shifter.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
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
                                        .setSavingRunnable(Config::save);

    /**
     * A new option category.
     */
    ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.gamma_shifter.general"));
    
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
        // toggle mod (boolean button)
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("Enable mod effects"), GammaShifter.isEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("Toggles mod effects on/off"))
                .setSaveConsumer(newValue -> {
                    if(newValue != GammaShifter.isEnabled()) {
                        GammaHandler.toggle();
                    }
                })
                .build());

        // set brightness (field)
        general.addEntry(entryBuilder.startIntField(Text.translatable("Brightness (%)"),
                        (int)(GammaHandler.getCurrentCustomGamma()*100))
                        .setMin(0)
                        .setMax(1000)
                        .setDefaultValue(100)
                        .setTooltip(Text.translatable("The brightness value"))
                        .setSaveConsumer(newValue -> {
                            if(GammaShifter.isEnabled()) {
                                GammaHandler.set((Math.round(newValue)) / 100.0);
                            }else{
                                GammaHandler.setCurrentCustomGamma((Math.round(newValue)) / 100.0);
                            }
                        } )
                        .build()
                );

        // set step (field)
        general.addEntry(entryBuilder.startIntField(Text.translatable("Brightness change per input (%)"),
                            (int)(GammaHandler.getChangePerInput()*100))
                        .setMin(1)
                        .setMax(250)
                        .setDefaultValue(25)
                        .setTooltip(Text.translatable("The percent change in the brightness value when the increase/decrease key is pressed"))
                        .setSaveConsumer(newValue -> GammaHandler.setChangePerInput(Math.round(newValue)/100.0))
                        .build()
                );

        // set alwaysStartEnabled (boolean button)
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("Always start enabled"), GammaShifter.alwaysStartEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("Whether the mod always starts enabled or not"))
                .setSaveConsumer(GammaShifter::setAlwaysStartEnabled)
                .build()
        );

        // return the built screen
        return builder.build();
    }
}
