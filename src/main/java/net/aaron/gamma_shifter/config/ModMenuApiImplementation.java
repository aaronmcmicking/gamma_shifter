package net.aaron.gamma_shifter.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuApiImplementation implements ModMenuApi {

        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory(){
            return parent -> (new ScreenBuilder()).build();
        }
}
