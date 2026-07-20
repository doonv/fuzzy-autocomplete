//? fabric {
package doonv.fuzzyautocomplete.mixin.client.compat;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import doonv.fuzzyautocomplete.FuzzyMatcher;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Pseudo
@Mixin(targets = "com.mt1006.nbt_ac.autocomplete.suggestions.CustomSuggestion", remap = false)
public class NbtAcCustomSuggestionMixin {

	/// Replaces [NBT Autocomplete](https://github.com/mt1006/mc-nbtac-mod)'s matching with [FuzzyMatcher#isFuzzyMatch].
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
//?}
