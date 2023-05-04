package net.aaron.gamma_shifter.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.aaron.gamma_shifter.GammaHandler;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class ScreenBuilder {
        ConfigBuilder builder = ConfigBuilder.create()
                                            .setParentScreen(MinecraftClient.getInstance().currentScreen)
                                            .setTitle(Text.translatable("title.gamma_shifter.config"))
                                            .setDoesConfirmSave(false)
                                            .setSavingRunnable(() -> {
                                                // Serialise the config into the config file. This will be called last after all variables are updated.
                                            });

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.gamma_shifter.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        SimpleOption<Double> gamma = MinecraftClient.getInstance().options.getGamma();
        Integer tempCurrentValue;


        public Screen build() {
            general.addEntry(entryBuilder.startIntSlider(Text.translatable("Brightness"),
                            (int) (MinecraftClient.getInstance().options.getGamma().getValue() * 100),
                            (int) (double) GammaHandler.MIN_GAMMA*100,
                            (int) (double) GammaHandler.MAX_GAMMA*100)
                    .setDefaultValue(100)
                    .setTooltip(Text.translatable("The brightness value"))
                    .setSaveConsumer(newValue -> tempCurrentValue = newValue)
                    .build());

            general.addEntry(entryBuilder.startIntSlider(Text.translatable("Brightness change per input"),
                    (int)(double)(GammaHandler.getChangePerInput()*100),
                    0,
                    250)
                    .setDefaultValue(50)
                    .setTooltip(Text.translatable("The amount the brightness value changes by when the increase/decrease key is pressed"))
                    .setSaveConsumer(newValue -> GammaHandler.changePerInput = newValue/100.0)
                    .build());

            return builder.build();
        }
}
