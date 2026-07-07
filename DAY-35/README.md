# 🚀 Day 35/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Pre, Post, Inorder in One Traversal](#1-pre-post-inorder-in-one-traversal) | 🟢 Easy | Binary Tree / Stack |
| 2 | [Vertical Order Traversal](#2-vertical-order-traversal) | 🟡 Medium | Binary Tree / BFS + Map |
| 3 | [Print Root to Leaf Path in BT](#3-print-root-to-leaf-path-in-bt) | 🟡 Medium | Binary Tree / DFS |
| 4 | [Maximum Width of BT](#4-maximum-width-of-bt) | 🟡 Medium | Binary Tree / BFS + Indexing |

---

## 1. Pre, Post, Inorder in One Traversal

### 🧩 Problem Statement

Given a binary tree, return all three traversals — **Inorder (L→R→Root)**, **Preorder (Root→L→R)**, and **Postorder (L→R→Root)** — in a single pass using one stack.

**Example:**
```
Input:  root = [1, 3, 4, 5, 2, 7, 6]
Output: Inorder=[5,3,2,1,7,4,6], Preorder=[1,3,5,2,4,7,6], Postorder=[5,2,3,7,6,4,1]
```

**Constraints:** `1 <= nodes <= 10⁵`

---

### 🟢 Approach — State Machine with Stack
**Time: O(3N) | Space: O(3N)**

#### 💡 Idea

Every node in a tree is visited **3 times** during a DFS:
1. **First visit** (coming from parent) → take action for **Preorder**, then go **left**
2. **Second visit** (returning from left) → take action for **Inorder**, then go **right**
3. **Third visit** (returning from right) → take action for **Postorder**

We model this with a `NodeState` class that pairs each TreeNode with a **state number (1, 2, or 3)**.

#### 📝 State Transitions

| State | Action | Next |
|-------|--------|------|
| `1` | Add to **Preorder** | Push node with state 2. Push left child with state 1 (if exists) |
| `2` | Add to **Inorder** | Push node with state 3. Push right child with state 1 (if exists) |
| `3` | Add to **Postorder** | Done (don't push anything) |

#### 💻 Code
```java
class NodeState {
    TreeNode node;
    int state;
    NodeState(TreeNode node, int state) { this.node = node; this.state = state; }
}

class Solution {
    List<List<Integer>> treeTraversal(TreeNode root) {
       List<Integer> preOrder = new ArrayList<>(), inOrder = new ArrayList<>(), postOrder = new ArrayList<>();
       if (root == null) return Arrays.asList(inOrder, preOrder, postOrder);

       Stack<NodeState> st = new Stack<>();
       st.push(new NodeState(root, 1));

       while (!st.isEmpty()) {
           NodeState peek = st.pop();
           TreeNode node = peek.node;
           int state = peek.state;

           if (state == 1) {
               preOrder.add(node.data);
               st.push(new NodeState(node, 2));
               if (node.left != null) st.push(new NodeState(node.left, 1));
           } else if (state == 2) {
               inOrder.add(node.data);
               st.push(new NodeState(node, 3));
               if (node.right != null) st.push(new NodeState(node.right, 1));
           } else {
               postOrder.add(node.data);
           }
       }
       return Arrays.asList(inOrder, preOrder, postOrder);
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3
       / \
      4   5

Start: stack = [(1,state=1)]

Pop (1,1): state=1 → preOrder=[1]. Push (1,2). Push left (2,1). stack=[(1,2),(2,1)]
Pop (2,1): state=1 → preOrder=[1,2]. Push (2,2). Push left (4,1). stack=[(1,2),(2,2),(4,1)]
Pop (4,1): state=1 → preOrder=[1,2,4]. Push (4,2). No left. stack=[(1,2),(2,2),(4,2)]
Pop (4,2): state=2 → inOrder=[4]. Push (4,3). No right. stack=[(1,2),(2,2),(4,3)]
Pop (4,3): state=3 → postOrder=[4]. Done. stack=[(1,2),(2,2)]
Pop (2,2): state=2 → inOrder=[4,2]. Push (2,3). Push right (5,1). stack=[(1,2),(2,3),(5,1)]
Pop (5,1): state=1 → preOrder=[1,2,4,5]. Push (5,2). No left. stack=[(1,2),(2,3),(5,2)]
Pop (5,2): state=2 → inOrder=[4,2,5]. Push (5,3). No right. stack=[(1,2),(2,3),(5,3)]
Pop (5,3): state=3 → postOrder=[4,5]. Done. stack=[(1,2),(2,3)]
Pop (2,3): state=3 → postOrder=[4,5,2]. Done. stack=[(1,2)]
Pop (1,2): state=2 → inOrder=[4,2,5,1]. Push (1,3). Push right (3,1). stack=[(1,3),(3,1)]
Pop (3,1): state=1 → preOrder=[1,2,4,5,3]. Push (3,2). No left. stack=[(1,3),(3,2)]
Pop (3,2): state=2 → inOrder=[4,2,5,1,3]. Push (3,3). No right. stack=[(1,3),(3,3)]
Pop (3,3): state=3 → postOrder=[4,5,2,3]. Done. stack=[(1,3)]
Pop (1,3): state=3 → postOrder=[4,5,2,3,1]. Done. stack=[]

Preorder:  [1,2,4,5,3] ✅
Inorder:   [4,2,5,1,3] ✅
Postorder: [4,5,2,3,1] ✅
```

> 💡 **Why push state 2 before pushing left child (state 1)?**
> Stack is LIFO. We want to process the left child BEFORE returning to state 2 of the current node. Pushing left on top ensures left is popped first.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(3N) — each node processed 3 times | O(3N) — stack + 3 result lists |

---

## 2. Vertical Order Traversal

### 🧩 Problem Statement

Return the **vertical order traversal** of a binary tree. Each node is at position `(row, col)`. Root is at `(0,0)`. Left child is at `(row+1, col-1)`, right child at `(row+1, col+1)`.

Group nodes by column (vertical line). Within the same column, order by row. If two nodes share the same row and column, sort by value.

**Example:**
```
Input:  root = [1, 2, 3, 4, 5, 6, 7]
Output: [[4], [2], [1,5,6], [3], [7]]
  Node 5 and 6 are both at (row=2, col=0) — sorted by value: 5 before 6
```

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟢 Approach — BFS + 3-Level Nested Map
**Time: O(N log N) | Space: O(N)**

#### 💡 Idea

Use **BFS** to process nodes level by level (top-to-bottom). Track each node's `(x=col, y=row)` coordinates.

**Data structure:** `TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>>`
- Outer TreeMap: sorted by column (x)
- Inner TreeMap: sorted by row (y) within each column
- PriorityQueue: auto-sorted by value for same (row, col) ties

**Why BFS (not DFS)?**
BFS naturally assigns increasing row values level by level, making the (row, col) tracking straightforward. DFS could also work but would require more careful coordination.

#### 📝 Steps
1. BFS with `Tuple(node, x, y)` where root=(0,0)
2. For each node: insert `node.data` into `nodesMap[x][y]` (PriorityQueue auto-sorts values)
3. Left child → `(x-1, y+1)`, Right child → `(x+1, y+1)`
4. Read the nested map: outer TreeMap by col → inner TreeMap by row → drain PQ by value

#### 💻 Code
```java
class Solution {
    static class Tuple { TreeNode node; int x, y; Tuple(TreeNode n, int x, int y){node=n;this.x=x;this.y=y;} }

    public List<List<Integer>> verticalTraversal(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>> nodesMap = new TreeMap<>();
        Queue<Tuple> q = new LinkedList<>();
        q.offer(new Tuple(root, 0, 0));

        while (!q.isEmpty()) {
            Tuple t = q.poll();
            nodesMap.putIfAbsent(t.x, new TreeMap<>());
            nodesMap.get(t.x).putIfAbsent(t.y, new PriorityQueue<>());
            nodesMap.get(t.x).get(t.y).offer(t.node.data);

            if (t.node.left  != null) q.offer(new Tuple(t.node.left,  t.x - 1, t.y + 1));
            if (t.node.right != null) q.offer(new Tuple(t.node.right, t.x + 1, t.y + 1));
        }

        for (TreeMap<Integer, PriorityQueue<Integer>> yMap : nodesMap.values()) {
            List<Integer> column = new ArrayList<>();
            for (PriorityQueue<Integer> pq : yMap.values()) while (!pq.isEmpty()) column.add(pq.poll());
            res.add(column);
        }
        return res;
    }
}
```

#### 🧪 Dry Run

```
Tree:       3
           / \
          9   20
              / \
             15   7

BFS processing:
  (3,  x=0, y=0) → nodesMap[0][0] = {3}
  (9,  x=-1,y=1) → nodesMap[-1][1]= {9}
  (20, x=1, y=1) → nodesMap[1][1] = {20}
  (15, x=0, y=2) → nodesMap[0][2] = {15}
  (7,  x=2, y=2) → nodesMap[2][2] = {7}

Reading nodesMap (sorted by col):
  x=-1: y=1 → [9]
  x=0:  y=0 → [3], y=2 → [15] → column=[3,15]
  x=1:  y=1 → [20]
  x=2:  y=2 → [7]

Output: [[9],[3,15],[20],[7]] ✅
```

**Tie-breaking example:** Nodes 5 and 6 at same (row=2, col=0):
```
PriorityQueue at nodesMap[0][2] = {5, 6} (min-heap, sorted by value)
Draining PQ: 5 first, then 6 → [5, 6] ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N log N) — TreeMap insertions + PQ sorting | O(N) |

---

## 3. Print Root to Leaf Path in BT

### 🧩 Problem Statement

Return **all root-to-leaf paths** in the binary tree. A leaf is a node with no children.

**Example 1:**
```
Input:  root = [1, 2, 3, null, 5, null, 4]
Output: [[1,2,5], [1,3,4]]
```

**Example 2:**
```
Input:  root = [1, 2, 3, 4, 5]
Output: [[1,2,4], [1,2,5], [1,3]]
```

**Constraints:** `1 <= nodes <= 3×10³`

---

### 🟢 Approach — DFS with Backtracking
**Time: O(N) | Space: O(N)**

#### 💡 Idea

**Why DFS (not BFS)?**
We need to track the **entire path from root to leaf** — that's depth-first by nature. BFS goes level by level and doesn't maintain a path naturally. DFS digs deep along one branch, records the full path at the leaf, then **backtracks** to explore other branches.

**Backtracking:** After exploring all children of a node, **remove** the node from the current path. This restores the path to its state before this node was added, allowing other branches to build on the correct prefix.

#### 📝 Steps
1. Start DFS at root with empty `path` list
2. At each node: `path.add(node.data)`
3. If leaf (`left == null && right == null`): save a **copy** of `path` to results
4. Otherwise: recurse on left child, then right child
5. After recursion: `path.remove(last)` ← **backtrack**

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> allRootToLeaf(TreeNode root) {
        List<List<Integer>> allPaths  = new ArrayList<>();
        List<Integer>       currentPath = new ArrayList<>();
        dfs(root, currentPath, allPaths);
        return allPaths;
    }

    private void dfs(TreeNode node, List<Integer> path, List<List<Integer>> allPaths) {
        if (node == null) return;

        path.add(node.data);

        if (node.left == null && node.right == null) {
            allPaths.add(new ArrayList<>(path)); // save copy
        } else {
            dfs(node.left,  path, allPaths);
            dfs(node.right, path, allPaths);
        }

        path.remove(path.size() - 1); // backtrack
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3
       / \
      4   5

dfs(1, []):
  path=[1]
  Not leaf. dfs(2,[1]):
    path=[1,2]
    Not leaf. dfs(4,[1,2]):
      path=[1,2,4]
      LEAF! → allPaths=[[1,2,4]]
      backtrack: path=[1,2]
    dfs(5,[1,2]):
      path=[1,2,5]
      LEAF! → allPaths=[[1,2,4],[1,2,5]]
      backtrack: path=[1,2]
    backtrack: path=[1]
  dfs(3,[1]):
    path=[1,3]
    LEAF! → allPaths=[[1,2,4],[1,2,5],[1,3]]
    backtrack: path=[1]
  backtrack: path=[]

Result: [[1,2,4],[1,2,5],[1,3]] ✅
```

> 💡 **Why `new ArrayList<>(path)` when saving?**
> `path` is a shared mutable list. If we add `path` directly, later backtracks would modify it and corrupt the saved result. We save a **copy** so the saved path is independent.

> 💡 **Why backtrack after both left AND right calls?**
> The backtrack `path.remove(last)` happens AFTER both recursive calls return. At that point, the current node's role is complete — we're returning to the parent, which should not see this node in the path.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited once | O(N) — DFS stack + path storage |

---

## 4. Maximum Width of BT

### 🧩 Problem Statement

Return the **maximum width** of the binary tree. Width of a level = distance between the leftmost and rightmost non-null nodes, **including null nodes in between** (as if it were a complete binary tree).

**Example 1:**
```
Input:  root = [1, 3, 2, 5, 3, null, 9]
Output: 4
Level 2: nodes 5, 3, null, 9 → width = 4 (from index 0 to 3 in complete BT)
```

**Example 2:**
```
Input:  root = [1, 3, 2, 5, null, null, 9, 6, null, 7]
Output: 7
```

**Constraints:** `1 <= nodes <= 3000`

---

### 🟢 Approach — BFS with Complete Binary Tree Indexing
**Time: O(N) | Space: O(N)**

#### 💡 Idea — Complete Binary Tree Indexing

Think of the tree as if it were a **complete binary tree** (indices starting from 0):
- Root has index `0`
- Left child of node with index `i` → index `2*i + 1`
- Right child of node with index `i` → index `2*i + 2`

Width of a level = `last_index - first_index + 1`

**The Overflow Problem:**
For a deep, wide tree, indices can grow exponentially and overflow `int`. Fix: at the start of each level, **subtract the minimum index** (`mmin`) from all indices at that level. This normalizes indices to start from 0 at each level, preventing overflow while preserving relative widths.

#### 📝 Steps
1. BFS with queue of `(TreeNode, index)` pairs. Root gets index 0
2. At each level:
   - `mmin = first element's index` (subtract this from all indices in this level)
   - Track `first` (normalized index of first node) and `last` (normalized index of last node)
   - Push children with indices `cur_id*2+1` and `cur_id*2+2`
3. `width = last - first + 1`. Track `ans = max(ans, width)`

#### 💻 Code
```java
class Solution {
    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) return 0;

        int ans = 0;
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(root, 0));

        while (!q.isEmpty()) {
            int size = q.size();
            int mmin = q.peek().getValue(); // normalize to prevent overflow

            int first = 0, last = 0;

            for (int i = 0; i < size; i++) {
                int cur_id = q.peek().getValue() - mmin; // normalized index
                TreeNode node = q.peek().getKey();
                q.poll();

                if (i == 0)        first = cur_id;
                if (i == size - 1) last  = cur_id;

                if (node.left  != null) q.offer(new Pair<>(node.left,  cur_id * 2 + 1));
                if (node.right != null) q.offer(new Pair<>(node.right, cur_id * 2 + 2));
            }

            ans = Math.max(ans, last - first + 1);
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

```
Tree:         1
             / \
            3   2
           / \    \
          5   3    9

Level 0: queue=[(1,0)]. mmin=0.
  i=0: cur_id=0-0=0 (node 1). first=0, last=0.
  Push: (3, 0*2+1=1), (2, 0*2+2=2)
  width = 0-0+1 = 1. ans=1

Level 1: queue=[(3,1),(2,2)]. mmin=1.
  i=0: cur_id=1-1=0 (node 3). first=0. Push (5,1),(3,2)
  i=1: cur_id=2-1=1 (node 2). last=1. Push (9,4) [right child idx=1*2+2=4, but adjusted by cur_id of 2→ push (9, 1*2+2=4)]
  width = 1-0+1 = 2. ans=2

Wait - let me redo with mmin normalization:
  i=0: cur_id=1-1=0. first=0. Push left(5, 0*2+1=1), right(3, 0*2+2=2)
  i=1: cur_id=2-1=1. last=1. No left. Push right(9, 1*2+2=4)
  width=1-0+1=2. ans=2

Level 2: queue=[(5,1),(3,2),(9,4)]. mmin=1.
  i=0: cur_id=1-1=0 (node 5). first=0. No children.
  i=1: cur_id=2-1=1 (node 3). No children.
  i=2: cur_id=4-1=3 (node 9). last=3. No children.
  width = 3-0+1 = 4. ans=4

Return 4 ✅
```

**Why the width is 4:**
```
Level 2 in complete BT:  [5, 3, null, 9]
                          0   1   2    3
Width = 3 - 0 + 1 = 4 ✅ (null node at index 2 is counted)
```

> 💡 **Why subtract `mmin` at the start of each level?**
> Without normalization, indices grow as `2^depth`. At depth 32, a left-skewed tree would overflow `int`. By resetting indices to start from 0 at each level, we keep values small while preserving all relative widths correctly.

> 💡 **Why `cur_id * 2 + 1` and `cur_id * 2 + 2` for children?**
> This is the standard complete binary tree indexing formula. For any node at index `i`: left child is at `2i+1`, right child at `2i+2`. The width calculation depends on this consistent indexing.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited once | O(N) — BFS queue |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Pre/In/Post in One Pass | Each node visited 3 times. State 1→Preorder, State 2→Inorder, State 3→Postorder. Push next state + child on stack |
| Vertical Order | 3-level nested map: col(sorted) → row(sorted) → values(PQ sorted). BFS ensures correct row assignment |
| Root to Leaf Paths | DFS + backtracking. Save COPY of path at leaf (not reference). Backtrack after both recursive calls return |
| Maximum Width | BFS + complete BT indexing. Subtract mmin at each level to prevent integer overflow. Width = last_index - first_index + 1 |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
