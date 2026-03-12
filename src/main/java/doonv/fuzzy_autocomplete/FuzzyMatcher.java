package doonv.fuzzy_autocomplete;

import java.util.Locale;

/**
 * Utility class handling subsequence evaluation, heuristic scoring,
 * and text highlighting for fuzzy command and resource suggestions.
 */
public class FuzzyMatcher {
    public static boolean isFuzzyMatch(String pattern, String text) {
        pattern = pattern.toLowerCase(Locale.ROOT);
        text = text.toLowerCase(Locale.ROOT);

        if (pattern.isEmpty()) return true;
        if (text.isEmpty()) return false;

        int patternIndex = 0;
        for (int textIndex = 0; textIndex < text.length() && patternIndex < pattern.length(); textIndex++) {
            if (pattern.charAt(patternIndex) == text.charAt(textIndex)) {
                patternIndex++;
            }
        }
        return patternIndex == pattern.length();
    }

    public static int score(String pattern, String text) {
        pattern = pattern.toLowerCase(Locale.ROOT);
        text = text.toLowerCase(Locale.ROOT);

        if (pattern.isEmpty()) return 0;
        if (text.equals(pattern)) return 10000;
        if (text.startsWith(pattern)) return 5000;
        if (text.startsWith("minecraft:" + pattern)) return 4000;

        int exactMatchIndex = findBestContiguousMatch(pattern, text);
        if (exactMatchIndex >= 0) {
            int score = 3000;
            if (exactMatchIndex == 0 || isWordBoundary(text, exactMatchIndex)) {
                score += 500;
            }
            score -= text.length();
            return score;
        }

        int[] bestMatch = findBestMatchIndices(pattern, text);
        if (bestMatch != null) {
            int score = 0;
            for (int i = 0; i < bestMatch.length; i++) {
                int textIndex = bestMatch[i];
                score += 100;

                // Massive bonus for acronym-style matches
                if (isWordBoundary(text, textIndex)) {
                    score += 200;
                }

                if (i > 0) {
                    int distance = textIndex - bestMatch[i - 1];
                    score += (distance == 1) ? 50 : -distance;
                }
            }
            // Subtract text.length() so shorter matching blocks appear slightly higher
            return score - text.length();
        }

        return -1000;
    }

    /**
     * Finds the absolute best matching character indices using Dynamic Programming.
     */
    public static int[] findBestMatchIndices(String pattern, String text) {
        int p = pattern.length();
        int t = text.length();
        if (p == 0) return new int[0];
        if (p > t) return null;

        int[] dp = new int[p * t];
        int[] parent = new int[p * t];

        for (int i = 0; i < p * t; i++) {
            dp[i] = -1000000;
            parent[i] = -1;
        }

        // Base case for the first pattern character
        for (int j = 0; j < t; j++) {
            if (pattern.charAt(0) == text.charAt(j)) {
                dp[j] = 100 + (isWordBoundary(text, j) ? 200 : 0);
            }
        }

        for (int i = 1; i < p; i++) {
            int max_k_val = -1000000;
            int best_k_idx = -1;
            int currentRow = i * t;
            int prevRow = (i - 1) * t;

            for (int j = i; j < t; j++) {
                // Keep track of the best non-contiguous match so far to remain O(p * t)
                int k_candidate = j - 2;
                if (k_candidate >= 0 && dp[prevRow + k_candidate] != -1000000) {
                    int val = dp[prevRow + k_candidate] + k_candidate;
                    if (val > max_k_val) {
                        max_k_val = val;
                        best_k_idx = k_candidate;
                    }
                }

                if (pattern.charAt(i) == text.charAt(j)) {
                    int matchScore = 100 + (isWordBoundary(text, j) ? 200 : 0);
                    int best_score = -1000000;
                    int best_parent = -1;

                    // Option 1: Contiguous match
                    if (dp[prevRow + j - 1] != -1000000) {
                        int score_contig = dp[prevRow + j - 1] + 50 + matchScore;
                        if (score_contig > best_score) {
                            best_score = score_contig;
                            best_parent = j - 1;
                        }
                    }

                    // Option 2: Best non-contiguous match
                    if (max_k_val != -1000000) {
                        int score_non_contig = max_k_val - j + matchScore;
                        if (score_non_contig > best_score) {
                            best_score = score_non_contig;
                            best_parent = best_k_idx;
                        }
                    }

                    dp[currentRow + j] = best_score;
                    parent[currentRow + j] = best_parent;
                }
            }
        }

        // Find the ending match with the highest overall score
        int max_final_score = -1000000;
        int end_j = -1;
        int lastRow = (p - 1) * t;
        for (int j = p - 1; j < t; j++) {
            if (dp[lastRow + j] > max_final_score) {
                max_final_score = dp[lastRow + j];
                end_j = j;
            }
        }

        if (max_final_score == -1000000) return null;

        // Backtrack to assemble the chosen indexes
        int[] result = new int[p];
        int curr_j = end_j;
        for (int i = p - 1; i >= 0; i--) {
            result[i] = curr_j;
            if (i > 0) {
                curr_j = parent[i * t + curr_j];
            }
        }

        return result;
    }

    public static boolean isWordBoundary(String text, int index) {
        if (index == 0) return true;
        char c = text.charAt(index - 1);
        return c == ':' || c == '_' || c == '.' || c == '/';
    }

    static int findBestContiguousMatch(String pattern, String text) {
        int searchIndex = 0;
        int bestIndex = -1;

        while ((searchIndex = text.indexOf(pattern, searchIndex)) >= 0) {
            bestIndex = searchIndex;
            if (searchIndex == 0 || isWordBoundary(text, searchIndex)) {
                break;
            }
            searchIndex++;
        }
        return bestIndex;
    }
}