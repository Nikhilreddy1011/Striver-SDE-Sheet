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

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        int[] dist = new int[V];
        Arrays.fill(dist, (int) 1e9);

        dist[S] = 0;
        pq.add(new int[]{0, S});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int dis = curr[0];
            int node = curr[1];

            for (ArrayList<Integer> it : adj.get(node)) {
                int adjNode = it.get(0);
                int edgeWt = it.get(1);

                if (dis + edgeWt < dist[adjNode]) {
                    dist[adjNode] = dis + edgeWt;
                    pq.add(new int[]{dist[adjNode], adjNode});
                }
            }
        }

        return dist;
    }
}
