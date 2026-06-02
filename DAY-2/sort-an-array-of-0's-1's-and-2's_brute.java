class Solution {
    public void sortZeroOneTwo(int[] nums) {
        int count0 = 0, count1 = 0, count2 = 0;
        int k = nums.length;
        for(int i = 0 ;i<k;i++){
            if(nums[i]== 0) count0++;
            else if(nums[i]== 1) count1++;
            else count2++;
        }
        int n = count0;
        int m = count0 + count1;
        for(int i = 0 ;i<n;i++ ){
            nums[i] = 0;
        
        }
        for(int i = n ;i<m;i++){
            nums[i] = 1;
        }
        for(int i = m ; i<k;i++){
            nums[i] = 2;
        }


        
    }
}