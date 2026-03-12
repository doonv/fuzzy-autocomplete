package doonv.fuzzy_autocomplete.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import doonv.fuzzy_autocomplete.FuzzyMatcher;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockStateParser.class)
public class BlockStateParserMixin {

    /**
     * Intercepts the strict `.startsWith()` checks when suggesting block properties
     * (e.g., facing, waterlogged, half) to allow fuzzy matching.
     */
    @WrapOperation(
            method = {
                    "suggestPropertyName",
                    "suggestVaguePropertyName"
            },
            at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z")
    )
    private boolean onBlockStateStartsWith(String instance, String prefix, Operation<Boolean> original) {
        // 'instance' is the property name (e.g. "facing"), 'prefix' is what the player typed.
        return FuzzyMatcher.isFuzzyMatch(prefix, instance);
    }
}