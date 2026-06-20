// Approach: Backtracking with used[] array + duplicate skipping
// Time: O(N * N!) | Space: O(N)
// Sort first so duplicates are adjacent.
// Skip a character if: (1) already used, or (2) same as previous and previous is NOT used
// Condition 2 ensures we don't start a new branch with the same character at the same level.

import java.util.*;

class Solution {
    public List<String> permuteUnique(String s) {
        char[] arr = s.toCharArray();
        Arrays.sort(arr); // group duplicates together

        List<String> result = new ArrayList<>();
        boolean[] used = new boolean[arr.length];

        backtrack(arr, used, new StringBuilder(), result);
        return result;
    }

    private void backtrack(char[] arr, boolean[] used,
                           StringBuilder path, List<String> result) {
        if (path.length() == arr.length) {
            result.add(path.toString()); // valid unique permutation found
            return;
        }

        for (int i = 0; i < arr.length; i++) {
            if (used[i]) continue; // already in current permutation

            // Skip duplicate: same char as previous, but previous NOT used
            // means we're starting a new branch with same char = duplicate permutation
            if (i > 0 && arr[i] == arr[i - 1] && !used[i - 1]) continue;

            used[i] = true;
            path.append(arr[i]);

            backtrack(arr, used, path, result);

            path.deleteCharAt(path.length() - 1); // backtrack
            used[i] = false;
        }
    }
}
