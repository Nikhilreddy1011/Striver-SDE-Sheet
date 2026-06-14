# 🚀 Day 13/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Rotate a Linked List](#1-rotate-a-linked-list) | 🔴 Hard | Linked List |
| 2 | [Clone a LL with Random and Next Pointer](#2-clone-a-linked-list-with-random-and-next-pointer) | 🔴 Hard | Linked List |
| 3 | [3 Sum](#3-3-sum) | 🟡 Medium | Arrays / Two Pointer |

---

## 1. Rotate a Linked List

### 🧩 Problem Statement

Given the head of a linked list and an integer `k`, shift all elements to the **right by k places** and return the new head. `k` can be greater than the length of the list.

**Example 1:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5, k=2
Output: 4 -> 5 -> 1 -> 2 -> 3
```

**Example 2:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5, k=4
Output: 2 -> 3 -> 4 -> 5 -> 1
```

**Constraints:**
- `0 <= nodes <= 10⁵`
- `0 <= k <= 5 × 10⁵`
- `k` may be greater than the number of nodes

---

### 🔴 Approach 1 — Brute Force (Simulate k Rotations)
**Time: O(N × k) | Space: O(1)**

#### 💡 Intuition
Perform each rotation one at a time. For each rotation: traverse to the second-to-last node, detach the last node, make it the new head.

#### 💻 Code
```java
class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;

        for (int i = 0; i < k; i++) {
            ListNode curr = head;
            ListNode prev = null;
            while (curr.next != null) { prev = curr; curr = curr.next; }
            prev.next = null;
            curr.next = head;
            head = curr;
        }
        return head;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, k=2

```
Rotation 1:
  traverse to last: prev=4, curr=5
  prev.next=null, curr(5).next=head(1), head=5
  List: 5 -> 1 -> 2 -> 3 -> 4

Rotation 2:
  traverse to last: prev=3, curr=4
  prev.next=null, curr(4).next=head(5), head=4
  List: 4 -> 5 -> 1 -> 2 -> 3 ✅
```

#### ⚠️ Limitation
If k=100000 and n=5, this runs 100000 iterations — most are redundant. We can reduce k using modulo.

---

### 🟢 Approach 2 — Optimal (Circular + Break)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
**Key observation:** Rotating a list of length `n` by `k` is the same as rotating by `k % n` (since a full rotation brings you back to the same list).

After `k % n` right rotations, the new head is at position `n - k` from the start (0-indexed). So:
1. Find the length and make the list **circular** (tail → head)
2. Walk `n - k` steps to find the **new tail**
3. `newHead = newTail.next`, then break the circle

#### 📝 Steps
1. Find `length` and `tail` in one pass
2. `tail.next = head` (make circular)
3. `k = k % length`
4. Walk `length - k` steps to find `newTail`
5. `newHead = newTail.next`, `newTail.next = null`
6. Return `newHead`

#### 💻 Code
```java
class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;

        int length = 1;
        ListNode tail = head;
        while (tail.next != null) { tail = tail.next; length++; }

        tail.next = head; // make circular

        k = k % length;
        int stepsToNewTail = length - k;

        ListNode newTail = head;
        for (int i = 1; i < stepsToNewTail; i++) newTail = newTail.next;

        ListNode newHead = newTail.next;
        newTail.next = null; // break circle

        return newHead;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3 -> 4 -> 5`, k=2

```
Step 1: length=5, tail=node(5)
Step 2: tail.next=head → 1->2->3->4->5->1 (circular)
Step 3: k = 2%5 = 2
Step 4: stepsToNewTail = 5-2 = 3
        newTail: start at 1, walk 2 more steps → node(3)
Step 5: newHead = node(3).next = node(4)
        node(3).next = null

Result: 4 -> 5 -> 1 -> 2 -> 3 ✅
```

**k > length case:** `k=7`, `length=5` → `k = 7%5 = 2` → same as k=2 ✅

> 💡 **Why `length - k` steps to find the new tail?**
> After rotating right by `k`, the last `k` nodes move to the front. The new tail is the node just before those last `k` nodes — which is at position `length - k` from the start.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Simulate k Rotations | O(N × k) | O(1) |
| Circular + Break | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Clone a Linked List with Random and Next Pointer

### 🧩 Problem Statement

Given a special linked list where each node has a `next` pointer and a `random` pointer (which can point to any node or `null`), create a **deep copy** of the list. All new nodes must be independent of the original.

**Example:**
```
Original: 1 -> 2 -> 3
          random: 1→null, 2→1, 3→2

Output: New deep copy with same structure and random pointers
        pointing to corresponding new nodes
```

**Constraints:**
- `1 <= n <= 10⁵`
- `-10⁴ <= node.val <= 10⁴`

---

### 🟢 Approach — Interleave + Connect + Separate (O(1) Space)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
The challenge is setting `random` pointers correctly without a HashMap. The trick: **interleave copied nodes between original nodes**, then use the interleaved structure to set random pointers, then separate.

**Three phases:**

**Phase 1 — Interleave:**
Insert a copy of each node immediately after it.
```
Before: 1 -> 2 -> 3
After:  1 -> 1' -> 2 -> 2' -> 3 -> 3'
```

**Phase 2 — Set random pointers:**
For each original node `temp`:
`temp.next.random = temp.random.next`
This works because `temp.random.next` is exactly the copy of `temp.random` (due to interleaving).

**Phase 3 — Separate:**
Detach copied nodes to form the result list. Restore original `next` pointers.

#### 💻 Code
```java
class Solution {
    public ListNode copyRandomList(ListNode head) {
        if (head == null) return null;

        // Phase 1: Interleave copies
        ListNode temp = head;
        while (temp != null) {
            ListNode copy = new ListNode(temp.val);
            copy.next = temp.next;
            temp.next = copy;
            temp = copy.next;
        }

        // Phase 2: Set random pointers
        temp = head;
        while (temp != null) {
            if (temp.random != null)
                temp.next.random = temp.random.next;
            temp = temp.next.next;
        }

        // Phase 3: Separate lists
        ListNode dummy = new ListNode(0);
        ListNode copyCurr = dummy;
        temp = head;

        while (temp != null) {
            ListNode copy = temp.next;
            copyCurr.next = copy;
            copyCurr = copy;
            temp.next = copy.next; // restore original
            temp = temp.next;
        }

        return dummy.next;
    }
}
```

#### 🧪 Dry Run

Input: `1 -> 2 -> 3`, random: `1→null`, `2→1`, `3→2`

**Phase 1 — Interleave:**
```
temp=1: copy=1', 1'→2, 1→1'   → 1->1'->2->3
temp=2: copy=2', 2'→3, 2→2'   → 1->1'->2->2'->3
temp=3: copy=3', 3'→null, 3→3'→ 1->1'->2->2'->3->3'
```

**Phase 2 — Set random:**
```
temp=1: random=null → skip
temp=2: random=node(1) → 2'.random = node(1).next = 1'
temp=3: random=node(2) → 3'.random = node(2).next = 2'
```

**Phase 3 — Separate:**
```
Extract: 1', 2', 3'
Restore: 1→2→3 (original intact)

Result: 1'->2'->3'
        1'.random=null, 2'.random=1', 3'.random=2' ✅
```

> 💡 **Why does `temp.random.next` give us the copied random node?**
> After Phase 1, every original node is immediately followed by its copy. So `node.next` is always `node`'s copy. Therefore, `temp.random.next` is the copy of `temp.random` — exactly what we want `temp.next.random` to point to.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | **O(1)** ✅ (no HashMap) |

---

## 3. 3 Sum

### 🧩 Problem Statement

Given an integer array `nums`, return all **unique triplets** `[a, b, c]` such that `a + b + c == 0`. No duplicate triplets in the result.

**Example 1:**
```
Input:  nums = [2, -2, 0, 3, -3, 5]
Output: [[-2,0,2], [-3,-2,5], [-3,0,3]]
```

**Example 2:**
```
Input:  nums = [2, -1, -1, 3, -1]
Output: [[-1,-1,2]]
```

**Constraints:**
- `1 <= nums.length <= 3000`
- `-10⁴ <= nums[i] <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (3 Nested Loops + Set)
**Time: O(N³ × log(triplets)) | Space: O(triplets)**

#### 💡 Intuition
Try every combination of 3 indices. If sum is 0, sort the triplet and add to a Set (which handles duplicates automatically).

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> threeSum(int[] arr) {
        int n = arr.length;
        Set<List<Integer>> st = new HashSet<>();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                for (int k = j + 1; k < n; k++)
                    if (arr[i] + arr[j] + arr[k] == 0) {
                        List<Integer> temp = Arrays.asList(arr[i], arr[j], arr[k]);
                        Collections.sort(temp);
                        st.add(temp);
                    }
        return new ArrayList<>(st);
    }
}
```

#### 🧪 Dry Run

Input: `[2, -2, 0, 3, -3, 5]`

```
i=0(2), j=1(-2), k=2(0): 2+(-2)+0=0 → sort→[-2,0,2] → add
i=0(2), j=1(-2), k=3(3): 2-2+3=3 ≠ 0
...
i=0(2), j=4(-3), k=5(5): 2-3+5=4 ≠ 0
i=1(-2), j=4(-3), k=5(5): -2-3+5=0 → [-3,-2,5] → add
i=2(0), j=3(3), k=4(-3): 0+3-3=0 → [-3,0,3] → add
...

Result: [[-2,0,2],[-3,-2,5],[-3,0,3]] ✅
```

---

### 🟡 Approach 2 — Better (2 Loops + HashSet)
**Time: O(N² × log(triplets)) | Space: O(N + triplets)**

#### 💡 Intuition
Fix `i`. For each `j`, the third element needed is `-(arr[i] + arr[j])`. Check if it exists in a HashSet of elements seen between `i` and `j`. Add `arr[j]` to the set after checking.

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> threeSum(int[] arr) {
        int n = arr.length;
        Set<List<Integer>> ans = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Set<Integer> hashset = new HashSet<>();
            for (int j = i + 1; j < n; j++) {
                int third = -(arr[i] + arr[j]);
                if (hashset.contains(third)) {
                    List<Integer> temp = Arrays.asList(arr[i], arr[j], third);
                    Collections.sort(temp);
                    ans.add(temp);
                }
                hashset.add(arr[j]);
            }
        }
        return new ArrayList<>(ans);
    }
}
```

#### 🧪 Dry Run

Input: `[2, -2, 0, 3, -3, 5]`, i=0 (arr[i]=2)

```
hashset = {}

j=1(-2): third = -(2-2) = 0, in hashset? No → add -2
j=2(0):  third = -(2+0) = -2, in hashset? Yes(-2)! → triplet [2,0,-2] → sort→[-2,0,2] → add
j=3(3):  third = -(2+3) = -5, not in hashset → add 0
j=4(-3): third = -(2-3) = 1,  not in hashset → add 3
j=5(5):  third = -(2+5) = -7, not in hashset → add -3
```

---

### 🟢 Approach 3 — Optimal (Sort + Two Pointers)
**Time: O(N²) | Space: O(1) extra**

#### 💡 Intuition
Sort the array. Fix `i`, use two pointers `left = i+1` and `right = n-1`. Adjust based on whether sum is too large, too small, or zero. Skip duplicates at all three pointer positions to avoid duplicate triplets.

**Why sort first?**
Sorting lets us use two pointers (works because the array is ordered). It also makes duplicate skipping simple — just compare with the previous element.

#### 📝 Steps
1. Sort `arr`
2. For each `i` (skip if `arr[i] == arr[i-1]`):
   - `left = i+1`, `right = n-1`
   - While `left < right`:
     - If `sum == 0` → add triplet, move both, skip duplicates
     - If `sum < 0` → `left++`
     - If `sum > 0` → `right--`

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> threeSum(int[] arr) {
        int n = arr.length;
        Arrays.sort(arr);
        List<List<Integer>> ans = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (i > 0 && arr[i] == arr[i - 1]) continue; // skip i duplicates

            int left = i + 1, right = n - 1;

            while (left < right) {
                int sum = arr[i] + arr[left] + arr[right];

                if (sum == 0) {
                    ans.add(Arrays.asList(arr[i], arr[left], arr[right]));
                    left++; right--;
                    while (left < right && arr[left] == arr[left - 1]) left++;
                    while (left < right && arr[right] == arr[right + 1]) right--;
                } else if (sum < 0) left++;
                else right--;
            }
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `[2, -2, 0, 3, -3, 5]`

After sort: `[-3, -3, -2, 0, 2, 3, 5]` → wait, original has no duplicates:
`[-3, -2, 0, 2, 3, 5]`

```
i=0 (arr[0]=-3):
  left=1(-2), right=5(5): sum=-3-2+5=0 → add [-3,-2,5], left=2, right=4
  left=2(0), right=4(3):  sum=-3+0+3=0 → add [-3,0,3], left=3, right=3
  left >= right → done

i=1 (arr[1]=-2):
  left=2(0), right=5(5): sum=-2+0+5=3 > 0 → right--
  left=2(0), right=4(3): sum=-2+0+3=1 > 0 → right--
  left=2(0), right=3(2): sum=-2+0+2=0 → add [-2,0,2], left=3, right=2
  left >= right → done

i=2 (arr[2]=0):
  left=3(2), right=5(5): sum=0+2+5=7 > 0 → right--
  left=3(2), right=4(3): sum=0+2+3=5 > 0 → right--
  left >= right → done

i=3,4,5: no valid triplets (all positive, can't sum to 0)

Result: [[-3,-2,5],[-3,0,3],[-2,0,2]] ✅
```

> 💡 **Why skip when `arr[i] == arr[i-1]`?**
> If we process the same value at `i` twice, we'll generate the same triplets twice. Since the array is sorted, duplicates are adjacent — a simple `continue` skips them.

> 💡 **Why `arr[left] == arr[left-1]` (not `arr[left] == arr[left+1]`)?**
> After adding a triplet and moving `left++`, we compare the new `left` with the one we just used. `arr[left] == arr[left-1]` catches that case correctly.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| 3 Loops + Set | O(N³ log M) | O(M) |
| 2 Loops + HashSet | O(N² log M) | O(N) |
| Sort + Two Pointers | **O(N²)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Rotate LL | k rotations = k%n rotations. Make circular, walk to new tail (n-k steps), break the circle |
| Clone LL with Random | Interleave copies between originals: `copy.random = original.random.next` works because of the interleaving |
| 3 Sum | Sort + two pointers. Skip duplicates at all 3 levels. No extra set needed — O(N²) |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
