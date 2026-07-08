# 🚀 Day 37/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [LCA in BT](#1-lca-in-bt) | 🔴 Hard | Binary Tree / DFS |
| 2 | [Check Identical Trees](#2-check-identical-trees) | 🟡 Medium | Binary Tree / DFS |
| 3 | [Zig Zag / Spiral Traversal](#3-zig-zag--spiral-traversal) | 🟡 Medium | Binary Tree / BFS |
| 4 | [Boundary Traversal](#4-boundary-traversal) | 🟡 Medium | Binary Tree / DFS |

---

## 1. LCA in BT

### 🧩 Problem Statement

Given the root of a binary tree and two nodes `p` and `q`, return their **Lowest Common Ancestor (LCA)** — the deepest node that has both `p` and `q` as descendants (a node is a descendant of itself).

**Example 1:**
```
Tree:       3
           / \
          5   1
         / \ / \
        6  2 0  8
          / \
         7   4

p=5, q=1 → LCA = 3  (p in left, q in right → root is LCA)
p=5, q=4 → LCA = 5  (4 is a descendant of 5 → 5 is LCA)
```

**Constraints:** `2 <= nodes <= 10⁵`, all values unique

---

### 🟢 Approach — Recursive DFS
**Time: O(N) | Space: O(N)**

#### 💡 Idea

The LCA can only be:
1. The root itself (if root equals `p` or `q`)
2. In the left subtree
3. In the right subtree
4. The current node (if one of p/q is found on each side)

**Recursive logic:**

**Base case:** If `root == null` or `root == p` or `root == q` → return `root`
- Null means not found. p or q means found one of the targets — return it immediately (it might be the LCA itself if the other is a descendant).

**Recurse:** Check left and right subtrees.

**Combine results:**
- `left == null` → both p and q are in right subtree → return `right`
- `right == null` → both p and q are in left subtree → return `left`
- Both non-null → p found on one side, q on the other → current `root` is the LCA

#### 💻 Code
```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;

        TreeNode left  = lowestCommonAncestor(root.left,  p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left  == null) return right;
        if (right == null) return left;
        return root; // both non-null → root is LCA
    }
}
```

#### 🧪 Dry Run

**p=5, q=1** on the tree above:

```
LCA(3,5,1):
  left  = LCA(5,5,1) → root==p → return node(5)
  right = LCA(1,5,1) → root==q → return node(1)
  left!=null && right!=null → return root=3 ✅

p=5, q=4:
LCA(3,5,4):
  left  = LCA(5,5,4) → root==p=5 → return node(5)  ← 4 is a descendant but we return early
  right = LCA(1,5,4) → neither p nor q found → return null
  right==null → return left = node(5) ✅
```

> 💡 **Why return root immediately when `root == p` or `root == q`?**
> If the current node IS one of the targets, it's either the LCA itself (if the other target is in its subtree) OR it's the target found in one side. Either way, returning it is correct — the combining logic handles both cases.

> 💡 **Why does returning early at `root==p` work even if `q` is a descendant of `p`?**
> When `left=node(5)` and `right=null` (q not found in right), we return `left=5`. This correctly identifies 5 as the LCA. The fact that q is in 5's subtree doesn't matter — 5 is still the deepest common ancestor.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — every node visited at most once | O(N) recursion stack |

---

## 2. Check Identical Trees

### 🧩 Problem Statement

Given roots of two binary trees `p` and `q`, return `true` if they are **structurally identical** with the **same node values** at every position.

**Example 1:**
```
p = [1,2,3], q = [1,2,3] → true
```

**Example 2:**
```
p = [1,2,1], q = [1,1,2] → false  (same values, different structure)
```

**Constraints:** `0 <= nodes <= 100`

---

### 🟢 Approach — Simultaneous DFS
**Time: O(N) | Space: O(H)**

#### 💡 Idea
Traverse both trees simultaneously. At every step, three conditions must hold:
1. Both null → this branch is identical → return `true`
2. One null, other not → structure differs → return `false`
3. Values differ → return `false`

If all three pass, recurse on both left and right subtrees. Both must return true.

#### 💻 Code
```java
class Solution {
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;   // both empty — identical
        if (p == null || q == null) return false;  // one empty — differ
        if (p.data != q.data)       return false;  // values differ
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
```

#### 🧪 Dry Run

**p=[1,2,3], q=[1,2,3]:**

```
isSame(1,1): values match. recurse left AND right.
  isSame(2,2): values match. recurse.
    isSame(null,null) → true
    isSame(null,null) → true
    return true
  isSame(3,3): values match. recurse.
    isSame(null,null) → true
    isSame(null,null) → true
    return true
return true ✅
```

**p=[1,2,1], q=[1,1,2]:**

```
isSame(1,1): match. recurse left AND right.
  isSame(2,1): 2 != 1 → return false
  (right not even evaluated — short-circuit &&)
return false ✅
```

> 💡 **Why check `p==null && q==null` BEFORE `p==null || q==null`?**
> Order matters. If both are null (case 1), the second condition `p==null || q==null` would also be true — but that case means "differ". We must catch the "both null = identical" case first and return true before hitting the false condition.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — visit each pair of nodes once | O(H) recursion stack |

---

## 3. Zig Zag / Spiral Traversal

### 🧩 Problem Statement

Return the **zigzag level order traversal** — alternate levels go left-to-right and right-to-left.

**Example:**
```
Tree:       1
           / \
          2   3
         / \ / \
        4  8 8  5

Output: [[1], [3,2], [4,8,8,5]]
  Level 0: left→right → [1]
  Level 1: right→left → [3,2]
  Level 2: left→right → [4,8,8,5]
```

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟢 Approach — BFS with Direction Flag + Pre-allocated Row
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Standard BFS level order, but with a `leftToRight` boolean that flips each level.

**Key trick — pre-allocate the row and use index formula:**
Instead of adding to the list and reversing it (two operations), pre-allocate a row of size `size` using `Collections.nCopies(size, 0)`, then place each node at the correct index using:

```
index = leftToRight ? i : (size - 1 - i)
```

- `leftToRight = true`: place at position `i` (normal order)
- `leftToRight = false`: place at position `size-1-i` (reversed position)

This avoids an extra `Collections.reverse()` call and is more elegant.

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> nodesQueue = new LinkedList<>();
        nodesQueue.add(root);
        boolean leftToRight = true;

        while (!nodesQueue.isEmpty()) {
            int size = nodesQueue.size();
            List<Integer> row = new ArrayList<>(Collections.nCopies(size, 0));

            for (int i = 0; i < size; i++) {
                TreeNode node = nodesQueue.poll();
                int index = leftToRight ? i : (size - 1 - i);
                row.set(index, node.data);

                if (node.left  != null) nodesQueue.add(node.left);
                if (node.right != null) nodesQueue.add(node.right);
            }

            leftToRight = !leftToRight;
            result.add(row);
        }
        return result;
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3

Level 0: size=1, leftToRight=true
  i=0: node=1, index=0 → row=[1]
  Push 2,3. leftToRight=false. result=[[1]]

Level 1: size=2, leftToRight=false. row=[0,0] (nCopies)
  i=0: node=2, index=2-1-0=1 → row=[0,2]
  i=1: node=3, index=2-1-1=0 → row=[3,2]
  No children. leftToRight=true. result=[[1],[3,2]]

Result: [[1],[3,2]] ✅
```

> 💡 **Why `Collections.nCopies(size, 0)`?**
> We need a pre-sized list to use `row.set(index, value)`. A regular `new ArrayList<>()` doesn't have pre-set indices — `set(i, v)` would throw IndexOutOfBoundsException. `nCopies` creates a list of `size` zeros that can be overwritten at any position.

> 💡 **Why not just reverse alternate levels after building them normally?**
> Works too, but adds an extra O(N) pass per level. The index formula does it in-place in a single pass.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** | O(N) queue + result list |

---

## 4. Boundary Traversal

### 🧩 Problem Statement

Return the **boundary nodes** of the binary tree in **anticlockwise** order: root → left boundary (top-down, excluding leaf) → all leaves (left to right) → right boundary (bottom-up, excluding leaf).

**Example:**
```
Tree:         1
             / \
            2   3
           / \ / \
          4  5 6  7
            / \
           8   9

Boundary: [1, 2, 4, 8, 9, 6, 7, 3]
  Root:          1
  Left boundary: 2  (4 is a leaf → excluded)
  Leaves:        4, 8, 9, 6, 7  (left to right)
  Right boundary: 3  (7 is a leaf → excluded, added bottom-up)
```

**Constraints:** `0 <= nodes <= 10⁴`

---

### 🟢 Approach — Three Separate Parts
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Break the problem into three independent parts:

**Part 1 — Left Boundary** (top-down, excluding leaf):
Start from `root.left`. At each step: if not a leaf, add to result. Go left if left exists, else go right.

**Part 2 — All Leaves** (left to right):
DFS preorder. If leaf, add. Otherwise recurse left then right.

**Part 3 — Right Boundary** (bottom-up, excluding leaf):
Start from `root.right`. Same as left boundary logic (prefer right, else left), but collect into a temp list and **add in reverse** (to get bottom-up order).

**Why exclude leaves from boundaries?**
Leaves are collected separately in Part 2. Including them in Part 1 or Part 3 would cause duplicates.

#### 💻 Code
```java
class Solution {
    public boolean isLeaf(TreeNode root) {
        return root.left == null && root.right == null;
    }

    public void addLeftBoundary(TreeNode root, List<Integer> res) {
        TreeNode curr = root.left;
        while (curr != null) {
            if (!isLeaf(curr)) res.add(curr.data);
            curr = (curr.left != null) ? curr.left : curr.right;
        }
    }

    public void addRightBoundary(TreeNode root, List<Integer> res) {
        TreeNode curr = root.right;
        List<Integer> temp = new ArrayList<>();
        while (curr != null) {
            if (!isLeaf(curr)) temp.add(curr.data);
            curr = (curr.right != null) ? curr.right : curr.left;
        }
        for (int i = temp.size() - 1; i >= 0; i--) res.add(temp.get(i));
    }

    public void addLeaves(TreeNode root, List<Integer> res) {
        if (isLeaf(root)) { res.add(root.data); return; }
        if (root.left  != null) addLeaves(root.left,  res);
        if (root.right != null) addLeaves(root.right, res);
    }

    public List<Integer> boundary(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        if (!isLeaf(root)) res.add(root.data); // add root if not leaf
        addLeftBoundary(root, res);
        addLeaves(root, res);
        addRightBoundary(root, res);
        return res;
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3
       / \ / \
      4  5 6  7

addLeftBoundary(root):
  curr=2: not leaf → add 2. curr=2.left=4.
  curr=4: isLeaf → skip. curr=4.left=null, 4.right=null → curr=null. stop.
  Left boundary: [2]

addLeaves(root):
  addLeaves(1): not leaf. recurse left(2), right(3).
    addLeaves(2): not leaf. recurse left(4), right(5).
      addLeaves(4): leaf → add 4.
      addLeaves(5): leaf → add 5.
    addLeaves(3): not leaf. recurse left(6), right(7).
      addLeaves(6): leaf → add 6.
      addLeaves(7): leaf → add 7.
  Leaves: [4,5,6,7]

addRightBoundary(root):
  curr=3: not leaf → temp=[3]. curr=3.right=7.
  curr=7: isLeaf → skip. curr=7.right=null, 7.left=null → stop.
  Reverse temp: [3]
  Right boundary (reversed): [3]

Final result: [1] + [2] + [4,5,6,7] + [3] = [1,2,4,5,6,7,3] ✅
```

> 💡 **Why reverse the right boundary?**
> We collect the right boundary top-down (3, then deeper nodes). But anticlockwise means we need it bottom-up (deepest first, back up to 3). Collecting into a temp list and reversing achieves this without a second traversal.

> 💡 **Why handle root separately (`if (!isLeaf(root)) res.add(root.data)`)?**
> If the root is itself a leaf (single-node tree), it would be collected by `addLeaves`. We only add root explicitly if it's not a leaf — otherwise it would appear twice.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited in at most one of the three parts | O(N) |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| LCA in BT | Base: return root if null/p/q. Recurse both sides. Both non-null → current node is LCA. One null → return the other |
| Identical Trees | Simultaneous DFS. Three base cases: both null→true, one null→false, values differ→false |
| Zig Zag Traversal | BFS + `leftToRight` flag. Pre-allocate row with `nCopies`, use index formula `leftToRight ? i : size-1-i` |
| Boundary Traversal | 3 separate passes: left boundary (top-down, skip leaves), all leaves (DFS), right boundary (top-down reversed, skip leaves) |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
