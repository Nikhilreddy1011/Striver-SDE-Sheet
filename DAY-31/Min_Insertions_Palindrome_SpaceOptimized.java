class Solution {

    private int lcs(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();

        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];

        for (int ind1 = 1; ind1 <= n; ind1++) {
            for (int ind2 = 1; ind2 <= m; ind2++) {
                if (s1.charAt(ind1 - 1) == s2.charAt(ind2 - 1))
                    cur[ind2] = 1 + prev[ind2 - 1];
                else
                    cur[ind2] = Math.max(prev[ind2], cur[ind2 - 1]);
            }
            System.arraycopy(cur, 0, prev, 0, m + 1);
        }

        return prev[m];
    }

    private int longestPalindromeSubsequence(String s) {
        String t = new StringBuilder(s).reverse().toString();
        return lcs(s, t);
    }

    public int minInsertion(String s) {
        int n = s.length();
        int k = longestPalindromeSubsequence(s);
        return n - k;
    }
}
