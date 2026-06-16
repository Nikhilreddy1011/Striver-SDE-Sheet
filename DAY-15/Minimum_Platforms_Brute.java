// Approach: For each train, count overlapping trains
// Time: O(N^2) | Space: O(1)
// For every train i, count how many trains j are present at the station
// at the same time as train i. Track the maximum overlap count.

class Solution {
    public int findPlatform(int[] Arrival, int[] Departure) {
        int n = Arrival.length;
        int maxCount = 1;

        for (int i = 0; i < n; i++) {
            int count = 1;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    // train j is present when train i arrives
                    if ((Arrival[i] >= Arrival[j]) && (Departure[j] >= Arrival[i])) {
                        count++;
                    }
                    maxCount = Math.max(count, maxCount);
                }
            }
        }
        return maxCount;
    }
}
