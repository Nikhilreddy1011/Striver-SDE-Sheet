class Solution {

    private int[] computeLPS(String s) {
        int n = s.length();
        int[] LPS = new int[n];

        int i = 1, j = 0;

        while (i < n) {
            if (s.charAt(i) == s.charAt(j)) {
                LPS[i] = j + 1;
                i++;
                j++;
            } else {
                while (j > 0 && s.charAt(i) != s.charAt(j)) {
                    j = LPS[j - 1];
                }

                if (s.charAt(i) == s.charAt(j)) {
                    LPS[i] = j + 1;
                    j++;
                }
                i++;
            }
        }

        return LPS;
    }

    public List<Integer> search(String pattern, String text) {
        String s = pattern + '$' + text;

        int[] LPS = computeLPS(s);

        int m = pattern.length();
        List<Integer> ans = new ArrayList<>();

        for (int i = m + 1; i < s.length(); i++) {
            if (LPS[i] == m) {
                ans.add(i - 2 * m);
            }
        }

        return ans;
    }
}
