package doonv.fuzzy_autocomplete.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> ClientConfig.HANDLER.generateGui()
                .generateScreen(parentScreen);
    }
}