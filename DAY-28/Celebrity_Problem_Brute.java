class Solution {

    public int celebrity(int[][] M) {
        int n = M.length;

        int[] knowMe = new int[n];
        int[] Iknow = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (M[i][j] == 1) {
                    knowMe[j]++;
                    Iknow[i]++;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (knowMe[i] == n - 1 && Iknow[i] == 0) {
                return i;
            }
        }

        return -1;
    }
}
