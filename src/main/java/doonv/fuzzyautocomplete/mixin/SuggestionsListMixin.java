package doonv.fuzzyautocomplete.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import doonv.fuzzyautocomplete.FuzzyHighlighter;
import doonv.fuzzyautocomplete.FuzzyPatternHolder;
import net.minecraft.client.gui.Font;
//? if >= 26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
 *///?}
import net.minecraft.client.gui.components.CommandSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionsListMixin implements FuzzyPatternHolder {

	// 0xFFFFFF00 — the yellow text color Minecraft passes for the currently selected suggestion.
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

	/// Redirects the standard string rendering to intercept text rendering.
	/// Applies precise formatting based on fuzzy match status and selection.
	//? if >= 26.1 {
	@WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
	private void onDrawString(GuiGraphicsExtractor graphics, Font font, String text, int x, int y, int color, Operation<Void> original) {
		boolean isSelected = (color == SELECTED_COLOR);

		graphics.text(font, FuzzyHighlighter.highlightMatch(this.fuzzyautocomplete$pattern, text, isSelected), x, y,
				-1);
	}
	//?} else if >=1.21.6 {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    private void onDrawString(GuiGraphics instance, Font font, String text, int x, int y, int color, Operation<Void> original) {
        boolean isSelected = (color == SELECTED_COLOR);

        instance.drawString(font, FuzzyHighlighter.highlightMatch(this.fuzzyautocomplete$pattern, text, isSelected), x, y, -1);
    }
    *///?} else {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"))
    private int onDrawString(GuiGraphics instance, Font font, String text, int x, int y, int color, Operation<Integer> original) {
        boolean isSelected = (color == SELECTED_COLOR);

        return instance.drawString(font, FuzzyHighlighter.highlightMatch(this.fuzzyautocomplete$pattern, text, isSelected), x, y, -1);
    }
    *///?}
}
