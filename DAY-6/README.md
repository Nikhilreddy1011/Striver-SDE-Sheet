# 🚀 Day 6/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Majority Element-II](#1-majority-element-ii) | 🔴 Hard | Arrays / Boyer-Moore |
| 2 | [Grid Unique Paths](#2-grid-unique-paths) | 🟡 Medium | DP / Recursion |
| 3 | [Reverse Pairs](#3-reverse-pairs) | 🔴 Hard | Arrays / Merge Sort |

---

## 1. Majority Element-II

### 🧩 Problem Statement

Given an integer array `nums` of size `n`, return **all elements that appear more than n/3 times**. Output can be in any order.

**Example 1:**
```
Input:  nums = [1, 2, 1, 1, 3, 2]
Output: [1]
Explanation: n/3 = 2. Only 1 appears 3 times (3 > 2).
```

**Example 2:**
```
Input:  nums = [1, 2, 1, 1, 3, 2, 2]
Output: [1, 2]
Explanation: n/3 = 2. Both 1 (3 times) and 2 (3 times) qualify.
```

**Constraints:**
- `2 <= n <= 10⁵`
- `-10⁴ <= nums[i] <= 10⁴`

---

> 💡 **Why at most 2 majority elements?**
> If three elements each appeared more than n/3 times, their combined count would exceed n — impossible. So at most **2** elements can satisfy the condition.

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
For every element, count how many times it appears in the full array. If count > n/3, add it to the result. Skip elements already in the result. Stop once we have 2 elements.

#### 📝 Steps
1. For each `nums[i]` (skip if already in list), count its occurrences
2. If count > n/3 → add to list
3. Break when list size reaches 2

#### 💻 Code
```java
class Solution {
    public List<Integer> majorityElementTwo(int[] nums) {
        int n = nums.length;
        List<Integer> list = new ArrayList<>();
        int min = n / 3;

        for (int i = 0; i < n; i++) {
            if (list.size() == 0 || list.get(0) != nums[i]) {
                int count = 0;
                for (int j = 0; j < n; j++) {
                    if (nums[j] == nums[i]) count++;
                }
                if (count > min) list.add(nums[i]);
            }
            if (list.size() == 2) break;
        }
        return list;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 2, 1, 1, 3, 2, 2]`, n=7, min=2

```
i=0 (val=1): list empty → count occurrences of 1
  count = 3 → 3 > 2 → list = [1]

i=1 (val=2): list.get(0)=1 ≠ 2 → count occurrences of 2
  count = 3 → 3 > 2 → list = [1, 2]

list.size()==2 → break

Return [1, 2] ✅
```

---

### 🟡 Approach 2 — Better (HashMap)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Use a HashMap to build frequency counts in one pass. The moment any element's frequency hits `n/3 + 1` (the minimum to qualify), add it to the result immediately. Stop once we have 2.

#### 📝 Steps
1. `min = n/3 + 1`
2. For each element, update its count in the map
3. If `map.get(nums[i]) == min` → add to list
4. Break when list size reaches 2

#### 💻 Code
```java
class Solution {
    public List<Integer> majorityElementTwo(int[] nums) {
        int n = nums.length;
        int min = n / 3 + 1;
        List<Integer> list = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
            if (map.get(nums[i]) == min) {
                list.add(nums[i]);
            }
            if (list.size() == 2) break;
        }
        return list;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 2, 1, 1, 3, 2, 2]`, n=7, min=3

```
i=0: map={1:1}  → 1≠3, no add
i=1: map={1:1,2:1} → 1≠3, no add
i=2: map={1:2,2:1} → 2≠3, no add
i=3: map={1:3,2:1} → 1's count==3 → list=[1]
i=4: map={1:3,2:1,3:1} → 1≠3, no add
i=5: map={1:3,2:2,3:1} → 2≠3, no add
i=6: map={1:3,2:3,3:1} → 2's count==3 → list=[1,2]
list.size()==2 → break

Return [1, 2] ✅
```

---

### 🟢 Approach 3 — Optimal (Extended Boyer-Moore Voting)
**Time: O(N) + O(N) | Space: O(1)**

#### 💡 Intuition
Extension of Boyer-Moore from Majority Element-I. Instead of tracking 1 candidate, we track **2 candidates** with 2 counts.

**The party analogy extended:** Imagine tracking up to 2 popular dishes. Each new guest either:
- Boosts one of the two tracked dishes (if it matches)
- Starts a new tracking slot (if a slot is free and dish is different)
- Cancels both tracked dishes by 1 (if it matches neither and both slots are full)

Since at most 2 elements can appear more than n/3 times, both true majority elements will always survive the cancellation process and remain as candidates. A second pass verifies them.

**Key rule:** When `count1 == 0` and `nums[i] != ele2`, set `ele1 = nums[i]`. This prevents both candidates from being the same element.

#### 📝 Steps
1. Phase 1: Find 2 candidates using the voting logic
2. Phase 2: Count actual occurrences of both candidates
3. Add to result if count >= `n/3 + 1`

#### 💻 Code
```java
class Solution {
    public List<Integer> majorityElementTwo(int[] nums) {
        int n = nums.length;
        List<Integer> list = new ArrayList<>();
        int count1 = 0, count2 = 0;
        int ele1 = 0, ele2 = 0;
        int min = n / 3 + 1;

        // Phase 1: Find candidates
        for (int i = 0; i < n; i++) {
            if (count1 == 0 && ele2 != nums[i]) {
                ele1 = nums[i];
                count1++;
            } else if (count2 == 0 && ele1 != nums[i]) {
                ele2 = nums[i];
                count2++;
            } else if (nums[i] == ele1) count1++;
            else if (nums[i] == ele2) count2++;
            else {
                count1--;
                count2--;
            }
        }

        // Phase 2: Verify candidates
        int cnt1 = 0, cnt2 = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] == ele1) cnt1++;
            if (nums[i] == ele2) cnt2++;
        }

        if (cnt1 >= min) list.add(ele1);
        if (cnt2 >= min && ele1 != ele2) list.add(ele2);

        return list;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 2, 1, 1, 3, 2, 2]`, n=7, min=3

**Phase 1 — Find candidates:**

```
Start: ele1=0,count1=0, ele2=0,count2=0

i=0 (val=1): count1==0 & ele2≠1 → ele1=1, count1=1
i=1 (val=2): count2==0 & ele1≠2 → ele2=2, count2=1
i=2 (val=1): nums[2]==ele1 → count1=2
i=3 (val=1): nums[3]==ele1 → count1=3
i=4 (val=3): 3≠ele1 & 3≠ele2 → count1=2, count2=0
i=5 (val=2): count2==0 & ele1≠2 → ele2=2, count2=1
i=6 (val=2): nums[6]==ele2 → count2=2

Candidates: ele1=1, ele2=2
```

**Phase 2 — Verify:**
```
Count of 1 in array = 3 → 3 >= 3 → add 1
Count of 2 in array = 3 → 3 >= 3 → add 2

Return [1, 2] ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Nested Loops | O(N²) | O(1) |
| HashMap | O(N) | O(N) |
| Extended Boyer-Moore | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Grid Unique Paths

### 🧩 Problem Statement

Given two integers `m` and `n` (rows and columns of a grid), find the number of **unique paths** from the top-left cell `(0,0)` to the bottom-right cell `(m-1,n-1)`. You can only move **right** or **down**.

**Example 1:**
```
Input:  m=3, n=2
Output: 3
Paths: right→down→down | down→right→down | down→down→right
```

**Example 2:**
```
Input:  m=2, n=4
Output: 4
```

**Example 3:**
```
Input:  m=3, n=3
Output: 6
```

**Constraints:**
- `1 <= m, n <= 100`
- Answer will not exceed 10⁹

---

### 🔴 Approach 1 — Recursion (Pure)
**Time: O(2^(M+N)) | Space: O(M+N) recursion stack**

#### 💡 Intuition
Think in reverse — start from `(m-1, n-1)` and find how many ways lead to `(0,0)`. At each cell, we could have arrived from **above** (row-1) or from the **left** (col-1). So the total paths = paths from above + paths from left.

**Base cases:**
- At `(0,0)` → 1 path (we've arrived)
- Out of bounds (i<0 or j<0) → 0 paths (invalid)

#### 💻 Code
```java
class Solution {
    private int func(int i, int j) {
        if (i == 0 && j == 0) return 1;
        if (i < 0 || j < 0) return 0;
        int up = func(i - 1, j);
        int left = func(i, j - 1);
        return up + left;
    }

    public int uniquePaths(int m, int n) {
        return func(m - 1, n - 1);
    }
}
```

#### 🧪 Dry Run

Input: `m=3, n=2` → `func(2, 1)`

```
func(2,1)
├── func(1,1)
│   ├── func(0,1)
│   │   ├── func(-1,1) = 0
│   │   └── func(0,0)  = 1  → return 1
│   └── func(1,0)
│       ├── func(0,0)  = 1
│       └── func(1,-1) = 0  → return 1
│   → return 1+1 = 2
└── func(2,0)
    ├── func(1,0)
    │   ├── func(0,0) = 1
    │   └── func(1,-1) = 0 → return 1
    └── func(2,-1) = 0
    → return 1+0 = 1

func(2,1) = 2+1 = 3 ✅
```

#### ⚠️ Limitation
Many subproblems are recomputed. For `m=n=100`, this hits TLE instantly.

---

### 🟡 Approach 2 — Memoization (Top-Down DP)
**Time: O(M×N) | Space: O(M×N) + O(M+N) stack**

#### 💡 Intuition
Same recursion, but store results in a `dp[i][j]` table. Before computing, check if `dp[i][j] != -1` — if so, return the stored value directly. This avoids recomputing the same subproblem.

#### 💻 Code
```java
class Solution {
    private int func(int i, int j, int[][] dp) {
        if (i == 0 && j == 0) return 1;
        if (i < 0 || j < 0) return 0;
        if (dp[i][j] != -1) return dp[i][j]; // Already computed

        int up = func(i - 1, j, dp);
        int left = func(i, j - 1, dp);
        return dp[i][j] = up + left;
    }

    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int[] row : dp) Arrays.fill(row, -1);
        return func(m - 1, n - 1, dp);
    }
}
```

#### 🧪 Dry Run

Input: `m=3, n=2`

```
dp initialized to all -1

