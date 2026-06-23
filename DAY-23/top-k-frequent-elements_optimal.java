class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums)
            freq.put(num, freq.getOrDefault(num, 0) + 1);

        PriorityQueue<Map.Entry<Integer, Integer>> pq =
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
            pq.add(e);
            if (pq.size() > k) pq.poll();
        }

        int[] result = new int[k];
        int i = 0;
        while (!pq.isEmpty()) result[i++] = pq.poll().getKey();
        return result;
    }
}