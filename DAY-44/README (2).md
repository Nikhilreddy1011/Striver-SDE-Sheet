# 🚀 Day 44/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Binary Tree to Doubly Linked List](#1-binary-tree-to-doubly-linked-list) | 🟡 Medium | Binary Tree / Inorder |
| 2 | [Find Median in a Stream](#2-find-median-in-a-stream) | 🟡 Medium | Heap / Two Heaps |
| 3 | [Kth Largest Element in a Stream](#3-kth-largest-element-in-a-stream) | 🔴 Hard | Heap / Min-Heap |

> 💡 **New Data Structure: Heap (Priority Queue)**
> Today introduced the Heap — a new topic alongside the miscellaneous binary tree problems.

---

## 1. Binary Tree to Doubly Linked List

### 🧩 Problem Statement

Convert a binary tree to a doubly linked list (DLL) **in-place**, following inorder traversal order. Left pointer = previous node, right pointer = next node.

**Example:**
```
Tree:       10
           /  \
          12   15
         / \   /
        25  30 36

Inorder: 25 → 12 → 30 → 10 → 36 → 15
DLL:     25 ↔ 12 ↔ 30 ↔ 10 ↔ 36 ↔ 15
```

**Constraints:** `0 <= nodes <= 10⁴`, no new nodes allowed

---

### 🟢 Approach — Inorder with prev pointer
**Time: O(N) | Space: O(H)**

#### 💡 Idea

Inorder traversal (L → Node → R) visits nodes in the exact order they should appear in the DLL. As we visit each node:
- If it's the first node (`prev == null`): it becomes the **head**
- Otherwise: link `prev.right = current` and `current.left = prev` (bidirectional connection)
- Then update `prev = current`

We use instance variables `prev` and `head` so the recursive calls share state without passing extra parameters.

#### 💻 Code
```java
class Solution {
    private TreeNode prev = null;
    private TreeNode head = null;

    private void inorder(TreeNode node) {
        if (node == null) return;
        inorder(node.left);
        if (prev == null) {
            head = node;               // first node = head of DLL
        } else {
            prev.right = node;         // prev's next = current
            node.left  = prev;         // current's prev = prev
        }
        prev = node;                   // advance prev
        inorder(node.right);
    }

    public TreeNode bToDLL(TreeNode root) {
        prev = null; head = null;
        inorder(root);
        return head;
    }
}
```

#### 🧪 Dry Run

**Tree: [10,12,15,25,30,36]**

```
Inorder visit order: 25, 12, 30, 10, 36, 15

Visit 25: prev=null → head=25. prev=25.
Visit 12: prev=25. 25.right=12, 12.left=25. prev=12.
Visit 30: prev=12. 12.right=30, 30.left=12. prev=30.
Visit 10: prev=30. 30.right=10, 10.left=30. prev=10.
Visit 36: prev=10. 10.right=36, 36.left=10. prev=36.
Visit 15: prev=36. 36.right=15, 15.left=36. prev=15.

DLL: 25 ↔ 12 ↔ 30 ↔ 10 ↔ 36 ↔ 15
head = 25 ✅
```

> 💡 **Why inorder and not preorder or postorder?**
> Inorder traversal of a BST gives sorted order. For a general binary tree, inorder visits left subtree → root → right subtree, which mirrors the natural DLL ordering that the problem requires. Pre/post would give a different connection sequence.

> 💡 **Why instance variables instead of parameters?**
> Recursive calls need to share and update `prev` and `head` across the entire traversal. Passing them as parameters would require arrays or wrappers (since Java passes by value for objects too when reassigning references). Instance variables are the cleanest solution here.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** — each node visited once | O(H) recursion stack |

---

## 2. Find Median in a Stream

### 🧩 Problem Statement

Given a stream of integers arriving one by one, return the median after each insertion.
- Odd count → middle element
- Even count → average of two middle elements

**Example:** `arr=[5,15,1,3,2,8]` → `[5.0, 10.0, 5.0, 4.0, 3.0, 4.0]`

**Constraints:** `1 <= arr.length <= 10⁵`, must return median after every insertion in O(log N)

---

### 🔴 Approach 1 — Brute (Sorted insertion + binary search)
**Time: O(N²) | Space: O(N)**

#### 💡 Idea
Maintain a sorted list. Use binary search to find insertion position (O(log N)), then insert (O(N) due to shifting). Access middle element(s) for median.

```java
class Solution {
    public List<Double> getMedian(int[] arr) {
        List<Integer> sortedList = new ArrayList<>();
        List<Double> medians = new ArrayList<>();
        for (int num : arr) {
            int pos = Collections.binarySearch(sortedList, num);
            if (pos < 0) pos = -(pos + 1);
            sortedList.add(pos, num);       // O(N) shifting
            int n = sortedList.size();
            if (n % 2 == 1) medians.add((double) sortedList.get(n / 2));
            else medians.add((sortedList.get(n/2-1) + sortedList.get(n/2)) / 2.0);
        }
        return medians;
    }
}
```

**Problem:** `ArrayList.add(pos, val)` shifts all elements to the right → O(N) per insertion → O(N²) total for N insertions.

---

### 🟢 Approach 2 — Optimal (Two Heaps)
**Time: O(N log N) | Space: O(N)**

#### 💡 Idea

**Core observation:** For the median, we only need the **middle elements** — not a fully sorted list. Split the stream into two halves:
- `left` (max-heap): holds the smaller half. Its top = largest of smaller half.
- `right` (min-heap): holds the larger half. Its top = smallest of larger half.

**Median:**
- Equal sizes → `(left.top + right.top) / 2.0`
- left has one more → `left.top`

**Rules to maintain:**
1. New element goes into `left` if `num ≤ left.top`, else into `right`
2. After insertion, rebalance so sizes differ by at most 1:
   - `left.size > right.size + 1` → move left's max to right
   - `right.size > left.size` → move right's min to left

#### 💻 Code
```java
class Solution {
    public List<Double> getMedian(int[] arr) {
        PriorityQueue<Integer> left  = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
        PriorityQueue<Integer> right = new PriorityQueue<>();                            // min-heap
        List<Double> medians = new ArrayList<>();

        for (int num : arr) {
            // Step 1: insert
            if (left.isEmpty() || num <= left.peek()) left.offer(num);
            else right.offer(num);

            // Step 2: balance
            if (left.size() > right.size() + 1)  right.offer(left.poll());
            else if (right.size() > left.size())  left.offer(right.poll());

            // Step 3: median
            if (left.size() == right.size())
                medians.add((left.peek() + right.peek()) / 2.0);
            else
                medians.add((double) left.peek());
        }
        return medians;
    }
}
```

#### 🧪 Dry Run

**arr=[5,15,1,3,2,8]**

```
Insert 5: left empty → left=[5], right=[]. |left|=1>|right|=0 but diff=1 ok. left>right → median=5.0 ✅

Insert 15: 15>left.top=5 → right=[15]. |left|=1=|right|=1. Equal → (5+15)/2=10.0 ✅

Insert 1: 1≤left.top=5 → left=[5,1]. |left|=2>|right|=1+1 → move left.top(5) to right. left=[1], right=[5,15].
  Wait — rebalance: left.size=2, right.size=1. 2>1+1? No (2=2). right.size>left.size? No. OK.
  left.size=2>right.size=1 → left bigger → median=left.top=5.0 ✅

Insert 3: 3≤left.top=5(after insert 1: left=[5,1], top=5). Wait, left is max-heap. After inserting 1: left=[5,1], top=5.
  3≤5 → left=[5,3,1], top=5. |left|=3>|right|=1+1=2 → right.offer(left.poll()=5). left=[3,1], right=[5,15].
  |left|=2=|right|=2 → median=(3+5)/2=4.0 ✅

Insert 2: 2≤left.top=3 → left=[3,2,1]. |left|=3>|right|=2+1=3? No. right.size=2>left.size=3? No.
  left.size=3>right.size=2 → median=left.top=3.0 ✅

Insert 8: 8>left.top=3 → right=[5,8,15]. |right|=3>|left|=3? No — right.size=3>left.size=3? No.
  Wait: right=[5,8,15] min-heap, top=5. right.size=3>left.size=3 → left.offer(right.poll()=5). left=[5,3,2,1], right=[8,15].
  |left|=4>|right|=2+1? 4>3 → right.offer(left.poll()=5). left=[3,2,1], right=[5,8,15].
  Actually: after inserting 8 into right: left.size=3, right.size=3. Equal → (left.top=3 + right.top=5)/2 = 4.0 ✅

Final output: [5.0, 10.0, 5.0, 4.0, 3.0, 4.0] ✅
```

> 💡 **Why max-heap for left and min-heap for right?**
> We always need the TOP of each heap for median computation. Left heap's maximum = the largest of the smaller half. Right heap's minimum = the smallest of the larger half. These two tops are exactly what we need for the median.

> 💡 **Why `right.size > left.size` triggers rebalancing to left?**
> We allow left to have at most 1 more element than right (for odd-count medians). Right having more than left means we can't take `left.peek()` as the sole median. We always want left ≥ right in size.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute (sorted list) | O(N²) — insert shifts | O(N) |
| Two Heaps | **O(N log N)** ✅ | O(N) |

---

## 3. Kth Largest Element in a Stream

### 🧩 Problem Statement

Implement `KthLargest(k, nums)` that maintains the kth largest element in a running stream. `add(val)` inserts a new element and returns the current kth largest.

**Example:** k=3, nums=[1,2,3,4]. add(5)→3, add(2)→3, add(7)→4

**Constraints:** stream always has ≥ k elements after any add call

---

### 🟢 Approach — Min-Heap of size k
**Time: O(N log K) init, O(log K) per add | Space: O(K)**

#### 💡 Idea

**Key insight:** The kth largest element = the minimum of the top-k elements. Maintain a min-heap of exactly size k containing the k largest elements seen so far. The heap's top (minimum) is always the kth largest.

**Why min-heap, not max-heap?**
- Max-heap would give the 1st largest at the top — not the kth
- Min-heap of size k stores the k largest values, and its minimum (top) is exactly the kth largest

**Three cases in `add(val)`:**
1. Heap size < k → just add, heap isn't full yet
2. Heap size == k and `val > heap.top` → new value displaces the old kth largest: remove minimum, add val
3. Heap size == k and `val ≤ heap.top` → val is NOT in top-k, ignore it. kth largest unchanged.

#### 💻 Code
```java
class KthLargest {
    private int K;
    private PriorityQueue<Integer> pq;  // min-heap

    public KthLargest(int k, int[] nums) {
        K = k;
        pq = new PriorityQueue<>();
        for (int num : nums) {
            if (pq.size() < K) pq.offer(num);
            else if (num > pq.peek()) { pq.poll(); pq.offer(num); }
        }
    }

    public int add(int val) {
        if (pq.size() < K) { pq.offer(val); }
        else if (val > pq.peek()) { pq.poll(); pq.offer(val); }
        return pq.peek();  // top of min-heap = kth largest
    }
}
```

#### 🧪 Dry Run

**k=3, nums=[1,2,3,4]**

```
Constructor:
  Add 1: size<3 → pq=[1]
  Add 2: size<3 → pq=[1,2]
  Add 3: size<3 → pq=[1,2,3]  (min-heap, top=1)
  Add 4: 4>pq.top=1 → remove 1, add 4. pq=[2,3,4], top=2

State: pq=[2,3,4], top=2. (3rd largest among [1,2,3,4] = 2 ✅)
```

**add(5):**
```
5>pq.top=2 → remove 2, add 5. pq=[3,4,5], top=3.
Return 3 ✅ (3rd largest of [1,2,3,4,5] = 3)
```

**add(2):**
```
2≤pq.top=3 → no change. pq=[3,4,5], top=3.
Return 3 ✅ (3rd largest of [1,2,2,3,4,5] = 3)
```

**add(7):**
```
7>pq.top=3 → remove 3, add 7. pq=[4,5,7], top=4.
Return 4 ✅ (3rd largest of [1,2,2,3,4,5,7] = 4)
```

> 💡 **Why does heap size staying at exactly k work?**
> We always keep the k largest elements. Any new element either replaces the current kth largest (if it's bigger) or doesn't change the top-k set (if it's smaller or equal). The minimum of this k-element set = kth largest of all seen elements.

> 💡 **How is this different from Find Median?**
> Median needs the exact middle — requires two heaps of balanced size. Kth largest only needs the kth element — a single min-heap of fixed size k is enough.

#### 📊 Complexity

| Time (init) | Time (add) | Space |
|------------|-----------|-------|
| O(N log K) | **O(log K)** | **O(K)** |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| BT to DLL | Inorder traversal visits in DLL order. Track `prev` node to stitch bidirectional links on the fly |
| Median in Stream | Two heaps: max-heap (smaller half) + min-heap (larger half). Balance sizes, read tops for median |
| Kth Largest Stream | Min-heap of size k. Top = kth largest. New element displaces min only if it's larger. O(log K) per add |

**Heap pattern:** Median needs two heaps (balanced). Kth largest needs one min-heap (fixed size k). Both avoid sorting.

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
