package doonv.fuzzyautocomplete.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import doonv.fuzzyautocomplete.FuzzyMatcher;
import doonv.fuzzyautocomplete.FuzzyPatternHolder;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin {
	@Shadow
	@Final
	EditBox input;

	@Shadow
	private CommandSuggestions.SuggestionsList suggestions;

	@Unique
	private String fuzzyautocomplete$currentPattern = "";

	/// Sorts suggestions by heuristic fuzzy relevance.
	/// Extracts the exact search pattern using Brigadier's text ranges instead of spaces.
	@Inject(method = "sortSuggestions", at = @At("HEAD"), cancellable = true)
	private void fuzzySortSuggestions(Suggestions suggestions, CallbackInfoReturnable<List<Suggestion>> cir) {
		if (suggestions.getList().isEmpty()) {
			return;
		}

		// Brigadier exactly knows the bounds of the current argument being typed
		int start = suggestions.getRange().getStart();
		int cursor = this.input.getCursorPosition();

		// Safety bounds check
		if (start > cursor) {
			start = cursor;
		}

		String pattern = this.input.getValue().substring(start, cursor).toLowerCase(Locale.ROOT);

		if (pattern.startsWith("/")) {
			pattern = pattern.substring(1);
		}

		this.fuzzyautocomplete$currentPattern = pattern;

		List<Suggestion> sortedList = new ArrayList<>(suggestions.getList());
		// pattern is already lowercased above; scoreLower avoids re-lowercasing it per suggestion.
		// Caching scores means each suggestion is scored exactly once instead of O(n log n) times in the comparator.
		String lowerPattern = pattern;
		Map<String, Integer> scoreCache = new HashMap<>();
		for (Suggestion suggestion : sortedList) {
			scoreCache.computeIfAbsent(suggestion.getText(), text ->
					FuzzyMatcher.scoreLower(lowerPattern, text.toLowerCase(Locale.ROOT)));
		}
		sortedList.sort((s1, s2) -> Integer.compare(scoreCache.get(s2.getText()), scoreCache.get(s1.getText())));

		cir.setReturnValue(sortedList);
	}

	@Inject(method = "showSuggestions", at = @At("TAIL"))
	private void onShowSuggestions(boolean immediateNarration, CallbackInfo ci) {
		if (this.suggestions != null) {
			((FuzzyPatternHolder) this.suggestions).fuzzyautocomplete$setPattern(
					this.fuzzyautocomplete$currentPattern);
		}
	}
}
