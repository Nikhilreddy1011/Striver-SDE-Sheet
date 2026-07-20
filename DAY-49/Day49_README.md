# 🚀 Day 49 – #SDESheetChallenge

> Grids as graphs, and two ways to check if a graph is bipartite.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Number of Islands](#1-number-of-islands) | 🟡 Medium | Graph / Grid / BFS |
| 2 | [Bipartite Graph — BFS](#2-bipartite-graph--bfs) | 🔴 Hard | Graph / BFS / 2-Coloring |
| 3 | [Bipartite Check — DFS](#3-bipartite-check--dfs) | 🔴 Hard | Graph / DFS / 2-Coloring |

---

## 1. Number of Islands

### 🧩 Problem Statement

Given a grid of size N x M consisting of `'0'`s (Water) and `'1'`s (Land), find the number of islands. An island is formed by connecting adjacent lands horizontally, vertically, **or diagonally** — all 8 directions.

**Example 1:**
```
grid = [["1","1","1","0","1"],
        ["1","0","0","0","0"],
        ["1","1","1","0","1"],
        ["0","0","0","1","1"]]
Output: 2
```

**Example 2:**
```
grid = [["1","0","0","0","1"],
        ["0","1","0","1","0"],
        ["0","0","1","0","0"],
        ["0","1","0","1","0"]]
Output: 1
```

**Try it:**
```
grid = [["1","1","1","1","0"],
        ["1","1","0","1","0"],
        ["1","1","0","0","0"],
        ["0","0","0","0","0"]]
Output: 1   (all the 1s connect into a single landmass, including via diagonals)
```

**Constraints:** `N == grid.length`, `M == grid[i].length`, `1 <= N, M <= 300`, `grid[i][j]` is `'0'` or `'1'`

### 💡 Intuition
Treat every cell in the grid as a node, connected to its neighbors via up to 8 edges. The 8 neighbors of a cell `(row, col)` are all combinations of `deltaRow ∈ {-1,0,1}` and `deltaCol ∈ {-1,0,1}` (excluding the cell itself) — so a simple nested loop over both ranges visits all 8 directions cleanly, instead of writing 8 separate if-checks.

### Approach
1. Get grid dimensions. Create a 2D `visited` array, all `false`. Initialize an island counter to 0.
2. Loop through every cell. If it's land (`'1'`) and not yet visited, that's the start of a brand-new island.
3. BFS from that cell to mark every 8-directionally connected land cell as visited.
4. Increment the island count after each BFS completes.

### 💻 Code
```java
class Solution {
    private boolean isValid(int i, int j, int n, int m) {
        if (i < 0 || i >= n) return false;
        if (j < 0 || j >= m) return false;
        return true;
    }

    private void bfs(int i, int j, boolean[][] vis, char[][] grid) {
        vis[i][j] = true;

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{i, j});

        int n = grid.length;
        int m = grid[0].length;

        while (!q.isEmpty()) {
            int[] cell = q.poll();

            int row = cell[0];
            int col = cell[1];

            for (int delRow = -1; delRow <= 1; delRow++) {
                for (int delCol = -1; delCol <= 1; delCol++) {
                    int newRow = row + delRow;
                    int newCol = col + delCol;

                    if (isValid(newRow, newCol, n, m)
                            && grid[newRow][newCol] == '1'
                            && !vis[newRow][newCol]) {

                        vis[newRow][newCol] = true;
                        q.add(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    public int numIslands(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        boolean[][] vis = new boolean[n][m];

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!vis[i][j] && grid[i][j] == '1') {
                    count++;
                    bfs(i, j, vis, grid);
                }
            }
        }

        return count;
    }
}
```

### 🧪 Dry Run

**grid (3x3, simplified):**
```
1 1 0
0 1 0
0 0 1
```

```
Scan (0,0): land, unvisited → count=1, bfs(0,0):
  vis[0][0]=true, queue=[(0,0)]
  pop (0,0): check 8 neighbors (delRow,delCol both -1..1)
    (0,1): land, unvisited → vis[0][1]=true, queue=[(0,1)]
    (1,0): water → skip
    (1,1): land, unvisited → vis[1][1]=true, queue=[(0,1),(1,1)]
    (others out of bounds or water)
  pop (0,1): neighbors (0,0) visited, (1,1) visited, (1,0) water, (1,2) water → nothing new
  pop (1,1): neighbors all already visited or water/out of bounds → nothing new
  BFS done. Island 1 = {(0,0),(0,1),(1,1)}

Scan (0,1): already visited → skip
Scan (0,2): water → skip
Scan (1,0): water → skip
Scan (1,1): already visited → skip
Scan (1,2): water → skip
Scan (2,0): water → skip
Scan (2,1): water → skip
Scan (2,2): land, unvisited → count=2, bfs(2,2): marks just itself (no land neighbors)

Final count = 2 islands ✅
```
Note how (1,1) being land connects the top-left block diagonally — without 8-direction checking it would've looked like 2 separate blobs plus the corner, but here (0,0)-(0,1)-(1,1) merge into one island via the diagonal at (1,1).

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| BFS per unvisited land cell | **O(N × M)** — each cell visited once, ×9 for 8-neighbor + self check ✅ | **O(N × M)** — visited array + queue ✅ |

---

## 2. Bipartite Graph — BFS

### 🧩 Problem Statement

Given an undirected graph with V vertices and a list of edges, determine if the graph is bipartite. A graph is bipartite if its nodes can be split into two sets A and B such that every edge connects a node in A to a node in B (never within the same set).

**Example 1:**
```
V=4, edges = [[0,1],[0,3],[1,2],[2,3]]
Output: True   (sets {0,2} and {1,3})
```

**Example 2:**
```
V=4, edges = [[0,1],[0,2],[0,3],[2,1],[3,2]]
Output: False
```

**Try it:**
```
V=5, edges = [[0,1],[0,3],[1,2],[2,4],[4,3]]
Output: False   (0-1-2-4-3-0 forms a single 5-length cycle — an odd cycle, so not bipartite)
```

**Constraints:** `1 <= V, E <= 10^4`

### 💡 Intuition
A bipartite graph is one that can be 2-colored so no adjacent nodes share a color. Any graph with no cycle (a tree/forest) is always bipartite. With a cycle: an **even**-length cycle can still be 2-colored validly, but an **odd**-length cycle can never be — you'll always end up forcing two adjacent nodes into the same color. So checking bipartiteness = trying to alternately color the graph and seeing if you ever get stuck.

### Approach
1. `color[]` array, all initialized to -1 (uncolored). 0 and 1 represent the two colors.
2. BFS from every uncolored node:
   - Color the start node 0.
   - For each neighbor: if uncolored, color it the opposite of the current node and enqueue it. If already colored **the same** as the current node, the graph isn't bipartite.
3. If every component passes, the graph is bipartite.

### 💻 Code
```java
class Solution {
    private boolean bfs(int start, List<List<Integer>> adj, int[] color) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(start);
        color[start] = 0;

        while (!q.isEmpty()) {
            int node = q.poll();

            for (int it : adj.get(node)) {
                if (color[it] == -1) {
                    color[it] = 1 - color[node];
                    q.offer(it);
                } else if (color[it] == color[node]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBipartite(int V, List<List<Integer>> edges) {
        int[] color = new int[V];
        Arrays.fill(color, -1);

        List<List<Integer>> adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        for (int i = 0; i < V; i++) {
            if (color[i] == -1) {
                if (!bfs(i, adj, color)) {
                    return false;
                }
            }
        }

        return true;
    }
}
```

### 🧪 Dry Run

**V=4, edges = [[0,1],[0,3],[1,2],[2,3]]** → a 4-cycle: 0-1-2-3-0

```
adj: 0:[1,3], 1:[0,2], 2:[1,3], 3:[0,2]
color = [-1,-1,-1,-1]

bfs(0): color[0]=0, queue=[0]
pop 0: neighbors 1,3
  1: uncolored → color[1] = 1-0 = 1, queue=[1]
  3: uncolored → color[3] = 1-0 = 1, queue=[1,3]

pop 1: neighbors 0,2
  0: colored=0, color[1]=1 → different, OK
  2: uncolored → color[2] = 1-1 = 0, queue=[3,2]

pop 3: neighbors 0,2
  0: colored=0, color[3]=1 → different, OK
  2: colored=0, color[3]=1 → different, OK

pop 2: neighbors 1,3
  1: colored=1, color[2]=0 → different, OK
  3: colored=1, color[2]=0 → different, OK

No conflicts found. Returns true — the graph IS bipartite. ✅
Final coloring: {0,2} = color 0, {1,3} = color 1 — matches the expected sets.
```

> 💡 **Why does an odd cycle break this?**
> Walking around a cycle, colors must alternate 0,1,0,1,... If the cycle length is odd, by the time you get back to the starting node, the alternation forces it to be the opposite color of itself — a direct contradiction. Even cycles land back on the same color as the start, so no contradiction.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| BFS + alternating coloring | **O(V + E)** ✅ | **O(V)** ✅ |

---

## 3. Bipartite Check — DFS

### 🧩 Problem Statement
Same problem as above — check if an undirected graph is bipartite — solved using DFS instead of BFS.

### 💡 Intuition
Identical coloring logic to the BFS version — a graph is bipartite if and only if it can be validly 2-colored — just driven by recursion instead of a queue.

### Approach
1. `color[]` array, all -1.
2. DFS from every uncolored node: color the current node with the given color, then for each neighbor — if uncolored, recurse with the flipped color; if already colored and it matches the current node's color, return false.
3. If every component passes, the graph is bipartite.

### 💻 Code
```java
class Solution {
    private boolean dfs(int node, int col, int[] color, List<List<Integer>> adj) {
        color[node] = col;

        for (int it : adj.get(node)) {
            if (color[it] == -1) {
                if (!dfs(it, 1 - col, color, adj))
                    return false;
            } else if (color[it] == col) {
                return false;
            }
        }

        return true;
    }

    public boolean isBipartite(int V, List<List<Integer>> edges) {
        int[] color = new int[V];
        Arrays.fill(color, -1);

        List<List<Integer>> adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<>());

        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        for (int i = 0; i < V; i++) {
            if (color[i] == -1) {
                if (!dfs(i, 0, color, adj))
                    return false;
            }
        }

        return true;
    }
}
```

### 🧪 Dry Run

**V=4, edges = [[0,1],[0,2],[0,3],[2,1],[3,2]]** — this one is NOT bipartite

```
adj: 0:[1,2,3], 1:[0,2], 2:[0,1,3], 3:[0,2]
color = [-1,-1,-1,-1]

dfs(0, col=0): color[0]=0
  neighbor 1: uncolored → dfs(1, col=1): color[1]=1
    neighbor 0: colored=0, col=1 → different, OK
    neighbor 2: uncolored → dfs(2, col=0): color[2]=0
      neighbor 0: colored=0, col=0 → SAME → return false
    → dfs(2) returns false → dfs(1) returns false
  → dfs(0) returns false

isBipartite returns false — graph is NOT bipartite ✅
```
Node 2 got forced to color 0 (opposite of node 1's color 1), but node 2 is also directly connected to node 0, which is already color 0 — a same-color clash. That's the odd-cycle contradiction (0-1-2-0 is a 3-cycle) showing up during DFS.

### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| DFS + alternating coloring | **O(V + E)** ✅ | **O(V)** — color array + recursion stack ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Number of Islands | Grid cells are graph nodes. Loop `delRow`/`delCol` both -1 to 1 for clean 8-direction traversal. One BFS per unvisited land cell = one island |
| Bipartite — BFS | 2-color via BFS. A same-colored, already-colored neighbor = odd cycle = not bipartite |
| Bipartite — DFS | Identical coloring rule, driven by recursion. Same clash condition, same result |

---

*#SDESheetChallenge #DSA #Java #GraphAlgorithms #Bipartite #TakeUForward #Striver*
