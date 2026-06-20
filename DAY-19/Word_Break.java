// Approach: 1D DP (Bottom-Up)
// Time: O(N * maxWordLen) | Space: O(N)
// dp[i] = true if s[0..i-1] can be segmented using dictionary words
// dp[0] = true (empty prefix always valid)
// From every valid checkpoint i (dp[i]=true), try extending by each word length.
// If s[i..i+len-1] is in dictionary, mark dp[i+len] = true.
// Answer is dp[n].

import java.util.*;

class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();

        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // empty prefix is always a valid segmentation

        // Optimization: only try substrings up to max dictionary word length
        int maxLen = 0;
        for (String word : wordDict) maxLen = Math.max(maxLen, word.length());

        for (int i = 0; i < n; i++) {
            if (!dp[i]) continue; // can't reach checkpoint i, skip

            for (int len = 1; len <= maxLen && i + len <= n; len++) {
                if (dict.contains(s.substring(i, i + len))) {
                    dp[i + len] = true; // found a valid word from i to i+len
                }
            }
        }
        return dp[n];
    }
}
