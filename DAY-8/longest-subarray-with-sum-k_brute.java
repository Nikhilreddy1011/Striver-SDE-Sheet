
class Solution {
    public int longestSubarray(int[] nums, int k) {
        int n = nums.length;
        int longest = 0;
        for(int i =0 ;i<n;i++){
            int sum  = 0;
            for(int j = i;j<n;j++){
                sum += nums[j];
                if(sum == k) {
                    longest = Math.max(longest, j-i+1);
                }

            }
        }
        return longest;
    }
}