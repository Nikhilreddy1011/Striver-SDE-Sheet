class Solution {
    private int[] prefix(int[] arr){
        int n = arr.length;
        int[] prefixMax = new int[n];
        prefixMax[0] = arr[0];
        for(int i = 1;i<n;i++){
            prefixMax[i] = Math.max(prefixMax[i-1],arr[i]);
        }
        return prefixMax;

    }private int[] suffix(int[] arr){
        int n = arr.length;
        int[] suffixMax = new int[n];
        suffixMax[n-1] = arr[n-1];
        for(int i = n-2;i>=0;i--){
            suffixMax[i] = Math.max(suffixMax[i+1],arr[i]);
        }
        return suffixMax;

    }
    public int trap(int[] height) {
        
       int n = height.length;
       int total = 0;
       int[] left = prefix(height);
       int[] right = suffix(height);
       for(int i = 0;i<n;i++){
        if(height[i] < left[i] && height[i] < right[i]){
            total += (Math.min(left[i],right[i])-height[i]);
        }
       }
       return total;

    }
}
