class Solution {

    private void bfs(int node, List<List<Integer>> adj, boolean[] vis, List<Integer> ans) {
        Queue<Integer> q = new LinkedList<>();
        q.add(node);

        while (!q.isEmpty()) {
            node = q.poll();
            ans.add(node);

            for (int it : adj.get(node)) {
                if (!vis[it]) {
                    vis[it] = true;
                    q.add(it);
                }
            }
        }
    }

    private void dfs(int node, List<List<Integer>> adj, boolean[] vis, List<Integer> ans) {
        vis[node] = true;
        ans.add(node);

        for (int it : adj.get(node)) {
            if (!vis[it]) {
                dfs(it, adj, vis, ans);
            }
        }
    }

    public List<Integer> dfsOfGraph(int V, List<List<Integer>> edges) {
        boolean[] vis = new boolean[V];
        List<Integer> ans = new ArrayList<>();

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            adj.get(edge.get(0)).add(edge.get(1));
            adj.get(edge.get(1)).add(edge.get(0));
        }

        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                dfs(i, adj, vis, ans);
            }
        }

        return ans;
    }

    public List<Integer> bfsOfGraph(int V, List<List<Integer>> edges) {
        boolean[] vis = new boolean[V];
        List<Integer> ans = new ArrayList<>();

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            adj.get(edge.get(0)).add(edge.get(1));
            adj.get(edge.get(1)).add(edge.get(0));
        }

        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                vis[i] = true;
                bfs(i, adj, vis, ans);
            }
        }

        return ans;
    }
}
