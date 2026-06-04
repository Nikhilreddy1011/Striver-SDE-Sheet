class Solution {
    public int findDuplicate(int[] nums) {
       int[] temp = new int[nums.length + 1];
       int n = nums.length;
       for(int i = 0;i<n;i++){
        if(temp[nums[i]] == 0){
            temp[nums[i]]++;
        }else{
            return nums[i];

        }
       }
       return 0;
    }
}