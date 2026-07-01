class Solution {
    public List<Integer> search(String pat, String txt) {
        int m = pat.length();
        int n = txt.length();
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i <= n -m ; i++){
            boolean flag = true;
            for(int j = 0 ; j < m ; j++){
                if(pat.charAt(j) != txt.charAt(i+j)){
                    flag = false;
                    break;
                }
            }
            if(flag){
                list.add(i);
            }
        }
        return list;
    }
}
