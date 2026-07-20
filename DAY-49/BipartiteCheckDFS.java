class Solution {
    private boolean dfs(int node, int col, int[] color, List<List<Integer>> adj) {
        color[node] = col;

        for (int it : adj.get(node)) {
            if (color[it] == -1) {
                if (!dfs(it, 1 - col, color, adj))
                    return false;
            } else if (color[it] == col) {
                return false;
            }
        }

        return true;
    }

    public boolean isBipartite(int V, List<List<Integer>> edges) {
        int[] color = new int[V];
        Arrays.fill(color, -1);

        List<List<Integer>> adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<>());

        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        for (int i = 0; i < V; i++) {
            if (color[i] == -1) {
                if (!dfs(i, 0, color, adj))
                    return false;
            }
        }

        return true;
    }
}
