# 🚀 Day 38/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Maximum Path Sum](#1-maximum-path-sum) | 🟡 Medium | Binary Tree / DFS |
| 2 | [Construct BT from Preorder and Inorder](#2-construct-bt-from-preorder-and-inorder) | 🔴 Hard | Binary Tree / HashMap |
| 3 | [Construct BT from Postorder and Inorder](#3-construct-bt-from-postorder-and-inorder) | 🔴 Hard | Binary Tree / HashMap |

---

## 1. Maximum Path Sum

### 🧩 Problem Statement

Given the root of a binary tree, return the **maximum path sum** of any non-empty path. A path is any sequence of connected nodes — it does **not** have to pass through the root.

**Example 1:**
```
Input:  root = [20, 9, -10, null, null, 15, 7]
Output: 34
Path:   15 → -10 → 20 → 9  (sum = 34)
```

**Example 2:**
```
Input:  root = [-10, 9, 20, null, null, 15, 7]
Output: 42
Path:   15 → 20 → 7  (sum = 42)
```

**Constraints:** `1 <= nodes <= 3×10⁴`, `-10³ <= val <= 10³`

---

### 🟢 Approach — Postorder DFS with Global Maximum
**Time: O(N) | Space: O(H)**

#### 💡 Idea

Think of each node as a potential **turning point** of the maximum path. The maximum path through any node = `leftMax + rightMax + node.data`.

**Key decisions:**
1. If left or right subtree gives a **negative** contribution, ignore it (take `max(0, subtreeSum)`)
2. Update global `maxi` at every node: `maxi = max(maxi, left + right + node.data)`
3. But when **returning to parent**, we can only go through **one side** (can't curve the path twice). So return `node.data + max(left, right)`

**Why `int[]` instead of `int`?**
Java passes primitives by value. Changes to an `int` inside a recursive call don't persist outside. An `int[]` (array object) is passed by reference — changes to `maxi[0]` are visible across all recursive frames.

#### 💻 Code
```java
class Solution {
    int findMaxPathSum(TreeNode root, int[] maxi) {
        if (root == null) return 0;

        int left  = Math.max(0, findMaxPathSum(root.left,  maxi));
        int right = Math.max(0, findMaxPathSum(root.right, maxi));

        maxi[0] = Math.max(maxi[0], left + right + root.data); // path through this node

        return root.data + Math.max(left, right); // return only one branch to parent
    }

    public int maxPathSum(TreeNode root) {
        int[] maxi = {Integer.MIN_VALUE};
        findMaxPathSum(root, maxi);
        return maxi[0];
    }
}
```

#### 🧪 Dry Run

```
Tree:       20
           /  \
          9  -10
             / \
            15   7

findMaxPathSum(9):   left=0,right=0. maxi=max(MIN,0+0+9)=9.   return 9+0=9
findMaxPathSum(15):  left=0,right=0. maxi=max(9,0+0+15)=15.   return 15
findMaxPathSum(7):   left=0,right=0. maxi=max(15,0+0+7)=15.   return 7
findMaxPathSum(-10): left=max(0,15)=15, right=max(0,7)=7.
                     maxi=max(15, 15+7+(-10))=max(15,12)=15.
                     return -10+max(15,7)=-10+15=5
findMaxPathSum(20):  left=max(0,9)=9, right=max(0,5)=5.
                     maxi=max(15, 9+5+20)=max(15,34)=34.
                     return 20+max(9,5)=20+9=29

Return maxi[0] = 34 ✅

Path: 9 → 20 → -10 → 15 = 9+20+(-10)+15 = 34
```

> 💡 **Why initialize `maxi` to `Integer.MIN_VALUE` (not 0)?**
> If all node values are negative, the answer is still the largest (least negative) single node. Starting at 0 would incorrectly return 0 for an all-negative tree.

> 💡 **Why return only one branch to parent?**
> A path in a tree cannot fork — it must be a straight line. When we compute `left + right + node` for the current node, that path "uses up" both branches. We can only contribute **one direction** back to the parent for the parent's path calculation.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited once | O(H) recursion stack |

---

## 2. Construct BT from Preorder and Inorder

### 🧩 Problem Statement

Given `preorder` and `inorder` traversal arrays of a binary tree, reconstruct and return the tree.

**Example:**
```
preorder = [3, 9, 20, 15, 7]
inorder  = [9, 3, 15, 20, 7]
Output: Tree rooted at 3, with 9 as left and 20 as right (20 has children 15 and 7)
```

**Constraints:** `1 <= nodes <= 10⁴`, all values unique

---

### 🟢 Approach — Preorder root + Inorder split + HashMap
**Time: O(N) | Space: O(N)**

#### 💡 Key Observations

1. **Preorder's first element is always the root** of the current subtree
2. **Inorder splits around the root**: everything to the left of root's position in inorder = left subtree; everything to the right = right subtree
3. The **size of the left subtree** in inorder tells us how many elements belong to the left subtree in preorder too

**HashMap optimization:** Finding the root's position in inorder naively takes O(N) per call → O(N²) total. Store inorder element → index in a HashMap for O(1) lookup → O(N) total.

#### 📝 Algorithm

```
buildTree(preorder, preStart, preEnd, inorder, inStart, inEnd, inMap):
  root = preorder[preStart]      ← always the current subtree's root
  inRoot = inMap[root]           ← root's position in inorder
  leftSize = inRoot - inStart    ← number of elements in left subtree

  root.left = buildTree(preStart+1, preStart+leftSize, inStart, inRoot-1)
  root.right = buildTree(preStart+leftSize+1, preEnd, inRoot+1, inEnd)
```

#### 💻 Code
```java
class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) inMap.put(inorder[i], i);
        return buildTree(preorder, 0, preorder.length - 1,
                         inorder, 0, inorder.length - 1, inMap);
    }

    private TreeNode buildTree(int[] preorder, int preStart, int preEnd,
                               int[] inorder, int inStart, int inEnd,
                               Map<Integer, Integer> inMap) {
        if (preStart > preEnd || inStart > inEnd) return null;

        TreeNode root = new TreeNode(preorder[preStart]);
        int inRoot   = inMap.get(root.data);
        int leftSize = inRoot - inStart;

        root.left  = buildTree(preorder, preStart + 1,            preStart + leftSize,
                               inorder,  inStart,                 inRoot - 1,           inMap);
        root.right = buildTree(preorder, preStart + leftSize + 1, preEnd,
                               inorder,  inRoot + 1,              inEnd,                inMap);
        return root;
    }
}
```

#### 🧪 Dry Run

```
preorder = [3, 9, 20, 15, 7]
inorder  = [9, 3, 15, 20, 7]
inMap    = {9:0, 3:1, 15:2, 20:3, 7:4}

Call: preStart=0, preEnd=4, inStart=0, inEnd=4
  root = preorder[0] = 3
  inRoot = inMap[3] = 1
  leftSize = 1 - 0 = 1

  Left call:  preStart=1, preEnd=1, inStart=0, inEnd=0
    root = preorder[1] = 9
    inRoot = inMap[9] = 0. leftSize = 0-0 = 0
    Left:  preStart=2>preEnd=1 → null
    Right: preStart=2>preEnd=1 → null
    return node(9) ← leaf node

  Right call: preStart=2, preEnd=4, inStart=2, inEnd=4
    root = preorder[2] = 20
    inRoot = inMap[20] = 3. leftSize = 3-2 = 1

    Left call:  preStart=3, preEnd=3, inStart=2, inEnd=2
      root = preorder[3] = 15 → leaf → return node(15)

    Right call: preStart=4, preEnd=4, inStart=4, inEnd=4
      root = preorder[4] = 7 → leaf → return node(7)

    return node(20) with left=15, right=7

Final tree:
      3
     / \
    9   20
        / \
       15   7  ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — HashMap gives O(1) root lookup per call | O(N) HashMap + O(H) stack |

---

## 3. Construct BT from Postorder and Inorder

### 🧩 Problem Statement

Given `postorder` and `inorder` traversal arrays, reconstruct and return the binary tree.

**Example:**
```
postorder = [9, 15, 7, 20, 3]
inorder   = [9, 3, 15, 20, 7]
Output: Same tree as above (rooted at 3)
```

**Constraints:** `1 <= nodes <= 3000`, all values unique

---

### 🟢 Approach — Same Pattern, Postorder Root is Last
**Time: O(N) | Space: O(N)**

#### 💡 How It Differs from Preorder+Inorder

| | Preorder + Inorder | Postorder + Inorder |
|--|--|--|
| Root location | **First** of preorder | **Last** of postorder |
| Left subtree in traversal | `preorder[preStart+1 ... preStart+leftSize]` | `postorder[ps ... ps+leftSize-1]` |
| Right subtree in traversal | `preorder[preStart+leftSize+1 ... preEnd]` | `postorder[ps+leftSize ... pe-1]` |

**Everything else is identical** — use inorder to find root position, split into left and right, recurse.

#### 💻 Code
```java
class Solution {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder.length != postorder.length) return null;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) map.put(inorder[i], i);
        return buildTreePostIn(inorder, 0, inorder.length - 1,
                               postorder, 0, postorder.length - 1, map);
    }

    public TreeNode buildTreePostIn(int[] inorder, int is, int ie,
                                    int[] postorder, int ps, int pe,
                                    Map<Integer, Integer> map) {
        if (ps > pe || is > ie) return null;

        TreeNode root = new TreeNode(postorder[pe]); // ROOT IS LAST in postorder
        int inRoot   = map.get(root.data);
        int leftSize = inRoot - is;

        root.left  = buildTreePostIn(inorder, is,         inRoot - 1,
                                     postorder, ps,        ps + leftSize - 1, map);
        root.right = buildTreePostIn(inorder, inRoot + 1,  ie,
                                     postorder, ps + leftSize, pe - 1,        map);
        return root;
    }
}
```

#### 🧪 Dry Run

```
postorder = [9, 15, 7, 20, 3]
inorder   = [9, 3, 15, 20, 7]
map       = {9:0, 3:1, 15:2, 20:3, 7:4}

