# 🚀 Day 11/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Find Intersection of Two Linked Lists](#1-find-intersection-of-two-linked-lists) | 🟡 Medium | Linked List / Two Pointer |
| 2 | [Detect a Loop in Linked List](#2-detect-a-loop-in-linked-list) | 🟡 Medium | Linked List / Floyd's Algorithm |
| 3 | [Reverse Linked List in Groups of K](#3-reverse-linked-list-in-groups-of-k) | 🔴 Hard | Linked List |

---

## 1. Find Intersection of Two Linked Lists

### 🧩 Problem Statement

Given heads of two linked lists `A` and `B`, find the node at which they **intersect** (same node reference, not same value). Return `null` if they don't intersect. The original structure must remain unchanged.

**Example 1:**
```
listA: 1 -> 2 -> 3 -> 4 -> 5
listB:           7 -> 8 -> 4 -> 5
                          ↑
                    intersection at node(4)
Output: node with value 4
```

**Example 2:**
```
listA: 1 -> 2 -> 3
listB: 8 -> 9
Output: null (no intersection)
```

**Constraints:**
- `1 <= m, n <= 5 × 10⁴`
- Intersection is determined by node reference, not value

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(M × N) | Space: O(1)**

#### 💡 Intuition
For every node in `list2`, scan all of `list1` to see if the same node reference exists. The first match is the intersection point.

#### 💻 Code
```java
class Solution {
    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        while (head2 != null) {
            ListNode temp = head1;
            while (temp != null) {
                if (temp == head2) return head2;
                temp = temp.next;
            }
            head2 = head2.next;
        }
        return null;
    }
}
```

#### 🧪 Dry Run

```
listA: 1 -> 2 -> 3 -> [4] -> 5
listB:           7 -> [4] -> 5   (same node [4])

head2 = node(7):
  scan list1: 1, 2, 3, 4, 5 → none match node(7)

head2 = node(8):
  scan list1: 1, 2, 3, 4, 5 → none match node(8)

head2 = node(4):
  scan list1: 1, 2, 3, node(4) → match! return node(4) ✅
```

---

### 🟡 Approach 2 — Better (HashSet)
**Time: O(M + N) | Space: O(M)**

#### 💡 Intuition
Store all nodes from `list1` in a HashSet. Then traverse `list2` — the first node that exists in the set is the intersection point.

#### 💻 Code
```java
class Solution {
    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        Set<ListNode> st = new HashSet<>();

        while (head1 != null) { st.add(head1); head1 = head1.next; }

        while (head2 != null) {
            if (st.contains(head2)) return head2;
            head2 = head2.next;
        }
        return null;
    }
}
```

#### 🧪 Dry Run

```
listA: 1 -> 2 -> 3 -> [4] -> 5

Pass 1: st = {node1, node2, node3, node4, node5}

listB: 7 -> 8 -> [4] -> 5

Pass 2:
  node(7): not in st
  node(8): not in st
  node(4): in st! → return node(4) ✅
```

---

### 🟢 Approach 3 — Optimal (Length Difference)
**Time: O(M + N) | Space: O(1)**

#### 💡 Intuition
If two lists intersect, their tails are the same. The difference in their lengths tells us how much ahead the longer list is. Advance the pointer of the longer list by that difference, then traverse both together. They will meet at the intersection.

#### 💻 Code
```java
class Solution {
    public int getDifference(ListNode head1, ListNode head2) {
        int len1 = 0, len2 = 0;
        while (head1 != null || head2 != null) {
            if (head1 != null) { len1++; head1 = head1.next; }
            if (head2 != null) { len2++; head2 = head2.next; }
        }
        return len1 - len2;
    }

    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        int diff = getDifference(head1, head2);

        if (diff < 0) { while (diff++ != 0) head2 = head2.next; }
        else          { while (diff-- != 0) head1 = head1.next; }

        while (head1 != null) {
            if (head1 == head2) return head1;
            head1 = head1.next;
            head2 = head2.next;
        }
        return null;
    }
}
```

#### 🧪 Dry Run

```
listA: 1 -> 2 -> 3 -> [4] -> 5   (length 5)
listB:      7 -> 8 -> [4] -> 5   (length 4)

diff = 5 - 4 = 1
Advance head1 by 1: head1 = node(2)

Now traverse together:
  head1=node(2), head2=node(7)  → not same
  head1=node(3), head2=node(8)  → not same
  head1=node(4), head2=node(4)  → same! return node(4) ✅
```

---

### 🟢 Approach 4 — Optimal (Two Pointer Redirect)
**Time: O(M + N) | Space: O(1)**

#### 💡 Intuition
Two pointers `d1` and `d2` start at `head1` and `head2`. When either reaches `null`, redirect it to the other list's head. Both pointers travel a total of `M + N` nodes. If there's an intersection, they will meet at it. If not, both reach `null` simultaneously.

**Why does this work?**
- `d1` travels: `listA` + `listB` = `M + N` total
- `d2` travels: `listB` + `listA` = `N + M` total
- Both cover the same total distance — they must meet at the intersection, or both reach `null` together if no intersection exists.

#### 💻 Code
```java
class Solution {
    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        ListNode d1 = head1;
        ListNode d2 = head2;

        while (d1 != d2) {
            d1 = (d1 == null) ? head2 : d1.next;
            d2 = (d2 == null) ? head1 : d2.next;
        }
        return d1;
    }
}
```

#### 🧪 Dry Run

```
listA: 1 -> 2 -> 3 -> [4] -> 5 -> null   (M=5)
listB:      7 -> 8 -> [4] -> 5 -> null   (N=4)

d1 starts at 1, d2 starts at 7

d1: 1→2→3→4→5→null→7→8→[4]
d2: 7→8→4→5→null→1→2→3→[4]

They meet at node(4) ✅

No intersection case:
listA: 1→2→null   listB: 3→null
d1: 1→2→null→3→null
d2: 3→null→1→2→null
Both reach null → d1 == d2 == null → return null ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Nested Loops | O(M × N) | O(1) |
| HashSet | O(M + N) | O(M) |
| Length Difference | O(M + N) | O(1) |
| Two Pointer Redirect | **O(M + N)** ✅ | **O(1)** ✅ |

---

## 2. Detect a Loop in Linked List

### 🧩 Problem Statement

Given the head of a singly linked list, return `true` if a **cycle/loop** exists, else `false`. A loop exists if a node can be reached again by continuously following `next`.

**Example 1:**
```
1 -> 2 -> 3 -> 4 -> 5
               ↑         ↙ (tail points to node at index 1)
          loop at pos=1
Output: true
```

**Example 2:**
```
1 -> 3 -> 7 -> 4 -> null
Output: false
```

**Constraints:**
- `0 <= nodes <= 10⁵`
- `pos` is -1 (no loop) or a valid index

---

### 🔴 Approach 1 — Brute Force (HashMap)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Track every visited node in a HashMap. If we encounter a node that's already in the map, we've found a cycle. If we reach `null`, there's no cycle.

#### 💻 Code
```java
class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode temp = head;
        HashMap<ListNode, Integer> nodeMap = new HashMap<>();

        while (temp != null) {
            if (nodeMap.containsKey(temp)) return true;
            nodeMap.put(temp, 1);
            temp = temp.next;
        }
        return false;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4`, where node(4).next = node(2)

```
temp=node(1): not in map → add
temp=node(2): not in map → add
temp=node(3): not in map → add
temp=node(4): not in map → add
temp=node(2): already in map! → return true ✅
```

No cycle: `1 -> 3 -> 7 -> 4 -> null`
```
1 added, 3 added, 7 added, 4 added
temp=null → exit loop → return false ✅
```

---

### 🟢 Approach 2 — Optimal (Floyd's Cycle Detection)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Use slow and fast pointers. `slow` moves 1 step, `fast` moves 2 steps. If there's a cycle, fast will eventually lap slow and they will meet inside the cycle. If there's no cycle, fast reaches `null`.

**Why do they always meet inside the cycle?**
Once both pointers enter the cycle, the distance between them decreases by 1 each step (fast gains 1 step on slow per iteration). They will meet in at most `cycle_length` steps.

#### 💻 Code
```java
class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;

        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) return true;
        }
        return false;
    }
}
```

#### 🧪 Dry Run — With Cycle

Input: `1 -> 2 -> 3 -> 4 -> 5`, pos=1 (tail points to node(2))

```
slow=1, fast=1

