class Solution {
    public List<Integer> distinctNumbers(int[] nums, int k) {
        int n = nums.length;
        Map<Integer, Integer> freq = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < k; i++)
            freq.put(nums[i], freq.getOrDefault(nums[i], 0) + 1);

        result.add(freq.size());

        for (int i = k; i < n; i++) {
            int outgoing = nums[i - k];
            freq.put(outgoing, freq.get(outgoing) - 1);
            if (freq.get(outgoing) == 0)
                freq.remove(outgoing);

            int incoming = nums[i];
            freq.put(incoming, freq.getOrDefault(incoming, 0) + 1);

            result.add(freq.size());
        }

        return result;
    }
}
