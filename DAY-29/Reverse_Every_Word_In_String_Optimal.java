class Solution {
    private void reverse(StringBuilder s, int start , int end){
        while(start < end){
            char temp = s.charAt(start);
            s.setCharAt(start,s.charAt(end));
            s.setCharAt(end,temp);
            start++;
            end--;
        }
    }
    public String reverseWords(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(s);
        reverse(sb,0,n-1);
        int i = 0, j = 0, start = 0, end = 0;
        while(j < n){
            while(j < n && sb.charAt(j) == ' ') j++;
            if(j == n) break;
            start =  i;
            while( j < n && sb.charAt(j) != ' '){
                if(i < sb.length()){
                    sb.setCharAt(i++, sb.charAt(j++));
                }
                else{
                    sb.append(sb.charAt(j++));
                    i++;
                }
            }
            end = i -1;
            reverse(sb,start,end);
            if( j < n){
                if(i < sb.length()){
                    sb.setCharAt(i++,' ');
                }
                else{
                    sb.append(' ');
                    i++;
                }
            }
        }
        if(i > 0 && sb.charAt(i-1) == ' ') i--;
        return sb.substring(0,i);
    }
}
