// Approach: Sort arrival and departure separately + Two pointers
// Time: O(N log N) | Space: O(1)
// Sort both arrays. Use two pointers - one for arrivals, one for departures.
// If next arrival <= current departure, a new platform is needed (count++).
// Otherwise, a platform frees up (count--). Track maximum count.

import java.util.*;

class Solution {
    public int findPlatform(int[] Arrival, int[] Departure) {
        int n = Arrival.length;
        Arrays.sort(Arrival);
        Arrays.sort(Departure);

        int ans = 1;
        int count = 1;
        int i = 1, j = 0;

        while (i < n && j < n) {
            if (Arrival[i] <= Departure[j]) {
                count++; // new train arrives before earliest departure - need new platform
                i++;
            } else {
                count--; // a train departs - platform freed
                j++;
            }
            ans = Math.max(ans, count);
        }
        return ans;
    }
}
