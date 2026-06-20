# 🚀 Day 19/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [M Coloring Problem](#1-m-coloring-problem) | 🔴 Hard | Backtracking / Graphs |
| 2 | [Rat in a Maze](#2-rat-in-a-maze) | 🔴 Hard | Backtracking / Grid |
| 3 | [Word Break](#3-word-break) | 🟡 Medium | Dynamic Programming |

---

## 1. M Coloring Problem

### 🧩 Problem Statement

Given an undirected graph with `N` vertices and `E` edges, determine if the graph can be colored using **at most `M` colors** such that no two adjacent vertices share the same color. Return `true` or `false`.

**Example 1:**
```
Input:  N=4, M=3, Edges=[(0,1),(1,2),(2,3),(3,0),(0,2)]
Output: true
Coloring: 0→Red, 1→Blue, 2→Green, 3→Blue
```

**Example 2:**
```
Input:  N=3, M=2, Edges=[(0,1),(1,2),(0,2)]
Output: false
Triangle graph — 3 colors needed, only 2 available
```

**Constraints:** `1 <= N <= 20`, `1 <= M <= N`

---

### 🟢 Approach — Backtracking
**Time: O(Mᴺ) | Space: O(N)**

#### 💡 Intuition
Try assigning colors to nodes one by one (node 0 to n-1). For each node, try every color from 1 to M. A color is **valid** if none of the node's neighbors already use that color. If a valid color is found, assign it and recurse to the next node. If recursion fails (no solution downstream), reset and try the next color. If no color works, return `false` (backtrack signal).

#### 📝 Steps
1. Build adjacency list from edges
2. `solve(node=0)`: base case `node==n` → return `true`
3. For each color `i` from 1 to M:
   - `isPossible(i, node)` → check all neighbors, none should have color `i`
   - If possible: `colors[node] = i`, recurse to `node+1`
   - If recursion returns `true` → propagate `true`
   - Else: `colors[node] = 0` (backtrack)
4. Return `false` if no color worked

#### 💻 Code
```java
class Solution {
    private boolean isPossible(int col, int node, int[] colors, List<List<Integer>> adj) {
        for (int adjacent : adj.get(node)) {
            if (colors[adjacent] == col) return false;
        }
        return true;
    }

    private boolean solve(int node, int m, int n, int[] colors, List<List<Integer>> adj) {
        if (node == n) return true;

        for (int i = 1; i <= m; i++) {
            if (isPossible(i, node, colors, adj)) {
                colors[node] = i;
                if (solve(node + 1, m, n, colors, adj)) return true;
                colors[node] = 0; // backtrack
            }
        }
        return false;
    }

    boolean graphColoring(int[][] edges, int m, int n) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }
        int[] colors = new int[n];
        return solve(0, m, n, colors, adj);
    }
}
```

#### 🧪 Dry Run

Input: `N=4, M=3, Edges=[(0,1),(1,2),(2,3),(3,0),(0,2)]`

```
Adjacency list:
0: [1,2,3]
1: [0,2]
2: [1,3,0]
3: [2,0]

colors = [0,0,0,0]

solve(node=0):
  Try color 1: neighbors of 0=[1,2,3], all colors[.]=0 → safe
    colors=[1,0,0,0]
    solve(node=1):
      Try color 1: neighbor 0 has color 1 → NOT safe
      Try color 2: neighbor 0→color 1, neighbor 2→0 → safe
        colors=[1,2,0,0]
        solve(node=2):
          Try color 1: neighbor 1→2, neighbor 3→0, neighbor 0→1 → color 1 blocked by node 0!
          Try color 2: neighbor 1→2 → color 2 blocked by node 1!
          Try color 3: neighbor 1→2 ✓, neighbor 3→0 ✓, neighbor 0→1 ✓ → safe!
            colors=[1,2,3,0]
            solve(node=3):
              Try color 1: neighbor 2→3 ✓, neighbor 0→1 → blocked!
              Try color 2: neighbor 2→3 ✓, neighbor 0→1 ✓ → safe!
                colors=[1,2,3,2]
                solve(node=4): node==n=4 → return TRUE ✅

Return true ✅
Final coloring: 0→1(Red), 1→2(Blue), 2→3(Green), 3→2(Blue)
```

**Impossible case:** `N=3, M=2, Edges=[(0,1),(1,2),(0,2)]` (triangle)

```
solve(node=2):
  All 3 nodes connected to each other. Node 0=color1, Node 1=color2.
  Node 2: color1 blocked (adj to 0), color2 blocked (adj to 1) → return false ✅
```

> 💡 **Why `colors[node] = 0` on backtrack (not some other reset)?**
> `0` represents "uncolored". Resetting to 0 ensures `isPossible` doesn't think the node already has a color, which would cause it to mistakenly reject valid colors for future nodes.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(Mᴺ) | O(N) adjacency list + colors array |

---

## 2. Rat in a Maze

### 🧩 Problem Statement

A rat starts at `(0,0)` and must reach `(n-1, n-1)` in an `n×n` grid. Cells with value `1` are open, `0` are blocked. Find all possible paths using moves `U` (up), `D` (down), `L` (left), `R` (right). No cell can be visited twice in a single path.

**Example 1:**
```
Input:  n=4, grid=[[1,0,0,0],[1,1,0,1],[1,1,0,0],[0,1,1,1]]
Output: ["DDRDRR","DRDDRR"]
```

**Example 2:**
```
Input:  n=2, grid=[[1,0],[1,0]]
Output: -1  (no path)
```

**Constraints:** `2 <= n <= 5`

---

### 🟢 Approach — Backtracking
**Time: O(4^(N²)) | Space: O(N²)**

#### 💡 Intuition
At each cell, try all 4 directions. To avoid revisiting, **mark the current cell as 0** (blocked) before exploring, then **restore it to 1** after backtracking. When `(n-1, n-1)` is reached, the accumulated direction string is a valid path.

**Why mark as 0 instead of using a `visited[]` array?**
The grid itself already has 0/1 values. Temporarily setting visited cells to 0 reuses the existing blocked-cell check — no extra space needed.

#### 📝 Steps
1. If `grid[0][0] == 0` or `grid[n-1][n-1] == 0` → return empty (no path possible)
2. Call `path(i=0, j=0, ans="")`:
   - If `(i,j) == (n-1,n-1)` → add `ans` to result
   - If `grid[i][j] == 0` → return (blocked or visited)
   - Set `grid[i][j] = 0` (mark visited)
   - Try U, L, D, R (in this order for lexicographic output)
   - Set `grid[i][j] = 1` (restore)
3. Sort results (for lexicographic order)

#### 💻 Code
```java
class Solution {
    List<String> res = new ArrayList<>();

    private void path(int[][] m, int i, int j, String ans, int n) {
        if (i == n - 1 && j == n - 1) { res.add(ans); return; }
        if (m[i][j] == 0) return;

        m[i][j] = 0; // mark visited

        if (i > 0)     path(m, i-1, j, ans + 'U', n);
        if (j > 0)     path(m, i, j-1, ans + 'L', n);
        if (i < n - 1) path(m, i+1, j, ans + 'D', n);
        if (j < n - 1) path(m, i, j+1, ans + 'R', n);

        m[i][j] = 1; // restore (backtrack)
    }

    public List<String> findPath(int[][] grid) {
        int n = grid.length;
        res.clear();
        if (grid[0][0] == 0 || grid[n-1][n-1] == 0) return res;
        path(grid, 0, 0, "", n);
        Collections.sort(res);
        return res;
    }
}
```

#### 🧪 Dry Run

Input: `n=3, grid=[[1,0,0],[1,1,0],[0,1,1]]`

```
Grid:
1 0 0
1 1 0
0 1 1

path(0,0,""):
  grid[0][0]=0 (mark)
  U: i-1=-1 < 0 → skip
  L: j-1=-1 < 0 → skip
  D: path(1,0,"D"):
    grid[1][0]=0 (mark)
    U: path(0,0,"DU"): grid[0][0]=0 → blocked (we marked it) → return ✅ (prevents revisit)
    L: j-1=-1 → skip
    D: path(2,0,"DD"): grid[2][0]=0 → blocked → return
    R: path(1,1,"DR"):
      grid[1][1]=0 (mark)
      U: path(0,1,"DRU"): grid[0][1]=0 → blocked → return
      L: path(1,0,"DRL"): grid[1][0]=0 → blocked (marked) → return ✅
      D: path(2,1,"DRD"):
        grid[2][1]=0 (mark)
        D: i+1=3=n → skip
        R: path(2,2,"DRDR"):
          i==n-1 && j==n-1 → ADD "DRDR" ✅
        grid[2][1]=1 (restore)
      R: path(1,2,"DRR"): grid[1][2]=0 → blocked → return
      grid[1][1]=1 (restore)
    grid[1][0]=1 (restore)
  R: j+1=1, grid[0][1]=0 → blocked → return
  grid[0][0]=1 (restore)

Result: ["DRDR"] ✅
```

> 💡 **Why explore in U, L, D, R order?**
> The results are sorted at the end, so order of exploration doesn't affect the output. U/L/D/R is just a consistent convention. The sort at the end handles lexicographic ordering.

> 💡 **Why check `grid[i][j] == 0` at the start of `path()` instead of before calling it?**
> The destination `(n-1, n-1)` is checked first — if we checked `grid[i][j]` before the destination check, we'd return early when the destination itself is visited, missing valid complete paths. Checking destination first, then blockage, is the correct order.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(4^(N²)) | O(N²) stack + result |

---

## 3. Word Break

### 🧩 Problem Statement

Given string `s` and dictionary `wordDict`, return `true` if `s` can be segmented into a sequence of one or more dictionary words. Words can be reused.

**Example 1:**
```
Input:  s="takeuforward", wordDict=["take","forward","you","u"]
Output: true  ("take"+"u"+"forward")
```

**Example 2:**
```
Input:  s="applepineapple", wordDict=["apple"]
Output: false  ("pine" not in dict)
```

**Constraints:** `1 <= s.length <= 300`, `1 <= wordDict[i].length <= 20`

---

### 🟢 Approach — 1D Dynamic Programming
**Time: O(N × maxWordLen) | Space: O(N)**

#### 💡 Intuition
Think of the string as a series of **checkpoints**. `dp[i] = true` means the prefix `s[0..i-1]` can be broken into valid dictionary words. If we're at a valid checkpoint `i`, we try extending by each possible word length. If `s[i..i+len-1]` is in the dictionary, we can reach checkpoint `i+len`.

Start: `dp[0] = true` (empty prefix is trivially valid).
Answer: `dp[n]` — can we reach the end?

**Optimization:** Precompute `maxLen` (longest dictionary word). No point checking substrings longer than the longest word.

#### 📝 Steps
1. Build `dict = HashSet(wordDict)`, compute `maxLen`
2. `dp[0] = true`, all others `false`
3. For `i` from 0 to n-1:
   - If `dp[i]` is false → skip (can't reach this point)
   - For `len` from 1 to `min(maxLen, n-i)`:
     - If `s[i..i+len-1]` is in dict → `dp[i+len] = true`
4. Return `dp[n]`

#### 💻 Code
```java
class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();

        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        int maxLen = 0;
        for (String word : wordDict) maxLen = Math.max(maxLen, word.length());

        for (int i = 0; i < n; i++) {
            if (!dp[i]) continue;
            for (int len = 1; len <= maxLen && i + len <= n; len++) {
                if (dict.contains(s.substring(i, i + len))) {
                    dp[i + len] = true;
                }
            }
        }
        return dp[n];
    }
}
```

#### 🧪 Dry Run

Input: `s = "takeuforward"`, `wordDict = ["take","forward","you","u"]`

```
n=12, maxLen=7 (length of "forward")
dict = {take, forward, you, u}
dp = [T,F,F,F,F,F,F,F,F,F,F,F,F]
      0  1  2  3  4  5  6  7  8  9 10 11 12

i=0 (dp[0]=true):
  len=1: s[0..0]="t" → not in dict
  len=2: s[0..1]="ta" → not in dict
  len=3: s[0..2]="tak" → not in dict
  len=4: s[0..3]="take" → IN DICT → dp[4]=true
  len=5,6,7: not in dict

i=1,2,3: dp[i]=false → skip

i=4 (dp[4]=true):
  len=1: s[4..4]="u" → IN DICT → dp[5]=true
  len=2..7: not in dict (s[4..5]="uf" etc.)

i=5 (dp[5]=true):
  len=1..6: check "f","fo","for","forw","forwa","forwar" → not in dict
  len=7: s[5..11]="forward" → IN DICT → dp[12]=true

i=6..11: continue but dp[12] already true

dp[12] = true → return true ✅

Segmentation: "take" + "u" + "forward"
```

**False case:** `s = "applepineapple"`, `wordDict = ["apple"]`

```
n=14, maxLen=5, dict={apple}
dp[0]=T
i=0: s[0..4]="apple" → dp[5]=T
i=5: s[5..9]="pinea"? No. s[5..6]="pi"? No. ... None match "apple" → dp[10] stays F
i=10: dp[10]=F → skip
i=...: no more checkpoints reachable past i=5

dp[14]=false → return false ✅
```

> 💡 **Why `maxLen` optimization matters?**
> Without it, we'd check all substrings up to length `n` from each checkpoint — O(N²) substring checks. With `maxLen`, we only check up to the longest possible word. For a 300-char string with max word length 20, this cuts the inner loop from 300 iterations to 20 — a 15× speedup.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N × maxWordLen) ✅ | O(N) ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| M Coloring | Backtrack node by node. `isPossible` checks all neighbors in O(degree). Reset to 0 on backtrack. Worst case O(M^N) |
| Rat in a Maze | Mark cell as 0 before recursing (uses the grid itself as visited array). Restore to 1 on backtrack. Check destination BEFORE checking blocked |
| Word Break | DP checkpoint approach: dp[i]=true if prefix reachable. Jump forward by valid dictionary words. maxLen optimization avoids checking oversized substrings |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
