# 🚀 Day 34/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Morris Preorder Traversal](#1-morris-preorder-traversal) | 🔴 Hard | Binary Tree / O(1) Space |
| 2 | [Right/Left View of BT](#2-rightleft-view-of-binary-tree) | 🟡 Medium | Binary Tree / BFS |
| 3 | [Bottom View of BT](#3-bottom-view-of-binary-tree) | 🟡 Medium | Binary Tree / BFS + Map |
| 4 | [Top View of BT](#4-top-view-of-binary-tree) | 🟡 Medium | Binary Tree / BFS + Map |

---

## 1. Morris Preorder Traversal

### 🧩 Problem Statement

Given the root of a binary tree, return the **preorder traversal** (Root → Left → Right) using **O(1) space** — no recursion, no stack.

**Example 1:**
```
Input:  root = [1, 4, null, 4, 2]
Output: [1, 4, 4, 2]
```

---

### 🟢 Approach — Morris Preorder (One Line Different from Morris Inorder)
**Time: O(2N) | Space: O(1)**

#### 💡 How It Relates to Morris Inorder

Yesterday we did **Morris Inorder** (Left → Root → Right). Morris Preorder (Root → Left → Right) uses the **exact same threaded approach** with just one key difference:

| | Morris Inorder | Morris Preorder |
|--|--|--|
| When to **visit** node | When removing thread (`prev.right = null` case) | When **setting** thread (`prev.right = null` case) |
| Visit on no-left-child case | Yes | Yes |

In inorder: visit when returning from the left subtree.
In preorder: visit **before** going into the left subtree.

#### 📝 Algorithm

At each node `cur`:
    
**Case 1 — No left child:**
- Visit `cur` (add to result)
- Move to `cur.right`

**Case 2 — Has left child:**
- Find inorder predecessor `prev` (rightmost of left subtree)
- If `prev.right == null` (thread not set yet):
  - **Visit `cur` here** (preorder = root first!) ← **difference from inorder**
  - Set thread: `prev.right = cur`
  - Move to `cur.left`
- If `prev.right == cur` (returning via thread):
  - Remove thread: `prev.right = null`
  - Move to `cur.right` (don't visit — already visited above)

#### 💻 Code
```java
class Solution {
    public List<Integer> preorder(TreeNode root) {
        List<Integer> preorder = new ArrayList<>();
        TreeNode cur = root;

        while (cur != null) {
            if (cur.left == null) {
                preorder.add(cur.data);
                cur = cur.right;
            } else {
                TreeNode prev = cur.left;

                while (prev.right != null && prev.right != cur) {
                    prev = prev.right;
                }

                if (prev.right == null) {
                    prev.right = cur;
                    preorder.add(cur.data); // ← visit BEFORE going left
                    cur = cur.left;
                } else {
                    prev.right = null;
                    cur = cur.right; // ← don't visit again
                }
            }
        }
        return preorder;
    }
}
```

#### 🧪 Dry Run

```
Tree:       1
           / \
          2   3
         / \
        4   5

cur=1: has left. Find pred of 1 → rightmost of {2,4,5} = 5
  5.right=null → SET thread: 5.right=1, ADD 1, cur=2

cur=2: has left. Find pred of 2 → rightmost of {4} = 4
  4.right=null → SET thread: 4.right=2, ADD 2, cur=4

cur=4: no left → ADD 4, cur=4.right=2 (via thread)

cur=2: has left. Find pred of 2 → 4. 4.right==2 → REMOVE thread: 4.right=null, cur=2.right=5

cur=5: no left → ADD 5, cur=5.right=1 (via thread)

cur=1: has left. Find pred of 1 → 5. 5.right==1 → REMOVE thread: 5.right=null, cur=1.right=3

cur=3: no left → ADD 3, cur=null

Result: [1, 2, 4, 5, 3] ✅ (preorder: Root→Left→Right)
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(2N) — each node visited at most twice | **O(1)** ✅ |

---

## 2. Right/Left View of Binary Tree

### 🧩 Problem Statement

**Right View:** Return the values of nodes visible when the tree is viewed from the right side (one node per level — the rightmost).

**Left View:** The leftmost node per level (same logic, just change which node you pick).

**Example:**
```
Tree:       1
           / \
          2   3
           \    \
            5    4

Right View: [1, 3, 4]  (rightmost node at each level)
Left View:  [1, 2, 5]  (leftmost node at each level)
```

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟢 Approach — Level Order BFS
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Use **BFS (level order traversal)** with a queue. Process the tree level by level. At each level, we know the total number of nodes (`size`). The **last node processed** in each level is the rightmost — it's what's visible from the right.

**For Left View:** Take the **first node** of each level (`i == 0`) instead of the last.

#### 📝 Steps
1. Push root into queue
2. While queue is not empty:
   - Get current level size `size = q.size()`
   - Loop `i` from 0 to size-1:
     - Poll node from queue
     - If `i == size - 1` → it's the rightmost → add to answer (right view)
     - Push left and right children if they exist
3. Return answer

#### 💻 Code
```java
class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) return ans;

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size(); // nodes at this level

            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();

                if (i == size - 1) { // last node = rightmost
                    ans.add(node.data);
                }

                if (node.left != null)  q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

```
Tree:       1
           / \
          2   3
           \    \
            5    4

Level 0: queue=[1]. size=1. i=0: poll 1, i==size-1 → ADD 1. Push 2,3. ans=[1]
Level 1: queue=[2,3]. size=2.
  i=0: poll 2, not last. Push 5.
  i=1: poll 3, last → ADD 3. Push 4. ans=[1,3]
Level 2: queue=[5,4]. size=2.
  i=0: poll 5, not last. (no children)
  i=1: poll 4, last → ADD 4. ans=[1,3,4]

Result: [1, 3, 4] ✅
```

**Left View (change):** `if (i == 0)` instead of `if (i == size - 1)` → picks leftmost each level.

> 💡 **Why use BFS (not DFS) for views?**
> BFS naturally processes level by level with `size` tracking. DFS can also work (using recursion depth as level index, updating a result array) — but BFS is more intuitive here: "process each level, pick the last/first node."

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | O(N) — queue holds at most one full level |

---

## 3. Bottom View of Binary Tree

### 🧩 Problem Statement

Return the bottom view of the tree — nodes visible when viewed from below. For each **vertical line (horizontal distance)**, return the **bottommost** node. If two nodes share the same horizontal distance, the one encountered later in level order wins.

**Example:**
```
Tree:            20
                /  \
               8    22
              / \     \
             5   3     25
                / \
               10  14

Vertical lines:   -2 -1  0  1   2
                   5  8  3 14  25  ← bottommost per vertical line

Bottom View: [5, 10, 3, 14, 25]
```

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟢 Approach — BFS + TreeMap (Horizontal Distance)
**Time: O(N log N) | Space: O(N)**

#### 💡 Key Concepts

**Horizontal Distance (HD):**
- Root has HD = 0
- Going left → HD - 1
- Going right → HD + 1

**Bottom View Rule:** For each HD, keep the **last** node seen in level order (BFS). Since BFS processes level by level top to bottom, the last node encountered at each HD is the bottommost.

**Why overwrite the map?**
Unlike top view, we don't skip existing keys. We keep **overwriting** `map[HD] = node.data` — so the last (bottommost) value at each HD survives.

**Why TreeMap?**
TreeMap keeps keys sorted by HD automatically (left columns first, right columns last).

#### 📝 Steps
1. Create `TreeMap<Integer, Integer> map` (HD → node value)
2. BFS using queue of `(TreeNode, HD)` pairs
3. For each node:
   - `map.put(HD, node.data)` → **always overwrite** (bottom view keeps last value)
   - Push left child with `HD - 1`, right child with `HD + 1`
4. Collect `map.values()` in order → answer

#### 💻 Code
```java
class Solution {
    public List<Integer> bottomView(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) return ans;

        Map<Integer, Integer> map = new TreeMap<>();
        Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new AbstractMap.SimpleEntry<>(root, 0));

        while (!q.isEmpty()) {
            Map.Entry<TreeNode, Integer> curr = q.poll();
            TreeNode node = curr.getKey();
            int line = curr.getValue();

            map.put(line, node.data); // overwrite = keep bottommost

            if (node.left  != null) q.offer(new AbstractMap.SimpleEntry<>(node.left,  line - 1));
            if (node.right != null) q.offer(new AbstractMap.SimpleEntry<>(node.right, line + 1));
        }

        for (int val : map.values()) ans.add(val);
        return ans;
    }
}
```

#### 🧪 Dry Run

```
Tree:         1
             / \
            2   3
           / \
          4   5

BFS order with HDs:
  (1, HD=0)  → map={0:1}. Push (2,-1), (3,+1)
  (2, HD=-1) → map={-1:2, 0:1}. Push (4,-2), (5,0)
  (3, HD=+1) → map={-1:2, 0:1, 1:3}. No children.
  (4, HD=-2) → map={-2:4, -1:2, 0:1, 1:3}.
  (5, HD=0)  → map={-2:4, -1:2, 0:5, 1:3}. ← 5 overwrites 1 at HD=0

TreeMap sorted: HD=-2:4, HD=-1:2, HD=0:5, HD=1:3

Bottom View: [4, 2, 5, 3] ✅
(Node 5 is below node 1 at the same vertical line → 5 is visible from bottom)
```

---

## 4. Top View of Binary Tree

### 🧩 Problem Statement

Return the top view — nodes visible from above. For each vertical line (HD), return the **first** node encountered (topmost). If multiple nodes share the same HD, only the first one in level order is visible.

**Example:**
```
Tree:      1
          / \
         2   3
        / \ / \
       4  5 6  7

Top View: [4, 2, 1, 3, 7]
```

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟢 Approach — BFS + TreeMap (Same as Bottom View, One Change)
**Time: O(N log N) | Space: O(N)**

#### 💡 Idea
**Same approach as Bottom View** — BFS with horizontal distances, TreeMap for sorted output.

**The one difference:**
- **Bottom View:** Always overwrite → `map.put(HD, node.data)`
- **Top View:** Only write if HD not seen yet → `if (!map.containsKey(HD)) map.put(HD, node.data)`

Since BFS visits nodes level by level (top to bottom), the first time we encounter an HD is always from the top → that's the node visible from above.

#### 💻 Code
```java
class Solution {
    public List<Integer> topView(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) return ans;

        Map<Integer, Integer> map = new TreeMap<>();
        Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new AbstractMap.SimpleEntry<>(root, 0));

        while (!q.isEmpty()) {
            Map.Entry<TreeNode, Integer> curr = q.poll();
            TreeNode node = curr.getKey();
            int line = curr.getValue();

            if (!map.containsKey(line)) { // only first occurrence
                map.put(line, node.data);
            }

            if (node.left  != null) q.offer(new AbstractMap.SimpleEntry<>(node.left,  line - 1));
            if (node.right != null) q.offer(new AbstractMap.SimpleEntry<>(node.right, line + 1));
        }

        for (int val : map.values()) ans.add(val);
        return ans;
    }
}
```

#### 🧪 Dry Run

```
Tree:         1
             / \
            2   3
           / \
          4   5

BFS order with HDs:
  (1, 0)  → map={0:1}. Push (2,-1), (3,+1)
  (2,-1)  → map={-1:2, 0:1}. Push (4,-2), (5,0)
  (3,+1)  → map={-1:2, 0:1, 1:3}. No children.
  (4,-2)  → map={-2:4, -1:2, 0:1, 1:3}.
  (5, 0)  → HD=0 already in map → SKIP. ← 5 NOT added (1 is topmost at HD=0)

TreeMap sorted: HD=-2:4, HD=-1:2, HD=0:1, HD=1:3

Top View: [4, 2, 1, 3] ✅
```

#### 📊 Top View vs Bottom View Summary

| Aspect | Top View | Bottom View |
|--------|---------|-------------|
| Which node per HD? | First (topmost) | Last (bottommost) |
| Map update rule | `if (!map.containsKey(HD))` | Always `map.put(HD, val)` |
| Code difference | One condition added | One condition removed |
| Both use | BFS + TreeMap + HD tracking | BFS + TreeMap + HD tracking |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Morris Preorder | Same as Morris Inorder — visit node when setting the thread (not removing it). One line change from inorder |
| Right/Left View | BFS level order. Pick last node per level (right view) or first (left view). `i == size-1` or `i == 0` |
| Bottom View | BFS + HD tracking. TreeMap for sorted output. Always overwrite map — bottommost value survives |
| Top View | BFS + HD tracking. TreeMap. Only write if HD not seen — topmost value is kept. One `containsKey` check |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
