// Approach: Recursion / Backtracking (Unbounded - same element can be picked multiple times)
// Time: O(K * N^(target/min)) | Space: O(target/min) recursion depth
// At each index: skip current (move to i-1) OR take current (stay at i, reduce sum)
// Unlimited usage because when we "take", we stay at same index

import java.util.*;

class Solution {
    public void func(List<Integer> v, int i, int sum,
                     List<Integer> v2, List<List<Integer>> ans) {
        if (sum == 0) {
            ans.add(new ArrayList<>(v2)); // valid combination found
            return;
        }
        if (sum < 0 || i < 0) return; // overshot or exhausted

        func(v, i - 1, sum, v2, ans);     // skip current element
        v2.add(v.get(i));
        func(v, i, sum - v.get(i), v2, ans); // take current element (stay at i)
        v2.remove(v2.size() - 1);             // backtrack
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> v = new ArrayList<>();
        for (int num : candidates) v.add(num);
        func(v, v.size() - 1, target, new ArrayList<>(), ans);
        return ans;
    }
}
