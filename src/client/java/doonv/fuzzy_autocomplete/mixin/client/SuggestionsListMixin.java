package doonv.fuzzy_autocomplete.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import doonv.fuzzy_autocomplete.FuzzyHighlighter;
import doonv.fuzzy_autocomplete.FuzzyPatternHolder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CommandSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionsListMixin implements FuzzyPatternHolder {

    @Unique
    private String fuzzy_autocomplete$pattern = "";

    @Override
    public void fuzzy_autocomplete$setPattern(String pattern) {
        this.fuzzy_autocomplete$pattern = pattern;
    }

    @Override
    public String fuzzy_autocomplete$getPattern() {
        return this.fuzzy_autocomplete$pattern;
    }

    /**
     * Redirects the standard string rendering to intercept text rendering.
     * Applies precise formatting based on fuzzy match status and selection.
     */
    //? if >= 26.1 {
    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    private void onDrawString(GuiGraphicsExtractor graphics, Font font, String text, int x, int y, int color, Operation<Void> original) {
        boolean isSelected = (color == -256);

        graphics.text(font, FuzzyHighlighter.highlightMatch(this.fuzzy_autocomplete$pattern, text, isSelected), x, y, -1);
    }
    //?} else if >=1.21.6 {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    private void onDrawString(GuiGraphicsExtractor instance, Font font, String text, int x, int y, int color, Operation<Void> original) {
        boolean isSelected = (color == -256);

        instance.drawString(font, FuzzyHighlighter.highlightMatch(this.fuzzy_autocomplete$pattern, text, isSelected), x, y, -1);
    }
    *///?} else {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"))
    private int onDrawString(GuiGraphicsExtractor instance, Font font, String text, int x, int y, int color, Operation<Integer> original) {
        boolean isSelected = (color == -256);

        return instance.drawString(font, FuzzyHighlighter.highlightMatch(this.fuzzy_autocomplete$pattern, text, isSelected), x, y, -1);
    }
    *///?}
}