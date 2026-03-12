package doonv.fuzzy_autocomplete.mixin.client;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import doonv.fuzzy_autocomplete.FuzzyMatcher;
import doonv.fuzzy_autocomplete.FuzzyPatternHolder;
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
import java.util.List;
import java.util.Locale;

@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin {
    @Shadow
    @Final
    EditBox input;

    @Shadow
    private CommandSuggestions.SuggestionsList suggestions;

    @Unique
    private String fuzzy_autocomplete$currentPattern = "";

    /**
     * Sorts suggestions by heuristic fuzzy relevance.
     * Extracts the exact search pattern using Brigadier's text ranges instead of spaces.
     */
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

        this.fuzzy_autocomplete$currentPattern = pattern;

        List<Suggestion> sortedList = new ArrayList<>(suggestions.getList());
        String finalPattern = pattern;
        sortedList.sort((s1, s2) -> {
            int score1 = FuzzyMatcher.score(finalPattern, s1.getText());
            int score2 = FuzzyMatcher.score(finalPattern, s2.getText());
            return Integer.compare(score2, score1);
        });

        cir.setReturnValue(sortedList);
    }

    @Inject(method = "showSuggestions", at = @At("TAIL"))
    private void onShowSuggestions(boolean immediateNarration, CallbackInfo ci) {
        if (this.suggestions != null) {
            ((FuzzyPatternHolder) this.suggestions).fuzzy_autocomplete$setPattern(this.fuzzy_autocomplete$currentPattern);
        }
    }
}