package doonv.fuzzy_autocomplete.mixin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import doonv.fuzzy_autocomplete.FuzzyMatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(value = LiteralCommandNode.class, remap = false)
public abstract class LiteralCommandNodeMixin<S> {

    @Final
    @Shadow
    private String literalLowerCase;

    @Final
    @Shadow
    private String literal;

    /**
     * Intercepts Brigadier's strict start-of-string matching for root commands
     * and literal arguments, replacing it with fuzzy matching logic.
     */
    @Inject(method = "listSuggestions", at = @At("HEAD"), cancellable = true)
    private void onListSuggestions(CommandContext<S> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        String remaining = builder.getRemainingLowerCase();

        if (FuzzyMatcher.isFuzzyMatch(remaining, this.literalLowerCase)) {
            cir.setReturnValue(builder.suggest(this.literal).buildFuture());
        } else {
            cir.setReturnValue(Suggestions.empty());
        }
    }
}