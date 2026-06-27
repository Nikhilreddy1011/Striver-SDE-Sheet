class Solution {
    public int[] nextSmallerElements(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Arrays.fill(ans,-1);
        for(int i = 0; i < n ; i++){
            for(int j = i+1; j < n ; j++){
                if(arr[j] < arr[i]){
                    ans[i] = arr[j]; 
                    break;
                }
            }
        }
        return ans;
    }
}
