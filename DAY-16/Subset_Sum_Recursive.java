// Approach: Pure Recursion
// Time: O(2^N) | Space: O(N) recursion stack
// At each index, either pick arr[i] (reduce target) or skip it.
// Base cases: target==0 → found, i<0 → exhausted without finding.

class Solution {
    public boolean isSubsetSum(int[] arr, int target) {
        return solve(arr, arr.length - 1, target);
    }

    private boolean solve(int[] arr, int i, int target) {
        if (target == 0) return true;   // subset found
        if (i < 0) return false;        // no elements left

        if (arr[i] > target)            // can't pick this element
            return solve(arr, i - 1, target);

        // pick arr[i] OR skip arr[i]
        return solve(arr, i - 1, target - arr[i])
            || solve(arr, i - 1, target);
    }
}
