package doonv.fuzzyautocomplete;

import net.minecraft.client.gui.components.CommandSuggestions;

/// Interface injected into [CommandSuggestions.SuggestionsList] to hold the current fuzzy search pattern.
/// Allows decoupling the rendering logic from the input state.
public interface FuzzyPatternHolder {
	void fuzzyautocomplete$setPattern(String pattern);

	String fuzzyautocomplete$getPattern();
}
