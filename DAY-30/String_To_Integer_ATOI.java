class Solution {
    public int myAtoi(String input) {
        int i = 0 , n = input.length();
        while( i < n && input.charAt(i)== ' '){
            i++;
        }
        int sign = 1;
        if( i < n && input.charAt(i)== '-'){
            sign = -1;
            i++;
        }
        else if(i < n && input.charAt(i)=='+'){
            i++;
        }
        long res = 0 ;
        while( i < n && Character.isDigit(input.charAt(i))){
            res = res * 10 + (input.charAt(i)-'0' );
            i++;
       
        if(res * sign >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        if(res * sign <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }
        }
        return (int)(res * sign);
    }
}
