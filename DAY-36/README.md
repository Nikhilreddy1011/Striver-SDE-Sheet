# 🚀 Day 36/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Level Order Traversal](#1-level-order-traversal) | 🟢 Easy | Binary Tree / BFS |
| 2 | [Maximum Depth in BT](#2-maximum-depth-in-bt) | 🟡 Medium | Binary Tree / Height |
| 3 | [Diameter of Binary Tree](#3-diameter-of-binary-tree) | 🟢 Easy | Binary Tree / Height |
| 4 | [Check for Balanced Binary Tree](#4-check-for-balanced-binary-tree) | 🟡 Medium | Binary Tree / Height |

> 💡 **Theme of the day:** If you know how to find the **height of a binary tree**, all four problems today become straightforward. Height is the building block.

---

## Height of a Binary Tree (Foundation)

Before diving into today's problems, understand this recursive function — it powers everything else today:

```java
int height(TreeNode node) {
    if (node == null) return 0;           // empty tree has height 0
    int lh = height(node.left);           // height of left subtree
    int rh = height(node.right);          // height of right subtree
    return 1 + Math.max(lh, rh);          // current node + taller subtree
}
```

**Dry Run:**
```
Tree:     1
         / \
        2   3
       /
      4

height(4) = 1+max(0,0) = 1
height(2) = 1+max(1,0) = 2
height(3) = 1+max(0,0) = 1
height(1) = 1+max(2,1) = 3
```

**Time: O(N) — every node visited once. Space: O(H) — recursion stack depth.**

---

## 1. Level Order Traversal

### 🧩 Problem Statement

Given the root of a binary tree, return the **level order traversal** of its values — left to right, level by level.

**Example:**
```
Input:  root = [3, 9, 20, null, null, 15, 7]
Output: [[3], [9,20], [15,7]]
```

**Constraints:** `0 <= nodes <= 2000`

---

### 🟢 Approach — BFS with Queue
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Use a **Queue** (FIFO) to process nodes level by level. At the start of each level, `q.size()` tells us exactly how many nodes are at that level. Process all of them in one inner loop, adding their children to the queue for the next level.

#### 📝 Steps
1. Push root into queue
2. While queue is not empty:
   - `size = q.size()` — number of nodes at current level
   - Create empty `level` list
   - Loop `size` times: poll node, add to `level`, push its children
   - Add `level` to `ans`
3. Return `ans`

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) return ans;
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                level.add(node.data);
                if (node.left  != null) q.add(node.left);
                if (node.right != null) q.add(node.right);
            }
            ans.add(level);
        }
        return ans;
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

Initial: queue=[3]

Level 0: size=1. Poll 3, level=[3]. Push 9,20. queue=[9,20]. ans=[[3]]
Level 1: size=2. Poll 9, level=[9]. No children. Poll 20, level=[9,20]. Push 15,7. queue=[15,7]. ans=[[3],[9,20]]
Level 2: size=2. Poll 15, level=[15]. Poll 7, level=[15,7]. No children. queue=[]. ans=[[3],[9,20],[15,7]]

Result: [[3],[9,20],[15,7]] ✅
```

> 💡 **Why `size = q.size()` before the loop (not `q.size()` inside)?**
> As we add children during the loop, `q.size()` changes. Capturing size before the loop freezes the count for the current level, preventing us from accidentally processing next-level nodes in the same iteration.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** | O(N) — queue holds at most one full level |

---

## 2. Maximum Depth in BT

### 🧩 Problem Statement

Return the **maximum depth** of a binary tree — the number of nodes along the longest path from root to the farthest leaf.

**Example:**
```
Input:  root = [1, 2, 3, null, null, null, 6]
Output: 3  (path: 1→3→6)
```

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟡 Approach 1 — Recursive (DFS)
**Time: O(N) | Space: O(H)**

#### 💡 Idea
Maximum depth = height of the tree. Exactly the foundation function from above.

```java
class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        int left  = maxDepth(root.left);
        int right = maxDepth(root.right);
        return 1 + Math.max(left, right);
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3
               \
                6

