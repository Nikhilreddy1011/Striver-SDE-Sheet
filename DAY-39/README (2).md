# 🚀 Day 39/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Symmetric Binary Tree](#1-symmetric-binary-tree) | 🟡 Medium | Binary Tree / Recursion |
| 2 | [Flatten Binary Tree to Linked List](#2-flatten-binary-tree-to-linked-list) | 🟡 Medium | Binary Tree / Recursion |
| 3 | [Children Sum Property](#3-children-sum-property-in-binary-tree) | 🟡 Medium | Binary Tree / Recursion |

---

## 1. Symmetric Binary Tree

### 🧩 Problem Statement

Given the root of a binary tree, check whether it is a **mirror of itself** — i.e., symmetric around its center.

**Example 1:**
```
Input:  [1, 2, 2, 3, 4, 4, 3]
Output: true

        1
       / \
      2   2
     / \ / \
    3  4 4  3   ← mirror image ✅
```

**Example 2:**
```
Input:  [1, 2, 2, null, 3, null, 3]
Output: false

        1
       / \
      2   2
       \    \
        3    3  ← NOT mirror (left's right ≠ right's left) ❌
```

**Constraints:** `1 <= nodes <= 10⁴`, `-100 <= val <= 100`

---

### 🟢 Approach — Simultaneous Recursive Mirror Check
**Time: O(N) | Space: O(H)**

#### 💡 Idea

Draw a vertical line through the root. For the tree to be symmetric:
- The **left subtree** of root should be a **mirror** of the **right subtree**
- For any two nodes `left` and `right` being compared as mirrors:
  - `left.left` mirrors `right.right` (outer children)
  - `left.right` mirrors `right.left` (inner children)

This is **different from checking identical trees** — identical checks `left.left == right.left`, but mirror checks `left.left == right.right`.

#### 📝 Base Cases for `isSame(left, right)`

| Condition | Return |
|-----------|--------|
| `left == null && right == null` | `true` (both empty — symmetric) |
| `left == null \|\| right == null` | `false` (one empty — asymmetric) |
| `left.data != right.data` | `false` (values differ) |
| Otherwise | Recurse: `isSame(left.left, right.right) && isSame(left.right, right.left)` |

#### 💻 Code
```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        return root == null || isSame(root.left, root.right);
    }

    private boolean isSame(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        if (left.data != right.data) return false;
        return isSame(left.left, right.right) && isSame(left.right, right.left);
    }
}
```

#### 🧪 Dry Run

**Tree: [1, 2, 2, 3, 4, 4, 3]**

```
isSymmetric(1) → isSame(2_left, 2_right)

isSame(2L, 2R):
  both non-null. 2==2 ✓
  recurse: isSame(2L.left=3, 2R.right=3) AND isSame(2L.right=4, 2R.left=4)

  isSame(3, 3):
    3==3 ✓
    isSame(null, null) → true
    isSame(null, null) → true
    → true

  isSame(4, 4):
    4==4 ✓
    isSame(null, null) → true
    isSame(null, null) → true
    → true

→ true && true = true ✅
```

**Tree: [1, 2, 2, null, 3, null, 3]**

```
isSame(2L, 2R):
  2==2 ✓
  isSame(2L.left=null, 2R.right=3):
    left==null, right!=null → false ✅ (short-circuit → return false)
```

> 💡 **Symmetric vs Identical:**
> - Identical: `isSame(left.left, right.left)` and `isSame(left.right, right.right)` — same side
> - Symmetric: `isSame(left.left, right.right)` and `isSame(left.right, right.left)` — cross sides (mirror)

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node pair visited once | O(H) recursion stack |

---

## 2. Flatten Binary Tree to Linked List

### 🧩 Problem Statement

Given the root of a binary tree, **flatten it in-place** into a singly linked list following **preorder traversal** order. Each node's `right` pointer points to the next node; `left` pointer is always `null`.

**Example:**
```
Input:      1
           / \
          2   5
         / \   \
        3   4   6

Output:  1→2→3→4→5→6  (right pointers, left=null everywhere)
Preorder: 1, 2, 3, 4, 5, 6 ✅
```

**Constraints:** `0 <= nodes <= 2000`, no new nodes allowed

---

### 🟢 Approach — Reverse Preorder (Right → Left → Root) with `prev` pointer
**Time: O(N) | Space: O(H)**

#### 💡 Idea

**Key insight:** If we process nodes in **reverse preorder** (Right → Left → Root), we can build the linked list backwards by maintaining a `prev` pointer.

For each node:
1. `root.right = prev` (link to previously processed node)
2. `root.left = null` (clear left)
3. `prev = root` (this node becomes the new prev)

Why reverse? Because when we process Root last (after right and left), `prev` already holds the correct next node in preorder — which is `root`'s immediate right neighbor in the flattened list.

#### 📝 Steps

```
flattenHelper(root):
  1. Base: if root==null, return
  2. flattenHelper(root.right)   ← process right subtree first
  3. flattenHelper(root.left)    ← then left subtree
  4. root.right = prev           ← link to previously flattened node
  5. root.left = null            ← clear left
  6. prev = root                 ← update prev
```

#### 💻 Code
```java
class Solution {
    private TreeNode prev = null;

    private void flattenHelper(TreeNode root) {
        if (root == null) return;

        flattenHelper(root.right);
        flattenHelper(root.left);

        root.right = prev;
        root.left = null;
        prev = root;
    }

    public void flatten(TreeNode root) {
        prev = null;
        flattenHelper(root);
    }
}
```

#### 🧪 Dry Run

```
Tree:       1
           / \
          2   5
         / \   \
        3   4   6

Reverse Preorder processing order: 6, 5, 4, 3, 2, 1

flattenHelper(6):
  right=null, left=null. root.right=prev=null. root.left=null. prev=6.
  [6→null]

flattenHelper(5):
  right processed (6 done). left=null.
  root.right=prev=6. root.left=null. prev=5.
  [5→6→null]

flattenHelper(4):
  right=null, left=null. root.right=prev=5. root.left=null. prev=4.
  [4→5→6→null]

flattenHelper(3):
  right=null, left=null. root.right=prev=4. root.left=null. prev=3.
  [3→4→5→6→null]

flattenHelper(2):
  right(4) done, left(3) done.
  root.right=prev=3. root.left=null. prev=2.
  [2→3→4→5→6→null]

flattenHelper(1):
  right(5) done, left(2) done.
  root.right=prev=2. root.left=null. prev=1.
  [1→2→3→4→5→6→null] ✅
```

> 💡 **Why process right BEFORE left?**
> In preorder (Root→Left→Right), the very last element to be processed is the rightmost leaf. We process in reverse, so we start from that last element. By going Right first in our reverse traversal, we process Right subtree nodes before Left subtree nodes — which gives us correct reverse preorder.

> 💡 **Why not use extra space (collect preorder then re-link)?**
> That works but uses O(N) extra space. This approach reuses existing tree structure — `prev` is the only extra variable, so O(1) additional space (excluding recursion stack).

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited once | O(H) recursion stack only |

---

## 3. Children Sum Property in Binary Tree

### 🧩 Problem Statement

Given the root of a binary tree, return `true` if and only if **every node's value equals the sum of its left and right children's values**. Missing (null) children count as 0. Leaf nodes automatically satisfy the property.

**Example 1:**
```
Input:  [1, 4, 3, 5]
Output: false
Root=1, children=4+3=7. 1≠7 → false
```

**Example 2:**
```
Input:  [10, 4, 6, 1, 3, 2, 4]
Output: true
  4=1+3 ✓, 6=2+4 ✓, 10=4+6 ✓
```

**Constraints:** `1 <= n <= 10⁴`, `-10⁵ <= val <= 10⁵`

---

### 🟢 Approach — Recursive Check at Every Node
**Time: O(N) | Space: O(H)**

#### 💡 Idea

For every internal node, check: `node.val == leftChild.val + rightChild.val`. If either child is null, treat its value as 0. Recurse to both children — ALL must satisfy the property simultaneously.

**Three base cases:**
1. `root == null` → return `true` (empty tree satisfies by definition)
2. `root.left == null && root.right == null` → return `true` (leaf node satisfies automatically)
3. Otherwise: check `root.val == leftVal + rightVal` AND recurse on both subtrees

#### 💻 Code
```java
class Solution {
    public boolean checkChildrenSum(TreeNode root) {
        if (root == null) return true;                          // empty → true
        if (root.left == null && root.right == null) return true; // leaf → true

        int leftVal  = (root.left  != null) ? root.left.val  : 0;
        int rightVal = (root.right != null) ? root.right.val : 0;

        return (root.val == leftVal + rightVal)
                && checkChildrenSum(root.left)
                && checkChildrenSum(root.right);
    }
}
```

#### 🧪 Dry Run

**Tree: [10, 4, 6, 1, 3, 2, 4]**

```
         10
        /  \
       4    6
      / \  / \
     1   3 2  4

checkChildrenSum(1): leaf → true
checkChildrenSum(3): leaf → true
checkChildrenSum(4_left):
  leftVal=1, rightVal=3. 4==1+3 ✓
  both children → true
  → true

checkChildrenSum(2): leaf → true
checkChildrenSum(4_right): leaf → true
checkChildrenSum(6):
  leftVal=2, rightVal=4. 6==2+4 ✓
  both children → true
  → true

checkChildrenSum(10):
  leftVal=4, rightVal=6. 10==4+6 ✓
  left(4)→true, right(6)→true
  → true ✅
```

**Tree: [1, 4, 3, 5]**

```
      1
     / \
    4   3
   /
  5

checkChildrenSum(5): leaf → true
checkChildrenSum(4): leftVal=5, rightVal=0. 4==5+0? 4≠5 → false
(short-circuit: 1's check doesn't even run)
→ false ✅
```

> 💡 **Why use short-circuit `&&`?**
> `(root.val == leftVal + rightVal) && checkChildrenSum(left) && checkChildrenSum(right)` — if the current node fails, the children aren't even checked. If any check fails anywhere, false propagates upward immediately.

> 💡 **Why check leaf separately before computing leftVal/rightVal?**
> If we didn't have the leaf check, we'd compute `leftVal=0, rightVal=0` and check `root.val == 0`. This would make leaf nodes with non-zero values return `false` — which is wrong. The leaf base case handles this explicitly before reaching the sum check.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited once | O(H) recursion stack |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Symmetric BT | Mirror check: cross-compare `(left.left, right.right)` and `(left.right, right.left)`. Three base cases before recursion |
| Flatten BT to LL | Process in reverse preorder (Right→Left→Root) with a `prev` pointer. Each node's right = prev, left = null, then update prev |
| Children Sum Property | Base cases: null→true, leaf→true. Then check `val == leftVal + rightVal` AND recurse both sides with short-circuit `&&` |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
