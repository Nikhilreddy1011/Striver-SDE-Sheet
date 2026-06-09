# 🚀 Day 9/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Reverse a Linked List](#1-reverse-a-linked-list) | 🟡 Medium | Linked List |
| 2 | [Find Middle of Linked List](#2-find-middle-of-linked-list) | 🟢 Easy | Linked List / Two Pointer |
| 3 | [Merge Two Sorted Lists](#3-merge-two-sorted-lists) | 🔴 Hard | Linked List |

---

## 1. Reverse a Linked List

### 🧩 Problem Statement

Given the head of a singly linked list, **reverse the list** and return the head of the modified list.

**Example 1:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5
Output: 5 -> 4 -> 3 -> 2 -> 1
```

**Example 2:**
```
Input:  6 -> 8
Output: 8 -> 6
```

**Example 3:**
```
Input:  1
Output: 1  (single node, unchanged)
```

**Constraints:**
- `0 <= number of nodes <= 10⁵`
- `0 <= ListNode.val <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Stack)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Push all node values onto a stack. Then traverse the list again, popping values from the stack back into the nodes. Since a stack is LIFO, the values come out in reverse order. The node structure stays the same — only the values are overwritten.

#### 📝 Steps
1. Traverse the list and push every `node.val` onto a `Stack`
2. Traverse the list again, popping values from the stack and overwriting `node.val`
3. Return `head` (same structure, reversed values)

#### 💻 Code
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        Stack<Integer> st = new Stack<>();
        ListNode temp = head;

        // Push all values onto stack
        while (temp != null) {
            st.push(temp.val);
            temp = temp.next;
        }

        // Pop values back into the list
        temp = head;
        while (temp != null) {
            temp.val = st.pop();
            temp = temp.next;
        }
        return head;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`

**Pass 1 — Push to stack:**
```
temp=1 → push 1
temp=2 → push 2
temp=3 → push 3
temp=4 → push 4
temp=5 → push 5
Stack (top to bottom): [5, 4, 3, 2, 1]
```

**Pass 2 — Pop back into list:**
```
node(1): val = pop() = 5 → node now has val 5
node(2): val = pop() = 4 → node now has val 4
node(3): val = pop() = 3 → node now has val 3
node(4): val = pop() = 2 → node now has val 2
node(5): val = pop() = 1 → node now has val 1
```

Result: `5 -> 4 -> 3 -> 2 -> 1` ✅

#### ⚠️ Limitation
Uses O(N) extra space for the stack. The node connections are unchanged — we only swap values, not pointers. Can be improved.

---

### 🟡 Approach 2 — Recursive
**Time: O(N) | Space: O(N) recursion stack**

#### 💡 Intuition
Recursively go to the end of the list. On the way back (unwinding the call stack), reverse each link:
- Let `head.next` point back to `head`
- Set `head.next = null` to break the original forward link

The base case returns the last node, which becomes the new head — it bubbles up unchanged through all recursive calls.

#### 📝 Steps
1. Base case: if `head == null` or `head.next == null` → return `head`
2. Recurse: `newHead = reverseList(head.next)`
3. `head.next.next = head` (make next node point back)
4. `head.next = null` (break the original link)
5. Return `newHead`

#### 💻 Code
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode newHead = reverseList(head.next);

        ListNode nextNode = head.next;
        nextNode.next = head;  // reverse the link
        head.next = null;      // break the old link

        return newHead;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> null`

**Recursion going down:**
```
reverseList(1): calls reverseList(2)
  reverseList(2): calls reverseList(3)
    reverseList(3): head.next==null → return node(3) as newHead
```

**Unwinding (back up):**
```
At reverseList(2):
  newHead = node(3)
  nextNode = node(3)
  nextNode.next = node(2)   → 3 -> 2
  node(2).next = null       → 3 -> 2 -> null
  return node(3)

At reverseList(1):
  newHead = node(3)
  nextNode = node(2)
  nextNode.next = node(1)   → 2 -> 1
  node(1).next = null       → 2 -> 1 -> null
  return node(3)

Final: 3 -> 2 -> 1 -> null ✅
```

---

### 🟢 Approach 3 — Optimal (Iterative / Three Pointers)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Use three pointers: `prev`, `curr`, and `nextNode`. At each step, reverse the link from `curr` to `prev`, then advance all three pointers forward. When `curr` becomes `null`, `prev` is the new head.

#### 📝 Steps
1. `prev = null`, `curr = head`
2. While `curr != null`:
   - Save `nextNode = curr.next`
   - `curr.next = prev` (reverse the link)
   - `prev = curr` (advance prev)
   - `curr = nextNode` (advance curr)
3. Return `prev` (new head)

#### 💻 Code
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;

        while (curr != null) {
            ListNode nextNode = curr.next; // save next
            curr.next = prev;              // reverse link
            prev = curr;                   // move prev forward
            curr = nextNode;               // move curr forward
        }
        return prev;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`

```
Initial: prev=null, curr=1

Step 1: nextNode=2, curr(1).next=null, prev=1, curr=2
  State: null <- 1    2 -> 3 -> 4 -> 5

Step 2: nextNode=3, curr(2).next=1,   prev=2, curr=3
  State: null <- 1 <- 2    3 -> 4 -> 5

Step 3: nextNode=4, curr(3).next=2,   prev=3, curr=4
  State: null <- 1 <- 2 <- 3    4 -> 5

Step 4: nextNode=5, curr(4).next=3,   prev=4, curr=5
  State: null <- 1 <- 2 <- 3 <- 4    5

Step 5: nextNode=null, curr(5).next=4, prev=5, curr=null
  State: null <- 1 <- 2 <- 3 <- 4 <- 5

curr==null → stop. Return prev = node(5) ✅

Result: 5 -> 4 -> 3 -> 2 -> 1 -> null ✅
```

> 💡 **Why save `nextNode` first?**
> When we do `curr.next = prev`, we lose the reference to the rest of the list. Saving `nextNode = curr.next` before the reversal ensures we can still move forward.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Stack (swap values) | O(N) | O(N) |
| Recursive | O(N) | O(N) stack |
| Iterative (3 pointers) | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Find Middle of Linked List

### 🧩 Problem Statement

Given the head of a singly linked list, return the **middle node**. If there are two middle nodes (even length), return the **second middle**.

**Example 1:**
```
Input:  3 -> 8 -> 7 -> 1 -> 3
Output: node with value 7
Explanation: 5 nodes → middle is 3rd node
```

**Example 2:**
```
Input:  2 -> 9 -> 1 -> 4 -> 0 -> 4
Output: node with value 4
Explanation: 6 nodes → both 3rd and 4th are middles → return 2nd middle (4th node)
```

**Constraints:**
- `1 <= number of nodes <= 10⁵`
- `-10⁴ <= ListNode.val <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Count + Traverse)
**Time: O(N) + O(N/2) = O(N) | Space: O(1)**

