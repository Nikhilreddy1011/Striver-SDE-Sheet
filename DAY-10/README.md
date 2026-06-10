# 🚀 Day 10/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Remove Nth Node From End](#1-remove-nth-node-from-end) | 🟡 Medium | Linked List / Two Pointer |
| 2 | [Add Two Numbers in Linked List](#2-add-two-numbers-in-linked-list) | 🟡 Medium | Linked List / Math |
| 3 | [Delete Node in a Linked List O(1)](#3-delete-node-in-a-linked-list-o1) | 🟡 Medium | Linked List |

---

## 1. Remove Nth Node From End

### 🧩 Problem Statement

Given the head of a singly linked list and an integer `n`, remove the **nth node from the back** of the list and return the head of the modified list. `n` is always valid (≤ total nodes).

**Example 1:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5, n = 2
Output: 1 -> 2 -> 3 -> 5
Explanation: 2nd from end = node with value 4, removed.
```

**Example 2:**
```
Input:  5 -> 4 -> 3 -> 2 -> 1, n = 5
Output: 4 -> 3 -> 2 -> 1
Explanation: 5th from end = head node, removed.
```

**Constraints:**
- `1 <= nodes <= 10⁵`
- `1 <= n <= nodes`

---

### 🔴 Approach 1 — Brute Force (Count + Traverse)
**Time: O(N) + O(N) | Space: O(1)**

#### 💡 Intuition
Count total nodes in one pass. The nth node from the end is the `(count - n)`th node from the front. Traverse again to reach the node just before it and skip it.

**Edge case:** If `n == count`, we're removing the head — return `head.next`.

#### 📝 Steps
1. Count total nodes → `cnt`
2. If `cnt == n` → return `head.next`
3. Traverse to `(cnt - n)`th node
4. `temp.next = temp.next.next`
5. Return `head`

#### 💻 Code
```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return null;

        int cnt = 0;
        ListNode temp = head;

        // Pass 1: count total nodes
        while (temp != null) { cnt++; temp = temp.next; }

        // Edge case: remove head
        if (cnt == n) return head.next;

        int res = cnt - n;
        temp = head;

        // Pass 2: traverse to node just before target
        while (temp != null) {
            res--;
            if (res == 0) break;
            temp = temp.next;
        }

        temp.next = temp.next.next;
        return head;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, n=2

```
Pass 1: cnt = 5
cnt != n (5 != 2), proceed

res = 5 - 2 = 3
temp = head (node 1)

Pass 2:
node(1): res=2
node(2): res=1
node(3): res=0 → break

temp is at node(3)
temp.next = temp.next.next
→ node(3).next = node(5)  (skips node 4)

Result: 1 -> 2 -> 3 -> 5 ✅
```

**Edge case dry run:** `5 -> 4 -> 3 -> 2 -> 1`, n=5

```
cnt = 5, n = 5 → cnt == n
return head.next = node(4)

Result: 4 -> 3 -> 2 -> 1 ✅
```

---

### 🟢 Approach 2 — Optimal (Fast + Slow Pointers + Dummy Node)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Use two pointers both starting at a **dummy node** (placed before head). Move `fast` exactly `n+1` steps ahead. Then move both `fast` and `slow` one step at a time until `fast` reaches `null`. At this point, `slow` is exactly at the node **just before** the one to delete.

**Why `n+1` steps and not `n`?**
We want `slow` to stop at the node **before** the target, so we can do `slow.next = slow.next.next`. Moving `fast` one extra step ensures this gap is maintained correctly.

**Why a dummy node?**
Handles the edge case where the head itself needs to be removed — without dummy, we'd need special logic for it.

#### 📝 Steps
1. Create `dummy(-1)` pointing to `head`
2. `fast = dummy`, `slow = dummy`
3. Move `fast` forward `n+1` times
4. Move both until `fast == null`
5. `slow.next = slow.next.next`
6. Return `dummy.next`

#### 💻 Code
```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return null;

        ListNode dummy = new ListNode(-1, head);
        ListNode fast = dummy;
        ListNode slow = dummy;

        // Move fast n+1 steps ahead
        for (int i = 0; i <= n; i++)
            fast = fast.next;

        // Move both until fast reaches null
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }

        slow.next = slow.next.next;
        return dummy.next;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, n=2

```
dummy(-1) -> 1 -> 2 -> 3 -> 4 -> 5
fast = dummy, slow = dummy

Move fast n+1 = 3 steps:
  Step 0: fast = node(1)
  Step 1: fast = node(2)
  Step 2: fast = node(3)

State: slow=dummy, fast=node(3)

Move both until fast==null:
  fast=node(4), slow=node(1)
  fast=node(5), slow=node(2)
  fast=null,    slow=node(3)

slow is at node(3), which is just before node(4) (target)
slow.next = slow.next.next
→ node(3).next = node(5)

Return dummy.next = node(1)
Result: 1 -> 2 -> 3 -> 5 ✅
```

**Edge case:** `1 -> 2`, n=2 (remove head)

```
dummy(-1) -> 1 -> 2
fast = dummy, slow = dummy

Move fast 3 steps: fast = node(1) → node(2) → null
Wait: n+1 = 3 steps from dummy:
  Step 0: fast = node(1)
  Step 1: fast = node(2)
  Step 2: fast = null

While fast != null: loop doesn't execute (fast already null)
slow = dummy

slow.next = slow.next.next
→ dummy.next = node(2)  (skips node 1)

Return dummy.next = node(2) ✅
```

> 💡 **Why `i <= n` (not `i < n`) in the loop?**
> We need `n+1` total moves. `for (int i = 0; i <= n; i++)` runs `n+1` times — exactly right. Using `i < n` would only move `n` times, leaving `slow` one position too early.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Count + Traverse | O(2N) | O(1) |
| Fast + Slow Pointers | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Add Two Numbers in Linked List

### 🧩 Problem Statement

Two non-negative integers are represented as linked lists where digits are stored in **reverse order** (least significant digit first). Add the two numbers and return the sum as a linked list (also in reverse order).

**Example 1:**
```
Input:  L1 = [5, 4]  (represents 45)
        L2 = [4]     (represents 4)
Output: [9, 4]       (represents 49)
```

**Example 2:**
```
Input:  L1 = [4, 5, 6]  (represents 654)
        L2 = [1, 2, 3]  (represents 321)
Output: [5, 7, 9]       (represents 975)
```

**Constraints:**
- `1 <= nodes in each list <= 100`
- `0 <= node value <= 9`
- No leading zeros

---

### 🟢 Approach — Simulate Digit-by-Digit Addition
**Time: O(max(M, N)) | Space: O(max(M, N))**

#### 💡 Intuition
Since digits are stored in reverse order, we can directly process both lists from the start — that's already least significant digit first. Simulate the addition exactly as we do it by hand:
- Add corresponding digits from both lists
- Add the carry from the previous step
- Current digit = `sum % 10`
- New carry = `sum / 10`

Continue until both lists are exhausted **and** carry is 0. The carry check in the loop condition handles cases like `[5] + [5]` → `[0, 1]` (10), where a final carry creates an extra node.

#### 📝 Steps
1. Create `dummy(-1)`, `temp = dummy`, `carry = 0`
2. While `l1 != null` OR `l2 != null` OR `carry != 0`:
   - `sum = 0`
   - Add `l1.data` if `l1` exists, advance `l1`
   - Add `l2.data` if `l2` exists, advance `l2`
   - Add `carry`
   - `carry = sum / 10`
   - Create new node with `sum % 10`, attach to result
3. Return `dummy.next`

#### 💻 Code
```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode temp = dummy;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int sum = 0;

            if (l1 != null) { sum += l1.data; l1 = l1.next; }
            if (l2 != null) { sum += l2.data; l2 = l2.next; }

            sum += carry;
            carry = sum / 10;

            temp.next = new ListNode(sum % 10);
            temp = temp.next;
        }
        return dummy.next;
    }
}
```

#### 🧪 Dry Run — Example 1

Input: `L1 = [5, 4]` (45), `L2 = [4]` (4), expected: `[9, 4]` (49)

```
dummy(-1), carry=0