maxDepth(null) = 0
maxDepth(2)    = 1+max(0,0) = 1
maxDepth(6)    = 1+max(0,0) = 1
maxDepth(3)    = 1+max(0,1) = 2
maxDepth(1)    = 1+max(1,2) = 3 ✅
```

---

### 🟢 Approach 2 — Iterative (BFS / Level Order)
**Time: O(N) | Space: O(W) where W = max width**

#### 💡 Idea
Run level order traversal and count how many levels we process. The number of levels = maximum depth.

```java
class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int level = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                if (node.left  != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            level++;
        }
        return level;
    }
}
```

#### 🧪 Dry Run

```
Same tree: 1→3→6

Level 1: process [1]. queue=[2,3]. level=1
Level 2: process [2,3]. queue=[6]. level=2
Level 3: process [6]. queue=[]. level=3

Return 3 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Recursive | O(N) | O(H) recursion stack |
| Iterative BFS | O(N) | O(W) queue |

---

## 3. Diameter of Binary Tree

### 🧩 Problem Statement

Return the **diameter** of a binary tree — the length of the longest path between any two nodes. The path may or may not pass through the root.

**Example 1:**
```
Input:  root = [1, 2, 3, 4, 5]
Output: 3  (path: 4→2→1→3, length=3)
```

**Example 2:**
```
Input:  root = [1, 2, 3, null, 4, null, 5]
Output: 4  (path: 4→2→1→3→5, length=4)
```

**Key insight:** `diameter at node = leftHeight + rightHeight`

---

### 🔴 Approach 1 — Brute Force
**Time: O(N²) | Space: O(H)**

#### 💡 Idea
For each node, compute `leftHeight + rightHeight` as the diameter passing through that node. Also recursively check the diameter of the left and right subtrees (the path might not go through the current node). Return the maximum of all three.

**Why O(N²)?** `height()` is called for each node, and height itself takes O(N) → O(N) × O(N) = O(N²).

```java
class Solution {
    private int height(TreeNode node){
        if(node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) return 0;
        int lHeight     = height(root.left);
        int rHeight     = height(root.right);
        int curDiameter = lHeight + rHeight;
        int leftDiam    = diameterOfBinaryTree(root.left);
        int rightDiam   = diameterOfBinaryTree(root.right);
        return Math.max(curDiameter, Math.max(leftDiam, rightDiam));
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

At root(1):
  lHeight = height(2) = 2
  rHeight = height(3) = 1
  curDiameter = 2+1 = 3

At node(2):
  lHeight = height(4) = 1
  rHeight = height(5) = 1
  curDiameter = 1+1 = 2

At node(3): curDiameter = 0+0 = 0

max(3, max(2, 0)) = 3 ✅
```

---

### 🟢 Approach 2 — Optimal (Height + Update in Same Pass)
**Time: O(N) | Space: O(H)**

#### 💡 Idea
The brute force calls `height()` separately — causing repeated traversals. In the optimal approach, while computing height in a **postorder fashion** (bottom-up), we **simultaneously update the diameter**.

At each node: `diameter = max(diameter, lh + rh)` — done during the height calculation itself. One pass, no redundant work.

**Why `int[] diameter` (array of size 1)?**
Java passes primitives by value — changes to an `int` won't persist across recursive calls. An array is an object, passed by reference, so changes to `diameter[0]` are visible to all recursive calls.

```java
class Solution {
    public int diameterOfBinaryTree(TreeNode root) {
        int[] diameter = new int[1];
        height(root, diameter);
        return diameter[0];
    }

    private int height(TreeNode node, int[] diameter) {
        if (node == null) return 0;
        int lh = height(node.left,  diameter);
        int rh = height(node.right, diameter);
        diameter[0] = Math.max(diameter[0], lh + rh); // update diameter here
        return 1 + Math.max(lh, rh);
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

height(4): lh=0,rh=0. diameter=max(0,0+0)=0. return 1
height(5): lh=0,rh=0. diameter=max(0,0+0)=0. return 1
height(2): lh=1,rh=1. diameter=max(0,1+1)=2. return 2
height(3): lh=0,rh=0. diameter=max(2,0+0)=2. return 1
height(1): lh=2,rh=1. diameter=max(2,2+1)=3. return 3

Return diameter[0] = 3 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(H) |
| Optimal | **O(N)** ✅ | O(H) |

---

## 4. Check for Balanced Binary Tree

### 🧩 Problem Statement

A binary tree is **height-balanced** if for every node, the difference between left and right subtree heights is at most 1. Return `true` if balanced, else `false`.

**Example 1:**
```
Input:  [3, 9, 20, null, null, 15, 7]
Output: Yes (balanced)
```

**Example 2:**
```
Input:  [1, 2, null, null, 3]
Output: No (not balanced — difference > 1)
```

**Constraints:** `0 <= nodes <= 10⁵`

---

### 🔴 Approach 1 — Brute Force
**Time: O(N²) | Space: O(N)**

#### 💡 Idea
For each node, compute `leftHeight` and `rightHeight`. If `|left - right| > 1`, return false immediately. Otherwise, recursively check both subtrees.

**Same problem as diameter brute force:** `height()` is called for each node → O(N²).

```java
class Solution {
    int height(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(height(root.left), height(root.right));
    }

