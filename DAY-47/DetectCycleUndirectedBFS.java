class Solution {
    private boolean detectCycle(int i, List<Integer>[] adj, boolean[] visited) {
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{i, -1});
        visited[i] = true;
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int node = curr[0];
            int parent = curr[1];
            for (int it : adj[node]) {
                if (!visited[it]) {
                    visited[it] = true;
                    q.add(new int[]{it, node});
                } else if (it != parent) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCycle(int V, List<Integer>[] adj) {
        boolean[] visited = new boolean[V];
        boolean ans = false;
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                ans = detectCycle(i, adj, visited);
                if (ans) break;
            }
        }
        return ans;
    }
}
