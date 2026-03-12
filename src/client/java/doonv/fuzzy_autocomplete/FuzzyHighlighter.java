package doonv.fuzzy_autocomplete;

import doonv.fuzzy_autocomplete.config.ClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.Locale;

public class FuzzyHighlighter {

    /**
     * Formats the suggestion text to highlight matched characters.
     * Uses the best possible match indices found by the fuzzy algorithm.
     *
     * @param pattern    The search query.
     * @param text       The raw suggestion text.
     * @param isSelected Whether the suggestion is currently focused in the UI.
     * @return A formatted character sequence ready for UI rendering.
     */
    public static FormattedCharSequence highlightMatch(String pattern, String text, boolean isSelected) {
        Style unmatchedStyle = isSelected ? ClientConfig.unmatchedSelectedStyle() : ClientConfig.unmatchedUnselectedStyle();

        if (pattern.isEmpty()) {
            return Component.literal(text).withStyle(unmatchedStyle).getVisualOrderText();
        }

        String lowerText = text.toLowerCase(Locale.ROOT);
        String lowerPattern = pattern.toLowerCase(Locale.ROOT);

        // 1. Optimization: check for perfect contiguous matches first
        int exactMatchIndex = FuzzyMatcher.findBestContiguousMatch(lowerPattern, lowerText);
        if (exactMatchIndex >= 0) {
            return buildContiguousHighlight(text, pattern.length(), exactMatchIndex, unmatchedStyle);
        }

        // 2. Use the Dynamic Programming result to get the exact indices of the best "acronym" match
        int[] bestIndices = FuzzyMatcher.findBestMatchIndices(lowerPattern, lowerText);
        if (bestIndices != null && bestIndices.length > 0) {
            return buildIndicesHighlight(text, bestIndices, unmatchedStyle);
        }

        // 3. Fallback to greedy matching (should rarely be reached if DP works, but good for safety)
        if (FuzzyMatcher.isFuzzyMatch(pattern, text)) {
            return buildSubsequenceHighlight(text, lowerPattern, lowerText, unmatchedStyle);
        }

        return Component.literal(text).withStyle(unmatchedStyle).getVisualOrderText();
    }

    private static FormattedCharSequence buildContiguousHighlight(String text, int patternLength, int matchIndex, Style unmatchedStyle) {
        MutableComponent result = Component.empty();

        if (matchIndex > 0) {
            result.append(Component.literal(text.substring(0, matchIndex)).withStyle(unmatchedStyle));
        }

        result.append(Component.literal(text.substring(matchIndex, matchIndex + patternLength)).withStyle(ClientConfig.matchedStyle()));

        if (matchIndex + patternLength < text.length()) {
            result.append(Component.literal(text.substring(matchIndex + patternLength)).withStyle(unmatchedStyle));
        }

        return result.getVisualOrderText();
    }

    private static FormattedCharSequence buildIndicesHighlight(String text, int[] indices, Style unmatchedStyle) {
        MutableComponent result = Component.empty();
        int lastEnd = 0;

        for (int index : indices) {
            // Append unmatched segment before this match
            if (index > lastEnd) {
                result.append(Component.literal(text.substring(lastEnd, index)).withStyle(unmatchedStyle));
            }

            // Append the matched character using the Config color
            // We use text.charAt(index) to preserve the original casing of the suggestion
            result.append(Component.literal(String.valueOf(text.charAt(index))).withStyle(ClientConfig.matchedStyle()));

            lastEnd = index + 1;
        }

        // Append remaining unmatched text after the last match
        if (lastEnd < text.length()) {
            result.append(Component.literal(text.substring(lastEnd)).withStyle(unmatchedStyle));
        }

        return result.getVisualOrderText();
    }

    private static FormattedCharSequence buildSubsequenceHighlight(String text, String lowerPattern, String lowerText, Style unmatchedStyle) {
        MutableComponent result = Component.empty();
        int patternIndex = 0;
        int lastMatchEnd = 0;

        for (int textIndex = 0; textIndex < lowerText.length() && patternIndex < lowerPattern.length(); textIndex++) {
            if (lowerText.charAt(textIndex) == lowerPattern.charAt(patternIndex)) {
                if (textIndex > lastMatchEnd) {
                    result.append(Component.literal(text.substring(lastMatchEnd, textIndex)).withStyle(unmatchedStyle));
                }

                result.append(Component.literal(text.substring(textIndex, textIndex + 1)).withStyle(ClientConfig.matchedStyle()));

                lastMatchEnd = textIndex + 1;
                patternIndex++;
            }
        }

        if (lastMatchEnd < text.length()) {
            result.append(Component.literal(text.substring(lastMatchEnd)).withStyle(unmatchedStyle));
        }

        return result.getVisualOrderText();
    }
}