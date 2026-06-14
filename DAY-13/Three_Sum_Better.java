// Approach: 2 Loops + HashSet for third element
// Time: O(N^2 * log(no. of triplets)) | Space: O(N + triplets)
// Fix i, fix j, check if -(arr[i]+arr[j]) exists in hashset

import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] arr) {
        int n = arr.length;
        Set<List<Integer>> ans = new HashSet<>();

        for (int i = 0; i < n; i++) {
            Set<Integer> hashset = new HashSet<>();

            for (int j = i + 1; j < n; j++) {
                int third = -(arr[i] + arr[j]); // needed third element

                if (hashset.contains(third)) {
                    List<Integer> temp = Arrays.asList(arr[i], arr[j], third);
                    Collections.sort(temp);
                    ans.add(temp);
                }
                hashset.add(arr[j]);
            }
        }
        return new ArrayList<>(ans);
    }
}