#### 💡 Intuition
Count total nodes in one pass. The middle index is `count/2 + 1` (1-indexed). Traverse again to reach that index.

#### 📝 Steps
1. Traverse the list to count total nodes
2. Compute `mid = count/2 + 1`
3. Traverse again until reaching the `mid`-th node
4. Return that node

#### 💻 Code
```java
class Solution {
    public ListNode middleOfLinkedList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode temp = head;
        int count = 0;

        // Count nodes
        while (temp != null) {
            temp = temp.next;
            count++;
        }

        int mid = count / 2 + 1;
        temp = head;

        // Traverse to middle
        while (temp != null) {
            mid--;
            if (mid == 0) break;
            temp = temp.next;
        }
        return temp;
    }
}
```

#### 🧪 Dry Run

Input: `2 -> 9 -> 1 -> 4 -> 0 -> 4` (6 nodes)

```
Pass 1: count = 6
mid = 6/2 + 1 = 4

Pass 2:
node(2): mid=3
node(9): mid=2
node(1): mid=1
node(4): mid=0 → break

Return node(4) ✅ (value = 4, the 4th node = second middle)
```

---

### 🟢 Approach 2 — Optimal (Slow and Fast Pointers)
**Time: O(N/2) = O(N) | Space: O(1)**

#### 💡 Intuition
Use the **tortoise and hare** technique. Move `slow` one step at a time and `fast` two steps at a time. When `fast` reaches the end, `slow` is exactly at the middle.

