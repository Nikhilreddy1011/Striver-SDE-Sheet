class Solution {
    public String reverseWords(String s) {
        int n = s.length();
        List<String> list = new ArrayList<>();
        int left,right;
        int j = 0; 
        while(j < n){
            while(j < n && s.charAt(j) == ' ') j++;
            if(j >= n) break;
            left = j ;
            while(j < n && s.charAt(j) != ' ') j++;
            right = j -1;
            String word = s.substring(left,right+1);
            list.add(word);
        }     
        StringBuilder ans = new StringBuilder();
        for( int i = list.size()-1; i >=0 ; i--){
            ans.append(list.get(i));
            if(i != 0) ans.append(' ');
        }   
        return ans.toString();
    }
}
