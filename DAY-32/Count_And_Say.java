class Solution {
    public String countAndSay(int n) {
        String result = "1";
        for (int i = 1; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            int j = 0;
            while (j < result.length()) {
                char ch = result.charAt(j);
                int count = 0;
                while (j < result.length() && result.charAt(j) == ch) {
                    j++;
                    count++;
                }
                sb.append(count).append(ch);
            }
            result = sb.toString();
        }
        return result;
    }
}