**Why does this work?**
- `fast` covers twice the distance of `slow`
- When `fast` has traversed N nodes, `slow` has traversed N/2 nodes
- N/2 nodes from the start = the middle

For even-length lists, the loop condition `fast != null && fast.next != null` stops when `fast` is at the last node (odd) or null (even), leaving `slow` at the **second middle** — which is exactly what the problem asks for.

#### 💻 Code
```java
class Solution {
    public ListNode middleOfLinkedList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next; // move 2 steps
            slow = slow.next;      // move 1 step
        }
        return slow;
    }
}
```

#### 🧪 Dry Run — Odd Length

Input: `3 -> 8 -> 7 -> 1 -> 3` (5 nodes)

```
Initial: slow=3, fast=3

Step 1: fast=8.next=7,  slow=8
Step 2: fast=1.next=3,  slow=7
Step 3: fast=3.next=null → fast.next==null → STOP

Return slow = node(7) ✅
```

#### 🧪 Dry Run — Even Length

Input: `2 -> 9 -> 1 -> 4 -> 0 -> 4` (6 nodes)

```
Initial: slow=2, fast=2

Step 1: fast=9.next=1,  slow=9
Step 2: fast=4.next=0,  slow=1
Step 3: fast=4.next=null → fast.next==null → STOP

Return slow = node(4) ✅  (4th node = second middle)
```

> 💡 **Why `fast.next != null` in the loop condition?**
> When `fast.next == null`, `fast` is at the last node. If we tried `fast.next.next`, we'd get a NullPointerException. So we stop before that. For even lists, this leaves `fast` one step before the end, and `slow` at the second middle.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Count + Traverse | O(N) + O(N/2) | O(1) |
| Slow-Fast Pointers | **O(N/2)** ✅ | **O(1)** ✅ |

---

## 3. Merge Two Sorted Lists

### 🧩 Problem Statement

Given the heads of two sorted linked lists `list1` and `list2`, merge them into a **single sorted linked list** and return its head. Nodes from both lists must be used — no new values.

**Example 1:**
```
Input:  list1: 2 -> 4 -> 7 -> 9
        list2: 1 -> 2 -> 5 -> 6
Output: 1 -> 2 -> 2 -> 4 -> 5 -> 6 -> 7 -> 9
```

**Example 2:**
```
Input:  list1: 1 -> 2 -> 3 -> 4
        list2: 5 -> 6 -> 10
Output: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 10
```

**Constraints:**
- `0 <= nodes in list1, list2 <= 5 × 10⁴`
- `-10⁴ <= ListNode.val <= 10⁴`
- Both lists sorted in non-decreasing order

---

### 🔴 Approach 1 — Brute Force (Collect + Sort + Rebuild)
**Time: O((M+N) log(M+N)) | Space: O(M+N)**

#### 💡 Intuition
Collect all values from both lists into an ArrayList. Sort it. Then create a new linked list from the sorted values using a dummy node.

#### 📝 Steps
1. Traverse `list1` and `list2`, adding all values to `li`
2. `Collections.sort(li)`
3. Create a new linked list by iterating through `li` with a dummy head node
4. Return `dummyNode.next`

#### 💻 Code
```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        List<Integer> li = new ArrayList<>();
        ListNode temp = list1;

        while (temp != null) { li.add(temp.val); temp = temp.next; }
        temp = list2;
        while (temp != null) { li.add(temp.val); temp = temp.next; }

        Collections.sort(li);

        ListNode dummyNode = new ListNode(-1);
        temp = dummyNode;
        for (int ele : li) {
            temp.next = new ListNode(ele);
            temp = temp.next;
        }
        return dummyNode.next;
    }
}
```

#### 🧪 Dry Run

Input: `list1: 2 -> 4`, `list2: 1 -> 3`

```
Collect: li = [2, 4, 1, 3]
Sort:    li = [1, 2, 3, 4]

Build new list:
dummyNode(-1) -> 1 -> 2 -> 3 -> 4

Return node(1) ✅
```

#### ⚠️ Limitation
Creates new nodes — wastes memory. The sorting step ignores the fact that both lists are **already sorted**. We can merge them in O(M+N) without sorting.

