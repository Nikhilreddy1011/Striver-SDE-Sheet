class KthLargest {
    private int K;
    private PriorityQueue<Integer> pq;

    public KthLargest(int k, int[] nums) {
        K = k;
        pq = new PriorityQueue<>();

        for (int i = 0; i < nums.length; i++) {
            if (pq.size() < K)
                pq.offer(nums[i]);
            else if (nums[i] > pq.peek()) {
                pq.poll();
                pq.offer(nums[i]);
            }
        }
    }

    public int add(int val) {
        if (pq.size() < K) {
            pq.offer(val);
            return pq.peek();
        }

        if (val > pq.peek()) {
            pq.poll();
            pq.offer(val);
        }

        return pq.peek();
    }
}
