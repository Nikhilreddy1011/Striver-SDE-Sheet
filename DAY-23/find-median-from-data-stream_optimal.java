class MedianFinder {
    private PriorityQueue<Integer> max;
    private PriorityQueue<Integer> min;

    public MedianFinder() {
        max = new PriorityQueue<>(Collections.reverseOrder());
        min = new PriorityQueue<>();
    }

    public void addNum(int num) {
        if(max.isEmpty() || num <= max.peek()) max.offer(num);
        else min.offer(num);
        if(max.size() >min.size() + 1){
            min.offer(max.poll());
        }
        else if(min.size() > max.size()){
            max.offer(min.poll());
        }
    }

    public double findMedian() {
        if(max.size() == min.size()) return (max.peek() + min.peek())/2.0;
        return max.peek();
    }
}