---

### 🟢 Approach 2 — Optimal (Two Pointers + Dummy Node)
**Time: O(M+N) | Space: O(1)**

#### 💡 Intuition
Since both lists are already sorted, use two pointers `temp1` (on list1) and `temp2` (on list2). At each step, compare the current nodes and attach the smaller one to the result list. Advance that pointer. When one list is exhausted, attach the rest of the other.

A **dummy node** simplifies edge case handling — it gives us a clean starting point so we never need to check if the result list is empty.

#### 📝 Steps
1. Create `dummyNode(-1)`, `temp = dummyNode`, `temp1 = list1`, `temp2 = list2`
2. While both `temp1` and `temp2` are not null:
   - If `temp1.val <= temp2.val` → attach `temp1`, advance `temp1`
   - Else → attach `temp2`, advance `temp2`
   - Advance `temp`
3. Attach remaining nodes from whichever list isn't exhausted
4. Return `dummyNode.next`

#### 💻 Code
```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummyNode = new ListNode(-1);
        ListNode temp = dummyNode, temp1 = list1, temp2 = list2;

        while (temp1 != null && temp2 != null) {
            if (temp1.val <= temp2.val) {
                temp.next = temp1;
                temp1 = temp1.next;
            } else {
                temp.next = temp2;
                temp2 = temp2.next;
            }
            temp = temp.next;
        }

        // Attach remaining nodes
        while (temp1 != null) { temp.next = temp1; temp1 = temp1.next; temp = temp.next; }
        while (temp2 != null) { temp.next = temp2; temp2 = temp2.next; temp = temp.next; }

        return dummyNode.next;
    }
}
```

#### 🧪 Dry Run

Input: `list1: 2 -> 4 -> 7 -> 9`, `list2: 1 -> 2 -> 5 -> 6`

```
dummy(-1), temp=dummy, temp1=2, temp2=1

Step 1: temp1=2 > temp2=1 → attach 1, temp2=2, temp=1
  Result so far: dummy -> 1

Step 2: temp1=2 <= temp2=2 → attach 2(list1), temp1=4, temp=2
  Result so far: dummy -> 1 -> 2(L1)

Step 3: temp1=4 > temp2=2 → attach 2(list2), temp2=5, temp=2(L2)
  Result so far: dummy -> 1 -> 2(L1) -> 2(L2)

Step 4: temp1=4 <= temp2=5 → attach 4, temp1=7, temp=4
  Result so far: dummy -> 1 -> 2 -> 2 -> 4

Step 5: temp1=7 > temp2=5 → attach 5, temp2=6, temp=5
  Result so far: dummy -> 1 -> 2 -> 2 -> 4 -> 5

Step 6: temp1=7 > temp2=6 → attach 6, temp2=null, temp=6
  Result so far: dummy -> 1 -> 2 -> 2 -> 4 -> 5 -> 6

temp2==null → exit main while loop

Remaining temp1: 7 -> 9
Attach: temp.next=7, advance, temp.next=9
  Result: dummy -> 1 -> 2 -> 2 -> 4 -> 5 -> 6 -> 7 -> 9

Return dummyNode.next = node(1) ✅
```

> 💡 **Why a dummy node?**
> Without it, we'd need a special case to handle the very first node — we can't do `temp.next = ...` if `temp` doesn't exist yet. The dummy node acts as a fixed anchor. We always return `dummyNode.next` to skip it.

> 💡 **Why attach remaining nodes with a loop instead of just `temp.next = temp1`?**
> If we just do `temp.next = temp1`, it works too — because the remaining nodes are already linked. The loop just makes the logic explicit and consistent.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Collect + Sort + Rebuild | O((M+N) log(M+N)) | O(M+N) |
| Two Pointers + Dummy Node | **O(M+N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Reverse Linked List | Three pointer iterative (prev, curr, next) reverses in-place O(1) space. Save next before reversing the link |
| Middle of Linked List | Slow-fast pointers: fast moves 2x, when fast reaches end slow is at middle. `fast.next != null` handles even length correctly |
| Merge Two Sorted Lists | Dummy node + two pointers: compare heads, attach smaller, advance that pointer. No edge case handling needed for empty list |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
