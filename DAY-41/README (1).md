# 🚀 Day 41/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Construct BST from Preorder](#1-construct-bst-from-preorder-traversal) | 🟡 Medium | BST / Construction |
| 2 | [Check if a Tree is BST](#2-check-if-a-tree-is-bst-or-not) | 🟡 Medium | BST / Validation |
| 3 | [LCA in BST](#3-lca-in-bst) | 🟡 Medium | BST |
| 4 | [Inorder Successor and Predecessor in BST](#4-inorder-successor-and-predecessor-in-bst) | 🟡 Medium | BST |

---

## 1. Construct BST from Preorder Traversal

### 🧩 Problem Statement

Given an array representing the preorder traversal of a BST, construct and return the BST.

**Example:**
```
Input:  preorder = [8, 5, 1, 7, 10, 12]
Output: BST rooted at 8, with 5 and 10 as children, etc.
```

**Constraints:** `1 <= preorder.length <= 100`, all values unique

---

### 🔴 Approach 1 — Brute (Sort to get Inorder + Build like Pre+In construction)
**Time: O(N log N) | Space: O(N)**

#### 💡 Idea
**Key insight:** Sorting the preorder array gives us the inorder traversal (BST inorder = sorted order). Once we have both preorder and inorder, we can use the standard "Construct BT from Preorder and Inorder" algorithm.

#### 💻 Code
```java
class Solution {
    public TreeNode bstFromPreorder(int[] preorder) {
        List<Integer> inorder = new ArrayList<>();
        for (int val : preorder) inorder.add(val);
        Collections.sort(inorder);  // sorted preorder = inorder of BST

        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < inorder.size(); i++) inMap.put(inorder.get(i), i);

        return buildTree(preorder, inMap, 0, preorder.length - 1, 0, inorder.size() - 1);
    }

    private TreeNode buildTree(int[] preorder, Map<Integer, Integer> inMap,
                               int preStart, int preEnd, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) return null;
        TreeNode root = new TreeNode(preorder[preStart]);
        int inRoot = inMap.get(root.data);
        int leftSize = inRoot - inStart;
        root.left  = buildTree(preorder, inMap, preStart + 1, preStart + leftSize, inStart, inRoot - 1);
        root.right = buildTree(preorder, inMap, preStart + leftSize + 1, preEnd, inRoot + 1, inEnd);
        return root;
    }
}
```

---

### 🟢 Approach 2 — Optimal (BST Bound + Single Index)
**Time: O(N) | Space: O(H)**

#### 💡 Idea

**Connection to "Check if tree is BST":** A BST maintains that every node must be within a `[min, max]` range. Use the same idea during construction — pass an upper `bound` to each call. If the next preorder element exceeds the bound, it doesn't belong to this subtree; return null.

**How it works:**
- Start with `bound = Integer.MAX_VALUE` and `index[0] = 0`
- Take `preorder[index[0]]` as root, increment index
- Left subtree: bound = `root.data` (all left values < root)
- Right subtree: bound = same as parent's bound
- If `preorder[index[0]] > bound` → element belongs to a higher subtree → return null

**Why `int[]` for index?** Java passes primitives by value. An `int[]` is an object — changes to `index[0]` persist across all recursive calls, acting as a shared global counter.

#### 💻 Code
```java
class Solution {
    public TreeNode bstFromPreorder(int[] preorder) {
        return bstFromPreorderHelper(preorder, Integer.MAX_VALUE, new int[]{0});
    }

    private TreeNode bstFromPreorderHelper(int[] preorder, int bound, int[] index) {
        if (index[0] == preorder.length || preorder[index[0]] > bound) return null;

        TreeNode root = new TreeNode(preorder[index[0]++]);  // consume element
        root.left  = bstFromPreorderHelper(preorder, root.data, index); // left bound = root
        root.right = bstFromPreorderHelper(preorder, bound,     index); // right bound = parent's
        return root;
    }
}
```

#### 🧪 Dry Run

**preorder = [8, 5, 1, 7, 10, 12]**, bound=MAX, index=[0]

```
call(MAX): preorder[0]=8≤MAX → root=8, index=[1]
  call(8):  preorder[1]=5≤8  → root=5, index=[2]
    call(5): preorder[2]=1≤5 → root=1, index=[3]
      call(1): preorder[3]=7>1 → return null  (left of 1)
      call(5): preorder[3]=7>5 → return null  (right of 1, bound=5)
      return node(1) [leaf]
    call(8): preorder[3]=7≤8 → root=7, index=[4]
      call(7): preorder[4]=10>7 → null (left of 7)
      call(8): preorder[4]=10>8 → null (right of 7, bound=8)
      return node(7) [leaf]
    5.left=1, 5.right=7
  call(MAX): preorder[4]=10≤MAX → root=10, index=[5]
    call(10): preorder[5]=12>10 → null (left of 10)
    call(MAX): preorder[5]=12≤MAX → root=12, index=[6]
      index=6=length → null, null
      return node(12) [leaf]
    10.left=null, 10.right=12
  8.left=5, 8.right=10

BST:         8
            / \
           5   10
          / \    \
         1   7   12  ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Sort + BT Construction | O(N log N) | O(N) |
| BST Bound | **O(N)** ✅ | O(H) |

---

## 2. Check if a Tree is BST or Not

### 🧩 Problem Statement

Given the root of a binary tree, return `true` if it is a valid BST, else `false`.

**Example 1:** `[5,3,6,2,4,null,7]` → `true`
**Example 2:** `[5,3,6,4,2,null,7]` → `false` (node 4 is in left subtree of 3, but 4 > 3)

**Constraints:** `1 <= nodes <= 10⁴`

---

### 🟢 Approach — Range Validation (min/max bounds)
**Time: O(N) | Space: O(N)**

#### 💡 Idea

A naive check "left < root < right" is wrong — it misses violations across subtree boundaries. Example:

```
      5
     / \
    1   6
       / \
      3   7   ← node 3 is in right subtree of 5, but 3 < 5 → NOT a BST!
```

**Correct approach:** Each node must be within a `[min, max]` range:
- Root: `(-∞, +∞)`
- Left child of node with value `v`: `(min, v)` (must be < v)
- Right child of node with value `v`: `(v, max)` (must be > v)

**Why `long` instead of `int`?** If node value equals `Integer.MIN_VALUE` or `Integer.MAX_VALUE`, the check `node.data <= min` or `node.data >= max` would fail incorrectly. Using `Long.MIN_VALUE` and `Long.MAX_VALUE` avoids this edge case.

#### 💻 Code
```java
class Solution {
    public boolean isBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.data <= min || node.data >= max) return false;
        return validate(node.left, min, node.data) && validate(node.right, node.data, max);
    }
}
```

#### 🧪 Dry Run

**Tree: [5,3,6,4,2,null,7]** (invalid — node 4 in left of 3 is wrong)

```
validate(5, -∞, +∞): 5 in (-∞,+∞) ✓
  validate(3, -∞, 5): 3 in (-∞,5) ✓
    validate(4, -∞, 3): 4 > 3 (max) → return false ✅

Short-circuit: false propagates up → isBST returns false ✅
```

**Tree: [5,3,6,2,4,null,7]** (valid)

```
validate(5,-∞,+∞): ✓
  validate(3,-∞,5): ✓
    validate(2,-∞,3): ✓ → leaf → true
    validate(4,3,5): ✓  → leaf → true
  validate(6,5,+∞): ✓
    validate(null,...): true
    validate(7,6,+∞): ✓ → true

All true → isBST = true ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** | O(N) recursion stack |

---

## 3. LCA in BST

### 🧩 Problem Statement

Given the root of a BST and two values `p` and `q`, return the Lowest Common Ancestor node.

**Example:** BST=[5,3,6,2,4,null,7], p=2, q=4 → LCA=3

---

### 🔴 Approach 1 — Brute (Path tracking — same as LCA in BT)
**Time: O(N) | Space: O(N)**

Find paths from root to both p and q. The last common node in both paths is the LCA.

```java
class Solution {
    boolean getPath(TreeNode root, List<TreeNode> path, int x) {
        if (root == null) return false;
        path.add(root);
        if (root.data == x) return true;
        if (getPath(root.left, path, x) || getPath(root.right, path, x)) return true;
        path.remove(path.size() - 1);
        return false;
    }

    public TreeNode lca(TreeNode root, int p, int q) {
        List<TreeNode> path1 = new ArrayList<>(), path2 = new ArrayList<>();
        if (!getPath(root, path1, p) || !getPath(root, path2, q)) return null;
        int i = 0;
        while (i < path1.size() && i < path2.size() && path1.get(i) == path2.get(i)) i++;
        return path1.get(i - 1);
    }
}
```

---

### 🟢 Approach 2 — Optimal (BST Property — O(H))
**Time: O(H) | Space: O(H)**

#### 💡 Idea

BST gives us free directional information:
- If **both p and q < root**: LCA must be in the **left** subtree
- If **both p and q > root**: LCA must be in the **right** subtree
- Otherwise: one is on each side (or one equals root) → **root is the LCA**

This is O(H) vs O(N) for the brute force because we eliminate half the tree at each step.

#### 💻 Code
```java
class Solution {
    TreeNode lca(TreeNode root, int p, int q) {
        if (root == null) return null;
        int curr = root.data;
        if (curr < p && curr < q) return lca(root.right, p, q);
        if (curr > p && curr > q) return lca(root.left,  p, q);
        return root;
    }
}
```

#### 🧪 Dry Run

**BST=[5,3,6,2,4,null,7], p=2, q=4**

```
lca(5, 2, 4): curr=5. 5>2 && 5>4 → go left
lca(3, 2, 4): curr=3. 3>2 but 3<4 → neither both left nor both right → return node(3) ✅
```

**p=2, q=7:**

```
lca(5, 2, 7): 2<5 and 7>5 → one each side → return node(5) ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute (path tracking) | O(N) | O(N) |
| BST property | **O(H)** ✅ | O(H) |

---

## 4. Inorder Successor and Predecessor in BST

### 🧩 Problem Statement

Given a BST and a `key`, return the inorder predecessor (largest value less than key) and inorder successor (smallest value greater than key). Return `-1` if either doesn't exist.

**Example:** BST=[5,2,10,1,4,7,12], key=10 → [7, 12]
(7 is the largest value < 10, 12 is the smallest value > 10)

**Constraints:** `key` is always present in the BST

---

### 🔴 Approach 1 — Brute (Inorder + Scan)
**Time: O(N) | Space: O(N)**

Do inorder traversal (gives sorted list). Scan for the last value < key (predecessor) and first value > key (successor).

```java
class Solution {
    List<Integer> succPredBST(TreeNode root, int key) {
        List<Integer> sortedList = new ArrayList<>();
        inorderTraversal(root, sortedList);
        int predecessor = -1, successor = -1;
        for (int val : sortedList) {
            if (val < key) predecessor = val;
            else if (val > key) { successor = val; break; }
        }
        return Arrays.asList(predecessor, successor);
    }
    private void inorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.add(node.data);
        inorderTraversal(node.right, list);
    }
}
```

---

### 🟡 Approach 2 — Better (Inorder Traversal with prev tracking)
**Time: O(N) | Space: O(H)**

During inorder traversal, track previous node. When we see `prev.data < key`, update predecessor. When we see current node > key for the first time, set successor.

```java
class Solution {
    List<Integer> succPredBST(TreeNode root, int key) {
        int[] result = {-1, -1};
        TreeNode[] prev = {null};
        inorderTraversal(root, key, result, prev);
        return Arrays.asList(result[0], result[1]);
    }
    private void inorderTraversal(TreeNode node, int key, int[] res, TreeNode[] prev) {
        if (node == null) return;
        inorderTraversal(node.left, key, res, prev);
        if (prev[0] != null && prev[0].data < key) res[0] = prev[0].data;
        if (res[1] == -1 && node.data > key) res[1] = node.data;
        prev[0] = node;
        inorderTraversal(node.right, key, res, prev);
    }
}
```

---

### 🟢 Approach 3 — Optimal (BST property — iterative)
**Time: O(H) | Space: O(1)**

#### 💡 Idea

Use BST navigation to find predecessor and successor without full traversal:

**While traversing:**
- If `key > curr`: curr could be predecessor → save it, go right
- If `key < curr`: curr could be successor → save it, go left
- If `key == curr`: found! Now:
  - Predecessor = rightmost of left subtree
  - Successor = leftmost of right subtree

#### 💻 Code
```java
class Solution {
    public List<Integer> succPredBST(TreeNode root, int key) {
        TreeNode predecessor = null, successor = null, curr = root;

        while (curr != null) {
            if (key > curr.data) {
                predecessor = curr;         // curr could be pred
                curr = curr.right;
            } else if (key < curr.data) {
                successor = curr;           // curr could be succ
                curr = curr.left;
            } else {
                if (curr.left != null) {    // rightmost of left subtree
                    TreeNode temp = curr.left;
                    while (temp.right != null) temp = temp.right;
                    predecessor = temp;
                }
                if (curr.right != null) {   // leftmost of right subtree
                    TreeNode temp = curr.right;
                    while (temp.left != null) temp = temp.left;
                    successor = temp;
                }
                break;
            }
        }

        return Arrays.asList(
            predecessor != null ? predecessor.data : -1,
            successor   != null ? successor.data   : -1
        );
    }
}
```

#### 🧪 Dry Run

**BST=[5,2,10,1,4,7,12], key=10**

```
curr=5:  key=10>5 → predecessor=5, go right
curr=10: key=10==10 → found!
  Left subtree of 10: node(7). 7.right=null → predecessor=7
  Right subtree of 10: node(12). 12.left=null → successor=12
  break

