// Approach: 1D DP (Space Optimized)
// Time: O(N * amount) | Space: O(amount)
// Since current coin can be used unlimited times, dp[j] = min coins to make j
// For each coin, update dp[j] = min(dp[j], dp[j - coin] + 1)
// Initialize dp[0]=0 (0 coins for amount 0), rest = 1e9 (unreachable)

import java.util.*;

class Solution {
    public int MinimumCoins(int[] coins, int amount) {
        int n = coins.length;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, (int) 1e9);
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            for (int j = coins[i]; j <= amount; j++) {
                dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
            }
        }
        return dp[amount] == (int) 1e9 ? -1 : dp[amount];
    }
}
