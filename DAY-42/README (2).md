# 🚀 Day 42/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Floor in a BST](#1-floor-and-ceil-in-a-bst) | 🟢 Easy | BST |
| 2 | [Ceil in a BST](#1-floor-and-ceil-in-a-bst) | 🟢 Easy | BST |
| 3 | [Kth Smallest Element in BST](#2-kth-smallest-and-largest-element-in-bst) | 🟡 Medium | BST / Inorder |
| 4 | [Kth Largest Element in BST](#2-kth-smallest-and-largest-element-in-bst) | 🟡 Medium | BST / Inorder |

---

## 1. Floor and Ceil in a BST

### 🧩 Problem Statement

Given the root of a BST and a key, find:
- **Floor**: Largest node value ≤ key
- **Ceil**: Smallest node value ≥ key

If either doesn't exist, return `-1`.

**Example:**
```
BST:         8
            / \
           4   12
          / \ /  \
         2  6 10  14

key=11 → Floor=10, Ceil=12
key=15 → Floor=14, Ceil=-1
key=1  → Floor=-1, Ceil=2
```

**Constraints:** `1 <= nodes <= 5000`

---

### 🟢 Approach — Two Separate BST Traversals
**Time: O(H) | Space: O(1)**

#### 💡 Floor Logic

Use BST property to find the **greatest value ≤ key**:
- `current.data == key` → exact match, this IS the floor. Return it.
- `current.data < key` → current is a candidate floor (it's ≤ key). Update `floor = current.data`. But there might be a larger value closer to key in the right subtree → go right.
- `current.data > key` → current is too large, can't be floor. Go left to find smaller values.

#### 💡 Ceil Logic

Symmetric — find the **smallest value ≥ key**:
- `current.data == key` → exact match, this IS the ceil. Return it.
- `current.data > key` → current is a candidate ceil (it's ≥ key). Update `ceil = current.data`. But there might be a smaller value closer to key in the left subtree → go left.
- `current.data < key` → current is too small, can't be ceil. Go right to find larger values.

#### 💻 Code
```java
class Solution {
    public List<Integer> floorCeilOfBST(TreeNode root, int key) {
        int floor = -1, ceil = -1;

        // --- Floor ---
        TreeNode current = root;
        while (current != null) {
            if (current.data == key)    { floor = current.data; break; }
            else if (current.data < key){ floor = current.data; current = current.right; }
            else                        { current = current.left; }
        }

        // --- Ceil ---
        current = root;
        while (current != null) {
            if (current.data == key)    { ceil = current.data; break; }
            else if (current.data > key){ ceil = current.data; current = current.left; }
            else                        { current = current.right; }
        }

        return Arrays.asList(floor, ceil);
    }
}
```

#### 🧪 Dry Run — Floor (key=11)

```
BST: 8→4→2→6, 8→12→10→14

current=8:  8<11  → floor=8,  go right
current=12: 12>11 → go left
current=10: 10<11 → floor=10, go right
current=null → stop

Floor = 10 ✅
```

#### 🧪 Dry Run — Ceil (key=11)

```
current=8:  8<11  → go right
current=12: 12>11 → ceil=12, go left
current=10: 10<11 → go right
current=null → stop

Ceil = 12 ✅
```

**key=15 (no ceil):**
```
Floor: 8<15→go right, 12<15→floor=12→go right, 14<15→floor=14→go right, null→stop. Floor=14 ✅
Ceil:  8<15→right, 12<15→right, 14<15→right, null→stop. ceil stays -1 ✅
```

> 💡 **Why two separate passes instead of one?**
> Floor and ceil require opposite navigation decisions at each node. Trying to track both in one pass leads to branching confusion. Two clean O(H) passes is cleaner and still O(H) total.

> 💡 **Why update floor/ceil and keep searching instead of stopping?**
> When `current.data < key` for floor — current satisfies the floor condition (≤ key), but there might be a value closer to key in the right subtree that also satisfies it. We save the candidate and keep looking for a better one.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(H)** — two passes, each O(H) | **O(1)** |

---

## 2. Kth Smallest and Largest Element in BST

### 🧩 Problem Statement

Given a BST and integer `k`, return the kth smallest and kth largest values (1-indexed).

**Example 1:** BST=[3,1,4,null,2], k=1 → [1, 4]
**Example 2:** BST=[5,3,6,2,null,null,null,1], k=3 → [3, 3]

**Constraints:** `1 <= k <= n <= 10⁴`

---

### 🔑 Key Property of BST Inorder

**Inorder traversal of a BST = elements in sorted (ascending) order.**

```
BST:     3        Inorder: 1, 2, 3, 4
        / \
       1   4      kth smallest = inorder[k-1]
        \         kth largest  = inorder[n-k]
         2
```

This is why inorder beats other traversals for this problem — no sorting needed, elements come out sorted naturally.

---

### 🔴 Approach 1 — Brute (Inorder → List → Index)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Do inorder traversal, store all values in a list. Then:
- kth smallest = `list.get(k-1)` (0-indexed)
- kth largest = `list.get(n-k)` (since `n-k` from start = k from end)

**Why inorder and not pre/post?** Pre/post gives unsorted order — you'd need to sort (O(N log N) extra). Inorder gives sorted order for free in a BST. Sorting manually defeats the purpose of the BST structure.

```java
class Solution {
    private void inorder(TreeNode node, List<Integer> val) {
        if (node != null) {
            inorder(node.left, val);
            val.add(node.data);
            inorder(node.right, val);
        }
    }

    public List<Integer> kLargesSmall(TreeNode root, int k) {
        List<Integer> val = new ArrayList<>();
        inorder(root, val);
        int ksmallest = val.get(k - 1);
        int klargest  = val.get(val.size() - k);
        return Arrays.asList(ksmallest, klargest);
    }
}
```

**Dry Run — BST=[3,1,4,null,2], k=1:**
```
Inorder: [1, 2, 3, 4]
ksmallest = val.get(0) = 1
klargest  = val.get(4-1) = val.get(3) = 4
Return [1, 4] ✅
```

**Problem:** Stores ALL N nodes in a list. O(N) space. For kth element, we only need to visit k nodes — no need to store everything.

---

### 🟢 Approach 2 — Optimal (Counter during traversal)
**Time: O(N) worst case | Space: O(H)**

#### 💡 Idea

Instead of storing all values, maintain a **counter** during inorder traversal. When `counter == k`, we've found the kth smallest. Stop immediately — no need to visit the rest.

**Key insight:** In inorder (L→Node→R), nodes are visited in ascending order. The kth node visited = kth smallest.

**For kth largest:** Reverse inorder (R→Node→L) visits nodes in descending order. The kth node visited in reverse = kth largest.

**Equivalent insight:** kth largest = (n-k+1)th smallest. But reverse inorder is cleaner — no need to know n.

**Why `int[]` for k and result?** Instance variables (`this.k`, `this.result`) are used to share state across recursive calls without passing extra parameters. An alternative is `int[]` as before.

```java
class Solution {
    private int k, result;

    public List<Integer> kLargesSmall(TreeNode root, int k) {
        List<Integer> ans = new ArrayList<>();
        ans.add(kthSmallest(root, k));
        ans.add(kthLargest(root, k));
        return ans;
    }

    // Inorder: L → Node → R  (ascending order)
    public int kthSmallest(TreeNode root, int k) {
        this.k = k; this.result = -1;
        inorder(root);
        return result;
    }
    private void inorder(TreeNode node) {
        if (node == null) return;
        inorder(node.left);
        if (--k == 0) { result = node.data; return; }  // found kth!
        inorder(node.right);
    }

    // Reverse Inorder: R → Node → L  (descending order)
    public int kthLargest(TreeNode root, int k) {
        this.k = k; this.result = -1;
        reverseInorder(root);
        return result;
    }
    private void reverseInorder(TreeNode node) {
        if (node == null) return;
        reverseInorder(node.right);
        if (--k == 0) { result = node.data; return; }  // found kth largest!
        reverseInorder(node.left);
    }
}
```

#### 🧪 Dry Run — kth Smallest

**BST=[3,1,4,null,2], k=2**

```
Inorder visits: 1, 2, 3, 4

inorder(3):
  inorder(1):
    inorder(null): return
    k-- → k=1. 1≠0, continue.
    inorder(2):
      inorder(null): return
      k-- → k=0. result=2. return! ← STOP HERE
      
kthSmallest = 2 ✅ (2nd smallest in [1,2,3,4])
```

**BST=[3,1,4,null,2], k=1 (kth Largest)**

```
Reverse inorder visits: 4, 3, 2, 1

reverseInorder(3):
  reverseInorder(4):
    reverseInorder(null): return
    k-- → k=0. result=4. return! ← STOP
    
kthLargest = 4 ✅ (1st largest)
```

#### 🧪 Dry Run — kth Largest insight

**For kth largest = (n-k)th element in inorder list:**
```
BST=[5,3,6,2], k=2, n=4
Inorder: [2,3,5,6]
n-k = 4-2 = 2. list[2] = 5.

Reverse inorder: 6→5→3→2
Visit 1: 6, k-- → k=1. Not 0.
Visit 2: 5, k-- → k=0. result=5 ✅
```

> 💡 **Why `--k` (pre-decrement) not `k--` (post-decrement)?**
> `--k` decrements first, then checks. When k becomes 0 after decrement, we've visited exactly k nodes. `k--` would check the old value first — the logic would need `k-- == 1` instead, which is less natural.

> 💡 **Optimal space savings:**
> Brute stores O(N) elements. Optimal uses O(H) for the call stack only. For a balanced BST with 10⁴ nodes, that's ~14 stack frames vs 10⁴ stored integers.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute (inorder list) | O(N) | O(N) — stores all nodes |
| Optimal (counter) | O(k) average, O(N) worst | **O(H)** — only stack |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Floor in BST | If `curr < key`: curr is candidate floor, go right for better. If `curr > key`: too large, go left |
| Ceil in BST | If `curr > key`: curr is candidate ceil, go left for better. If `curr < key`: too small, go right |
| Kth Smallest | Inorder of BST = sorted ascending. Count nodes; stop at kth visit |
| Kth Largest | Reverse inorder (R→N→L) = sorted descending. Count nodes; stop at kth visit |

**The pattern:** BST inorder is sorted. Use a counter instead of a list to avoid O(N) space — stop as soon as count reaches k.

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
