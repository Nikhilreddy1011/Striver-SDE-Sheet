class Solution {
    private void dfs(int node, List<List<Integer>> adj, int[] vis, Stack<Integer> st) {
        vis[node] = 1;

        for (int it : adj.get(node)) {
            if (vis[it] == 0) {
                dfs(it, adj, vis, st);
            }
        }

        st.push(node);
    }

    public int[] topoSort(int V, List<List<Integer>> adj) {
        int[] ans = new int[V];
        Stack<Integer> st = new Stack<>();
        int[] vis = new int[V];

        for (int i = 0; i < V; i++) {
            if (vis[i] == 0) {
                dfs(i, adj, vis, st);
            }
        }

        for (int i = 0; i < V; i++) {
            ans[i] = st.pop();
        }

        return ans;
    }
}
