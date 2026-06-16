// Approach: Greedy - Sort by profit descending, schedule as late as possible
// Time: O(N logN + N^2) | Space: O(maxDeadline)
// Sort jobs by profit descending. For each job, try to place it at the
// latest available slot <= its deadline. This keeps earlier slots free
// for other jobs.

import java.util.*;

class Solution {
    public int[] JobScheduling(int[][] Jobs) {
        int n = Jobs.length;

        // Sort by profit descending
        Arrays.sort(Jobs, (a, b) -> b[2] - a[2]);

        // Find max deadline to size the slots array
        int maxDeadline = -1;
        for (int[] num : Jobs) {
            maxDeadline = Math.max(maxDeadline, num[1]);
        }

        int[] hash = new int[maxDeadline];
        Arrays.fill(hash, -1); // -1 = slot is free

        int count = 0;
        int total = 0;

        for (int i = 0; i < n; i++) {
            // Try to place job in the latest available slot <= deadline
            for (int j = Jobs[i][1] - 1; j >= 0; j--) {
                if (hash[j] == -1) {
                    count++;
                    hash[j] = Jobs[i][0];
                    total += Jobs[i][2];
                    break;
                }
            }
        }
        return new int[]{count, total};
    }
}
