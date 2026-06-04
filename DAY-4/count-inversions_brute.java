class Solution {
    public long numberOfInversions(int[] nums) {
        long n = nums.length;
        long count = 0;
        for(int i = 0;i<n;i++){
            for(int j = i+1;j<n;j++){
                if(nums[i] > nums[j]){
                    count++;
                }
            }
        }
        return count;

    }
}