Step 1: fast=3, slow=2   (fast jumps 2, slow jumps 1)
Step 2: fast=5, slow=3
Step 3: fast=3, slow=4   (fast: 5→next=2→next=3 in cycle, wraps around)
Step 4: fast=5, slow=5   → slow==fast → return true ✅
```

#### 🧪 Dry Run — No Cycle

Input: `1 -> 3 -> 7 -> 4 -> null`

```
slow=1, fast=1

Step 1: fast=7, slow=3
Step 2: fast=null (fast: 7→next=4, 4→next=null) → exit loop → return false ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| HashMap | O(N) | O(N) |
| Floyd's Cycle Detection | **O(N)** ✅ | **O(1)** ✅ |

---

## 3. Reverse Linked List in Groups of K

### 🧩 Problem Statement

Given the head of a linked list and integer `k`, reverse the nodes in groups of `k`. If remaining nodes at the end are fewer than `k`, leave them as is.

**Example 1:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5, k=2
Output: 2 -> 1 -> 4 -> 3 -> 5
```

**Example 2:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5, k=3
Output: 3 -> 2 -> 1 -> 4 -> 5
(last 2 nodes not reversed as 2 < k=3)
```

**Constraints:**
- `1 <= k <= nodes <= 10⁵`

---

### 🟢 Approach — Iterative with Dummy Node
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Process the list group by group:
1. Find the `k`th node from current position — if it doesn't exist, we're done (remaining < k)
2. Reverse the `k` nodes in this group using the standard iterative reversal
3. Reconnect the reversed group back to the rest of the list
4. Move `groupPrev` to the tail of the just-reversed group (for the next iteration)

