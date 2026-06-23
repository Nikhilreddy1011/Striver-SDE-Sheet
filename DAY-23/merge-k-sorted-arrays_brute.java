class Solution {
    public List<Integer> mergeKSortedArrays(int[][] arr, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                pq.offer(arr[i][j]);
            }
        }

        List<Integer> ans = new ArrayList<>();

        while (!pq.isEmpty()) {
            ans.add(pq.poll());
        }

        return ans;
    }
}