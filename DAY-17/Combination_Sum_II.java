// Approach: Backtracking with duplicate skipping
// Time: O(2^N * N) | Space: O(N) recursion stack
// Each element can only be used ONCE. Sort first so duplicates are adjacent.
// After picking candidates[index], for the "skip" branch:
//   jump past all duplicates of candidates[index] to avoid duplicate combinations.

import java.util.*;

class Solution {
    private void func(int index, int sum, List<Integer> nums,
                      int[] candidates, List<List<Integer>> ans) {
        if (sum == 0) {
            ans.add(new ArrayList<>(nums)); // found valid unique combination
            return;
        }
        if (sum < 0 || index == candidates.length) return;

        // Pick candidates[index]
        nums.add(candidates[index]);
        func(index + 1, sum - candidates[index], nums, candidates, ans);
        nums.remove(nums.size() - 1); // backtrack

        // Skip duplicates of candidates[index] before moving to next distinct value
        for (int i = index + 1; i < candidates.length; i++) {
            if (candidates[i] != candidates[index]) {
                func(i, sum, nums, candidates, ans);
                break;
            }
        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(candidates); // group duplicates together
        func(0, target, new ArrayList<>(), candidates, ans);
        return ans;
    }
}
