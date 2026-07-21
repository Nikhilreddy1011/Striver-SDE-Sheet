class Solution {
    public int[] dijkstra(int V, ArrayList<ArrayList<Integer>> edges, int S) {
        ArrayList<ArrayList<ArrayList<Integer>>> adj = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for (ArrayList<Integer> e : edges) {
            int u = e.get(0);
            int v = e.get(1);
            int wt = e.get(2);

            adj.get(u).add(new ArrayList<>(Arrays.asList(v, wt)));
            adj.get(v).add(new ArrayList<>(Arrays.asList(u, wt)));
        }

        TreeSet<int[]> set = new TreeSet<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });

        int[] dist = new int[V];
        Arrays.fill(dist, (int) 1e9);

        dist[S] = 0;
        set.add(new int[]{0, S});

        while (!set.isEmpty()) {
            int[] current = set.pollFirst();
            int dis = current[0];
            int node = current[1];

            for (ArrayList<Integer> neighbor : adj.get(node)) {
                int adjNode = neighbor.get(0);
                int edgeWt = neighbor.get(1);

                if (dis + edgeWt < dist[adjNode]) {
                    set.remove(new int[]{dist[adjNode], adjNode});
                    dist[adjNode] = dis + edgeWt;
                    set.add(new int[]{dist[adjNode], adjNode});
                }
            }
        }

        return dist;
    }
}