**Key pointers:**
- `groupPrev` — the node just before the current group (starts at dummy)
- `kth` — the last node of the current group
- `groupNext` — the first node of the next group

#### 📝 Steps
1. Create `dummy` → set `groupPrev = dummy`
2. Loop:
   - Find `kth` node from `groupPrev`. If null → break (fewer than k nodes left)
   - Save `groupNext = kth.next`
   - Reverse `k` nodes starting from `groupPrev.next`, with `groupNext` as the `prev` sentinel
   - After reversal: `kth` is the new head, `groupPrev.next` is the new tail
   - Connect: `groupPrev.next = kth`, then `groupPrev = old head` (now tail)
3. Return `dummy.next`

#### 💻 Code
```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;

        while (true) {
            ListNode kth = getKthNode(groupPrev, k);
            if (kth == null) break;

            ListNode groupNext = kth.next;

            // Reverse k nodes
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            for (int i = 0; i < k; i++) {
                ListNode temp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = temp;
            }

            // Reconnect
            ListNode temp = groupPrev.next; // original head (now tail)
            groupPrev.next = kth;           // kth is new head of reversed group
            groupPrev = temp;               // move groupPrev to new tail
        }

        return dummy.next;
    }

    private ListNode getKthNode(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }
        return curr;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, k=2

```
dummy(-1) -> 1 -> 2 -> 3 -> 4 -> 5
groupPrev = dummy

--- Iteration 1 ---
getKthNode(dummy, 2): dummy→1→2 → kth = node(2)
groupNext = node(3)

Reverse nodes 1 and 2 (with prev=node(3) as sentinel):
  i=0: curr=1, temp=2, curr.next=node(3), prev=1, curr=2
  i=1: curr=2, temp=3, curr.next=1,       prev=2, curr=3

After reversal: 2 -> 1 -> 3 -> 4 -> 5

Reconnect:
  temp = groupPrev.next = node(1)  [original head, now tail]
  groupPrev.next = kth = node(2)   [dummy now points to 2]
  groupPrev = temp = node(1)       [groupPrev moves to tail of group]

State: dummy -> 2 -> 1 -> 3 -> 4 -> 5
       groupPrev = node(1)

--- Iteration 2 ---
getKthNode(node(1), 2): 1→3→4 → kth = node(4)
groupNext = node(5)

Reverse nodes 3 and 4 (with prev=node(5) as sentinel):
  i=0: curr=3, temp=4, curr.next=5, prev=3, curr=4
  i=1: curr=4, temp=5, curr.next=3, prev=4, curr=5

After reversal: 4 -> 3 -> 5

Reconnect:
  temp = groupPrev.next = node(3)
  groupPrev.next = kth = node(4)   [node(1) now points to 4]
  groupPrev = node(3)

State: dummy -> 2 -> 1 -> 4 -> 3 -> 5

--- Iteration 3 ---
getKthNode(node(3), 2): 3→5→null → returns null (fewer than k=2 nodes)
Break!

Return dummy.next = node(2)
Result: 2 -> 1 -> 4 -> 3 -> 5 ✅
```

> 💡 **Why `prev = groupNext` before the reversal loop?**
> When reversing the group, the last node of the group should point to the first node of the **next** group (not `null`). Setting `prev = groupNext` ensures this connection is made correctly during the reversal.

> 💡 **Why `getKthNode` returns `null` for incomplete groups?**
> The function walks `k` steps. If it hits `null` before completing `k` steps, the group has fewer than `k` nodes — we should leave them unchanged. Returning `null` signals this to the main loop, which then breaks.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Intersection of Two Lists | Two pointer redirect: when one reaches end, point to other list's head. Both travel M+N total — they meet at intersection |
| Detect Loop | Floyd's: slow and fast pointers. If they meet, cycle exists. If fast reaches null, no cycle |
| Reverse in K Groups | Find kth node first — if null, stop. Reverse group with groupNext as sentinel, then reconnect carefully |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
