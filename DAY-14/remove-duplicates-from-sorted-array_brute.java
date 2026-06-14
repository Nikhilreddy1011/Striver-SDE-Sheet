class Solution {
    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        Set<Integer> set = new TreeSet<>();
        for(int num : nums){
            set.add(num);
        }
        int length = set.size();
        int index = 0;
        for(int num : set){
            nums[index] = num;
            index++;
        }
        return length;
    }
}