Iteration 1: l1=5, l2=4
  sum = 5 + 4 + 0 = 9
  carry = 9/10 = 0
  digit = 9%10 = 9
  result: dummy -> 9
  l1=node(4), l2=null

Iteration 2: l1=4, l2=null
  sum = 4 + 0 + 0 = 4
  carry = 0
  digit = 4
  result: dummy -> 9 -> 4
  l1=null, l2=null

l1==null, l2==null, carry==0 → exit loop
Return [9, 4] ✅
```

#### 🧪 Dry Run — Example 2

Input: `L1 = [4, 5, 6]` (654), `L2 = [1, 2, 3]` (321), expected: `[5, 7, 9]`

```
carry=0

Iter 1: sum = 4+1+0=5, carry=0, digit=5 | result: [5]
Iter 2: sum = 5+2+0=7, carry=0, digit=7 | result: [5,7]
Iter 3: sum = 6+3+0=9, carry=0, digit=9 | result: [5,7,9]

Return [5, 7, 9] ✅
```

#### 🧪 Dry Run — Carry Case

Input: `L1 = [9, 9]` (99), `L2 = [1]` (1), expected: `[0, 0, 1]` (100)

```
Iter 1: sum = 9+1+0=10, carry=1, digit=0 | result: [0]
Iter 2: sum = 9+0+1=10, carry=1, digit=0 | result: [0,0]
         l1=null, l2=null but carry=1