func(2,1):
  func(1,1):
    func(0,1):
      func(-1,1)=0, func(0,0)=1 → dp[0][1]=1
    func(1,0):
      func(0,0)=1, func(1,-1)=0 → dp[1][0]=1
    → dp[1][1] = 1+1 = 2
  func(2,0):
    func(1,0) → dp[1][0]=1 (already stored, no recompute!)
    func(2,-1)=0 → dp[2][0]=1
  → dp[2][1] = 2+1 = 3

Return 3 ✅

dp table:
[[-1, 1],
 [ 1, 2],
 [ 1, 3]]
```

---

### 🟡 Approach 3 — Tabulation (Bottom-Up DP)
**Time: O(M×N) | Space: O(M×N)**

#### 💡 Intuition
Build the dp table **bottom-up** — start from `(0,0)` and fill to `(m-1,n-1)`. Each cell's value = `dp[i-1][j] + dp[i][j-1]` (from above + from left). No recursion needed.

#### 📝 Steps
1. Create `dp[m][n]`
2. `dp[0][0] = 1` (base case)
3. For each cell: `dp[i][j] = up + left` (handle boundary with `if i>0` checks)
4. Return `dp[m-1][n-1]`

#### 💻 Code
```java
class Solution {
    private int func(int m, int n, int[][] dp) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    dp[i][j] = 1;
                    continue;
                }
                int up = 0, left = 0;
                if (i > 0) up = dp[i - 1][j];
                if (j > 0) left = dp[i][j - 1];
                dp[i][j] = up + left;
            }
        }
        return dp[m - 1][n - 1];
    }

    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        return func(m, n, dp);
    }
}
```

#### 🧪 Dry Run

Input: `m=3, n=2`

```
Fill cell by cell:

