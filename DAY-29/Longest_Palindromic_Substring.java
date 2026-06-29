class Solution {

    private int[] expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return new int[]{left + 1, right - 1};
    }

    public String longestPalindrome(String s) {
        int n = s.length();
        if (n == 0) return "";

        int start = 0, end = 0;

        for (int i = 0; i < n; i++) {
            int[] odd = expandAroundCenter(s, i, i);
            int[] even = expandAroundCenter(s, i, i + 1);

            if (odd[1] - odd[0] > end - start) {
                start = odd[0];
                end = odd[1];
            }

            if (even[1] - even[0] > end - start) {
                start = even[0];
                end = even[1];
            }
        }

        return s.substring(start, end + 1);
    }
}
