// Approach: Recursion + Memoization (Top-Down DP)
// Time: O(N * amount) | Space: O(N * amount)
// For each index and remaining sum, choose min of:
//   - not taking current coin (move to idx-1)
//   - taking current coin (stay at idx, reduce sum by coins[idx])
// Use 1e9 as infinity. Return -1 if not achievable.

import java.util.*;

class Solution {
    public int solve(int[] coins, int sum, int idx, int[][] dp) {
        if (idx == 0) {
            return sum % coins[0] == 0 ? sum / coins[0] : (int) 1e9;
        }
        if (dp[idx][sum] != -1) return dp[idx][sum];

        int nt = solve(coins, sum, idx - 1, dp); // not take
        int t = (int) 1e9;
        if (coins[idx] <= sum)                   // take (stay at same idx — unlimited coins)
            t = 1 + solve(coins, sum - coins[idx], idx, dp);

        return dp[idx][sum] = Math.min(nt, t);
    }

    public int MinimumCoins(int[] coins, int amount) {
        int n = coins.length;
        int[][] dp = new int[n][amount + 1];
        for (int[] row : dp) Arrays.fill(row, -1);

        int res = solve(coins, amount, n - 1, dp);
        return res == (int) 1e9 ? -1 : res;
    }
}
