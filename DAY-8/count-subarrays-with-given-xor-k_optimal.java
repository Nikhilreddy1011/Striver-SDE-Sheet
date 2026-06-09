class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
      int n = nums.length;
      int xr = 0;
      Map<Integer,Integer> map = new HashMap<>();
      map.put(0,1);
      int count = 0;
      for(int i = 0;i<n;i++){
        xr = xr ^ nums[i];
        int x = xr ^ k;
        count += map.getOrDefault(x,0);
        map.put(xr,map.getOrDefault(xr,0)+1);
      }
      return count;

    }
}