# 🚀 Day 4/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Find the Duplicate Number](#1-find-the-duplicate-number) | 🟡 Medium | Arrays / Two Pointer |
| 2 | [Find the Repeating and Missing Number](#2-find-the-repeating-and-missing-number) | 🔴 Hard | Arrays / Math |
| 3 | [Count Inversions](#3-count-inversions) | 🔴 Hard | Arrays / Merge Sort |

---

## 1. Find the Duplicate Number

### 🧩 Problem Statement

Given an array `nums` of `n+1` integers where each integer is in the range `[1, n]` inclusive, there is **only one repeated number** — find and return it.

**Rules:**
- Must not modify the array
- Must use only constant extra space
- Must run in less than O(N²) time

**Example 1:**
```
Input:  nums = [1, 3, 4, 2, 2]
Output: 2
```

**Example 2:**
```
Input:  nums = [3, 1, 3, 4, 2]
Output: 3
```

**Example 3:**
```
Input:  nums = [1, 1]
Output: 1
```

**Constraints:**
- `1 <= n <= 10⁵`
- `nums.length == n + 1`
- `1 <= nums[i] <= n`
- Only one duplicate number, but it can be repeated more than once

---

### 🔴 Approach 1 — Brute Force (Sort and Compare)
**Time: O(N log N) | Space: O(1)**

#### 💡 Intuition
Sort the array. In a sorted array, the duplicate number will always appear at two consecutive positions. Simply compare `nums[i]` with `nums[i+1]` — when they match, that's our answer.

#### 📝 Steps
1. Sort the array
2. Loop from `i = 0` to `n-1`
3. If `nums[i] == nums[i+1]` → return `nums[i]`

#### 💻 Code
```java
class Solution {
    public int findDuplicate(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return nums[i];
            }
        }
        return -1;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 3, 4, 2, 2]`

After sorting: `[1, 2, 2, 3, 4]`

```
i=0: nums[0]=1 == nums[1]=2? No
i=1: nums[1]=2 == nums[2]=2? Yes → return 2 ✅
```

#### ⚠️ Limitation
Sorting itself costs O(N log N) and **modifies the array** — which violates the problem constraint. Not acceptable as a final solution.

---

### 🟡 Approach 2 — Better (Hash / Frequency Array)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Use a `temp` array as a frequency counter. For each element, if it hasn't been seen before (`temp[nums[i]] == 0`), mark it. If it has already been marked, that element is the duplicate.

#### 📝 Steps
1. Create `temp[n+1]` initialized to 0
2. For each `nums[i]`:
   - If `temp[nums[i]] == 0` → mark it as seen (`temp[nums[i]]++`)
   - Else → `nums[i]` is the duplicate, return it

#### 💻 Code
```java
class Solution {
    public int findDuplicate(int[] nums) {
        int[] temp = new int[nums.length + 1];
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (temp[nums[i]] == 0) {
                temp[nums[i]]++;
            } else {
                return nums[i];
            }
        }
        return 0;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 3, 4, 2, 2]`

```
temp = [0, 0, 0, 0, 0, 0]  (size 6)

i=0: nums[0]=1 → temp[1]=0, mark → temp=[0,1,0,0,0,0]
i=1: nums[1]=3 → temp[3]=0, mark → temp=[0,1,0,1,0,0]
i=2: nums[2]=4 → temp[4]=0, mark → temp=[0,1,0,1,1,0]
i=3: nums[3]=2 → temp[2]=0, mark → temp=[0,1,1,1,1,0]
i=4: nums[4]=2 → temp[2]=1, already seen → return 2 ✅
```

#### ⚠️ Limitation
Uses O(N) extra space — violates the constant space constraint. We need something better.

---

### 🟢 Approach 3 — Optimal (Floyd's Cycle Detection)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
This is Floyd's Tortoise and Hare algorithm — typically used for detecting cycles in Linked Lists, but it works here too.

**Key Observation:** Think of the array as a function `f(x) = nums[x]`. Starting from index 0, if you keep jumping to `nums[current]`, you're essentially traversing a linked list where each value points to the next index. Since there's a duplicate, two indices point to the same next index — forming a **cycle**. The duplicate number is the **entry point of that cycle**.

**Phase 1 — Detect the cycle:**
- `slow` moves one step: `slow = nums[slow]`
- `fast` moves two steps: `fast = nums[nums[fast]]`
- When `slow == fast`, a cycle is detected

**Phase 2 — Find the cycle entry (duplicate):**
- Reset `fast` to `nums[0]`
- Move both `slow` and `fast` one step at a time
- Where they meet = entry point of cycle = duplicate number

#### 📝 Steps
1. Initialize `slow = nums[0]`, `fast = nums[0]`
2. Phase 1: Move until `slow == fast`
3. Reset `fast = nums[0]`
4. Phase 2: Move both one step until `slow == fast`
5. Return `slow` (or `fast`)

#### 💻 Code
```java
class Solution {
    public int findDuplicate(int[] nums) {
        int slow = nums[0];
        int fast = nums[0];

        // Phase 1: Detect cycle
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        // Phase 2: Find cycle entry point
        fast = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        return fast;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 3, 4, 2, 2]`

Visualize as a linked list (index → nums[index]):
```
0 → 1 → 3 → 2 → 4 → 2 → 4 → 2 ...  (cycle at 2)
```

**Phase 1 — Detect cycle:**
```
Start: slow=nums[0]=1, fast=nums[0]=1

Step 1: slow=nums[1]=3,  fast=nums[nums[1]]=nums[3]=2
Step 2: slow=nums[3]=2,  fast=nums[nums[2]]=nums[4]=2
slow==fast==2 → cycle detected!
```

**Phase 2 — Find entry:**
```
Reset fast=nums[0]=1
slow=2, fast=1

Step 1: slow=nums[2]=4, fast=nums[1]=3
Step 2: slow=nums[4]=2, fast=nums[3]=2
slow==fast==2 → duplicate found!

Return 2 ✅
```

> 💡 **Why does Phase 2 find the entry point?**
> This is a mathematical property of Floyd's algorithm. When slow and fast meet inside the cycle, the distance from that meeting point to the cycle entry equals the distance from the array start to the cycle entry. So resetting one pointer to the start and moving both at the same speed guarantees they meet exactly at the entry point.

#### 📊 Complexity Summary

| Approach | Time | Space | Modifies Array? |
|---------|------|-------|----------------|
| Sort + Compare | O(N log N) | O(1) | ❌ Yes |
| Frequency Array | O(N) | O(N) | No |
| Floyd's Cycle Detection | **O(N)** ✅ | **O(1)** ✅ | No ✅ |

---

## 2. Find the Repeating and Missing Number

### 🧩 Problem Statement

Given an integer array `nums` of size `n` containing values from `[1, n]`, where exactly one number `A` appears **twice** and one number `B` is **missing**, return `[A, B]`.

**Example 1:**
```
Input:  nums = [3, 5, 4, 1, 1]
Output: [1, 2]
Explanation: 1 appears twice, 2 is missing
```

**Example 2:**
```
Input:  nums = [1, 2, 3, 6, 7, 5, 7]
Output: [7, 4]
Explanation: 7 appears twice, 4 is missing
```

**Constraints:**
- `1 <= n <= 10⁵`
- Exactly one duplicate, exactly one missing value

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
For every number `i` from `1` to `n`, count how many times it appears in the array. If count is `2` → it's the duplicate. If count is `0` → it's the missing number.

#### 📝 Steps
1. For each `i` from 1 to n, count occurrences in `nums`
2. If count == 2 → `duplicate = i`
3. If count == 0 → `missing = i`
4. Break once both are found

#### 💻 Code
```java
class Solution {
    public int[] findMissingRepeatingNumbers(int[] nums) {
        int n = nums.length;
        int duplicate = -1, missing = -1;

        for (int i = 1; i <= n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] == i) count++;
            }
            if (count == 2) duplicate = i;
            else if (count == 0) missing = i;

            if (duplicate != -1 && missing != -1) break;
        }
        return new int[]{duplicate, missing};
    }
}
```

#### 🧪 Dry Run

Input: `[3, 5, 4, 1, 1]`, n=5

```
i=1: count=2 → duplicate=1
i=2: count=0 → missing=2
Both found → break

Return [1, 2] ✅
```

---

### 🟡 Approach 2 — Better (Frequency Array)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Use a frequency array `temp[n+1]`. Count occurrences of each number in one pass. Then scan `temp` from 1 to n — wherever count is 2 it's the duplicate, wherever count is 0 it's the missing number.

#### 💻 Code
```java
class Solution {
    public int[] findMissingRepeatingNumbers(int[] nums) {
        int n = nums.length;
        int[] temp = new int[n + 1];

        for (int i = 0; i < n; i++) {
            temp[nums[i]]++;
        }

        int duplicate = -1, missing = -1;
        for (int i = 1; i <= n; i++) {
            if (temp[i] == 2) duplicate = i;
            else if (temp[i] == 0) missing = i;
            if (missing != -1 && duplicate != -1) break;
        }
        return new int[]{duplicate, missing};
    }
}
```

#### 🧪 Dry Run

Input: `[3, 5, 4, 1, 1]`, n=5

**Pass 1 — Build frequency array:**
```
temp = [0, 2, 0, 1, 1, 1]
       (0) (1)(2)(3)(4)(5)
```

**Pass 2 — Scan:**
```
i=1: temp[1]=2 → duplicate=1
i=2: temp[2]=0 → missing=2
Both found → break

Return [1, 2] ✅
```

---

### 🟢 Approach 3 — Optimal (Math / Equations)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Let `X` = repeating number, `Y` = missing number.

We form **two equations** using known mathematical formulas:

**Equation 1 — Sum:**
```
S  = actual sum of array elements
SN = n*(n+1)/2  (expected sum of 1 to n)
S - SN = X - Y   →  val1 = X - Y
```

**Equation 2 — Sum of Squares:**
```
S2  = actual sum of squares of array elements
S2N = n*(n+1)*(2n+1)/6  (expected sum of squares)
S2 - S2N = X² - Y²
         = (X+Y)(X-Y)
So: X + Y = (S2 - S2N) / (X - Y)  →  val2 = X + Y
```

Now we have `X - Y` and `X + Y`:
```
X = (val1 + val2) / 2
Y = X - val1
```

> 💡 **Why `long`?** With n up to 10⁵, sum of squares can reach up to 10⁵ × (10⁵)² = 10¹⁵ — way beyond `int` range. Using `long` prevents overflow.

#### 📝 Steps
1. Compute `SN = n*(n+1)/2` and `S2N = n*(n+1)*(2n+1)/6`
2. Compute actual `S` and `S2` from the array
3. `val1 = S - SN` (this is X - Y)
4. `val2 = (S2 - S2N) / val1` (this is X + Y)
5. `X = (val1 + val2) / 2`, `Y = X - val1`
6. Return `[X, Y]`

#### 💻 Code
```java
class Solution {
    public int[] findMissingRepeatingNumbers(int[] nums) {
        long n = nums.length;

        // Expected sum and sum of squares
        long sum = (n * (n + 1)) / 2;
        long sum2 = (n * (n + 1) * (2 * n + 1)) / 6;

        // Actual sum and sum of squares
        long s1 = 0, s2 = 0;
        for (int i = 0; i < n; i++) {
            s1 += nums[i];
            s2 += (long) nums[i] * (long) nums[i];
        }

        // val1 = X - Y,  val2 = X + Y
        long x = s1 - sum;         // X - Y
        long y = s2 - sum2;        // X² - Y²
        y = y / x;                 // X + Y

        long n1 = (x + y) / 2;    // X (repeating)
        long n2 = n1 - x;         // Y (missing)

        return new int[]{(int) n1, (int) n2};
    }
}
```

#### 🧪 Dry Run

Input: `[3, 5, 4, 1, 1]`, n=5

**Expected values:**
```
SN  = 5*6/2       = 15
S2N = 5*6*11/6    = 55
```

**Actual values:**
```
S  = 3+5+4+1+1   = 14
S2 = 9+25+16+1+1 = 52
```

**Equations:**
```
val1 = S - SN   = 14 - 15 = -1   → X - Y = -1
val2 = (S2-S2N) / val1
     = (52-55) / (-1)
     = (-3) / (-1) = 3             → X + Y = 3

X = (-1 + 3) / 2 = 1   (repeating)
Y = 1 - (-1)     = 2   (missing)

Return [1, 2] ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Nested Loops | O(N²) | O(1) |
| Frequency Array | O(N) | O(N) |
| Math (Equations) | **O(N)** ✅ | **O(1)** ✅ |

---

## 3. Count Inversions

### 🧩 Problem Statement

Given an integer array `nums`, return the **number of inversions** in the array.

Two elements `nums[i]` and `nums[j]` form an inversion if:
- `nums[i] > nums[j]` AND
- `i < j`

A sorted array has 0 inversions. A reverse-sorted array has the maximum number of inversions.

**Example 1:**
```
Input:  nums = [2, 3, 7, 1, 3, 5]
Output: 5

Inversions:
(2,1), (3,1), (7,1), (7,3), (7,5)
```

**Example 2:**
```
Input:  nums = [-10, -5, 6, 11, 15, 17]
Output: 0   (already sorted)
```

**Example 3:**
```
Input:  nums = [9, 5, 4, 2]
Output: 6   (reverse sorted — maximum inversions)
```

**Constraints:**
- `1 <= nums.length <= 10⁵`
- `-10⁴ <= nums[i] <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
The problem definition directly tells us what to do — check every pair `(i, j)` where `i < j`. If `nums[i] > nums[j]`, it's an inversion. Count them all.

#### 📝 Steps
1. Two nested loops: outer `i` from 0 to n, inner `j` from `i+1` to n
2. If `nums[i] > nums[j]` → increment count
3. Return count

#### 💻 Code
```java
class Solution {
    public long numberOfInversions(int[] nums) {
        long n = nums.length;
        long count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] > nums[j]) {
                    count++;
                }
            }
        }
        return count;
    }
}
```

#### 🧪 Dry Run

Input: `[2, 3, 7, 1, 3, 5]`

```
i=0 (val=2): j=3 → 2>1 ✅  count=1
i=1 (val=3): j=3 → 3>1 ✅  count=2
i=2 (val=7): j=3 → 7>1 ✅  count=3
             j=4 → 7>3 ✅  count=4
             j=5 → 7>5 ✅  count=5
i=3 (val=1): no j gives 1 > something
i=4 (val=3): no j gives 3 > something after it

Return 5 ✅
```

O(N²) — TLE for large inputs.

---

### 🟢 Approach 2 — Optimal (Modified Merge Sort)
**Time: O(N log N) | Space: O(N)**

#### 💡 Intuition
This is the key insight that makes this problem elegant: **during the merge step of Merge Sort, we can count inversions for free.**

During merge, we have two sorted halves:
- Left half: `arr[low..mid]`
- Right half: `arr[mid+1..high]`

When comparing `arr[left]` and `arr[right]`:
- If `arr[left] <= arr[right]` → no inversion, pick left
- If `arr[left] > arr[right]` → `arr[right]` is smaller than `arr[left]` AND all remaining elements in the left half (since left half is sorted). So `count += (mid - left + 1)`

This counts all inversions between the two halves in one pass. The mergeSort recursion handles the left-half and right-half inversions separately.

**Why does this work?**
When merging two sorted halves, if `arr[left] > arr[right]`, every element from `arr[left]` to `arr[mid]` is also greater than `arr[right]` (left half is sorted). So all of them form an inversion with `arr[right]` — giving us `mid - left + 1` inversions at once instead of counting one by one.

#### 📝 Steps
1. `mergeSort(arr, low, high)`:
   - Recursively sort and count left half
   - Recursively sort and count right half
   - Merge and count cross-inversions
   - Return total count

2. In `merge(arr, low, mid, high)`:
   - Standard merge with a temp array
   - When `arr[left] > arr[right]` → `count += (mid - left + 1)`
   - Copy merged result back to original array

#### 💻 Code
```java
class Solution {

    private long merge(int[] arr, int low, int mid, int high) {
        ArrayList<Integer> temp = new ArrayList<>();
        int left = low;
        int right = mid + 1;
        long count = 0;

        while (left <= mid && right <= high) {
            if (arr[left] <= arr[right]) {
                temp.add(arr[left]);
                left++;
            } else {
                // arr[left..mid] all > arr[right] → all form inversions
                count += (mid - left + 1);
                temp.add(arr[right]);
                right++;
            }
        }

        // Copy remaining left half
        while (left <= mid) temp.add(arr[left++]);

        // Copy remaining right half
        while (right <= high) temp.add(arr[right++]);

        // Copy temp back to original array
        for (int i = low; i <= high; i++) {
            arr[i] = temp.get(i - low);
        }

        return count;
    }

    private long mergeSort(int[] arr, int low, int high) {
        if (low >= high) return 0;

        int mid = low + (high - low) / 2;
        long count = 0;

        count += mergeSort(arr, low, mid);       // Left half inversions
        count += mergeSort(arr, mid + 1, high);  // Right half inversions
        count += merge(arr, low, mid, high);     // Cross inversions

        return count;
    }

    public long numberOfInversions(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }
}
```

#### 🧪 Dry Run

Input: `[2, 3, 7, 1, 3, 5]`

**Recursion tree (simplified):**
```
mergeSort([2,3,7,1,3,5], 0, 5)
├── mergeSort([2,3,7], 0, 2)
│   ├── mergeSort([2,3], 0, 1)   → 0 inversions
│   └── mergeSort([7], 2, 2)     → 0 inversions
│   └── merge([2,3], [7])        → 0 inversions (both halves in order)
│   Left subtree total: 0
│
└── mergeSort([1,3,5], 3, 5)
    ├── mergeSort([1,3], 3, 4)   → 0 inversions
    └── mergeSort([5], 5, 5)     → 0 inversions
    └── merge([1,3], [5])        → 0 inversions
    Right subtree total: 0
│
└── merge([2,3,7], [1,3,5])  ← Cross inversions counted here
```

**Final merge step** — merging `[2, 3, 7]` and `[1, 3, 5]`:

```
left=0(val=2), right=3(val=1):
  arr[left]=2 > arr[right]=1
  count += (mid - left + 1) = (2 - 0 + 1) = 3
  → inversions: (2,1), (3,1), (7,1)
  pick 1 → temp=[1], right=4

left=0(val=2), right=4(val=3):
  arr[left]=2 <= arr[right]=3 → no inversion
  pick 2 → temp=[1,2], left=1

left=1(val=3), right=4(val=3):
  arr[left]=3 <= arr[right]=3 → no inversion
  pick 3 → temp=[1,2,3], left=2

left=2(val=7), right=4(val=3):
  arr[left]=7 > arr[right]=3
  count += (mid - left + 1) = (2 - 2 + 1) = 1
  → inversion: (7,3)
  pick 3 → temp=[1,2,3,3], right=5

left=2(val=7), right=5(val=5):
  arr[left]=7 > arr[right]=5
  count += (mid - left + 1) = (2 - 2 + 1) = 1
  → inversion: (7,5)
  pick 5 → temp=[1,2,3,3,5], right=6

right exhausted, copy remaining left:
  pick 7 → temp=[1,2,3,3,5,7]

Cross inversions = 3 + 1 + 1 = 5
```

Total: `0 (left) + 0 (right) + 5 (cross) = 5` ✅

> 💡 **Why `count += (mid - left + 1)` and not just `count++`?**
> When `arr[left] > arr[right]`, since the left half is already sorted, every element from index `left` to `mid` is also greater than `arr[right]`. That's `(mid - left + 1)` inversions all at once — counting them individually would bring us back to O(N²).

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (Nested Loops) | O(N²) | O(1) |
| Modified Merge Sort | **O(N log N)** ✅ | **O(N)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Find Duplicate | Array indices form an implicit linked list — Floyd's cycle detection finds the duplicate as the cycle entry point |
| Repeating & Missing | Two equations (sum + sum of squares) give X-Y and X+Y — solve for both in O(1) space |
| Count Inversions | During merge sort, when arr[left] > arr[right], all remaining left elements also form inversions — count += (mid - left + 1) |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet —daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*