// Approach: Backtracking with duplicate skipping
// Time: O(2^N * N) | Space: O(N) recursion stack
// Sort first so duplicates are adjacent. At each recursion level,
// skip nums[i] if it equals nums[i-1] (same value already explored at this level).
// This avoids generating duplicate subsets without needing a HashSet.

import java.util.*;

class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, nums, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int[] nums,
                           List<Integer> current,
                           List<List<Integer>> result) {
        result.add(new ArrayList<>(current)); // snapshot current subset

        for (int i = start; i < nums.length; i++) {
            // Skip duplicate at the same recursion level
            if (i > start && nums[i] == nums[i - 1]) continue;

            current.add(nums[i]);                     // pick
            backtrack(i + 1, nums, current, result);
            current.remove(current.size() - 1);       // unpick (backtrack)
        }
    }
}
