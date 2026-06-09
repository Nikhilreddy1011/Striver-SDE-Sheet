class Solution {
    public int longestConsecutive(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int longest = 1;
        int lastSeen = Integer.MIN_VALUE;
        int count = 0;
        for(int i =0;i<n;i++){
            if(nums[i] -1 == lastSeen){
                count+=1;
                lastSeen = nums[i];
            }
            if(nums[i] != lastSeen){
                count = 1;
                lastSeen = nums[i];
            }
            longest = Math.max(longest,count);
        }
        return longest;
        
    }
}