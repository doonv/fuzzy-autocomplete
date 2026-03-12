package doonv.fuzzy_autocomplete.mixin;

import doonv.fuzzy_autocomplete.FuzzyMatcher;
import net.minecraft.commands.SharedSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SharedSuggestionProvider.class)
public interface SharedSuggestionProviderMixin {

    /**
     * Intercepts default dynamic resource matching to allow fuzzy matching
     * for registry keys, tags, player names, and custom identifiers.
     */
    @Inject(method = "matchesSubStr", at = @At("HEAD"), cancellable = true)
    private static void onMatchesSubStr(String pattern, String input, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(FuzzyMatcher.isFuzzyMatch(pattern, input));
    }
}