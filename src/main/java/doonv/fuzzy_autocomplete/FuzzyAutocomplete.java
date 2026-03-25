package doonv.fuzzy_autocomplete;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuzzyAutocomplete implements ModInitializer {
    public static final String MOD_ID = /*$ mod_id*/ "fuzzy-autocomplete";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final String VERSION = /*$ mod_version*/ "1.0.0";

    public static final String MINECRAFT = /*$ minecraft*/ "26.1";

    @Override
    public void onInitialize() {}
}