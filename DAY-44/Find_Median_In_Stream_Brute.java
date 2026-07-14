class Solution {
    public List<Double> getMedian(int[] arr) {
        List<Integer> sortedList = new ArrayList<>();
        List<Double> medians = new ArrayList<>();

        for (int num : arr) {
            int pos = Collections.binarySearch(sortedList, num);
            if (pos < 0) pos = -(pos + 1);
            sortedList.add(pos, num);

            int n = sortedList.size();

            if (n % 2 == 1)
                medians.add((double) sortedList.get(n / 2));
            else
                medians.add((sortedList.get(n / 2 - 1) + sortedList.get(n / 2)) / 2.0);
        }

        return medians;
    }
}
