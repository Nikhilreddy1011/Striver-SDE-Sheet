class Solution {
    private boolean dfs(int node, List<List<Integer>> adj, boolean[] visited, boolean[] pathVisited) {
        visited[node] = true;
        pathVisited[node] = true;

        for (int it : adj.get(node)) {
            if (pathVisited[it]) {
                return true;
            } else if (!visited[it]) {
                if (dfs(it, adj, visited, pathVisited)) {
                    return true;
                }
            }
        }

        pathVisited[node] = false;
        return false;
    }

    public boolean isCyclic(int V, List<List<Integer>> adj) {
        boolean[] visited = new boolean[V];
        boolean[] pathVisited = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (dfs(i, adj, visited, pathVisited)) {
                    return true;
                }
            }
        }
        return false;
    }
}
