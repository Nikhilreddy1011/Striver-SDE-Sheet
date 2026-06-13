// Approach: Sort + Two Pointers
// Time: O(N^2) | Space: O(triplets) for result only
// Fix i, use two pointers left and right to find pairs summing to -arr[i]
// Skip duplicates at all three levels to avoid duplicate triplets

import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] arr) {
        int n = arr.length;
        Arrays.sort(arr);
        List<List<Integer>> ans = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            // Skip duplicates for i
            if (i > 0 && arr[i] == arr[i - 1]) continue;

            int left = i + 1, right = n - 1;

            while (left < right) {
                int sum = arr[i] + arr[left] + arr[right];

                if (sum == 0) {
                    ans.add(Arrays.asList(arr[i], arr[left], arr[right]));
                    left++;
                    right--;
                    // Skip duplicates for left and right
                    while (left < right && arr[left] == arr[left - 1]) left++;
                    while (left < right && arr[right] == arr[right + 1]) right--;
                } else if (sum < 0) {
                    left++;  // need larger sum
                } else {
                    right--; // need smaller sum
                }
            }
        }
        return ans;
    }
}
