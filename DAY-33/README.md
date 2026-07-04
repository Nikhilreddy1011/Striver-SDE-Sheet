# 🚀 Day 33/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Inorder Traversal](#1-inorder-traversal) | 🟢 Easy | Binary Tree |
| 2 | [Preorder Traversal](#2-preorder-traversal) | 🟢 Easy | Binary Tree |
| 3 | [Postorder Traversal](#3-postorder-traversal) | 🟢 Easy | Binary Tree |
| 4 | [Morris Inorder Traversal](#4-morris-inorder-traversal) | 🔴 Hard | Binary Tree / O(1) Space |

---

## 1. Inorder Traversal

### 🧩 Problem Statement

Given the root of a binary tree, return the **inorder traversal** (Left → Root → Right).

**Example 1:**
```
Input:  root = [1,4,null,4,2]
Output: [4,4,2,1]
```

**Example 2:**
```
Input:  root = [1,null,2,3]
Output: [1,3,2]
```

---

### 🟡 Approach 1 — Recursive
**Time: O(N) | Space: O(H) recursion stack**

#### 💡 Idea
Follow the standard recursion: go left, visit root, go right.

```java
class Solution {
    private void recursiveInorder(TreeNode root, List<Integer> arr) {
        if (root == null) return;
        recursiveInorder(root.left, arr);
        arr.add(root.data);
        recursiveInorder(root.right, arr);
    }
    public List<Integer> inorder(TreeNode root) {
        List<Integer> arr = new ArrayList<>();
        recursiveInorder(root, arr);
        return arr;
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

Call stack:
inorder(1) → inorder(2) → inorder(4)
  inorder(null) → return
  add 4
  inorder(null) → return
add 2
inorder(5)
  add 5
add 1 → inorder(3) → add 3

Result: [4, 2, 5, 1, 3] ✅
```

---

### 🟢 Approach 2 — Iterative (Stack)
**Time: O(N) | Space: O(H)**

#### 💡 Idea
Use a stack to simulate the recursion. Push nodes going left; when null, pop and visit, then go right.

```java
class Solution {
    public List<Integer> inorder(TreeNode root) {
        Stack<TreeNode> st = new Stack<>();
        TreeNode node = root;
        List<Integer> inorder = new ArrayList<>();
        while (true) {
            if (node != null) {
                st.push(node);
                node = node.left;
            } else {
                if (st.isEmpty()) break;
                node = st.pop();
                inorder.add(node.data);
                node = node.right;
            }
        }
        return inorder;
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3

node=1: push 1, go left
node=2: push 2, go left
node=null: pop 2, add 2, go right (null)
node=null: pop 1, add 1, go right
node=3: push 3, go left (null)
node=null: pop 3, add 3, go right (null)
stack empty → break

Result: [2, 1, 3] ✅
```

#### 📊 Complexity

| Approach | Time | Space |
|---------|------|-------|
| Recursive | O(N) | O(H) stack |
| Iterative | O(N) | O(H) stack |

---

## 2. Preorder Traversal

### 🧩 Problem Statement

Given root, return **preorder traversal** (Root → Left → Right).

---

### 🟡 Approach 1 — Recursive
**Time: O(N) | Space: O(H)**

```java
class Solution {
    public List<Integer> preorder(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        func(root, ans);
        return ans;
    }
    private void func(TreeNode node, List<Integer> ans) {
        if (node == null) return;
        ans.add(node.data);
        func(node.left, ans);
        func(node.right, ans);
    }
}
```

---

### 🟢 Approach 2 — Iterative (Stack)
**Time: O(N) | Space: O(H)**

#### 💡 Idea
Push root, then for each popped node: add its value, push **right first** then left (LIFO ensures left is processed first).

```java
class Solution {
    public List<Integer> preorder(TreeNode root) {
        List<Integer> preorder = new ArrayList<>();
        if (root == null) return preorder;
        Stack<TreeNode> st = new Stack<>();
        st.push(root);
        while (!st.empty()) {
            root = st.pop();
            preorder.add(root.data);
            if (root.right != null) st.push(root.right);
            if (root.left != null) st.push(root.left);
        }
        return preorder;
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3

stack=[1]

pop 1, add 1, push right(3), push left(2). stack=[3,2]
pop 2, add 2, push right(null skip), push left(null skip). stack=[3]
pop 3, add 3. stack=[]

Result: [1, 2, 3] ✅
```

> 💡 **Why push right before left?**
> Stack is LIFO. Pushing right first means left is on top — so left gets popped and processed first, maintaining Root→Left→Right order.

#### 📊 Complexity

| Approach | Time | Space |
|---------|------|-------|
| Recursive | O(N) | O(H) |
| Iterative | O(N) | O(H) |

---

## 3. Postorder Traversal

### 🧩 Problem Statement

Given root, return **postorder traversal** (Left → Right → Root).

---

### 🟡 Approach 1 — Recursive
**Time: O(N) | Space: O(H)**

```java
class Solution {
    private void recursivePostorder(TreeNode root, List<Integer> arr) {
        if (root == null) return;
        recursivePostorder(root.left, arr);
        recursivePostorder(root.right, arr);
        arr.add(root.data);
    }
    public List<Integer> postorder(TreeNode root) {
        List<Integer> arr = new ArrayList<>();
        recursivePostorder(root, arr);
        return arr;
    }
}
```

---

### 🟢 Approach 2 — Iterative (Modified Preorder + Reverse)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Postorder = reverse of (Root → Right → Left). Run a modified preorder that processes Root → Right → Left (push left before right), then reverse the result.

```java
class Solution {
    public List<Integer> postorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> nodeStack = new Stack<>();
        if (root != null) nodeStack.push(root);
        while (!nodeStack.isEmpty()) {
            TreeNode node = nodeStack.pop();
            result.add(node.data);
            if (node.left != null) nodeStack.push(node.left);
            if (node.right != null) nodeStack.push(node.right);
        }
        Collections.reverse(result);
        return result;
    }
}
```

#### 🧪 Dry Run

```
Tree:     1
         / \
        2   3

stack=[1]

pop 1, add 1, push left(2), push right(3). stack=[2,3]. result=[1]
pop 3, add 3, no children. stack=[2]. result=[1,3]
pop 2, add 2. stack=[]. result=[1,3,2]

After reverse: [2,3,1] ✅ (postorder: Left→Right→Root)
```

> 💡 **Why push left before right here (opposite of preorder)?**
> We want Root→Right→Left order before reversing. Since stack is LIFO, we push left first so right comes out first — giving Root→Right→Left. Reversing gives Left→Right→Root = postorder.

#### 📊 Complexity

| Approach | Time | Space |
|---------|------|-------|
| Recursive | O(N) | O(H) |
| Iterative | O(N) | O(N) |

---

## 4. Morris Inorder Traversal

### 🧩 Problem Statement

Perform inorder traversal using **O(1) extra space** — no recursion, no stack.

---

### 🟢 Approach — Morris Traversal (Thread-based)
**Time: O(2N) | Space: O(1)**

#### 💡 Idea
The key challenge: without a stack or recursion, how do we go back to the parent after visiting the left subtree?

**The trick:** Create **temporary threads** (links) from the **inorder predecessor** back to the current node. These threads let us return to the current node after finishing the left subtree, without any extra data structure.

**Inorder predecessor:** The rightmost node of the left subtree (the node that comes just before `cur` in inorder sequence).

**Two cases at each node `cur`:**

**Case 1 — No left child:**
→ Visit `cur`, move to `cur.right` (normal or via a thread set earlier)

**Case 2 — Has left child:**
- Find inorder predecessor `prev` (rightmost of left subtree)
- If `prev.right == null`: set thread `prev.right = cur`, move to `cur.left` (go deeper left)
- If `prev.right == cur`: thread already set → this is the return visit. Remove thread (`prev.right = null`), visit `cur`, move to `cur.right`

#### 💻 Code
```java
class Solution {
    public List<Integer> getInorder(TreeNode root) {
        List<Integer> inorder = new ArrayList<>();
        TreeNode cur = root;

        while (cur != null) {
            if (cur.left == null) {
                inorder.add(cur.data);
                cur = cur.right;
            } else {
                TreeNode prev = cur.left;
                while (prev.right != null && prev.right != cur) {
                    prev = prev.right;
                }
                if (prev.right == null) {
                    prev.right = cur;
                    cur = cur.left;
                } else {
                    prev.right = null;
                    inorder.add(cur.data);
                    cur = cur.right;
                }
            }
        }
        return inorder;
    }
}
```

#### 🧪 Dry Run

```
Tree:     2
         / \
        1   3

cur=2: has left child. Find pred of 2 in left subtree:
  prev=1, prev.right=null → set thread: 1.right=2, cur=1

cur=1: no left child → add 1, cur=cur.right=2 (via thread)

cur=2: has left child. Find pred again:
  prev=1, prev.right=2==cur → thread exists → remove: 1.right=null
  add 2, cur=cur.right=3

cur=3: no left child → add 3, cur=null

Result: [1, 2, 3] ✅ (correct inorder)
```

**Why `prev.right != cur` in the while condition?**
```
while (prev.right != null && prev.right != cur)
```
The second condition stops us from going into an infinite loop. If `prev.right == cur`, the thread was already set — we stop and handle the return case.

**Why O(1) space?**
No stack, no recursion. Threads use existing null right pointers in the tree temporarily — and are always restored before the algorithm ends.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(2N) — each node visited at most twice ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Inorder | Left→Root→Right. Iterative: push left going down, pop and visit on null, then go right |
| Preorder | Root→Left→Right. Iterative: push right then left (LIFO reverses the order) |
| Postorder | Left→Right→Root. Iterative: reverse of Root→Right→Left = push left then right, reverse result |
| Morris Inorder | Thread the inorder predecessor back to current node. O(1) space by temporarily using null right pointers |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
