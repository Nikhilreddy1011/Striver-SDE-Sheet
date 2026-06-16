// Approach: Bitmask (Generate all 2^n subsets)
// Time: O(2^N * N) | Space: O(1)
// Each number from 0 to 2^n-1 represents a subset via its binary bits.
// Bit i set = include arr[i] in the subset. Check if any subset sums to target.

class Solution {
    public boolean isSubsetSum(int[] arr, int target) {
        int n = arr.length;
        int totalSubsets = 1 << n; // 2^n

        for (int mask = 0; mask < totalSubsets; mask++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) { // if ith bit is set
                    sum += arr[i];
                }
            }
            if (sum == target) return true;
        }
        return false;
    }
}
