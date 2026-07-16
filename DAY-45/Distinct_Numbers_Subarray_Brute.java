class Solution {
    public List<Integer> distinctNumbers(int[] nums, int k) {
       int n = nums.length;
       List<Integer> res = new ArrayList<>();
       for (int i = 0; i <= n - k; i++) {
           Set<Integer> set = new HashSet<>();
           for (int j = i; j < i + k; j++) {
               set.add(nums[j]);
           }
           res.add(set.size());
       }
       return res;
    }
}
