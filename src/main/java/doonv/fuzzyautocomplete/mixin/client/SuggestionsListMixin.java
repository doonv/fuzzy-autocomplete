package doonv.fuzzyautocomplete.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import doonv.fuzzyautocomplete.FuzzyHighlighter;
import doonv.fuzzyautocomplete.FuzzyPatternHolder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CommandSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionsListMixin implements FuzzyPatternHolder {
	@Unique
	private static final int SELECTED_COLOR = -256;

	@Unique
	private String fuzzyautocomplete$pattern = "";

	@Override
	public void fuzzyautocomplete$setPattern(String pattern) {
		this.fuzzyautocomplete$pattern = pattern;
	}

	@Override
	public String fuzzyautocomplete$getPattern() {
		return this.fuzzyautocomplete$pattern;
	}

	//~ if >=1.21.6 'III)I"' -> 'III)V"', 'private int' -> 'private void', 'Operation<Integer>' -> 'Operation<Void>', 'return' -> '/*return*/' {
	//~ if >=26.1 'render' -> 'extractRenderState', 'drawString' -> 'text', 'instance' -> 'graphics' {
	@WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
	private void onDrawString(GuiGraphicsExtractor graphics, Font font, String text, int x, int y, int color, Operation<Void> original) {
		boolean isSelected = (color == SELECTED_COLOR);

		/*return*/ graphics.text(font, FuzzyHighlighter.highlightMatch(this.fuzzyautocomplete$pattern, text, isSelected), x, y, -1);
	}
	//~}
	//~}
}
