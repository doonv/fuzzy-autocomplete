package doonv.fuzzy_autocomplete;

import doonv.fuzzy_autocomplete.config.ClientConfig;
import net.fabricmc.api.ClientModInitializer;

public class FuzzyAutocompleteClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientConfig.HANDLER.load();
    }
}