class Solution {
    public int search(int[] nums, int k) {
       int n = nums.length;
       for(int i = 0 ; i < n ; i++){
        if(nums[i]== k)
        return i;
       }
       return -1;
    }
}