    boolean isBalanced(TreeNode root) {
        if (root == null) return true;
        int leftHeight  = height(root.left);
        int rightHeight = height(root.right);
        if (Math.abs(leftHeight - rightHeight) > 1) return false;
        return isBalanced(root.left) && isBalanced(root.right);
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         /
        2
       /
      3

At node(1): left=height(2)=2, right=0. |2-0|=2 > 1 → return false ✅

Tree:     1
         / \
        2   3

At node(1): left=1, right=1. |1-1|=0 ≤ 1. Check left(2): balanced. Check right(3): balanced → return true ✅
```

---

### 🟢 Approach 2 — Optimal (Return -1 for Unbalanced)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Same one-pass postorder trick as diameter, but with a different signal. Instead of computing height and balance separately, **use -1 as a sentinel** to propagate "this subtree is unbalanced" upward.

**Rules:**
- If `node == null` → return 0 (empty tree height)
- If left subtree returned -1 → already unbalanced → return -1 immediately
- If right subtree returned -1 → already unbalanced → return -1 immediately
- If `|leftHeight - rightHeight| > 1` → unbalanced here → return -1
- Otherwise → return actual height `1 + max(lh, rh)`

**Final answer:** `dfsHeight(root) != -1` (true if no -1 was returned)

```java
class Solution {
    public boolean isBalanced(TreeNode root) {
        return dfsHeight(root) != -1;
    }

    private int dfsHeight(TreeNode root) {
        if (root == null) return 0;

        int leftHeight = dfsHeight(root.left);
        if (leftHeight == -1) return -1;           // early exit

        int rightHeight = dfsHeight(root.right);
        if (rightHeight == -1) return -1;          // early exit

        if (Math.abs(leftHeight - rightHeight) > 1) return -1; // unbalanced here

        return 1 + Math.max(leftHeight, rightHeight);
    }
}
```

#### 🧪 Dry Run

**Balanced tree:** `[3, 9, 20, null, null, 15, 7]`

```
dfsHeight(9)  = 1
dfsHeight(15) = 1
dfsHeight(7)  = 1
dfsHeight(20) = 1+max(1,1)=2. |1-1|=0 ≤ 1 → 2
dfsHeight(3)  = lh=1, rh=2. |1-2|=1 ≤ 1 → 1+max(1,2)=3

Return 3 != -1 → true ✅ (balanced)
```

**Unbalanced tree:** `[1, 2, null, null, 3]`

```
Tree:   1
       /
      2
       \
        3

dfsHeight(3) = 1
dfsHeight(2): lh=0, rh=1. |0-1|=1 ≤ 1 → 2
dfsHeight(1): lh=2, rh=0. |2-0|=2 > 1 → return -1

Return -1 → -1 == -1 → false ✅ (not balanced)
```

> 💡 **Why return -1 instead of a boolean?**
> The function needs to return both "is this subtree balanced?" AND "what is its height?" in one call. We can't return both from a single `int` function... unless we use -1 as a special value meaning "unbalanced." This avoids a second pass.

> 💡 **Why check `leftHeight == -1` before computing rightHeight?**
> **Early exit / short-circuit.** If the left subtree is already unbalanced, there's no point computing the right subtree's height. Return -1 immediately and save work.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(N) |
| Optimal (-1 sentinel) | **O(N)** ✅ | O(N) |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Level Order Traversal | BFS with queue. Capture `size = q.size()` BEFORE the inner loop to freeze current level count |
| Maximum Depth | = height of tree. Recursive: `1 + max(left, right)`. Iterative: count BFS levels |
| Diameter | `diameter = lh + rh` at each node. Optimal: update diameter while computing height (one pass, O(N)) |
| Balanced BT | Return -1 from height function to signal "unbalanced". Early exit on -1 propagates result upward without extra pass |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
