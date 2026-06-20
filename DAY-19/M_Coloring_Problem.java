// Approach: Backtracking - try each color for each node
// Time: O(M^N) | Space: O(N)
// Build adjacency list. For each node (0 to n-1), try colors 1 to M.
// A color is valid if no adjacent node has the same color.
// If a valid color is found, assign it and recurse to next node.
// If recursion fails, reset color and try next. Return false if none work.

import java.util.*;

class Solution {
    private boolean isPossible(int col, int node, int[] colors, List<List<Integer>> adj) {
        for (int adjacent : adj.get(node)) {
            if (colors[adjacent] == col) return false; // neighbor has same color
        }
        return true;
    }

    private boolean solve(int node, int m, int n, int[] colors, List<List<Integer>> adj) {
        if (node == n) return true; // all nodes colored successfully

        for (int i = 1; i <= m; i++) {
            if (isPossible(i, node, colors, adj)) {
                colors[node] = i;
                if (solve(node + 1, m, n, colors, adj)) return true;
                colors[node] = 0; // backtrack
            }
        }
        return false; // no valid color for this node
    }

    boolean graphColoring(int[][] edges, int m, int n) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        int[] colors = new int[n]; // 0 = uncolored
        return solve(0, m, n, colors, adj);
    }
}
