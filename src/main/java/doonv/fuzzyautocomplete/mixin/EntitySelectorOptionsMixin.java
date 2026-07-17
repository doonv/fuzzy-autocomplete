package doonv.fuzzyautocomplete.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import doonv.fuzzyautocomplete.FuzzyMatcher;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
@Mixin(EntitySelectorOptions.class)
public class EntitySelectorOptionsMixin {

	/// Intercepts the strict \`.startsWith()\` check when filtering entity selector names (e.g., limit, distance, type)
	@WrapOperation(
			method = "suggestNames",
			at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z")
	)
	private static boolean onSuggestNamesStartsWith(String instance, String prefix, Operation<Boolean> original) {
		return FuzzyMatcher.isFuzzyMatch(prefix, instance);
	}
}