Call: is=0, ie=4, ps=0, pe=4
  root = postorder[4] = 3        ← LAST element
  inRoot = map[3] = 1
  leftSize = 1 - 0 = 1

  Left call:  is=0, ie=0, ps=0, pe=0
    root = postorder[0] = 9 → leaf → return node(9)

  Right call: is=2, ie=4, ps=1, pe=3
    root = postorder[3] = 20     ← LAST of this subarray
    inRoot = map[20] = 3. leftSize = 3-2 = 1

    Left call:  is=2, ie=2, ps=1, pe=1
      root = postorder[1] = 15 → leaf → return node(15)

    Right call: is=4, ie=4, ps=2, pe=2
      root = postorder[2] = 7 → leaf → return node(7)

    return node(20) with left=15, right=7

Final tree:
      3
     / \
    9   20
        / \
       15   7  ✅
```

> 💡 **Why does postorder's last element give the root?**
> Postorder visits Left → Right → Root. So the last element visited is always the root of the current subtree. For the whole tree, it's the very last element of the array.

> 💡 **Why use a HashMap for inorder lookup?**
> Without it, finding `inRoot` in the inorder array takes O(N) per call → O(N²) total for N nodes. With a HashMap (element → index), each lookup is O(1) → O(N) total.

> 💡 **Preorder+Inorder vs Postorder+Inorder — why can't we use just two traversals of the same type?**
> Two preorder arrays or two inorder arrays don't uniquely identify a tree — there could be multiple trees with the same preorder OR same inorder. But combining inorder with preorder OR postorder uniquely determines the tree because inorder tells us the left-right split while pre/post tells us the root.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** | O(N) HashMap + O(H) recursion stack |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Maximum Path Sum | Each node is a potential turning point. Update global max as `left + right + node`. Return only `node + max(left, right)` to parent — path can't fork twice. Use `int[]` for pass-by-reference |
| BT from Preorder+Inorder | Preorder[first] = root. Find root in inorder → splits left/right. leftSize determines preorder boundaries. HashMap for O(1) inorder lookup |
| BT from Postorder+Inorder | Same as above but Postorder[last] = root. Adjust index formulas for postorder boundaries accordingly |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
