class Solution {
    public List<Double> getMedian(int[] arr) {
        PriorityQueue<Integer> left  = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
        PriorityQueue<Integer> right = new PriorityQueue<>();                            // min-heap
        List<Double> medians = new ArrayList<>();

        for (int num : arr) {
            if (left.isEmpty() || num <= left.peek())
                left.offer(num);
            else
                right.offer(num);

            if (left.size() > right.size() + 1)
                right.offer(left.poll());
            else if (right.size() > left.size())
                left.offer(right.poll());

            if (left.size() == right.size())
                medians.add((left.peek() + right.peek()) / 2.0);
            else
                medians.add((double) left.peek());
        }

        return medians;
    }
}
