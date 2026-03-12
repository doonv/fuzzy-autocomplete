package doonv.fuzzy_autocomplete;

import net.minecraft.client.gui.components.CommandSuggestions;

/**
 * Interface injected into {@link CommandSuggestions.SuggestionsList} to hold the current fuzzy search pattern.
 * Allows decoupling the rendering logic from the input state.
 */
public interface FuzzyPatternHolder {
    void fuzzy_autocomplete$setPattern(String pattern);

    String fuzzy_autocomplete$getPattern();
}