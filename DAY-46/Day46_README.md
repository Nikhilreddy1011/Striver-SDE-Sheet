# 🚀 Day 46 – #SDESheetChallenge

> **Post-45.** The 45-day sprint is done — now clearing the remaining problems on the sheet, one topic at a time. Starting with Graphs.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Clone Graph](#1-clone-graph) | 🟡 Medium | Graph / DFS / HashMap |
| 2 | [Traversal Techniques (DFS & BFS)](#2-traversal-techniques-dfs--bfs) | 🟡 Medium | Graph / DFS / BFS |

---

## 1. Clone Graph

### 🧩 Problem Statement

You are given a reference to a node in a connected undirected graph. Each node has a value `val` (unique integer from 1 to 100) and a list of `neighbors`. Return a deep copy (clone) of the graph — same structure and values, but with entirely new nodes.

```java
class Node {
    public int val;
    public List<Node> neighbors;
}
```

**Example 1:**
```
adjList = [[2,4],[1,3],[2,4],[1,3]]
Output:   [[2,4],[1,3],[2,4],[1,3]]

Node 1 connects to nodes 2 and 4
Node 2 connects to nodes 1 and 3
Node 3 connects to nodes 2 and 4
Node 4 connects to nodes 1 and 3
```

**Edge case:** `adjList = []` → `Output: []`

**Constraints:** `0 <= nodes <= 100`, `1 <= Node.val <= 100`, values unique, no duplicate edges/self-loops, graph connected and fully reachable from node 1.

---

### 🔴 Naive idea — plain recursive copy (breaks)
**Why it fails:** just recursing "clone this node, then clone each neighbor" with no memory of what's already been cloned will loop forever the moment the graph has a cycle — node 1 clones node 2, node 2 clones node 1, node 1 clones node 2... infinite recursion.

---

### 🟢 Optimal — DFS + HashMap
**Time: O(V + E) | Space: O(V)**

#### 💡 Idea
Keep a `Map<Node, Node>` from original node → cloned node. Before cloning a node, check the map — if it's already been cloned, return that clone instead of making a new one. This is exactly what breaks the infinite loop: the second time we reach an already-visited node, we short-circuit instead of recursing again.

#### 💻 Code
```java
class Solution {
    private Map<Node, Node> map = new HashMap<>();

    public Node cloneGraph(Node node) {
        if (node == null) return null;

        if (map.containsKey(node)) return map.get(node);

        Node clone = new Node(node.val);
        map.put(node, clone);

        for (Node nei : node.neighbors) {
            clone.neighbors.add(cloneGraph(nei));
        }

        return clone;
    }
}
```

#### 🧪 Dry Run

**adjList = [[2,4],[1,3],[2,4],[1,3]]** → nodes 1-2-3-4 forming a 4-cycle (1↔2, 2↔3, 3↔4, 4↔1)

```
cloneGraph(1)
  map empty → not found
  clone1 = Node(1), map = {1: clone1}
  neighbors of 1 = [2, 4]

    cloneGraph(2)
      not in map → clone2 = Node(2), map = {1:clone1, 2:clone2}
      neighbors of 2 = [1, 3]
        cloneGraph(1) → already in map → return clone1  (cycle broken here)
        cloneGraph(3)
          not in map → clone3 = Node(3), map = {..., 3:clone3}
          neighbors of 3 = [2, 4]
            cloneGraph(2) → already in map → return clone2
            cloneGraph(4)
              not in map → clone4 = Node(4), map = {..., 4:clone4}
              neighbors of 4 = [1, 3]
                cloneGraph(1) → return clone1
                cloneGraph(3) → return clone3
              clone4.neighbors = [clone1, clone3]
              return clone4
          clone3.neighbors = [clone2, clone4]
          return clone3
      clone2.neighbors = [clone1, clone3]
      return clone2

  clone1.neighbors = [clone2, clone4]
  return clone1
```
Result: `1↔2, 2↔3, 3↔4, 4↔1` — same structure, all new node objects, each node cloned exactly once. ✅

> 💡 **Why a HashMap and not just a `visited` boolean array?**
> A plain visited flag tells you *that* a node was seen, but not *what its clone is*. When node 3 needs to attach clone2 as a neighbor, it needs the actual clone object back — the map gives you that in O(1), a visited array doesn't.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Naive recursive copy (no map) | ❌ infinite loop on cycles | — |
| DFS + HashMap | **O(V + E)** ✅ | **O(V)** ✅ |

---

## 2. Traversal Techniques (DFS & BFS)

### 🧩 Problem Statement

Given an undirected connected graph with V vertices numbered `0` to `V-1`, implement both Depth First Search (DFS) and Breadth First Search (BFS) traversals starting from vertex 0. The graph is given as an edge list `[u, v]`.

**Example 1:**
```
V = 5, edges = [[0,1],[0,2],[0,3],[2,4]]
DFS: 0 → 2 → 4 → 3 → 1
BFS: 0 → 2 → 3 → 1 → 4
```

**Example 2:**
```
V = 4, edges = [[0,1],[0,3],[1,2]]
DFS: [0, 1, 2, 3]
BFS: [0, 1, 3, 2]
```

**Constraints:** `1 <= V, E <= 10^4`

---

### 🟢 Approach — Adjacency List + DFS (recursion) / BFS (queue)
**Time: O(V + E) | Space: O(V)**

#### 💡 Idea
Build an adjacency list from the edge list first — since it's undirected, each edge `[u, v]` adds v to u's list *and* u to v's list. From there:
- **DFS** commits to a path and goes as deep as possible before backtracking — recursive, uses the call stack.
- **BFS** explores level by level, all neighbors of the current node before moving further out — iterative, uses an explicit queue.

Both need a `visited` array so cycles or already-processed nodes don't cause reprocessing.

**Key detail for BFS:** mark a node visited the moment it's *enqueued*, not when it's dequeued — otherwise the same node can be pushed into the queue multiple times by different neighbors before it's ever processed.

#### 💻 Code
```java
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
```

#### 🧪 Dry Run

**V = 3, edges = [[0,1],[0,2]]** → adjacency list: `0: [1,2]`, `1: [0]`, `2: [0]`

```
DFS from 0:
  dfs(0): vis[0]=true, ans=[0]
    neighbor 1: unvisited → dfs(1): vis[1]=true, ans=[0,1]
      neighbor 0: visited → skip
      backtrack to dfs(0)
    neighbor 2: unvisited → dfs(2): vis[2]=true, ans=[0,1,2]
      neighbor 0: visited → skip
      backtrack
  Result: [0, 1, 2]

BFS from 0:
  vis[0]=true, queue=[0]
  pop 0 → ans=[0]
    neighbor 1: unvisited → vis[1]=true, queue=[1]
    neighbor 2: unvisited → vis[2]=true, queue=[1,2]
  pop 1 → ans=[0,1]
    neighbor 0: visited → skip
  pop 2 → ans=[0,1,2]
    neighbor 0: visited → skip
  Result: [0, 1, 2]
```
Both give `[0, 1, 2]` here since the graph is a simple 2-level star. ✅

> 💡 **Why does DFS use a stack while BFS uses a queue?**
> DFS needs to remember the path back to where it can branch off again — a stack (or recursion) naturally does that (last node visited is the first one backtracked to). BFS needs to process nodes in the order they were discovered, layer by layer — a queue (first in, first out) enforces that ordering.

> 💡 **What happens if the graph contains cycles?**
> Without the `visited` array, both traversals would loop forever bouncing between connected nodes. The `visited` check is what makes both correct on cyclic graphs, not just trees.

#### 📊 Complexity Summary

| Traversal | Time | Space |
|-----------|------|-------|
| DFS | O(V + E) | O(V) — recursion stack |
| BFS | O(V + E) | O(V) — queue |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Clone Graph | DFS + `Map<Node, Node>` to remember original → clone. Map lookup breaks cycles and returns the actual clone object, not just a "seen" flag |
| Traversal Techniques | Build adjacency list from edge list (both directions, undirected). DFS = recursion/stack, goes deep first. BFS = queue, mark visited on enqueue to avoid duplicate queue entries |

---

*#SDESheetChallenge #DSA #Java #GraphAlgorithms #TakeUForward #Striver*