# 🚀 Day 40/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Search in BST](#1-search-in-bst) | 🟢 Easy | BST |
| 2 | [Populating Next Right Pointers in Each Node](#2-populating-next-right-pointers-in-each-node) | 🟡 Medium | Binary Tree / BFS |
| 3 | [Convert Sorted Array to BST](#3-convert-sorted-array-to-bst) | 🟢 Easy | BST / Recursion |

> 💡 **New Chapter: Binary Search Tree**
> BST property: for every node, **left subtree < node < right subtree**. This allows search in O(log N) instead of O(N) — we eliminate half the search space at every step, just like binary search.

---

## BST Fundamentals

Before today's problems, understand why BST is powerful:

```
Binary Tree search: O(N) — may need to check every node
BST search:        O(log N) — eliminate half at each step

BST Property: left < curr.node < right
```

In a regular binary tree, to find a node we need pre/in/post/level order traversal — O(N). In a BST, we ask "is val < root?" and immediately know to go left or right. Each comparison halves the remaining search space.

---

## 1. Search in BST

### 🧩 Problem Statement

Given the root of a BST and an integer `val`, find the node with value `val` and return the subtree rooted at it. If not found, return `null`.

**Example:**
```
BST:        4
           / \
          2   7
         / \
        1   3

Search val=2 → return subtree rooted at 2: [2, 1, 3]
Search val=5 → return null
```

**Constraints:** `1 <= nodes <= 5000`, all values unique

---

### 🟢 Approach — Iterative BST Search
**Time: O(log N) average | Space: O(1)**

#### 💡 Idea

Use the BST property at every step:
- If `val < root.data` → target must be in **left subtree** (right subtree only has values > root)
- If `val > root.data` → target must be in **right subtree**
- If `val == root.data` → found it

Iteratively move the pointer — no recursion, no extra space.

#### 💻 Code
```java
class Solution {
    public TreeNode searchBST(TreeNode root, int val) {
       while (root != null && root.data != val) {
           root = (val < root.data) ? root.left : root.right;
       }
       return root;
    }
}
```

#### 🧪 Dry Run

**BST: [4,2,7,1,3], val=2**

```
root=4: 2 < 4 → go left. root=2
root=2: 2 == 2 → exit loop
return node(2) ✅ (subtree: 2→1, 2→3)
```

**val=5:**

```
root=4: 5 > 4 → go right. root=7
root=7: 5 < 7 → go left. root=null
root=null → exit loop
return null ✅
```

> 💡 **Why O(log N)?**
> Each step eliminates one half of the remaining tree. For a balanced BST with N nodes, there are log₂(N) levels → at most log₂(N) comparisons. For a skewed BST (worst case), it degrades to O(N).

> 💡 **Why not check both left and right like in regular BT?**
> BST property guarantees the target can only be in one direction. If `val < root`, it cannot exist in the right subtree (all right values > root > val). This is exactly why BST search is O(log N) while general BT search is O(N).

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(log N) average, O(N) worst (skewed) | **O(1)** — iterative, no stack |

---

## 2. Populating Next Right Pointers in Each Node

### 🧩 Problem Statement

Given a **perfect binary tree** (all leaves at same level, every parent has exactly 2 children), populate each node's `next` pointer to point to its **next right node** at the same level. Last node at each level points to `null`.

**Example:**
```
Input:       1
            / \
           2   3
          / \ / \
         4  5 6  7

Output after connecting:
Level 0: 1 → null
Level 1: 2 → 3 → null
Level 2: 4 → 5 → 6 → 7 → null
```

**Constraints:** `0 <= nodes <= 2¹²-1`, `-1000 <= val <= 1000`

---

### 🟢 Approach — O(1) Space Level Linking
**Time: O(N) | Space: O(1)**

#### 💡 Idea

Since it's a perfect binary tree, once we've connected nodes at the current level, we can use those `next` pointers to traverse the current level and connect the next level — **without a queue**.

**Two connections at each internal node `curr`:**
1. `curr.left.next = curr.right` — connect left child to right child (same parent)
2. If `curr.next != null`: `curr.right.next = curr.next.left` — connect right child to left child of the next parent

Move to next level via `levelStart = levelStart.left` (leftmost path downward).

Stop when `levelStart.left == null` (reached leaf level — no children to connect).

#### 💻 Code
```java
class Solution {
    public Node connect(Node root) {
        if (root == null) return null;

        Node levelStart = root;

        while (levelStart.left != null) {  // stop at leaf level
            Node curr = levelStart;

            while (curr != null) {
                curr.left.next = curr.right;                    // within same parent

                if (curr.next != null)
                    curr.right.next = curr.next.left;           // across parents

                curr = curr.next;  // move right using already-set next pointers
            }

            levelStart = levelStart.left;  // go down one level
        }

        return root;
    }
}
```

#### 🧪 Dry Run

```
Tree:        1
            / \
           2   3
          / \ / \
         4  5 6  7

Level 0 → Connect Level 1:
levelStart=1. curr=1:
  1.left(2).next = 1.right(3)    → 2→3
  1.next=null → skip second connection
  curr=1.next=null → inner loop ends.
levelStart=1.left=2.

Level 1 → Connect Level 2:
levelStart=2. curr=2:
  2.left(4).next = 2.right(5)    → 4→5
  2.next=3 → 2.right(5).next = 3.left(6) → 5→6
  curr=2.next=3.

curr=3:
  3.left(6).next = 3.right(7)    → 6→7
  3.next=null → skip
  curr=3.next=null → inner loop ends.
levelStart=2.left=4.

levelStart=4: 4.left=null → outer loop ends.

Final: 1→null | 2→3→null | 4→5→6→7→null ✅
```

> 💡 **Why O(1) space (no queue)?**
> Normal level order BFS uses a queue → O(N) space. Here, we use already-established `next` pointers from the current level to traverse it and build the next level's connections. The `next` pointers themselves act as our "queue".

> 💡 **Why only works for perfect binary trees?**
> We rely on `curr.next.left` always existing. In a perfect tree, if `curr.next` exists, it always has a left child. For imperfect trees, this assumption breaks — we'd need to check more carefully or use a queue.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** | **O(1)** — no extra data structures |

---

## 3. Convert Sorted Array to BST

### 🧩 Problem Statement

Given a sorted array `nums` in ascending order, convert it to a **height-balanced BST** (depth of any two leaf nodes differs by at most 1).

**Example:**
```
Input:  nums = [-10, -3, 0, 5, 9]
Output: [0, -3, 9, -10, null, 5]

        0
       / \
     -3   9
     /   /
   -10  5
```

**Constraints:** `1 <= nums.length <= 10⁴`, sorted strictly increasing

---

### 🟢 Approach — Recursive Binary Search / Divide and Conquer
**Time: O(N) | Space: O(log N)**

#### 💡 Idea

To build a **height-balanced** BST from a sorted array:
1. Pick the **middle element** as the root — this ensures equal number of elements on left and right
2. Recursively build the left subtree from `left...mid-1`
3. Recursively build the right subtree from `mid+1...right`

**Why middle = root?**
If we pick the middle, left subarray and right subarray have (nearly) equal sizes. Since both halves recurse to the same depth, the resulting BST is height-balanced.

**Why is this a valid BST?**
Since the array is sorted: `left subarray < nums[mid] < right subarray`. Placing `nums[mid]` as root and recursing on left/right naturally satisfies the BST property.

#### 💻 Code
```java
class Solution {
    public TreeNode sortedArrayToBST(int[] nums) {
        return build(nums, 0, nums.length - 1);
    }

    private TreeNode build(int[] nums, int left, int right) {
        if (left > right) return null;  // empty subarray → no node

        int mid = left + (right - left) / 2;  // pick middle as root

        TreeNode root = new TreeNode(nums[mid]);
        root.left  = build(nums, left,    mid - 1);  // left half → left subtree
        root.right = build(nums, mid + 1, right);    // right half → right subtree

        return root;
    }
}
```

#### 🧪 Dry Run

**nums = [-10, -3, 0, 5, 9]** (indices 0..4)

```
build(0,4): mid=2, root=nums[2]=0
  build(0,1): mid=0, root=nums[0]=-10
    build(0,-1): left>right → null (left subtree of -10)
    build(1,1):  mid=1, root=nums[1]=-3
      build(1,0): left>right → null
      build(2,1): left>right → null
      return node(-3) [leaf]
    -10.left=null, -10.right=-3
    return node(-10)

  build(3,4): mid=3, root=nums[3]=5
    build(3,2): left>right → null
    build(4,4): mid=4, root=nums[4]=9
      build(4,3): null. build(5,4): null.
      return node(9) [leaf]
    5.left=null, 5.right=9
    return node(5)

  0.left=-10 (with right child -3), 0.right=5 (with right child 9)

Final tree:
        0
       / \
    -10   5
       \   \
       -3   9  ✅ (height-balanced BST)
```

> 💡 **Why `mid = left + (right - left) / 2` instead of `(left + right) / 2`?**
> `(left + right)` can overflow when both are large integers. The formula `left + (right - left) / 2` is overflow-safe and gives the same result.

> 💡 **Why does picking mid guarantee height-balanced?**
> When we split `[left...right]` at `mid`, left side has `mid - left` elements, right side has `right - mid` elements. These differ by at most 1. Since both halves recurse identically, all subtree heights are balanced recursively.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each element becomes exactly one node | O(log N) recursion stack (balanced tree height) |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Search in BST | BST property → go left if val < root, right if val > root. O(log N) vs O(N) for BT. Iterative → O(1) space |
| Next Right Pointers | For perfect BT: use already-set next pointers to traverse level and connect next level. Two connections: same-parent and cross-parent. O(1) space |
| Sorted Array → BST | Mid element = root (ensures balance). Recurse left half for left subtree, right half for right subtree. BST property naturally satisfied since array is sorted |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