(0,0): base → dp[0][0]=1
(0,1): i=0,j=1 → up=0, left=dp[0][0]=1 → dp[0][1]=1
(1,0): i=1,j=0 → up=dp[0][0]=1, left=0 → dp[1][0]=1
(1,1): up=dp[0][1]=1, left=dp[1][0]=1 → dp[1][1]=2
(2,0): up=dp[1][0]=1, left=0 → dp[2][0]=1
(2,1): up=dp[1][1]=2, left=dp[2][0]=1 → dp[2][1]=3

dp table:
[[1, 1],
 [1, 2],
 [1, 3]]

Return dp[2][1] = 3 ✅
```

---

### 🟢 Approach 4 — Space Optimized DP
**Time: O(M×N) | Space: O(N)**

#### 💡 Intuition
Notice from tabulation: `dp[i][j]` only needs `dp[i-1][j]` (previous row, same col) and `dp[i][j-1]` (current row, previous col). We don't need the entire 2D array — just **one row at a time**.

Keep `prev` as the previous row. For each new row, compute `temp` using `prev` and `temp` itself.

#### 💻 Code
```java
class Solution {
    private int func(int m, int n) {
        int[] prev = new int[n];
        for (int i = 0; i < m; i++) {
            int[] temp = new int[n];
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    temp[j] = 1;
                    continue;
                }
                int up = (i > 0) ? prev[j] : 0;
                int left = (j > 0) ? temp[j - 1] : 0;
                temp[j] = up + left;
            }
            prev = temp; // Move to next row
        }
        return prev[n - 1];
    }

    public int uniquePaths(int m, int n) {
        return func(m, n);
    }
}
```

#### 🧪 Dry Run

Input: `m=3, n=2`

```
prev = [0, 0]

