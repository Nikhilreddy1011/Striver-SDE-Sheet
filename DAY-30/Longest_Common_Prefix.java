class Solution {    
    public String longestCommonPrefix(String[] str) {
        int n = str.length;
        Arrays.sort(str);
        String first = str[0], last = str[n-1];
        StringBuilder ans = new StringBuilder();
        for(int i = 0 ; i < Math.min(first.length(),last.length());i++){
            if(first.charAt(i) != last.charAt(i)){
                return ans.toString();
            }
            ans.append(first.charAt(i));
        }
        return ans.toString();
    }
}
