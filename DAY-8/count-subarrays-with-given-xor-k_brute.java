class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
      int n = nums.length;
      int count = 0;
      for(int i = 0;i<n;i++){
        int xor = 0;
        for(int j = i;j<n;j++){
            xor ^= nums[j];
            if(xor == k) count ++;            
        }
      }
      return count;
    }
}