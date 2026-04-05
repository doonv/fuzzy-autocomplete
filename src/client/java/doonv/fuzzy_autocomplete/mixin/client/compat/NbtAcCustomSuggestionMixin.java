package doonv.fuzzy_autocomplete.mixin.client.compat;

import doonv.fuzzy_autocomplete.FuzzyMatcher;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Pseudo
@Mixin(targets = "com.mt1006.nbt_ac.autocomplete.suggestions.CustomSuggestion", remap = false)
public class NbtAcCustomSuggestionMixin {

    /**
     * Replaces <a href="https://github.com/mt1006/mc-nbtac-mod">NBT Autocomplete</a>'s matching with {@link FuzzyMatcher#isFuzzyMatch}.
     */
    @Dynamic
    @Inject(
            // https://github.com/mt1006/mc-nbtac-mod/blob/1476f049477ebe8daf3a7c28b6362af9cbe9c32d/common/src/main/java/com/mt1006/nbt_ac/autocomplete/suggestions/CustomSuggestion.java#L58
            method = "matchPrefix(Ljava/lang/String;Ljava/lang/String;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onNbtAcMatchPrefix(String str, String prefix, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(FuzzyMatcher.isFuzzyMatch(prefix, str));
    }
}