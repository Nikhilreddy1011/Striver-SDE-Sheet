class Solution {
    private boolean isValid(int i, int j, int n, int m) {
        if (i < 0 || i >= n) return false;
        if (j < 0 || j >= m) return false;
        return true;
    }

    private void bfs(int i, int j, boolean[][] vis, char[][] grid) {
        vis[i][j] = true;

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{i, j});

        int n = grid.length;
        int m = grid[0].length;

        while (!q.isEmpty()) {
            int[] cell = q.poll();

            int row = cell[0];
            int col = cell[1];

            for (int delRow = -1; delRow <= 1; delRow++) {
                for (int delCol = -1; delCol <= 1; delCol++) {
                    int newRow = row + delRow;
                    int newCol = col + delCol;

                    if (isValid(newRow, newCol, n, m)
                            && grid[newRow][newCol] == '1'
                            && !vis[newRow][newCol]) {

                        vis[newRow][newCol] = true;
                        q.add(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    public int numIslands(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        boolean[][] vis = new boolean[n][m];

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!vis[i][j] && grid[i][j] == '1') {
                    count++;
                    bfs(i, j, vis, grid);
                }
            }
        }

        return count;
    }
}
