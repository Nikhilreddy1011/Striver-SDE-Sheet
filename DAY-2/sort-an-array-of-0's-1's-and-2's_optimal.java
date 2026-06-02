class Solution {
    private void swap(int[] nums,int i , int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    public void sortZeroOneTwo(int[] nums) {
        //intialize the 3 pointers 
        int n = nums.length;
        int low = 0, mid = 0 , high = n-1;
        while(mid<=high){
            if(nums[mid]==0){
                swap(nums,low ,mid);
                low++;
                mid++;
            }
            else if(nums[mid]==1){
                mid++;
            }
            else{
                swap(nums,mid,high);
                high--;
            }
        }
    }
}