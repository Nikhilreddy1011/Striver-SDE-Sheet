# 🚀 Day 12/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Check if Linked List is Palindrome](#1-check-if-linked-list-is-palindrome) | 🟡 Medium | Linked List / Two Pointer |
| 2 | [Find Starting Point of Loop in LL](#2-find-starting-point-of-loop-in-linked-list) | 🟡 Medium | Linked List / Floyd's Algorithm |
| 3 | [Flatten a Linked List](#3-flatten-a-linked-list) | 🟡 Medium | Linked List / Merge |

---

## 1. Check if Linked List is Palindrome

### 🧩 Problem Statement

Given the head of a singly linked list where each node represents a digit, check whether the values form a **palindrome**. Return `true` if palindrome, else `false`.

**Example 1:**
```
Input:  3 -> 7 -> 5 -> 7 -> 3
Output: true  (37573 is a palindrome)
```

**Example 2:**
```
Input:  1 -> 1 -> 2 -> 1
Output: false  (1121 is not a palindrome)
```

**Constraints:**
- `1 <= nodes <= 10⁵`
- `0 <= node.val <= 9`

---

### 🔴 Approach 1 — Brute Force (ArrayList + Two Pointers)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Collect all node values into an ArrayList. Use two pointers `l` and `h` from both ends — if any pair mismatches, it's not a palindrome.

#### 💻 Code
```java
class Solution {
    private boolean isPalindrome(List<Integer> li) {
        int l = 0, h = li.size() - 1;
        while (l < h) {
            if (li.get(l) != li.get(h)) return false;
            l++; h--;
        }
        return true;
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null) return true;
        List<Integer> li = new ArrayList<>();
        ListNode temp = head;
        while (temp != null) { li.add(temp.val); temp = temp.next; }
        return isPalindrome(li);
    }
}
```

#### 🧪 Dry Run

Input: `3 -> 7 -> 5 -> 7 -> 3`

```
Collect: li = [3, 7, 5, 7, 3]

l=0, h=4: li[0]=3, li[4]=3 → match, l=1, h=3
l=1, h=3: li[1]=7, li[3]=7 → match, l=2, h=2
l >= h → exit

Return true ✅
```

Input: `1 -> 1 -> 2 -> 1`

```
li = [1, 1, 2, 1]

l=0, h=3: li[0]=1, li[3]=1 → match
l=1, h=2: li[1]=1, li[2]=2 → mismatch → return false ✅
```

---

### 🟢 Approach 2 — Optimal (Find Mid + Reverse Second Half)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
A palindrome reads the same forwards and backwards. So if we reverse the second half of the list and compare it with the first half node by node, they should match completely.

**Three steps:**
1. **Find middle** using slow-fast pointers (using `fast.next != null && fast.next.next != null` to get the *first* middle for even-length lists)
2. **Reverse** the second half (from `mid.next` onwards)
3. **Compare** first half with reversed second half

**Why `fast.next != null && fast.next.next != null` (not the usual condition)?**
For even-length lists (e.g., `1->2->2->1`), this gives us the **first** middle (`2` at index 1), so we split correctly into `[1,2]` and `[2,1]`. The usual condition `fast != null && fast.next != null` gives the second middle, which splits unevenly.

#### 💻 Code
```java
class Solution {
    private ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode nextNode = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextNode;
        }
        return prev;
    }

    private ListNode findMid(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        ListNode mid = findMid(head);
        ListNode secondHalf = mid.next;
        mid.next = null;          // split into two halves

        ListNode rev = reverse(secondHalf);
        ListNode temp = head;

        while (rev != null) {
            if (temp.val != rev.val) return false;
            temp = temp.next;
            rev = rev.next;
        }
        return true;
    }
}
```

#### 🧪 Dry Run

Input: `3 -> 7 -> 5 -> 7 -> 3` (5 nodes)

**Step 1 — Find mid:**
```
slow=3, fast=3
Iter 1: slow=7, fast=5
Iter 2: slow=5, fast=3  (fast.next=null → stop)
mid = node(5)
```

**Step 2 — Split and reverse second half:**
```
secondHalf = node(7) -> node(3)
mid.next = null
First half:  3 -> 7 -> 5
Second half: 7 -> 3

Reverse second half: 3 -> 7
```

**Step 3 — Compare:**
```
temp=3, rev=3 → 3==3 ✓
temp=7, rev=7 → 7==7 ✓
rev=null → exit

Return true ✅
```

**Even length:** `1 -> 2 -> 2 -> 1`

```
findMid: slow=1→2, fast=1→2→? 
  fast.next=node(2), fast.next.next=node(1) ≠ null → slow=2, fast=1
  fast.next=null → stop
  mid = second node (first 2)

secondHalf = third node (second 2) -> 1
Reverse: 1 -> 2

Compare:
  first half: 1 -> 2 (head to mid)
  temp=1, rev=1 → match
  temp=2, rev=2 → match
Return true ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| ArrayList | O(N) | O(N) |
| Find Mid + Reverse | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Find Starting Point of Loop in Linked List

### 🧩 Problem Statement

Given the head of a singly linked list, find the **starting node of the loop** if one exists. Return `null` if no loop.

**Example 1:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5  (tail connects back to node at index 1)
Output: node with value 2
```

**Example 2:**
```
Input:  1 -> 3 -> 7 -> 4  (no loop)
Output: null
```

---

### 🔴 Approach 1 — Brute Force (HashSet)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Track every visited node in a HashSet. The first node we encounter that is already in the set is the loop starting point.

#### 💻 Code
```java
class Solution {
    public ListNode findStartingPoint(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        ListNode temp = head;
        while (temp != null) {
            if (set.contains(temp)) return temp;
            set.add(temp);
            temp = temp.next;
        }
        return null;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, tail→node(2)

```
set = {}

temp=node(1): not in set → add
temp=node(2): not in set → add
temp=node(3): not in set → add
temp=node(4): not in set → add
temp=node(5): not in set → add
temp=node(2): already in set! → return node(2) ✅
```

---

### 🟢 Approach 2 — Optimal (Floyd's + Mathematical Reset)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
**Phase 1:** Use Floyd's cycle detection. When slow and fast meet inside the cycle, a loop exists.

**Phase 2:** Reset `slow` to `head`. Move both `slow` and `fast` one step at a time. They will meet exactly at the **loop starting point**.

**Why does Phase 2 work? (Mathematical proof)**

Let:
- `L` = distance from head to loop start
- `C` = cycle length
- `d` = distance from loop start to meeting point (inside cycle)

When slow and fast meet:
- slow travelled: `L + d`
- fast travelled: `L + d + C` (one full extra loop)
- Since fast = 2 × slow: `L + d + C = 2(L + d)`
- Solving: `C - d = L`

So the distance from the meeting point back to the loop start (`C - d`) equals `L` (distance from head to loop start). Moving one pointer from head and one from meeting point at the same speed — they arrive at the loop start simultaneously.

#### 💻 Code
```java
class Solution {
    public ListNode findStartingPoint(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode fast = head, slow = head;

        // Phase 1: Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }

        if (slow != fast) return null; // no cycle

        // Phase 2: Find starting point
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, tail→node(2) (loop start = node(2))

**Phase 1 — Detect cycle:**
```
slow=1, fast=1

Step 1: slow=2, fast=3
Step 2: slow=3, fast=5
Step 3: slow=4, fast=3  (fast: 5→2→3 in cycle)
Step 4: slow=5, fast=5  → slow==fast, break!

Meeting point = node(5)
```

**Phase 2 — Find start:**
```
slow = head = node(1)
fast = node(5) (meeting point)

Step 1: slow=2, fast=2  → slow==fast → return node(2) ✅
```

**Verification using formula:**
```
L = 1 (head→node(2): 1 step)
C = 4 (cycle: 2→3→4→5→2)
d = 3 (node(2) → node(5): 3 steps)
C - d = 4 - 3 = 1 = L ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| HashSet | O(N) | O(N) |
| Floyd's + Reset | **O(N)** ✅ | **O(1)** ✅ |

---

## 3. Flatten a Linked List

### 🧩 Problem Statement

A special linked list has `n` head nodes connected by `next` pointers. Each head node also has a `child` pointer to a sorted child linked list. **Flatten** all nodes into a single sorted list connected by `child` pointers.

**Structure:**
```
head1 -> head2 -> head3 -> ...
  |          |        |
child1    child2   child3
  |          |
...        ...
```

**Example:**
```
Input:  5 -> 10 -> 19 -> 28
        |     |     |     |
        7    20    22    35
        |           |
        8          50
        |
       30
Output: 5->7->8->10->19->20->22->28->30->35->50
```

---

### 🔴 Approach 1 — Brute Force (Collect + Sort + Rebuild)
**Time: O(N×M × log(N×M)) | Space: O(N×M)**

#### 💡 Intuition
Traverse all head nodes via `next`, and for each, traverse its child list via `child`. Collect all values, sort them, and build a new child-linked list.

#### 💻 Code
```java
class Solution {
    private ListNode convertArrToLinkedList(List<Integer> arr) {
        ListNode dummyNode = new ListNode(-1);
        ListNode temp = dummyNode;
        for (int val : arr) {
            temp.child = new ListNode(val);
            temp = temp.child;
        }
        return dummyNode.child;
    }

    public ListNode flattenLinkedList(ListNode head) {
        List<Integer> arr = new ArrayList<>();
        while (head != null) {
            ListNode t2 = head;
            while (t2 != null) { arr.add(t2.val); t2 = t2.child; }
            head = head.next;
        }
        Collections.sort(arr);
        return convertArrToLinkedList(arr);
    }
}
```

#### 🧪 Dry Run

Input: Two head nodes: `5->7->8` and `10->20`

```
Traverse head1 (via child): [5, 7, 8]
Traverse head2 (via child): [10, 20]
arr = [5, 7, 8, 10, 20]
sort: [5, 7, 8, 10, 20]

Build child list: 5->7->8->10->20 ✅
```

---

### 🟢 Approach 2 — Optimal (Recursive Merge)
**Time: O(N×M) | Space: O(N) recursion stack**

#### 💡 Intuition
Each child list is **already sorted**. This is exactly the merge-sorted-lists problem — but extended to N lists instead of 2.

Recursively flatten `head.next` first (reducing N lists to N-1), then merge the current `head` with the already-flattened result. This is like applying merge sort's merge step repeatedly from right to left.

**Key insight:** The merge is done via `child` pointers (not `next`). The `next` pointer of each node is cleared during merge to keep the structure clean.

#### 📝 Steps
1. Base case: if `head == null` or `head.next == null` → return `head`
2. Recursively flatten `head.next` → `mergedHead`
3. `merge(head, mergedHead)` → returns the merged sorted child list
4. In `merge`: standard sorted merge but using `.child` instead of `.next`

#### 💻 Code
```java
class Solution {
    private ListNode merge(ListNode list1, ListNode list2) {
        ListNode dummyNode = new ListNode(-1);
        ListNode res = dummyNode;

        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                res.child = list1;
                res = list1;
                list1 = list1.child;
            } else {
                res.child = list2;
                res = list2;
                list2 = list2.child;
            }
            res.next = null; // clean up next pointer
        }

        if (list1 != null) res.child = list1;
        else res.child = list2;

        if (dummyNode.child != null) dummyNode.child.next = null;

        return dummyNode.child;
    }

    public ListNode flattenLinkedList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode mergedHead = flattenLinkedList(head.next);
        return merge(head, mergedHead);
    }
}
```

#### 🧪 Dry Run

Input:
```
head1(5) -> head2(10) -> head3(19)
   |            |            |
   7           20           22
   |
   8
```

**Recursion:**
```
flatten(head1):
  mergedHead = flatten(head2):
    mergedHead2 = flatten(head3) = 19->22 (base, head3.next=null)
    merge(head2[10->20], head3[19->22]):
      10<19 → pick 10, list1=20
      19<20 → pick 19, list2=22
      20<22 → pick 20, list1=null
      remaining: 22
    → 10->19->20->22
  merge(head1[5->7->8], [10->19->20->22]):
    5<10 → pick 5, list1=7
    7<10 → pick 7, list1=8
    8<10 → pick 8, list1=null
    remaining: 10->19->20->22
  → 5->7->8->10->19->20->22 ✅
```

> 💡 **Why `res.next = null` during merge?**
> Nodes in the original structure have `next` pointers pointing to the next head node. After merging, we only want `child` pointers in the result. Clearing `next` prevents stale references from corrupting the flattened structure.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Collect + Sort + Rebuild | O(N×M × log(N×M)) | O(N×M) |
| Recursive Merge | **O(N×M)** ✅ | O(N) stack |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Palindrome LL | Find mid with `fast.next!=null && fast.next.next!=null`, reverse second half, compare — O(1) space |
| Loop Starting Point | Floyd's Phase 2: reset one pointer to head, move both 1 step — they meet at loop start (mathematical proof: C-d = L) |
| Flatten LL | Recursive merge: flatten right side first, then merge current with result. Clear `next` pointers during merge |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
