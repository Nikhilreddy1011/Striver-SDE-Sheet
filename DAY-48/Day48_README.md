# 🚀 Day 48 – #SDESheetChallenge

> DAGs — detecting cycles in a directed graph via topological sort, and two ways to compute the topological order itself: BFS (Kahn's Algorithm) and DFS.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Detect a Cycle in a Directed Graph using BFS](#1-detect-a-cycle-in-a-directed-graph-using-bfs) | 🔴 Hard | Graph / Kahn's Algorithm |
| 2 | [Topological Sort using BFS (Kahn's Algorithm)](#2-topological-sort-using-bfs-kahns-algorithm) | 🔴 Hard | Graph / BFS |
| 3 | [Topological Sort using DFS](#3-topological-sort-using-dfs) | 🔴 Hard | Graph / DFS |

---

## 1. Detect a Cycle in a Directed Graph using BFS

### 🧩 Problem Statement

Given a directed graph with V vertices labeled 0 to V-1, represented as an adjacency list. Determine if the graph contains any cycle.

**Example 1:**
```
V = 6, adj = [[1],[2,5],[3],[4],[1],[]]
Output: True
Explanation: 1 -> 2 -> 3 -> 4 -> 1 forms a cycle.
```

**Example 2:**
```
V = 4, adj = [[1,2],[2],[],[0,2]]
Output: False
```

**Try it:**
```
V = 3, adj = [[1],[2],[0]]
Output: True   (0 -> 1 -> 2 -> 0 forms a cycle)
```

**Constraints:** `1 <= V <= 10^4`, `adj.size() == V`, `0 <= adj[i][j] < V`, `1 <= sum(adj[i].size()) <= 10^4`

### 💡 Intuition
Topological ordering of a graph with V nodes only exists if the graph is a DAG (no cycles) — the ordering will always contain all V nodes. If the directed graph has a cycle, no valid linear ordering can include the nodes trapped in that cycle, so the topological sort produced by Kahn's algorithm will come up short — it will contain fewer than V nodes. So: run Kahn's algorithm, and if the result has fewer than V nodes, the graph is cyclic.

### Approach
1. Run Kahn's algorithm (BFS-based topological sort) to get the ordering.
2. If `topo.size() < V`, the graph contains a cycle → return `true`.
3. Otherwise, it's a DAG → return `false`.

### 💻 Code
```java
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
```

### 🧪 Dry Run

**adj: 1→2, 2→3, 3→4, 4→1, 1→5** (V=6, nodes 0..5, node 0 isolated)

```
In-degrees: 0:0, 1:1(from 4), 2:1(from 1), 3:1(from 2), 4:1(from 3), 5:1(from 1)

Queue starts with in-degree-0 nodes: [0]

pop 0 → ans=[0]. node 0 has no outgoing edges → nothing to decrement.

Queue is now empty. But nodes 1,2,3,4,5 all still have in-degree > 0
(they form a cycle: 1→2→3→4→1, plus 1→5 which never gets unlocked
since node 1 itself never reaches in-degree 0).

ans = [0], size = 1 < V(6) → isCyclic returns true ✅
```
The cycle 1→2→3→4→1 means none of those nodes ever reach in-degree zero, so they never get added to the queue — the topological order comes up short, correctly flagging the cycle.

> 💡 **Why does a short topological order mean a cycle?**
> Kahn's algorithm only adds a node once all its "dependencies" (incoming edges) are resolved. In a cycle, every node in that cycle depends on another node in the same cycle — so none of them can ever be the first one processed. They get permanently stuck with in-degree > 0.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Kahn's Algorithm (topo sort) + size check | **O(V + E)** ✅ | **O(V)** ✅ |

---

## 2. Topological Sort using BFS (Kahn's Algorithm)

### 🧩 Problem Statement

Given a Directed Acyclic Graph (DAG) with V vertices labeled 0 to V-1, find any valid topological ordering — an ordering where for every directed edge `u -> v`, node `u` appears before node `v`.

**Example 1:**
```
V = 6, adj = [[],[],[3],[1],[0,1],[0,2]]
Output: [5, 4, 2, 3, 1, 0]   (one valid ordering; multiple can exist)
```

**Example 2:**
```
V = 4, adj = [[],[0],[0],[0]]
Output: [3, 2, 1, 0]
```

**Try it:**
```
V = 3, adj = [[1],[2],[]]
Output: [0, 1, 2]   (0→1, 1→2 forces this exact order)
```

**Constraints:** `1 <= V <= 10^4`, `0 <= edges <= 10^4`

### 💡 Intuition
Why does topological sort only exist for a DAG?
- If edges were undirected, an edge between u and v means u→v *and* v→u simultaneously — there's no way to order them both "before" each other.
- If the directed graph has a cycle, nodes within that cycle have no valid starting point — there's no linear order possible for them.

**Kahn's Algorithm:** there will always be at least one node with in-degree 0 in a DAG — a node nothing points to yet. Add all such nodes first (no dependencies). Then remove their outgoing edges, which may expose new zero-in-degree nodes. Repeat until done.

### Approach
1. Compute in-degree for every node by scanning all edges.
2. Push all nodes with in-degree 0 into a queue — these have no unresolved dependencies.
3. While the queue isn't empty: pop a node, add it to the answer, and for each neighbor decrement its in-degree. If a neighbor's in-degree hits 0, push it into the queue.
4. The final answer array is a valid topological order.

### 💻 Code
```java
class Solution {
    public int[] topoSort(int V, List<List<Integer>> adj) {
        int[] ans = new int[V];
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

        int index = 0;

        while (!q.isEmpty()) {
            int node = q.poll();
            ans[index++] = node;

            for (int it : adj.get(node)) {
                inDegree[it]--;
                if (inDegree[it] == 0) {
                    q.add(it);
                }
            }
        }

        return ans;
    }
}
```

### 🧪 Dry Run

**adj: 2→3, 3→1, 4→0, 4→1, 5→0, 5→2** (V=6)

```
In-degrees: 0:2(from 4,5), 1:2(from 3,4), 2:1(from 5), 3:1(from 2), 4:0, 5:0

Queue starts with in-degree-0 nodes: [4, 5]

pop 4 → ans=[4]. neighbors 0,1: inDegree[0]=1, inDegree[1]=1. neither hits 0.
pop 5 → ans=[4,5]. neighbors 0,2: inDegree[0]=0 → push 0. inDegree[2]=0 → push 2.
  queue=[0, 2]

pop 0 → ans=[4,5,0]. node 0 has no outgoing edges.
pop 2 → ans=[4,5,0,2]. neighbor 3: inDegree[3]=0 → push 3.
  queue=[3]

pop 3 → ans=[4,5,0,2,3]. neighbor 1: inDegree[1]=0 → push 1.
  queue=[1]

pop 1 → ans=[4,5,0,2,3,1]. no outgoing edges.

Final: [4, 5, 0, 2, 3, 1] — a valid topological order ✅
```
Every edge respects "u before v": 2 before 3, 3 before 1, 4 before 0 and 1, 5 before 0 and 2. ✔️

> 💡 **Why can multiple valid topological orders exist?**
> Whenever more than one node has in-degree 0 at the same time (like nodes 4 and 5 above), their relative order doesn't matter — neither depends on the other. Different tie-breaking (e.g., a different queue implementation) can produce an equally valid but different order.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Kahn's Algorithm (BFS) | **O(V + E)** ✅ | **O(V)** ✅ |

---

## 3. Topological Sort using DFS

### 🧩 Problem Statement
Same problem as above — find a valid topological ordering of a DAG — solved using DFS instead of BFS.

### 💡 Intuition
DFS is a natural fit here: it explores each path deeply before backtracking, which means by the time we finish processing a node's entire subtree, we know all of its dependents have already been fully handled. So a node is pushed onto a stack only *after* all its neighbors are done — meaning the deepest, most-depended-upon nodes get pushed first. Popping the stack at the end reverses that, producing nodes in the correct dependency order.

### Approach
1. Maintain a `visited` array and a `Stack<Integer>`.
2. Recursive DFS: mark the current node visited, recurse into every unvisited neighbor, then — **after** all neighbors are fully processed — push the current node onto the stack.
3. Run DFS from every unvisited node (to cover disconnected components).
4. Pop the stack fully to build the final topological order array.

### 💻 Code
```java
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
```

### 🧪 Dry Run

**adj: 2→3, 3→1, 4→0, 4→1, 5→0, 5→2** (same graph as above, V=6)

```
i=0: vis[0]=0 → dfs(0): vis[0]=1. No neighbors. push 0. stack=[0]

i=1: vis[1]=0 → dfs(1): vis[1]=1. No neighbors. push 1. stack=[0,1]

i=2: vis[2]=0 → dfs(2): vis[2]=1.
  neighbor 3: unvisited → dfs(3): vis[3]=1.
    neighbor 1: vis[1]=1 already → skip.
    push 3. stack=[0,1,3]
  push 2. stack=[0,1,3,2]

i=3: vis[3]=1 already → skip.

i=4: vis[4]=0 → dfs(4): vis[4]=1.
  neighbor 0: vis[0]=1 → skip.
  neighbor 1: vis[1]=1 → skip.
  push 4. stack=[0,1,3,2,4]

i=5: vis[5]=0 → dfs(5): vis[5]=1.
  neighbor 0: vis[0]=1 → skip.
  neighbor 2: vis[2]=1 → skip.
  push 5. stack=[0,1,3,2,4,5]

Pop all: ans = [5, 4, 2, 3, 1, 0]
```
Check: 2 before 3 ✔️, 3 before 1 ✔️, 4 before 0 and 1 ✔️, 5 before 0 and 2 ✔️. Valid topological order. ✅

> 💡 **Why push after processing neighbors, not before?**
> If we pushed a node before its neighbors were done, a dependent node could end up below its dependency on the stack — meaning it would pop out (and appear in the answer) too early. Pushing only after all neighbors are fully explored guarantees a node's dependents are already on the stack underneath it, so it always pops out first, before them.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| DFS + Stack | **O(V + E)** ✅ | **O(V)** — stack + recursion stack + visited array ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Detect Cycle (Directed) — via Kahn's | Topological order only exists for a DAG and must contain all V nodes. If Kahn's algorithm produces fewer than V nodes, some nodes were stuck in a cycle and never reached in-degree 0 |
| Topological Sort — BFS (Kahn's) | Push all zero in-degree nodes first (no dependencies). Removing their edges exposes new zero in-degree nodes — repeat until done |
| Topological Sort — DFS | Push a node onto the stack only after all its neighbors are fully processed. Popping the stack at the end reverses that into correct dependency order |

---

*#SDESheetChallenge #DSA #Java #GraphAlgorithms #TopologicalSort #KahnsAlgorithm #TakeUForward #Striver*
