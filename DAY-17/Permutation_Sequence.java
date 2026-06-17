// Approach: Mathematical - direct computation without generating all permutations
// Time: O(N^2) | Space: O(N)
// Key insight: for n numbers, there are (n-1)! permutations starting with each digit.
// So k / (n-1)! tells us which digit goes first. Remove it, update k, repeat.
// Convert to 0-indexed by doing k-- at start.

import java.util.*;

class Solution {
    public String getPermutation(int n, int k) {
        List<Integer> nums = new ArrayList<>();
        int fact = 1;

        // Build list [1..n] and compute (n-1)!
        for (int i = 1; i < n; i++) {
            fact *= i;       // fact = (n-1)! at end of loop
            nums.add(i);
        }
        nums.add(n);

        k--; // convert to 0-indexed

        StringBuilder ans = new StringBuilder();

        while (!nums.isEmpty()) {
            int index = k / fact;          // which number to pick
            ans.append(nums.get(index));
            nums.remove(index);            // remove picked number

            if (nums.isEmpty()) break;

            k %= fact;                     // remaining k within the block
            fact /= nums.size();           // shrink factorial for next position
        }
        return ans.toString();
    }
}
