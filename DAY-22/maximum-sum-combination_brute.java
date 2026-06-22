class Solution {
    
    public List<Integer> maxCombinations(int[] nums1, int[] nums2, int k) {
        List<Integer> allSums = new ArrayList<>();

        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                allSums.add(nums1[i] + nums2[j]);
            }
        }
        allSums.sort(Collections.reverseOrder());
        return allSums.subList(0, k);
    }
}