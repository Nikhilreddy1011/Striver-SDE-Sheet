// Approach: Backtracking - explore all 4 directions, mark visited cells
// Time: O(4^(N^2)) | Space: O(N^2)
// Start at (0,0), try U/L/D/R directions. Mark cell as visited (set to 0)
// before recursing to prevent revisiting. Unmark (restore to 1) after backtrack.
// When (n-1, n-1) is reached, record the path. Sort final result lexicographically.

import java.util.*;

class Solution {
    List<String> res = new ArrayList<>();

    private void path(int[][] m, int i, int j, String ans, int n) {
        if (i == n - 1 && j == n - 1) {
            res.add(ans); // destination reached — record path
            return;
        }
        if (m[i][j] == 0) return; // blocked cell

        m[i][j] = 0; // mark as visited

        if (i > 0)     path(m, i - 1, j, ans + 'U', n); // up
        if (j > 0)     path(m, i, j - 1, ans + 'L', n); // left
        if (i < n - 1) path(m, i + 1, j, ans + 'D', n); // down
        if (j < n - 1) path(m, i, j + 1, ans + 'R', n); // right

        m[i][j] = 1; // unmark (backtrack)
    }

    public List<String> findPath(int[][] grid) {
        int n = grid.length;
        res.clear();
        if (grid[0][0] == 0 || grid[n - 1][n - 1] == 0) return res;
        path(grid, 0, 0, "", n);
        Collections.sort(res); // lexicographic order
        return res;
    }
}
