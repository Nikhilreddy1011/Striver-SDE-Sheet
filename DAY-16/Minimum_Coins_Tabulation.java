// Approach: Bottom-Up Tabulation (2D DP)
// Time: O(N * amount) | Space: O(N * amount)
// Base case: dp[0][j] = j/coins[0] if divisible, else 1e9
// Fill table bottom-up; answer at dp[n-1][amount]

import java.util.*;

class Solution {
    public int MinimumCoins(int[] coins, int amount) {
        int n = coins.length;
        int[][] dp = new int[n][amount + 1];

        // Base case: only first coin available
        for (int i = 0; i <= amount; i++)
            dp[0][i] = i % coins[0] == 0 ? i / coins[0] : (int) 1e9;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= amount; j++) {
                int nt = dp[i - 1][j];         // not take
                int t = (int) 1e9;
                if (coins[i] <= j)             // take (unbounded — stay at row i)
                    t = 1 + dp[i][j - coins[i]];
                dp[i][j] = Math.min(nt, t);
            }
        }
        return dp[n - 1][amount] == (int) 1e9 ? -1 : dp[n - 1][amount];
    }
}
