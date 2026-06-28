class Solution {
    public int[] maxSlidingWindow(int[] arr, int k) {
        int n = arr.length;
        int[] ans = new int[n-k+1];
        
        for(int i = 0 ; i <= n-k ; i++){
            int max = arr[i];
            for(int j = i ; j < i+k ; j++){
                max = Math.max(max,arr[j]);
            }
            ans[i] = max;
        }
        return ans;
    }
}
