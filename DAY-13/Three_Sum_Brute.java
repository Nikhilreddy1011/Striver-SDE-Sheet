// Approach: 3 Nested Loops + HashSet for deduplication
// Time: O(N^3 * log(no. of triplets)) | Space: O(triplets)
// Try every combination of 3 elements, sort and add to set

import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] arr) {
        int n = arr.length;
        Set<List<Integer>> st = new HashSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (arr[i] + arr[j] + arr[k] == 0) {
                        List<Integer> temp = Arrays.asList(arr[i], arr[j], arr[k]);
                        Collections.sort(temp);
                        st.add(temp); // set handles duplicates
                    }
                }
            }
        }
        return new ArrayList<>(st);
    }
}
