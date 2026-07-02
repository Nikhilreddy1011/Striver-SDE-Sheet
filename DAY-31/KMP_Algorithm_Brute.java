class Solution {
    private int[] computeLPS(String s) {
        int n = s.length();
        int[] LPS = new int[n];

        for (int i = 1; i < n; i++) {
            for (int len = 1; len < i; len++) {
                if (s.substring(0, len).equals(s.substring(i - len + 1, i + 1))) {
                    LPS[i] = len;
                }
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
