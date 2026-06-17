// Approach: Backtracking + Palindrome check
// Time: O(N * 2^N) | Space: O(N) recursion stack
// At each index, try all substrings starting at 'index'.
// If a substring is a palindrome, add it to path and recurse on the rest.
// When index reaches end of string, we have a valid complete partition.

import java.util.*;

class Solution {
    private boolean isPalindrome(String s, int left, int right) {
        while (left <= right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        dfs(0, s, temp, res);
        return res;
    }

    private void dfs(int index, String s,
                     List<String> temp, List<List<String>> res) {
        if (index == s.length()) {
            res.add(new ArrayList<>(temp)); // valid partition complete
            return;
        }
        for (int i = index; i < s.length(); i++) {
            if (isPalindrome(s, index, i)) {
                temp.add(s.substring(index, i + 1)); // take this palindrome
                dfs(i + 1, s, temp, res);             // recurse on remainder
                temp.remove(temp.size() - 1);          // backtrack
            }
        }
    }
}
