// Approach: Bitmask + HashSet for deduplication
// Time: O(2^N * N) | Space: O(2^N * N)
// Generate all 2^n subsets using bitmask. Sort input so subsets from
// a sorted array are consistently ordered — HashSet handles deduplication.

import java.util.*;

class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer>> seen = new HashSet<>();
        int n = nums.length;

        for (int mask = 0; mask < (1 << n); mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            seen.add(subset); // HashSet auto-deduplicates
        }
        return new ArrayList<>(seen);
    }
}
