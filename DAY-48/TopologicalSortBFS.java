class Solution {
    public int[] topoSort(int V, List<List<Integer>> adj) {
        int[] ans = new int[V];
        int[] inDegree = new int[V];

        for (int i = 0; i < V; i++) {
            for (int it : adj.get(i)) {
                inDegree[it]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }

        int index = 0;

        while (!q.isEmpty()) {
            int node = q.poll();
            ans[index++] = node;

            for (int it : adj.get(node)) {
                inDegree[it]--;
                if (inDegree[it] == 0) {
                    q.add(it);
                }
            }
        }

        return ans;
    }
}
