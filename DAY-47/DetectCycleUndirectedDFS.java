class Solution {
    private boolean detectCycle(int i, List<Integer>[] adj, boolean[] visited, int parent) {
        visited[i] = true;
        for (int node : adj[i]) {
            if (!visited[node]) {
                if (detectCycle(node, adj, visited, i)) {
                    return true;
                }
            } else if (node != parent) {
                return true;
            }
        }
        return false;
    }

    public boolean isCycle(int V, List<Integer>[] adj) {
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (detectCycle(i, adj, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }
}