Return [7, 12] ✅
```

**key=1 (minimum — no predecessor):**

```
curr=5:  10>1? No. 5>1 → successor=5, go left
curr=2:  2>1 → successor=2, go left
curr=1:  1==1 → found!
  curr.left=null → predecessor stays null
  curr.right=null → successor stays null (but we already set it to 2 above)
  Wait: when key found, if curr.right != null, successor = leftmost of right subtree.
  curr.right=null → successor not updated in "found" block.
  So successor = 2 (from traversal above) ✅

predecessor=null → -1, successor=2 → Return [-1, 2] ✅
```

> 💡 **Why does traversal set successor before finding key?**
> When we go left (because `key < curr`), `curr` is an ancestor of the key — and it's greater than the key. The actual successor is the smallest such ancestor, which gets updated each time we go left. The final `successor` from traversal gives the right parent, even if the found node has no right subtree.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute (Inorder + Scan) | O(N) | O(N) |
| Better (Inorder + prev) | O(N) | O(H) |
| Optimal (BST iterative) | **O(H)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Construct BST from Preorder | Brute: sort preorder → inorder, then BT construction. Optimal: pass upper bound, consume index in-order — BST property tells us when to stop |
| Check if BST | Range validation: every node must be in `(min, max)`. Use `long` to avoid integer overflow edge cases |
| LCA in BST | BST property: if both nodes < root → go left. Both > root → go right. Otherwise → current node is LCA. O(H) vs O(N) brute |
| Inorder Successor/Predecessor | Optimal: during BST traversal, save potential pred (when going right) and succ (when going left). On finding key, refine using left/right subtrees |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
