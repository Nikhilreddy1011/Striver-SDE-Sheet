class Solution {

    private int[] delRow = {-1, 0, 1, 0};
    private int[] delCol = {0, 1, 0, -1};

    private boolean isValid(int i, int j, int n, int m) {
        if (i < 0 || i >= n) return false;
        if (j < 0 || j >= m) return false;
        return true;
    }

    private void dfs(int row, int col, int[][] ans, int[][] image, int newColor, int iniColor) {
        ans[row][col] = newColor;

        int n = image.length;
        int m = image[0].length;

        for (int i = 0; i < 4; i++) {
            int nrow = row + delRow[i];
            int ncol = col + delCol[i];

            if (isValid(nrow, ncol, n, m) &&
                image[nrow][ncol] == iniColor &&
                ans[nrow][ncol] != newColor) {

                dfs(nrow, ncol, ans, image, newColor, iniColor);
            }
        }
    }

    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int iniColor = image[sr][sc];

        int[][] ans = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; i++)
            ans[i] = Arrays.copyOf(image[i], image[i].length);

        dfs(sr, sc, ans, image, newColor, iniColor);

        return ans;
    }
}
