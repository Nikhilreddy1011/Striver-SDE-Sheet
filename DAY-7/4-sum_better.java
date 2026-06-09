class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> sol = new ArrayList<>();
        int n = nums.length;
        Set<List<Integer>> set = new HashSet<>();
        for(int i = 0;i < n;i++){
            for(int j = i+1;j<n;j++){
                Set<Long> hashset = new HashSet<>();
                for(int k = j+1;k<n;k++){
                    long sum = (long) nums[i] + nums[j] + nums[k];
                    long rem = target - sum;
                    if(hashset.contains(rem)){
                        List<Integer> list = Arrays.asList(nums[i],nums[j],nums[k],(int)rem);
                        Collections.sort(list);
                        set.add(list);
                    }
                    hashset.add((long) nums[k]);
                }
            }
        }
        sol.addAll(set);
        return sol;
    }
}