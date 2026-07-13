# 🚀 Day 43/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Two Sum in BST](#1-two-sum-in-bst) | 🔴 Hard | BST / Two Pointer |
| 2 | [BST Iterator](#2-bst-iterator) | 🔴 Hard | BST / Stack |
| 3 | [Largest BST in Binary Tree](#3-largest-bst-in-binary-tree) | 🔴 Hard | BST / Postorder |
| 4 | [Serialize and Deserialize BT](#4-serialize-and-deserialize-binary-tree) | 🔴 Hard | BFS / Design |

---

## 1. Two Sum in BST

### 🧩 Problem Statement

Given the root of a BST and an integer `k`, return `true` if there exist two different nodes whose values sum to `k`.

**Example:**
```
BST: [5,3,6,2,4,null,7], k=9 → true  (3+6, 5+4, 2+7)
BST: [5,3,6,2,4,null,7], k=14 → false
```

---

### 🔴 Approach 1 — Brute (Inorder + Two Pointers)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
BST inorder = sorted array. Apply classic two-sum on sorted array using two pointers.
- `left + right < k` → move left pointer right (need larger sum)
- `left + right > k` → move right pointer left (need smaller sum)
- `left + right == k` → found!

```java
class Solution {
    public boolean twoSumBST(TreeNode root, int k) {
        List<Integer> sorted = inorderTraversal(root); // O(N) space
        int left = 0, right = sorted.size() - 1;
        while (left < right) {
            int sum = sorted.get(left) + sorted.get(right);
            if (sum == k)     return true;
            else if (sum < k) left++;
            else              right--;
        }
        return false;
    }
}
```

**Dry Run — k=9:**
```
Inorder: [2, 3, 4, 5, 6, 7]
L=0(2), R=5(7): 2+7=9 == k → true ✅
```

---

### 🟢 Approach 2 — Optimal (BSTIterator — no extra list)
**Time: O(N) | Space: O(H)**

#### 💡 Idea

Same two-pointer logic but **without materializing the sorted list**. Use two BSTIterators:
- Forward iterator `l` (inorder, ascending) — gives next smallest
- Reverse iterator `r` (reverse inorder, descending) — gives next largest

These simulate left and right pointers on the sorted sequence, but traverse the BST lazily using a stack — only O(H) space at any point.

**BSTIterator mechanics:**
- `pushAll(node)`: push node and all left children onto stack (forward), or all right children (reverse)
- `next()`: pop top, then push the right subtree's leftmost path (forward), or left subtree's rightmost path (reverse)

This gives elements in sorted order one at a time, on demand.

```java
class BSTIterator {
    private Stack<TreeNode> stack;
    private boolean reverse;

    public BSTIterator(TreeNode root, boolean isReverse) {
        stack = new Stack<>();
        reverse = isReverse;
        pushAll(root);
    }
    private void pushAll(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = reverse ? node.right : node.left; // go left (forward) or right (reverse)
        }
    }
    public boolean hasNext() { return !stack.isEmpty(); }
    public int next() {
        TreeNode node = stack.pop();
        if (!reverse) pushAll(node.right); // forward: explore right subtree
        else          pushAll(node.left);  // reverse: explore left subtree
        return node.data;
    }
}

class Solution {
    public boolean twoSumBST(TreeNode root, int k) {
        BSTIterator l = new BSTIterator(root, false); // ascending
        BSTIterator r = new BSTIterator(root, true);  // descending
        int i = l.next(), j = r.next();
        while (i < j) {
            if      (i + j == k) return true;
            else if (i + j < k)  i = l.next();
            else                  j = r.next();
        }
        return false;
    }
}
```

#### 🧪 Dry Run

**BST=[5,3,6,2,4,null,7], k=9**

```
Forward iterator l: pushAll(5) → stack=[5,3,2]. next()=2, push right of 2 (null) → stack=[5,3]
Reverse iterator r: pushAll(5) → stack=[5,6,7]. next()=7, push left of 7 (null) → stack=[5,6]

i=2, j=7: 2+7=9 == k → true ✅
```

**k=14:**
```
i=2, j=7: 9<14 → i=l.next()=3. stack=[5,4]
i=3, j=7: 10<14 → i=l.next()=4. stack=[5]
i=4, j=7: 11<14 → i=l.next()=5. stack=[]
i=5, j=7: 12<14 → i=l.next()=6. stack=[]  (5's right pushed)
i=6, j=7: 13<14 → i=l.next()=7. (i<j fails: 7 not < 7)
→ false ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute (inorder list) | O(N) | O(N) |
| BSTIterator | O(N) | **O(H)** |

---

## 2. BST Iterator

### 🧩 Problem Statement

Implement `BSTIterator` with:
- `next()`: returns next smallest element in BST
- `hasNext()`: returns true if more elements remain

**Example:**
```
BST: [7,3,15,null,null,9,20]
next()×5: 3, 7, 9, 15, 20  (inorder order)
hasNext() after last next(): false
```

---

### 🔴 Approach 1 — Brute (Store full inorder in list)
**Time: O(N) init, O(1) per call | Space: O(N)**

```java
class BSTIterator {
    private List<Integer> values;
    private int index;

    public BSTIterator(TreeNode root) {
        values = new ArrayList<>();
        inorderTraversal(root);
        index = -1;
    }
    public boolean hasNext() { return index + 1 < values.size(); }
    public int next()        { return values.get(++index); }
    private void inorderTraversal(TreeNode node) {
        if (node == null) return;
        inorderTraversal(node.left);
        values.add(node.data);
        inorderTraversal(node.right);
    }
}
```

**Problem:** Stores ALL N values upfront. For a tree with 10⁴ nodes, we allocate 10⁴ integers even if only a few `next()` calls are made.

---

### 🟢 Approach 2 — Optimal (Stack-based lazy traversal)
**Time: O(1) amortized per call | Space: O(H)**

#### 💡 Idea

Simulate inorder traversal lazily using a stack. The key insight:
- At any point, the stack contains the "leftmost path" from the current node
- `next()` pops the top (smallest remaining), then pushes the right subtree's leftmost path
- Each node is pushed and popped exactly once → O(1) amortized

**Why only push left children initially?**
Inorder traversal visits L→Node→R. The leftmost path represents all the nodes we'd visit before their parents. When we pop a node, we then explore its right subtree (same logic applies recursively).

```java
class BSTIterator {
    private Stack<TreeNode> stack = new Stack<>();

    public BSTIterator(TreeNode root) { pushAll(root); }

    public boolean hasNext() { return !stack.isEmpty(); }

    public int next() {
        TreeNode temp = stack.pop();     // this is the next smallest
        pushAll(temp.right);             // push right subtree's leftmost path
        return temp.data;
    }

    private void pushAll(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;            // always go left
        }
    }
}
```

#### 🧪 Dry Run

**BST: [7,3,15,null,null,9,20]**

```
Constructor: pushAll(7) → 7.left=3, 3.left=null. stack=[7,3]

next(): pop 3. pushAll(3.right=null). return 3. stack=[7]
next(): pop 7. pushAll(7.right=15) → 15.left=9, 9.left=null. stack=[15,9]. return 7.
next(): pop 9. pushAll(9.right=null). return 9. stack=[15]
next(): pop 15. pushAll(15.right=20) → 20.left=null. stack=[20]. return 15.
next(): pop 20. pushAll(null). return 20. stack=[]
hasNext(): stack empty → false

Output: 3, 7, 9, 15, 20 ✅ (sorted inorder)
```

> 💡 **Why O(1) amortized?** Each node is pushed exactly once (in pushAll) and popped exactly once (in next). N pushes + N pops across all calls = O(N) total / N calls = O(1) per call.

#### 📊 Complexity Summary

| Approach | Time (init) | Time (next/hasNext) | Space |
|---------|------------|---------------------|-------|
| Brute (list) | O(N) | O(1) | O(N) |
| Stack-based | O(H) | O(1) amortized | **O(H)** |

---

## 3. Largest BST in Binary Tree

### 🧩 Problem Statement

Given a binary tree (NOT necessarily a BST), find the size (number of nodes) of the largest subtree that is also a valid BST.

**Example 1:** [2,1,3] → 3 (whole tree is a BST)
**Example 2:** [10,5,15,1,8,null,7] → 3 (subtree 5-1-8 is BST, but 15-7 violates BST since 7 < 15)

---

### 🟢 Approach — Bottom-up Postorder with NodeValue struct
**Time: O(N) | Space: O(H)**

#### 💡 Idea

For each node, we need to know:
1. Is this subtree a valid BST?
2. If yes, how many nodes does it contain?
3. What's the min and max value in this subtree? (to verify parent's BST property)

Return all three as a `NodeValue(minNode, maxNode, maxSize)` from each recursive call.

**BST condition at a node:**
`left.maxNode < node.data < right.minNode`

**Base case (null):**
Return `(MAX_VALUE, MIN_VALUE, 0)` — a null node has:
- min = MAX_VALUE (so any parent's `left.maxNode < parent` will be true)
- max = MIN_VALUE (so any parent's `right.minNode > parent` will be true)

**When BST condition holds:**
- `minNode = min(node.data, left.minNode)`
- `maxNode = max(node.data, right.maxNode)`
- `maxSize = left.maxSize + right.maxSize + 1`

**When BST condition fails:**
- Return `(MIN_VALUE, MAX_VALUE, max(left.maxSize, right.maxSize))`
- The invalid sentinels (MIN_VALUE, MAX_VALUE) ensure any ancestor will fail BST check too

```java
class Solution {
    class NodeValue {
        int minNode, maxNode, maxSize;
        NodeValue(int min, int max, int size) { minNode=min; maxNode=max; maxSize=size; }
    }

    private NodeValue helper(TreeNode node) {
        if (node == null) return new NodeValue(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);

        NodeValue left  = helper(node.left);
        NodeValue right = helper(node.right);

        if (left.maxNode < node.data && node.data < right.minNode) {
            // This subtree IS a valid BST
            return new NodeValue(
                Math.min(node.data, left.minNode),
                Math.max(node.data, right.maxNode),
                left.maxSize + right.maxSize + 1
            );
        }
        // Not a BST — carry forward the largest BST found below
        return new NodeValue(Integer.MIN_VALUE, Integer.MAX_VALUE,
                             Math.max(left.maxSize, right.maxSize));
    }

    public int largestBST(TreeNode root) { return helper(root).maxSize; }
}
```

#### 🧪 Dry Run

**Tree: [10,5,15,1,8,null,7]**

```
         10
        /  \
       5    15
      / \     \
     1   8     7

helper(1): null,null → return (1,1,1)  [leaf, BST of size 1]
helper(8): null,null → return (8,8,1)  [leaf, BST of size 1]
helper(5): left=(1,1,1), right=(8,8,1)
  left.max=1 < 5 < right.min=8 ✓ → BST!
  return (min(5,1), max(5,8), 1+1+1) = (1, 8, 3)

helper(7): null,null → return (7,7,1)  [leaf]
helper(15): left=null → (MAX,MIN,0). right=(7,7,1)
  left.max=MIN_VALUE < 15, but 15 < right.min=7? 15 < 7 is FALSE ✗
  Not a BST. return (MIN_VALUE, MAX_VALUE, max(0,1)) = (MIN,MAX,1)

helper(10): left=(1,8,3), right=(MIN,MAX,1)
  left.max=8 < 10 ✓, but 10 < right.min=MIN_VALUE? 10 < MIN_VALUE is FALSE ✗
  Not a BST. return (MIN,MAX, max(3,1)) = (MIN,MAX,3)

largestBST = 3 ✅ (the subtree 5-1-8)
```

> 💡 **Why return `(MIN_VALUE, MAX_VALUE)` when not a BST?**
> If any ancestor checks `left.maxNode < ancestor.data`, `MIN_VALUE < ancestor` is always true — but we also return `MAX_VALUE` for max, so `ancestor.data < right.minNode` where `right.minNode = MAX_VALUE` would also pass. This would incorrectly make the ancestor appear valid. Wait — when we return invalid sentinels, the SIZE doesn't grow (it's just max of children). So even if sentinels pass parent checks, `maxSize` correctly reflects the largest BST found below, not a new (incorrect) count.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node processed once postorder | O(H) recursion stack |

---

## 4. Serialize and Deserialize Binary Tree

### 🧩 Problem Statement

Design an algorithm to convert a binary tree to a string (serialize) and reconstruct it back (deserialize). The encoding must be reversible — no information loss.

**Example:**
```
Tree: [1,2,3,null,null,4,5]
Serialize → "1,2,3,#,#,4,5,#,#,#,#,"
Deserialize → reconstructed tree [1,2,3,null,null,4,5]
```

---

### 🟢 Approach — BFS Level Order Serialization
**Time: O(N) | Space: O(N)**

#### 💡 Key Design Decisions

**Why store null nodes?** Without null markers, we can't distinguish `[1,2]` (2 is left child) from `[1,null,2]` (2 is right child). The `#` placeholder preserves structure.

**Why BFS (not DFS)?** BFS naturally mirrors how trees are stored (level by level), making reconstruction intuitive. During deserialization, each node in the queue expects exactly two children from the string.

#### 💻 Serialize
```java
public String serialize(TreeNode root) {
    if (root == null) return "";
    StringBuilder sb = new StringBuilder();
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        TreeNode cur = q.poll();
        if (cur == null) { sb.append("#,"); }
        else {
            sb.append(cur.data).append(",");
            q.offer(cur.left);   // enqueue even if null
            q.offer(cur.right);
        }
    }
    return sb.toString();
}
```

#### 💻 Deserialize
```java
public TreeNode deserialize(String data) {
    if (data.isEmpty()) return null;
    StringBuilder s = new StringBuilder(data);
    // Extract root
    int ci = s.indexOf(",");
    TreeNode root = new TreeNode(Integer.parseInt(s.substring(0, ci)));
    s.delete(0, ci + 1);

    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        TreeNode node = q.poll();
        // Read left child
        ci = s.indexOf(",");
        String str = s.substring(0, ci); s.delete(0, ci + 1);
        if (!str.equals("#")) { node.left = new TreeNode(Integer.parseInt(str)); q.offer(node.left); }
        // Read right child
        ci = s.indexOf(",");
        str = s.substring(0, ci); s.delete(0, ci + 1);
        if (!str.equals("#")) { node.right = new TreeNode(Integer.parseInt(str)); q.offer(node.right); }
    }
    return root;
}
```

#### 🧪 Dry Run

**Tree: [1,2,3], 2 has children 4,5. 3 has no children.**

```
Serialize:
Queue=[1]. Pop 1 → sb="1,". Offer 2, 3.
Queue=[2,3]. Pop 2 → sb="1,2,". Offer 4, 5.
Queue=[3,4,5]. Pop 3 → sb="1,2,3,". Offer null, null.
Queue=[4,5,null,null]. Pop 4 → sb="1,2,3,4,". Offer null, null.
Pop 5 → "1,2,3,4,5,". Offer null, null.
Pop null → "#,". × 4 times.
Result: "1,2,3,4,5,#,#,#,#,#,#,"

Deserialize "1,2,3,4,5,#,#,#,#,#,#,":
root=1. q=[1].
Pop 1: left=2, right=3. q=[2,3].
Pop 2: left=4, right=5. q=[3,4,5].
Pop 3: left=#(null), right=#(null). q=[4,5].
Pop 4: left=#, right=#. q=[5].
Pop 5: left=#, right=#. q=[].

Reconstructed: same tree ✅
```

> 💡 **Why do we enqueue null nodes during serialization?**
> When we later deserialize, each non-null node in the queue expects exactly 2 children from the string. By enqueueing nulls during serialization (but skipping them on dequeue since we check `curNode == null`), we ensure the `#` markers are placed at the right positions in the string. This makes deserialization straightforward — every non-null node's two children are the next two tokens in the string.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** both serialize and deserialize | O(N) — queue + string |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Two Sum BST | Inorder = sorted → two pointer on BST. Optimal: dual BSTIterator (forward + reverse) for O(H) space — no list needed |
| BST Iterator | Stack simulates inorder lazily. Push leftmost path; on next(), pop and push right subtree's leftmost path. O(1) amortized, O(H) space |
| Largest BST in BT | Postorder + NodeValue(min, max, size). BST condition: `left.max < node < right.min`. Invalid subtrees return sentinel values to propagate failure upward |
| Serialize/Deserialize | BFS level order with `#` for nulls. Deserialization mirrors serialization — queue-based, each node reads exactly 2 children from string |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
