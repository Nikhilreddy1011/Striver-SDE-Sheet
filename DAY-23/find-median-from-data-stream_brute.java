class MedianFinder {

private List<Integer> list;
    public MedianFinder() {
        list = new ArrayList<>();

    }

    public void addNum(int num) {
        list.add(num);
    }

    public double findMedian() {
        if(list.isEmpty()) return 0.0;
        List<Integer> sorted = new ArrayList<>();
        Collections.sort(sorted);
        int n = sorted.size();
        if(n %2 == 1){
            return (double) sorted.get(n/2);
        }
        else{
            double a = sorted.get(n/2);
            double b = sorted.get(n/2-1);
            return (a+b)/2.0;
        }
    }
}