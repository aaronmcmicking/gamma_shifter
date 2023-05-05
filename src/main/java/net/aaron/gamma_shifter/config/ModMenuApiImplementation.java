package net.aaron.gamma_shifter.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Implements support for ModMenuApi to add support for ModMenu's custom options screen (in conjunction with custom
 * config screen made with Cloth Config)
 */

public class ModMenuApiImplementation implements ModMenuApi {
    // get the custom config screen created using Cloth Config
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory(){
        return parent -> (new ConfigScreenBuilder()).getGammaShifterOptionsMenu(); // a new instance is required every time the screen is opened
    }
}
