// Approach: Greedy - Sort by value/weight ratio descending
// Time: O(N log N) | Space: O(N)
// Since fractions of items are allowed, always prefer items with the
// highest value-per-unit-weight first. Take whole items until capacity
// runs out, then take a fraction of the next item.

import java.util.*;

class Solution {
    public double fractionalKnapsack(int[] val, int[] wt, long cap) {
        int n = val.length;

        // ratio[i] = {value/weight ratio, original index}
        double[][] ratio = new double[n][2];
        for (int i = 0; i < n; i++) {
            ratio[i] = new double[]{(double) val[i] / wt[i], i};
        }

        // Sort by ratio descending
        Arrays.sort(ratio, (a, b) -> Double.compare(b[0], a[0]));

        double totalValue = 0.0;
        for (double[] r : ratio) {
            int i = (int) r[1];
            if (wt[i] <= cap) {
                // Take entire item
                totalValue += val[i];
                cap -= wt[i];
            } else {
                // Take fraction of item that fits
                totalValue += val[i] * ((double) cap / wt[i]);
                break;
            }
        }
        return Math.round(totalValue * 1e6) / 1e6; // round to 6 decimals
    }
}