Iter 3: sum = 0+0+1=1,  carry=0, digit=1 | result: [0,0,1]

Return [0, 0, 1] ✅ (represents 100)
```

> 💡 **Why `carry != 0` in the loop condition?**
> After both lists are exhausted, there might still be a leftover carry (e.g., 99 + 1 = 100). Without this check, we'd miss the final digit.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(max(M, N)) ✅ | O(max(M, N)) for result list |

---

## 3. Delete Node in a Linked List O(1)

### 🧩 Problem Statement

Given only a reference to a node (not the head), delete that node from a singly linked list **in-place**. The node is guaranteed to not be the tail.

**Example 1:**
```
Input:  [4, 5, 1, 9], node = 5
Output: [4, 1, 9]
```

**Example 2:**
```
Input:  [1, 2, 3, 4], node = 3
Output: [1, 2, 4]
```

**Constraints:**
- `2 <= nodes <= 1000`
- Node to delete is never the tail
- At least two nodes in the list

---

### 🟢 Approach — Copy + Skip (The Only Way)
**Time: O(1) | Space: O(1)**

#### 💡 Intuition
Normally, to delete a node we need the **previous node** to redirect its `next` pointer. But here we only have access to the node itself — we can't go backwards in a singly linked list.

**The trick:** Instead of actually removing this node, **disguise it as the next node** and then delete the next node:
1. Copy the value of the next node into the current node
2. Skip the next node by `node.next = node.next.next`

This effectively makes the current node "disappear" from the perspective of the list's values, even though the physical node object that was originally "current" is still there — it now holds the next node's value.

**Why this works:** The list sees the same sequence of values as if the target node was removed.

#### 💻 Code
```java
class Solution {
    public void deleteNode(ListNode node) {
        node.val = node.next.val;       // copy next node's value here
        node.next = node.next.next;     // skip the next node
    }
}
```

#### 🧪 Dry Run

Input: `[4, 5, 1, 9]`, delete node with value `5`

```
Before:
4 -> 5 -> 1 -> 9
     ↑
   (given node)

Step 1: node.val = node.next.val
  → node's value becomes 1 (copy from next)
  List: 4 -> 1 -> 1 -> 9
             ↑
           (node, now has value 1)

Step 2: node.next = node.next.next
  → node now points to 9 (skips the duplicate 1)
  List: 4 -> 1 -> 9 ✅
```

**Example 2:** `[1, 2, 3, 4]`, delete node `3`

```
Step 1: node(3).val = 4 → List: 1 -> 2 -> 4 -> 4
Step 2: node.next = node.next.next = null → List: 1 -> 2 -> 4 ✅
```

> 💡 **Why can't we just set `node = null`?**
> In Java, setting the local variable `node = null` only affects the local reference — it doesn't change the actual list structure. The previous node still points to the old node. We must modify the node's contents in place.

> 💡 **Why is the node guaranteed to NOT be the tail?**
> If the node were the tail, `node.next` would be `null` and `node.next.val` would throw a NullPointerException. The problem guarantees this never happens.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(1)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Remove Nth From End | Fast pointer moves n+1 ahead so slow stops just before the target. Dummy node handles head deletion cleanly |
| Add Two Numbers | Digits are already in the right order (reverse = least significant first). Loop condition includes `carry != 0` to handle the final carry digit |
| Delete Node O(1) | No access to previous node — copy next node's value in, then skip next node. Two lines, O(1) |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
