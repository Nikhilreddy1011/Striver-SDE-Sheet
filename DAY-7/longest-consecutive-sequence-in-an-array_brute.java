class Solution {
    private boolean ls(int[] nums , int target){
        int n = nums.length;
        for(int i =0;i<n;i++){
            if(nums[i] == target){
                return true;
            }
        }
        return false;
    }
    public int longestConsecutive(int[] nums) {
        int n = nums.length;
        if(n==0) return 0;
        int longest = 1;
        for(int i = 0; i< n;i++){
            int x = nums[i];
            int count = 1;
            while(ls(nums,x+1) == true){
                x+=1;
                count++;
            }
            longest = Math.max(longest,count);
        }
        return longest;
    }
}