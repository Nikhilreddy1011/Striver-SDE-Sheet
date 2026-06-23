class Solution {

    public List<Integer> mergeKSortedArrays(int[][] arr, int k) {
        List<Integer> result = new ArrayList<>();

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        for (int i = 0; i < k; i++) {
            if (arr[i].length > 0) {
                pq.offer(new int[]{arr[i][0], i, 0});
            }
        }

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int val = top[0];
            int row = top[1];
            int col = top[2];

            result.add(val);

            if (col + 1 < arr[row].length) {
                pq.offer(new int[]{arr[row][col + 1], row, col + 1});
            }
        }

        return result;
    }
}