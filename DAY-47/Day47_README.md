# 🚀 Day 47 – #SDESheetChallenge

> Cycle detection in graphs — undirected via BFS, undirected via DFS, and directed via DFS.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Detect a Cycle in an Undirected Graph using BFS](#1-detect-a-cycle-in-an-undirected-graph-using-bfs) | 🔴 Hard | Graph / BFS |
| 2 | [Detect a Cycle in an Undirected Graph using DFS](#2-detect-a-cycle-in-an-undirected-graph-using-dfs) | 🔴 Hard | Graph / DFS |
| 3 | [Detect a Cycle in a Directed Graph using DFS](#3-detect-a-cycle-in-a-directed-graph-using-dfs) | 🔴 Hard | Graph / DFS |

---

## 1. Detect a Cycle in an Undirected Graph using BFS

### 🧩 Problem Statement

Given an undirected graph with V vertices labeled 0 to V-1, represented as an adjacency list where `adj[i]` lists all nodes connected to node `i`. Determine if the graph contains any cycle. The graph has no self-edges.

**Example 1:**
```
V = 6, adj = [[1,3],[0,2,4],[1,5],[0,4],[1,3,5],[2,4]]
Output: True
Explanation: 0 -> 1 -> 2 -> 5 -> 4 -> 1 forms a cycle.
```

**Example 2:**
```
V = 4, adj = [[1,2],[0],[0,3],[2]]
Output: False
```

**Try it:**
```
V = 4, adj = [[1,2],[0,2],[0,1,3],[2]]
Output: True   (0-1-2-0 forms a triangle → cycle)
```

**Constraints:** `1 <= V, E <= 10^4`

---

### 🟢 Approach — BFS with parent tracking
**Time: O(V + E) | Space: O(V)**

#### 💡 Idea
In an undirected graph, a cycle exists if there's a path that returns to the starting vertex without reusing the same edge. During BFS, if we reach a neighbor that's already visited **and it isn't the immediate parent** we just came from, we've found a second distinct path to that node — that's a cycle.

Why check the parent specifically? Because in an undirected graph, every edge `(u, v)` is stored both as `u → v` and `v → u`. So the node we just came from will *always* show up as a "visited neighbor" — that's not a cycle, that's just the edge we arrived on. We only flag a cycle when we see a visited node that is **not** that immediate parent.

#### 💻 Code
```java
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
```

#### 🧪 Dry Run

**adj: 1→{2,3}, 2→{1,5}, 3→{1,4,6}, 4→{3}, 5→{2,7}, 6→{3,7}, 7→{5,6}**

```
Start BFS(1): visited=[1:T], queue=[{1,-1}]

pop {1,-1}: node=1, parent=-1
  neighbor 2: unvisited → visited[2]=T, queue=[{2,1}]
  neighbor 3: unvisited → visited[3]=T, queue=[{2,1},{3,1}]

pop {2,1}: node=2, parent=1
  neighbor 1: visited AND == parent → skip (not a cycle, it's the edge we arrived on)
  neighbor 5: unvisited → visited[5]=T, queue=[{3,1},{5,2}]

pop {3,1}: node=3, parent=1
  neighbor 1: visited AND == parent → skip
  neighbor 4: unvisited → visited[4]=T, queue=[{5,2},{4,3}]
  neighbor 6: unvisited → visited[6]=T, queue=[{5,2},{4,3},{6,3}]

pop {5,2}: node=5, parent=2
  neighbor 2: visited AND == parent → skip
  neighbor 7: unvisited → visited[7]=T, queue=[{4,3},{6,3},{7,5}]

pop {4,3}: node=4, parent=3
  neighbor 3: visited AND == parent → skip
  (no other neighbors)

pop {6,3}: node=6, parent=3
  neighbor 3: visited AND == parent → skip
  neighbor 7: visited AND != parent(3) → CYCLE DETECTED → return true
```
The BFS from two different branches (via node 5 and via node 6) both reached node 7 — that collision is the cycle. ✅

> 💡 **Why does starting from two different points and "colliding" mean a cycle?**
> If a node can be reached by two different paths from the same root, then those two paths plus the node itself form a loop back to a common ancestor — which is exactly a cycle.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| BFS + parent tracking | **O(V + E)** ✅ | **O(V)** ✅ |

---

## 2. Detect a Cycle in an Undirected Graph using DFS

### 🧩 Problem Statement
Same problem as above — undirected graph, detect if it contains a cycle — solved using DFS instead of BFS.

---

### 🟢 Approach — DFS with parent tracking
**Time: O(V + E) | Space: O(V)**

#### 💡 Idea
Same core idea as the BFS version, just carried through recursion instead of a queue. Start DFS from a node, passing along its parent. For every neighbor:
- If unvisited, recurse into it (with the current node as its parent).
- If already visited and **not** the parent we came from, that's a second path into an already-visited node → cycle.

#### 💻 Code
```java
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
```

#### 🧪 Dry Run

**adj: 1↔2, 1↔3, 3↔4, 3↔6, 5↔2, 5↔7, 6↔7 (same graph as above)**

```
detectCycle(1, adj, visited, -1): visited[1]=T
  neighbor 2: unvisited → detectCycle(2, adj, visited, 1): visited[2]=T
    neighbor 1: visited, == parent(1) → skip
    neighbor 5: unvisited → detectCycle(5, adj, visited, 2): visited[5]=T
      neighbor 2: visited, == parent(2) → skip
      neighbor 7: unvisited → detectCycle(7, adj, visited, 5): visited[7]=T
        neighbor 5: visited, == parent(5) → skip
        neighbor 6: unvisited → detectCycle(6, adj, visited, 7): visited[6]=T
          neighbor 3: unvisited → detectCycle(3, adj, visited, 6): visited[3]=T
            neighbor 1: visited, != parent(6) → CYCLE DETECTED → return true
```
Node 1 was reached again while exploring from node 3, but node 3's parent is 6, not 1 — so this is a genuine second path to node 1 → cycle confirmed. ✅

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| DFS + parent tracking | **O(V + E)** ✅ | **O(V)** — recursion stack ✅ |

---

## 3. Detect a Cycle in a Directed Graph using DFS

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

---

### 🔴 Why the undirected trick (parent check) doesn't work here
My first instinct was that this would be the same as the undirected DFS — just check if a visited node isn't the parent. But that breaks for directed graphs: a node can legitimately be reached from two completely different, non-cyclic branches (since edges only go one way), and it would still show up as "already visited," wrongly flagging a cycle. Simply finding a previously visited node isn't enough — it might have been visited via a totally different path that never loops back.

### 🟢 Approach — DFS with visited + pathVisited
**Time: O(V + E) | Space: O(V)**

#### 💡 Idea
The fix: track two arrays.
- `visited[]` — has this node been processed at all, ever (in any branch)?
- `pathVisited[]` — is this node part of the *current* DFS recursion path (i.e., still "on the stack")?

If we hit a node that's `pathVisited == true`, we've looped back onto our own current path → cycle. If a node is `visited == true` but `pathVisited == false`, it was fully explored via some other branch earlier and safely finished — no cycle. Critically, once we finish exploring a node's branch and backtrack, we unmark it as `pathVisited = false` — it's no longer "on the current path," even though it stays `visited = true` forever.

#### 💻 Code
```java
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
```

#### 🧪 Dry Run

**adj: 1→2, 2→3, 3→4, 4→1 (cycle), and a separate branch 8→9, 9→10 (no cycle)**

```
i=1: dfs(1): visited[1]=T, pathVisited[1]=T
  neighbor 2: not pathVisited, not visited → dfs(2): visited[2]=T, pathVisited[2]=T
    neighbor 3: not pathVisited, not visited → dfs(3): visited[3]=T, pathVisited[3]=T
      neighbor 4: not pathVisited, not visited → dfs(4): visited[4]=T, pathVisited[4]=T
        neighbor 1: pathVisited[1]==true → CYCLE DETECTED → return true (bubbles all the way up)

(separate component, checked independently if the first component had returned false)
i=8: dfs(8): visited[8]=T, pathVisited[8]=T
  neighbor 9: dfs(9): visited[9]=T, pathVisited[9]=T
    neighbor 10: dfs(10): visited[10]=T, pathVisited[10]=T
      no neighbors → pathVisited[10]=false, return false
    pathVisited[9]=false, return false
  pathVisited[8]=false, return false
  → no cycle in this branch, all nodes stay visited=true but pathVisited resets to false
```
Node 1 was still `pathVisited = true` when node 4 looped back to it — because we never finished backtracking out of node 1's branch. That's exactly what marks it as a genuine cycle, as opposed to the 8→9→10 branch which fully unwinds with no revisit. ✅

> 💡 **Why can't we just reuse the undirected cycle logic here?**
> Undirected edges are always bidirectional, so "visited and not parent" is a reliable cycle signal. Directed edges only go one way, so a node being visited elsewhere doesn't mean anything looped back — only being visited *within the current active path* does. That's why `pathVisited` (not just `visited`) is essential.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| DFS + visited + pathVisited | **O(V + E)** ✅ | **O(V)** — two boolean arrays + recursion stack ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Undirected Cycle — BFS | Track `{node, parent}` pairs in the queue. A visited neighbor that isn't the parent = cycle |
| Undirected Cycle — DFS | Same parent-check idea, carried through recursion instead of a queue |
| Directed Cycle — DFS | Parent-check doesn't work — directed edges aren't bidirectional. Need `visited` (ever processed) + `pathVisited` (on the current recursion path). A hit on `pathVisited` = real cycle; unmark `pathVisited` on backtrack |

---

*#SDESheetChallenge #DSA #Java #GraphAlgorithms #CycleDetection #TakeUForward #Striver*
