class Solution {
    private boolean bS(int[] nums, int target){
        int low = 0;
        int n = nums.length;
        int high = n - 1;
        while(low <= high){
            int mid = low + (high - low)/2;
            if(nums[mid] == target){
                return true;
            }
            else if(nums[mid] < target){
                low  = mid + 1;
            }
            else{
                high = mid - 1;
            }
        }
        return false;
    }
    public boolean searchMatrix(int[][] mat, int target) {
        int n = mat.length;
        int m = mat[0].length;
        for(int i = 0;i<n;i++){
            if(mat[i][0] <= target &&  target <= mat[i][m-1]){
                return bS(mat[i],target);
            }
        }
        return false;
    }
}