i=0:
  j=0: base → temp[0]=1
  j=1: up=prev[1]=0, left=temp[0]=1 → temp[1]=1
  prev = [1, 1]

i=1:
  j=0: up=prev[0]=1, left=0 → temp[0]=1
  j=1: up=prev[1]=1, left=temp[0]=1 → temp[1]=2
  prev = [1, 2]

i=2:
  j=0: up=prev[0]=1, left=0 → temp[0]=1
  j=1: up=prev[1]=2, left=temp[0]=1 → temp[1]=3
  prev = [1, 3]

Return prev[1] = 3 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Recursion | O(2^(M+N)) | O(M+N) |
| Memoization | O(M×N) | O(M×N) + O(M+N) |
| Tabulation | O(M×N) | O(M×N) |
| Space Optimized | **O(M×N)** ✅ | **O(N)** ✅ |

---

## 3. Reverse Pairs

### 🧩 Problem Statement

Given an integer array `nums`, return the number of **reverse pairs** — index pairs `(i, j)` where:
- `0 <= i < j < nums.length`
- `nums[i] > 2 * nums[j]`

**Example 1:**
```
Input:  nums = [6, 4, 1, 2, 7]
Output: 3
Pairs: (0,2): 6>2*1, (0,3): 6>2*2, (1,2): 4>2*1
```

**Example 2:**
```
Input:  nums = [5, 4, 4, 3, 3]
Output: 0
```

**Constraints:**
- `1 <= nums.length <= 5 × 10⁴`
- `-2³¹ <= nums[i] <= 2³¹ - 1`

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
Check every pair `(i, j)` where `i < j`. If `nums[i] > 2 * nums[j]`, it's a reverse pair. Count them all.

> 💡 **Why `(long)` cast?** `nums[i]` can be up to 2³¹-1. Multiplying by 2 overflows `int`. Casting to `long` before multiplication prevents this.

#### 💻 Code
```java
class Solution {
    public int reversePairs(int[] nums) {
        int n = nums.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((long) nums[i] > (long) 2 * nums[j]) {
                    count++;
                }
            }
        }
        return count;
    }
}
```

#### 🧪 Dry Run

Input: `[6, 4, 1, 2, 7]`

```
i=0 (val=6):
  j=1: 6 > 2*4=8? No
  j=2: 6 > 2*1=2? Yes → count=1
  j=3: 6 > 2*2=4? Yes → count=2
  j=4: 6 > 2*7=14? No

i=1 (val=4):
  j=2: 4 > 2*1=2? Yes → count=3
  j=3: 4 > 2*2=4? No (not strictly greater)
  j=4: 4 > 2*7=14? No

i=2,3,4: no valid j found

Return 3 ✅
```

---

### 🟢 Approach 2 — Optimal (Modified Merge Sort)
**Time: O(2N × log N) | Space: O(N)**

#### 💡 Intuition
Same idea as Count Inversions — use Merge Sort. But there's an **important difference**:

- **Count Inversions:** condition is `arr[left] > arr[right]` — we can count AND merge simultaneously
- **Reverse Pairs:** condition is `arr[i] > 2 * arr[j]` — we must **count BEFORE merging**, because merging changes element positions

