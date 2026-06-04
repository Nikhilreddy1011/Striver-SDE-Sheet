class Solution {
    public int[] findMissingRepeatingNumbers(int[] nums) {
        long n = nums.length;
        long sum = (n *(n+1))/2;
        long sum2 = (n*(n+1)*(2*n+1))/6;
        long s1=0, s2 = 0;
        for(int i = 0 ;i<n;i++){
            s1 += nums[i];
            s2 += (long)nums[i] * (long)nums[i];
            
        }
        long x = s1 - sum;
        long y = s2 - sum2;
        y = y/x;
        long n1 = (x+y)/2;
        long n2 = n1 - x;
        return new int[]{(int)n1, (int)n2};
    }
}