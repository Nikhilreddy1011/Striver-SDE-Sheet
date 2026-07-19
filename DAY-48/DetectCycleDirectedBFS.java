class Solution {
    private List<Integer> topoSort(int V, List<List<Integer>> adj) {
        List<Integer> ans = new ArrayList<>();
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

        while (!q.isEmpty()) {
            int node = q.poll();
            ans.add(node);

            for (int it : adj.get(node)) {
                inDegree[it]--;
                if (inDegree[it] == 0) {
                    q.add(it);
                }
            }
        }

        return ans;
    }

    public boolean isCyclic(int V, List<List<Integer>> adj) {
        List<Integer> topo = topoSort(V, adj);
        return topo.size() < V;
    }
}