**Why count before merge?**
During counting, we use two pointers on the sorted left and right halves. For each `nums[i]` in the left half, we find how many elements in the right half satisfy `nums[i] > 2 * nums[right]`. Since both halves are sorted:
- If `nums[i] > 2 * nums[right]`, then all `nums[i+1..mid]` also satisfy this (they're all >= nums[i])
- So we count `right - (mid+1)` pairs at once for position `i`

We call `countPairs` first, then `mergeNum` separately.

#### 📝 Steps
1. `mergeSort(nums, low, high)`:
   - Recursively sort left half, count its pairs
   - Recursively sort right half, count its pairs
   - Count cross pairs using `countPairs` (on sorted halves)
   - Merge the two halves with `mergeNum`

2. `countPairs(nums, low, mid, high)`:
   - Two pointers: `i` scans left half, `right` scans right half
   - For each `i`, advance `right` while `nums[i] > 2 * nums[right]`
   - Add `right - (mid+1)` to count

3. `mergeNum` — standard merge into sorted order

#### 💻 Code
```java
class Solution {
    private int mergeSort(int[] nums, int low, int high) {
        if (low >= high) return 0;
        int mid = (low + high) / 2;
        int count = 0;

        count += mergeSort(nums, low, mid);
        count += mergeSort(nums, mid + 1, high);
        count += countPairs(nums, low, mid, high); // Count BEFORE merging
        mergeNum(nums, low, mid, high);             // Then merge

        return count;
    }

    private int countPairs(int[] nums, int low, int mid, int high) {
        int right = mid + 1, count = 0;
        for (int i = low; i <= mid; i++) {
            while (right <= high && (long) nums[i] > 2L * nums[right]) {
                right++;
            }
            count += (right - (mid + 1));
        }
        return count;
    }

    private void mergeNum(int[] nums, int low, int mid, int high) {
        int left = low, right = mid + 1;
        List<Integer> list = new ArrayList<>();

        while (left <= mid && right <= high) {
            if (nums[left] <= nums[right]) list.add(nums[left++]);
            else list.add(nums[right++]);
        }
        while (left <= mid) list.add(nums[left++]);
        while (right <= high) list.add(nums[right++]);

        for (int i = low; i <= high; i++) {
            nums[i] = list.get(i - low);
        }
    }

    public int reversePairs(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }
}
```

#### 🧪 Dry Run

Input: `[6, 4, 1, 2, 7]`

**Recursion overview:**
```
mergeSort([6,4,1,2,7], 0, 4)
├── mergeSort([6,4,1], 0, 2)
│   ├── mergeSort([6,4], 0, 1)
│   │   ├── mergeSort([6], 0, 0) = 0
│   │   ├── mergeSort([4], 1, 1) = 0
│   │   ├── countPairs([6],[4]) → 6>2*4? No → 0
│   │   └── mergeNum → [4,6]
│   ├── mergeSort([1], 2, 2) = 0
│   ├── countPairs([4,6],[1]):
│   │   i=0(val=4): 4>2*1=2? Yes, right=3 → count+=3-(2+1)=1
│   │   i=1(val=6): 6>2*1=2? Yes, right already=3 → count+=3-3=0+1=1 (total=2 here)
│   │   Wait: right doesn't reset per i — it advances monotonically
│   │   i=0(val=4): right starts at mid+1=2, nums[right]=1, 4>2=2? Yes, right=3
│   │     count += 3-3=0? No: count += right-(mid+1) = 3-3=0
│   │   Wait let me redo: low=0,mid=1,high=2
│   │   left half=[4,6] (indices 0,1), right half=[1] (index 2)
│   │   i=0(val=4): right=2, nums[2]=1, 4>2*1=2? Yes → right=3
│   │     count += 3-2=1
│   │   i=1(val=6): right=3 > high=2, loop ends
│   │     count += 3-2=1 → total=2
│   └── mergeNum → [1,4,6]
│
└── mergeSort([2,7], 3, 4)
    ├── mergeSort([2], 3, 3) = 0
    ├── mergeSort([7], 4, 4) = 0
    ├── countPairs([2],[7]): 2>2*7=14? No → 0
    └── mergeNum → [2,7]

countPairs([1,4,6], [2,7]):  low=0,mid=2,high=4
  left half=[1,4,6], right half=[2,7]
  i=0(val=1): right=3, 1>2*2=4? No → count+=3-3=0
  i=1(val=4): right=3, 4>2*2=4? No → count+=3-3=0
  i=2(val=6): right=3, 6>2*2=4? Yes → right=4
              6>2*7=14? No → stop
              count+=4-3=1
  Cross pairs = 1

Total = 2 (left subtree) + 0 (right subtree) + 1 (cross) = 3 ✅
```

> 💡 **Why `right` doesn't reset in `countPairs`?**
> The left half is sorted. If `nums[i] > 2*nums[right]`, then `nums[i+1] >= nums[i]` also satisfies the condition. So `right` only needs to move forward — never backwards. This makes the inner while loop O(N) total across all `i` iterations, not O(N) per `i`.

> 💡 **Why must we count BEFORE merging?**
> The condition `nums[i] > 2*nums[j]` requires the original relative order of elements. If we merge first, elements get rearranged and we lose the ability to correctly count cross-half pairs.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(1) |
| Modified Merge Sort | **O(2N × log N)** ✅ | **O(N)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Majority Element-II | At most 2 elements can appear > n/3 times — extend Boyer-Moore to track 2 candidates with 2 counts |
| Grid Unique Paths | Classic DP — paths to (i,j) = paths from above + paths from left. Optimize space to O(N) using one row |
| Reverse Pairs | Count BEFORE merge (not during) — the condition `nums[i] > 2*nums[j]` requires unmerged positions |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*