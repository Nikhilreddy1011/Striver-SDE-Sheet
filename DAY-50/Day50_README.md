# 🚀 Day 50 – #SDESheetChallenge

> Graph Part-II — Strongly Connected Components (Kosaraju's), shortest paths with Dijkstra's, and shortest paths with negative weights via Bellman-Ford.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Strongly Connected Components — Kosaraju's Algorithm](#1-strongly-connected-components--kosarajus-algorithm) | 🔴 Hard | Graph / DFS |
| 2 | [Dijkstra's Algorithm](#2-dijkstras-algorithm) | 🔴 Hard | Graph / Shortest Path |
| 3 | [Bellman-Ford Algorithm](#3-bellman-ford-algorithm) | 🔴 Hard | Graph / Shortest Path |

---

## 1. Strongly Connected Components — Kosaraju's Algorithm

### 🧩 Problem Statement

Given a directed graph with V vertices (0 to V-1) and adjacency list `Adj`, find the number of Strongly Connected Components (SCCs). An SCC is a set of nodes where every node is reachable from every other node in the same set.

**Example 1:**
```
V=5, Adj=[[2,3],[0],[1],[4],[]]
Output: 3
```

**Example 2:**
```
V=8, Adj=[[1],[2],[0,3],[4],[5,7],[6],[4,7],[]]
Output: 4
```

**Try it:**
```
V=3, Adj=[[1],[2],[]]
Output: 3   (0→1→2 is a chain with no back edges — each node is its own SCC)
```

**Constraints:** `1 <= V <= 5000`, `0 <= E <= V*(V-1)`

### 💡 Intuition
A single DFS from any node in a chain of SCCs (e.g., SCC1 → SCC2 → SCC3 via forward edges) will end up visiting *everything*, since it can follow the connecting edges from one SCC into the next. That makes it impossible to tell the SCCs apart from a single DFS pass.

But if we **reverse every edge** in the graph, and start DFS from a node in the *last* SCC in the chain (the one nothing points forward into from outside), that DFS can now only travel backward along the reversed connecting edges — which no longer exist as forward paths — so it stays confined to just that one SCC.

The catch: we need to know which node to start from. The fix is to first do a normal DFS on the *original* graph and track finishing times (when a node's entire subtree is done being explored). The node that finishes **last** is guaranteed to be in a "root" SCC — one that nothing else points into. Starting the reversed-graph DFS from that node (and continuing down the finish-time order) correctly isolates one SCC per DFS call.

### Kosaraju's Algorithm
1. **Sort nodes by finishing time:** DFS on the original graph, pushing each node onto a stack only after all its neighbors are fully processed.
2. **Reverse all edges** to build the transposed graph.
3. **DFS on the reversed graph**, popping nodes off the stack (largest finish time first). Each fresh DFS call = one new SCC. Count the calls.

### 💻 Code
```java
class Solution {
    private void dfs(int node, int[] visited, ArrayList<ArrayList<Integer>> adj, Stack<Integer> st) {
        visited[node] = 1;
        for (int it : adj.get(node)) {
            if (visited[it] == 0) {
                dfs(it, visited, adj, st);
            }
        }
        st.push(node);
    }

    private void dfs3(int node, int[] visited, ArrayList<ArrayList<Integer>> aReverse) {
        visited[node] = 1;
        for (int it : aReverse.get(node)) {
            if (visited[it] == 0) {
                dfs3(it, visited, aReverse);
            }
        }
    }

    public int kosaraju(int V, ArrayList<ArrayList<Integer>> adj) {
        int[] visited = new int[V];
        Stack<Integer> st = new Stack<>();

        for (int i = 0; i < V; i++) {
            if (visited[i] == 0) {
                dfs(i, visited, adj, st);
            }
        }

        ArrayList<ArrayList<Integer>> aReverse = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            aReverse.add(new ArrayList<>());
        }

        for (int i = 0; i < V; i++) {
            visited[i] = 0;
            for (int it : adj.get(i)) {
                aReverse.get(it).add(i);
            }
        }

        int scc = 0;
        while (!st.isEmpty()) {
            int node = st.peek();
            st.pop();
            if (visited[node] == 0) {
                scc += 1;
                dfs3(node, visited, aReverse);
            }
        }

        return scc;
    }
}
```

### 🧪 Dry Run

**V=5, adj: 0→2, 0→3, 1→0, 2→1, 3→4**

```
Step 1 — DFS on original graph for finish order:
dfs(0): visited[0]=1
  neighbor 2: unvisited → dfs(2): visited[2]=1
    neighbor 1: unvisited → dfs(1): visited[1]=1
      neighbor 0: visited → skip
      push 1. stack=[1]
    push 2. stack=[1,2]
  neighbor 3: unvisited → dfs(3): visited[3]=1
    neighbor 4: unvisited → dfs(4): visited[4]=1
      no neighbors → push 4. stack=[1,2,4]
    push 3. stack=[1,2,4,3]
  push 0. stack=[1,2,4,3,0]

Step 2 — reverse the graph:
Original: 0→2, 0→3, 1→0, 2→1, 3→4
Reversed: 2→0, 3→0, 0→1, 1→2, 4→3

Step 3 — pop stack, DFS on reversed graph, reset visited:
pop 0 (top of stack) → unvisited → scc=1, dfs3(0):
  visited[0]=1, neighbor 1: unvisited → dfs3(1): visited[1]=1
    neighbor 2: unvisited → dfs3(2): visited[2]=1
      neighbor 0: visited → skip
  → this DFS call visited {0, 1, 2} — first SCC

pop 3 → unvisited → scc=2, dfs3(3):
  visited[3]=1, neighbor 0: visited → skip
  → this DFS call visited just {3} — second SCC

pop 4 → unvisited → scc=3, dfs3(4):
  visited[4]=1, neighbor 3: visited → skip
  → this DFS call visited just {4} — third SCC

pop 2, pop 1 → both already visited → skip

Final scc count = 3 ✅ — matches expected output for this graph structure.
```
SCC1 = {0,1,2} (they cycle back to each other: 0→2→1→0), SCC2 = {3}, SCC3 = {4}.

> 💡 **Why does reversing the edges preserve the SCCs but isolate them?**
> Within an SCC, every node can reach every other node — reversing all edges doesn't break that mutual reachability (if u could reach v and v could reach u before, that's still true after reversing both directions get preserved). But the *connecting* edges between different SCCs only went one way — reversing them flips which SCC can "leak into" which, which is exactly what stops one DFS call from bleeding into a neighboring SCC when started at the right node.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Kosaraju's (2× DFS + edge reversal) | **O(V + E)** ✅ | **O(V + E)** — transposed graph, plus O(V) for stack/visited ✅ |

---

## 2. Dijkstra's Algorithm

### 🧩 Problem Statement

Given a weighted, undirected graph of V vertices and edges `[u, v, weight]`, and a source node S, find the shortest distance from S to every other vertex. If unreachable, distance is `10^9`.

**Example 1:**
```
V=2, edges=[[0,1,9]], S=0
Output: [0, 9]
```

**Example 2:**
```
V=3, edges=[[0,1,1],[0,2,6],[1,2,3]], S=2
Output: [4, 3, 0]
```

**Try it:**
```
V=4, edges=[[0,1,1],[0,3,2],[1,2,4],[2,3,3]], S=0
Output: [0, 1, 5, 2]   (0→1 costs 1; 0→3 costs 2; 0→3→2 costs 2+3=5, beats 0→1→2's 1+4=5 — tie, either path works)
```

**Constraints:** `1 <= V <= 10000`, non-negative edge weights required

### 💡 Intuition
Dijkstra's finds shortest distances from a source in a graph with **no negative weights**. Keep a tentative distance for every node (starting at infinity, except the source at 0). Repeatedly pick the unvisited node with the smallest known distance, "relax" all its edges (check if going through this node gives a shorter path to its neighbors), then mark it done and never revisit it.

**Why a min-heap (priority queue) or an ordered set?** Storing `{distance, node}` pairs in a structure that always gives you the smallest distance first means you always explore the currently-shortest-known path before longer ones — this is what makes the algorithm efficient instead of brute-force exploring every path.

**Priority Queue vs Set:** Both give you the min-distance pair first. But the priority queue can end up holding multiple *stale* entries for the same node (from before a shorter distance was found) — it just doesn't bother removing them, they get skipped later. A `TreeSet` lets you actively **remove** the old, now-outdated `{oldDist, node}` pair the moment a shorter distance is found, keeping the structure cleaner and only ever holding the most current known distance per node.

### Approach
1. Build an adjacency list of `{neighbor, weight}` pairs from the edge list.
2. `dist[]` array initialized to a large value (`1e9`), except `dist[S] = 0`.
3. Push `{0, S}` into the priority queue (or set).
4. While not empty: pop the smallest-distance pair `{dis, node}`. For each `{adjNode, edgeWt}` neighbor: if `dis + edgeWt < dist[adjNode]`, update `dist[adjNode]` and push the new pair (removing the stale one first, if using a set).
5. Return `dist[]`.

### 💻 Code — Priority Queue version
```java
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
```

### 💻 Code — TreeSet version
```java
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
```

### 🧪 Dry Run

**V=3, edges=[[0,1,1],[0,2,6],[1,2,3]], S=2** (using the set version)

```
adj: 0:[(1,1),(2,6)], 1:[(0,1),(2,3)], 2:[(0,6),(1,3)]
dist = [1e9, 1e9, 0], set = {(0,2)}

pop (0,2): dis=0, node=2
  neighbor (0,6): 0+6=6 < dist[0](1e9) → remove nothing (no stale entry) → dist[0]=6, add (6,0)
  neighbor (1,3): 0+3=3 < dist[1](1e9) → dist[1]=3, add (3,1)
  set = {(3,1),(6,0)}

pop (3,1): dis=3, node=1
  neighbor (0,1): 3+1=4 < dist[0](6) → remove stale (6,0) → dist[0]=4, add (4,0)
  neighbor (2,3): 3+3=6, not < dist[2](0) → skip
  set = {(4,0)}

pop (4,0): dis=4, node=0
  neighbor (1,1): 4+1=5, not < dist[1](3) → skip
  neighbor (2,6): 4+6=10, not < dist[2](0) → skip
  set empty → done

Final dist = [4, 3, 0] ✅ — matches expected output.
```

> 💡 **Why doesn't Dijkstra work with negative weights?**
> The algorithm permanently "finalizes" a node's distance the moment it's popped with the smallest known value, assuming no future relaxation could ever improve it. A negative edge discovered later could still shrink that distance — but Dijkstra never revisits a finalized node, so it can produce a wrong (too-large) answer.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Priority Queue | O((V+E) log V) | O(V) — can hold stale entries |
| TreeSet | **O((V+E) log V)** ✅ | **O(V)** — actively removes stale entries ✅ |

---

## 3. Bellman-Ford Algorithm

### 🧩 Problem Statement

Given a weighted, **directed** graph of V vertices and E edges `[a, b, w]`, find the shortest distance from source S to every vertex. Unreachable = `10^9`. If the graph has a negative cycle, return `[-1]`.

**Example 1:**
```
V=6, Edges=[[3,2,6],[5,3,1],[0,1,5],[1,5,-3],[1,2,-2],[3,4,-2],[2,4,3]], S=0
Output: 0 5 3 3 1 2
```

**Example 2:**
```
V=2, Edges=[[0,1,9]], S=0
Output: 0 9
```

**Try it:**
```
V=3, Edges=[[0,1,5],[1,0,3],[1,2,-1],[2,0,1]], S=2
Output: 1 6 0   (2→0 costs 1; 2→0→1 costs 1+5=6; direct 2→1 doesn't exist)
```

**Constraints:** `1 <= V <= 500`, `-1000 <= weight <= 1000`

### 💡 Intuition
Dijkstra's fails the moment negative edges are involved — it finalizes distances too early. Bellman-Ford fixes this by not finalizing anything early: it just relaxes **every edge, repeatedly**, enough times to guarantee correctness.

**Edge Relaxation:** for an edge `(u, v, wt)`, if `dist[u] + wt < dist[v]`, then a shorter path to `v` (via `u`) was found — update `dist[v] = dist[u] + wt`.

**Why exactly V-1 iterations?** The longest possible path without repeating a node (no cycles) in a graph with V nodes has at most V-1 edges. Each full pass over all edges guarantees at least one more edge along the true shortest path gets correctly relaxed — so after V-1 passes, every shortest path (up to V-1 edges long) has been fully resolved.

**Negative cycle detection:** if a graph has a negative cycle, relaxation could technically continue forever, shrinking distances infinitely. But we know all *real* shortest paths finish relaxing within V-1 iterations. So: run **one more** relaxation pass after the V-1. If *any* edge can still relax, that improvement could only be coming from a negative cycle — confirming one exists.

### Approach
1. `dist[]` initialized to `1e9`, except `dist[S] = 0`.
2. Repeat V-1 times: for every edge `(u, v, wt)`, if `dist[u] != 1e9` and `dist[u] + wt < dist[v]`, update `dist[v]`.
3. One extra pass over all edges: if any edge can still relax, return `[-1]` (negative cycle detected).
4. Otherwise return `dist[]`.

### 💻 Code
```java
class Solution {
    static int[] bellman_ford(int V, ArrayList<ArrayList<Integer>> edges, int S) {
        int[] dist = new int[V];
        Arrays.fill(dist, (int) 1e9);

        dist[S] = 0;

        for (int i = 0; i < V - 1; i++) {
            for (ArrayList<Integer> it : edges) {
                int u = it.get(0);
                int v = it.get(1);
                int wt = it.get(2);

                if (dist[u] != 1e9 && dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                }
            }
        }

        for (ArrayList<Integer> it : edges) {
            int u = it.get(0);
            int v = it.get(1);
            int wt = it.get(2);

            if (dist[u] != 1e9 && dist[u] + wt < dist[v]) {
                return new int[]{-1};
            }
        }

        return dist;
    }
}
```

### 🧪 Dry Run

**V=3, Edges=[[0,1,5],[1,0,3],[1,2,-1],[2,0,1]], S=2**

```
dist = [1e9, 1e9, 0]

Pass 1 of 2 (V-1 = 2 iterations):
  edge (0,1,5): dist[0]=1e9 → skip (unreached)
  edge (1,0,3): dist[1]=1e9 → skip
  edge (1,2,-1): dist[1]=1e9 → skip
  edge (2,0,1): dist[2]=0, 0+1=1 < dist[0](1e9) → dist[0]=1
  dist = [1, 1e9, 0]

Pass 2 of 2:
  edge (0,1,5): dist[0]=1, 1+5=6 < dist[1](1e9) → dist[1]=6
  edge (1,0,3): dist[1]=6, 6+3=9, not < dist[0](1) → skip
  edge (1,2,-1): dist[1]=6, 6-1=5, not < dist[2](0) → skip
  edge (2,0,1): dist[2]=0, 0+1=1, not < dist[0](1) → skip (equal, not strictly less)
  dist = [1, 6, 0]

Extra pass (negative cycle check):
  edge (0,1,5): 1+5=6, not < dist[1](6) → skip
  edge (1,0,3): 6+3=9, not < dist[0](1) → skip
  edge (1,2,-1): 6-1=5, not < dist[2](0) → skip
  edge (2,0,1): 0+1=1, not < dist[0](1) → skip
  No relaxation possible → no negative cycle.

Final dist = [1, 6, 0] ✅ — matches expected output.
```

> 💡 **Why does an extra successful relaxation confirm a negative cycle?**
> By the end of V-1 passes, every genuinely shortest path (which can have at most V-1 edges) is already fully computed and stable — nothing should be able to relax further. If something *still* relaxes on the (V)th pass, that improvement isn't coming from a legitimate shortest path — it's coming from going around a cycle whose total weight is negative, which keeps shrinking the distance indefinitely.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Bellman-Ford (V-1 relaxation passes + 1 check pass) | **O(V × E)** ✅ | **O(V)** — distance array ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| SCC — Kosaraju's | DFS for finishing-time order → reverse all edges → DFS again in that order. Each DFS call on the reversed graph = one SCC |
| Dijkstra's | Greedy: always expand the smallest known distance next. Only works with non-negative weights. Set > priority queue since it can evict stale entries |
| Bellman-Ford | Relax every edge V-1 times to guarantee correctness even with negative weights. One extra pass detects negative cycles — if anything still relaxes, a cycle is shrinking distances forever |

---

*#SDESheetChallenge #DSA #Java #GraphAlgorithms #Dijkstra #BellmanFord #Kosaraju #TakeUForward #Striver*
