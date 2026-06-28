class Solution {
    public int[] stockSpan(int[] arr, int n) {
     int[] ans = new int[n];
     for(int i = 0 ; i < n ; i++){
        int currSpan = 0;
        for(int j = i ; j >=0;j--){
            if(arr[j] <= arr[i]){
                currSpan++;
            }else
                break;
        }
        ans[i] = currSpan;
     }
     return ans;
    }
}
