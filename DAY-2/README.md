# 🚀 Day 2/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Kadane's Algorithm](#1-kadanes-algorithm) | 🟡 Medium | Arrays / Kadane's Algorithm |
| 2 | [Sort an Array of 0's, 1's and 2's](#2-sort-an-array-of-0s-1s-and-2s) | 🟡 Medium | Arrays /Dutch National Flag|
| 3 | [Stock Buy and Sell](#3-stock-buy-and-sell) | 🟡 Medium | Arrays / Prefix Minimum |

---

## 1. Kadane's Algorithm

### 🧩 Problem Statement

Given an integer array `nums`, find the **subarray with the largest sum** and return that sum.

A subarray is a **contiguous non-empty** sequence of elements within an array.

**Example 1:**
```
Input:  nums = [2, 3, 5, -2, 7, -4]
Output: 15
Explanation: Subarray [2, 3, 5, -2, 7] (index 0 to 4) has the maximum sum = 15
```

**Example 2:**
```
Input:  nums = [-2, -3, -7, -2, -10, -4]
Output: -2
Explanation: All elements are negative. Best single element = -2
```

**Example 3:**
```
Input:  nums = [-1, 2, 3, -1, 2, -6, 5]
Output: 6
```

**Constraints:**
- `1 <= nums.length <= 10⁵`
- `-10⁴ <= nums[i] <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (3 Nested Loops)
**Time: O(N³) | Space: O(1)**

#### 💡 Intuition
Try every possible subarray `[i..j]`. For each pair `(i, j)`, use a third loop `k` to compute the sum from scratch. Track the maximum sum seen across all subarrays.

#### 📝 Steps
1. Outer loop `i` → starting index of the subarray
2. Middle loop `j` → ending index of the subarray
3. Inner loop `k` → sums all elements from index `i` to `j`
4. Update `maxVal` if the current sum is larger

#### 💻 Code
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += nums[k];
                }
                maxVal = Math.max(maxVal, sum);
            }
        }
        return maxVal;
    }
}
```

#### 🧪 Dry Run

Input: `[2, 3, 5, -2, 7, -4]`

```
i=0, j=0: sum of [2]              = 2   → maxVal = 2
i=0, j=1: sum of [2, 3]           = 5   → maxVal = 5
i=0, j=2: sum of [2, 3, 5]        = 10  → maxVal = 10
i=0, j=3: sum of [2, 3, 5, -2]    = 8
i=0, j=4: sum of [2, 3, 5, -2, 7] = 15  → maxVal = 15
i=0, j=5: sum of all              = 11
i=1, j=1: sum of [3]              = 3
...
(remaining iterations don't exceed 15)

Return 15 ✅
```

#### ⚠️ Why this fails
Three nested loops → O(N³). For N = 10⁵, this is 10¹⁵ operations — causes **Time Limit Exceeded (TLE)** on large inputs.

---

### 🟡 Approach 2 — Better Brute Force (2 Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
Instead of recomputing the sum from scratch for every `[i..j]`, carry a **running sum** as we extend `j`. This eliminates the third loop entirely.

#### 📝 Steps
1. Outer loop `i` → starting index
2. Inner loop `j` → extend subarray by adding `nums[j]` to running `sum`
3. Update `maxVal` at every `j`

#### 💻 Code
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += nums[j];
                maxVal = Math.max(maxVal, sum);
            }
        }
        return maxVal;
    }
}
```

#### 🧪 Dry Run

Input: `[2, 3, 5, -2, 7, -4]`

```
i=0:
  j=0: sum = 0+2   = 2   → maxVal = 2
  j=1: sum = 2+3   = 5   → maxVal = 5
  j=2: sum = 5+5   = 10  → maxVal = 10
  j=3: sum = 10-2  = 8
  j=4: sum = 8+7   = 15  → maxVal = 15
  j=5: sum = 15-4  = 11

i=1:
  j=1: sum = 3     → maxVal = 15 (no change)
  j=2: sum = 3+5   = 8
  j=3: sum = 8-2   = 6
  j=4: sum = 6+7   = 13
  j=5: sum = 13-4  = 9

i=2:
  j=2: sum = 5
  j=3: sum = 5-2   = 3
  j=4: sum = 3+7   = 10
  j=5: sum = 10-4  = 6

... (no iteration exceeds 15)

Return 15 ✅
```

Better than O(N³) but still O(N²) — can still TLE for N = 10⁵.

---

### 🟢 Approach 3 — Kadane's Algorithm (Optimal)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
The core insight: **a negative running sum is a liability, not an asset.**

If at any point our running sum drops below `0`, adding it to the next element will only make the result *smaller*. So we discard the current subarray and start fresh.

Think of it as: *"Would carrying my history forward help the next element? If the running sum is negative — no. Drop it and start clean."*

#### 📝 Steps
1. Initialize `sum = 0`, `maxSum = Long.MIN_VALUE`
2. Add `nums[i]` to `sum`
3. Update `maxSum = max(maxSum, sum)`
4. If `sum < 0`, reset `sum = 0` (start a fresh subarray)
5. Return `maxSum`

#### 💻 Code
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        long maxSum = Long.MIN_VALUE;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            maxSum = Math.max(maxSum, sum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return (int) maxSum;
    }
}
```

#### 🧪 Dry Run — Normal Case

Input: `[2, 3, 5, -2, 7, -4]`

```
i=0: sum = 0+2  = 2   → maxSum = 2   | sum≥0, keep going
i=1: sum = 2+3  = 5   → maxSum = 5   | sum≥0, keep going
i=2: sum = 5+5  = 10  → maxSum = 10  | sum≥0, keep going
i=3: sum = 10-2 = 8   → maxSum = 10  | sum≥0, keep going
i=4: sum = 8+7  = 15  → maxSum = 15  | sum≥0, keep going
i=5: sum = 15-4 = 11  → maxSum = 15  | sum≥0, keep going

Return 15 ✅
```

#### 🧪 Dry Run — All Negative Case

Input: `[-2, -3, -7, -2, -10, -4]`

```
i=0: sum = -2  → maxSum = -2  | sum<0, reset sum=0
i=1: sum = -3  → maxSum = -2  | sum<0, reset sum=0
i=2: sum = -7  → maxSum = -2  | sum<0, reset sum=0
i=3: sum = -2  → maxSum = -2  | sum<0, reset sum=0
i=4: sum = -10 → maxSum = -2  | sum<0, reset sum=0
i=5: sum = -4  → maxSum = -2  | sum<0, reset sum=0

Return -2 ✅
```

> 💡 **Why `Long.MIN_VALUE` and not `0`?**
> If all elements are negative, the answer is the least negative element — a negative number, not 0.
> Initializing with `0` would incorrectly return `0` for an all-negative array.

> 💡 **Why use `long` for `maxSum` and `sum`?**
> With N up to 10⁵ and values up to 10⁴, the maximum possible sum = 10⁵ × 10⁴ = 10⁹ which fits in `int`, but intermediate values and `Long.MIN_VALUE` make `long` safer here.

#### 📊 Complexity Summary

| Approach | Time | Space | Notes |
|---------|------|-------|-------|
| 3-Loop Brute Force | O(N³) | O(1) | TLE for large N |
| 2-Loop Better | O(N²) | O(1) | TLE for large N |
| Kadane's Algorithm | **O(N)** ✅ | **O(1)** ✅ | Single pass, optimal |

---

## 2. Sort an Array of 0's, 1's and 2's

### 🧩 Problem Statement

Given an array `nums` consisting of only `0`, `1`, and `2`, sort it in **non-decreasing order in-place** — without making a copy of the array.

**Example 1:**
```
Input:  nums = [1, 0, 2, 1, 0]
Output: [0, 0, 1, 1, 2]
Explanation: 2 zeroes, 2 ones, 1 two
```

**Example 2:**
```
Input:  nums = [0, 0, 1, 1, 1]
Output: [0, 0, 1, 1, 1]
Explanation: 2 zeroes, 3 ones, 0 twos
```

**Constraints:**
- `1 <= nums.length <= 10⁵`
- `nums[i]` is `0`, `1`, or `2` only

---

### 🔴 Approach 1 — Brute Force (Sorting)
**Time: O(N log N) | Space: O(1)**

#### 💡 Intuition
The simplest idea: just call `Arrays.sort()`. Since sorting arranges elements in non-decreasing order, the result is automatically `[0...0, 1...1, 2...2]`.

#### 📝 Step
Call `Arrays.sort(nums)` — done in one line.

#### 💻 Code
```java
class Solution {
    public void sortZeroOneTwo(int[] nums) {
        Arrays.sort(nums);
    }
}
```

#### 🧪 Dry Run

Input: `[1, 0, 2, 1, 0]`

```
Arrays.sort() → internally uses Dual-Pivot QuickSort
Output: [0, 0, 1, 1, 2] ✅
```

#### ⚠️ Why we don't stop here
This is a general-purpose sort. It doesn't exploit the key constraint — **the array contains only 3 distinct values (0, 1, 2)**. We can do much better. O(N log N) is unnecessary when O(N) is achievable.

---

### 🟡 Approach 2 — Count and Overwrite
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Count how many 0s, 1s, and 2s appear. Then fill the array back in order: first all 0s, then all 1s, then all 2s.

#### 📝 Steps
1. Traverse the array and count `count0`, `count1`, `count2`
2. Fill indices `[0, count0)` with `0`
3. Fill indices `[count0, count0+count1)` with `1`
4. Fill remaining indices with `2`

#### 💻 Code
```java
class Solution {
    public void sortZeroOneTwo(int[] nums) {
        int count0 = 0, count1 = 0, count2 = 0;
        int k = nums.length;

        for (int i = 0; i < k; i++) {
            if (nums[i] == 0) count0++;
            else if (nums[i] == 1) count1++;
            else count2++;
        }

        int n = count0;
        int m = count0 + count1;

        for (int i = 0; i < n; i++)       nums[i] = 0;
        for (int i = n; i < m; i++)       nums[i] = 1;
        for (int i = m; i < k; i++)       nums[i] = 2;
    }
}
```

#### 🧪 Dry Run

Input: `[2, 0, 1, 2, 0, 1, 1]`

**Pass 1 — Count:**
```
i=0: nums[0]=2 → count2=1
i=1: nums[1]=0 → count0=1
i=2: nums[2]=1 → count1=1
i=3: nums[3]=2 → count2=2
i=4: nums[4]=0 → count0=2
i=5: nums[5]=1 → count1=2
i=6: nums[6]=1 → count1=3

count0=2, count1=3, count2=2
n=2, m=5, k=7
```

**Pass 2 — Overwrite:**
```
Fill indices [0,2)  with 0: nums = [0, 0, 1, 2, 0, 1, 1]
Fill indices [2,5)  with 1: nums = [0, 0, 1, 1, 1, 1, 1]
Fill indices [5,7)  with 2: nums = [0, 0, 1, 1, 1, 2, 2]

Output: [0, 0, 1, 1, 1, 2, 2] ✅
```

#### ⚠️ Limitation
Requires **two separate passes** over the array. The Dutch National Flag algorithm solves this in just **one pass**.

---

### 🟢 Approach 3 — Dutch National Flag Algorithm (Optimal)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
This is the classic **Dutch National Flag problem** proposed by Edsger Dijkstra. We use **3 pointers** — `low`, `mid`, `high` — to maintain three regions in the array simultaneously:

```
[0 ... low-1]  → confirmed 0s  (sorted)
[low ... mid-1] → confirmed 1s  (sorted)
[mid ... high]  → unknown region (yet to process)
[high+1 ... n-1] → confirmed 2s  (sorted)
```

The `mid` pointer scans through the unknown region. Based on `nums[mid]`:

| `nums[mid]` | Action | Why |
|-------------|--------|-----|
| `0` | swap(`nums[low]`, `nums[mid]`), `low++`, `mid++` | 0 belongs to the left region |
| `1` | `mid++` only | 1 is already in the correct middle region |
| `2` | swap(`nums[mid]`, `nums[high]`), `high--` | 2 belongs to the right region. **Don't increment `mid`** — the swapped value from `high` is unknown and needs to be re-examined |

#### 📝 Steps
1. Initialize `low = 0`, `mid = 0`, `high = n-1`
2. While `mid <= high`:
   - If `nums[mid] == 0` → swap with `low`, increment both `low` and `mid`
   - If `nums[mid] == 1` → increment `mid` only
   - If `nums[mid] == 2` → swap with `high`, decrement `high` only
3. Stop when `mid > high`

#### 💻 Code
```java
class Solution {
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public void sortZeroOneTwo(int[] nums) {
        int n = nums.length;
        int low = 0, mid = 0, high = n - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                swap(nums, mid, high);
                high--;
            }
        }
    }
}
```

#### 🧪 Dry Run

Input: `[1, 0, 2, 1, 0]`   (n=5)

Initial: `low=0, mid=0, high=4`
```
Array: [1, 0, 2, 1, 0]
        ↑           ↑
       low,mid     high
```

**Iteration 1:** `nums[mid=0] = 1`
```
→ It's a 1. mid++ only.
low=0, mid=1, high=4
Array: [1, 0, 2, 1, 0]
```

**Iteration 2:** `nums[mid=1] = 0`
```
→ It's a 0. swap(low=0, mid=1), low++, mid++
swap(nums[0], nums[1]): [0, 1, 2, 1, 0]
low=1, mid=2, high=4
```

**Iteration 3:** `nums[mid=2] = 2`
```
→ It's a 2. swap(mid=2, high=4), high--  (DON'T move mid)
swap(nums[2], nums[4]): [0, 1, 0, 1, 2]
low=1, mid=2, high=3
```

**Iteration 4:** `nums[mid=2] = 0`
```
→ It's a 0. swap(low=1, mid=2), low++, mid++
swap(nums[1], nums[2]): [0, 0, 1, 1, 2]
low=2, mid=3, high=3
```

**Iteration 5:** `nums[mid=3] = 1`
```
→ It's a 1. mid++ only.
low=2, mid=4, high=3
```

**Check:** `mid(4) > high(3)` → Loop ends ✅

```
Output: [0, 0, 1, 1, 2] ✅
```

> 💡 **Why don't we increment `mid` after swapping with `high`?**
> The element that came from `high` was in the "unknown" region — we've never examined it. If we moved `mid` forward, we'd skip checking it. So we stay at the same `mid` position and re-examine the newly swapped value in the next iteration.

#### 📊 Complexity Summary

| Approach | Time | Space | Passes |
|---------|------|-------|--------|
| Sorting (`Arrays.sort`) | O(N log N) | O(1) | 1 |
| Count & Overwrite | O(N) | O(1) | **2** |
| Dutch National Flag | **O(N)** ✅ | **O(1)** ✅ | **1** ✅ |

---

## 3. Stock Buy and Sell

### 🧩 Problem Statement

Given an array `arr` of `n` integers where `arr[i]` is the stock price on day `i`, find the **maximum profit** from **at most one buy and one sell**. You must buy before you sell. Both cannot happen on the same day. If no profit is possible, return `0`.

**Example 1:**
```
Input:  arr = [10, 7, 5, 8, 11, 9]
Output: 6
Explanation: Buy on day 3 (price=5), sell on day 5 (price=11). Profit = 11-5 = 6.
```

**Example 2:**
```
Input:  arr = [5, 4, 3, 2, 1]
Output: 0
Explanation: Prices only go down. No profitable transaction possible.
```

**Constraints:**
- `1 <= n <= 10⁵`
- `0 <= arr[i] <= 10⁶`

---

### 🔴 Approach 1 — Brute Force
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
Try every possible pair of days `(i, j)` where `i < j` (buy on day `i`, sell on day `j`). Compute profit `arr[j] - arr[i]` for each pair and track the maximum.

#### 📝 Steps
1. Outer loop `i` → buy day
2. Inner loop `j` → sell day (always `j > i`)
3. Compute `profit = arr[j] - arr[i]`
4. Track maximum profit

#### 🧪 Dry Run

Input: `[10, 7, 5, 8, 11, 9]`

```
i=0 (buy @ 10):
  j=1: 7-10  = -3
  j=2: 5-10  = -5
  j=3: 8-10  = -2
  j=4: 11-10 =  1  → maxProfit = 1
  j=5: 9-10  = -1

i=1 (buy @ 7):
  j=2: 5-7   = -2
  j=3: 8-7   =  1
  j=4: 11-7  =  4  → maxProfit = 4
  j=5: 9-7   =  2

i=2 (buy @ 5):
  j=3: 8-5   =  3
  j=4: 11-5  =  6  → maxProfit = 6 ✅
  j=5: 9-5   =  4

i=3 (buy @ 8):
  j=4: 11-8  =  3
  j=5: 9-8   =  1

i=4 (buy @ 11):
  j=5: 9-11  = -2

Return 6 ✅
```

O(N²) — fine for small arrays, but TLE for N = 10⁵.

---

### 🟢 Approach 2 — Optimal (Single Pass / Greedy)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
To maximize profit, at every day we ask: *"What's the best I could have bought before today?"* — which is simply the **minimum price seen so far**.

So we track:
- `minPrice` → the lowest price seen up to today (best possible buy day so far)
- `maxProfit` → the best profit seen so far

At each day, `profit = currentPrice - minPrice`. Update `maxProfit` if this is better, then update `minPrice` if today's price is lower.

#### 📝 Steps
1. Initialize `minPrice = arr[0]`, `maxProfit = 0`
2. For each price `arr[i]` from `i = 0` to `n-1`:
   - `profit = arr[i] - minPrice`
   - `maxProfit = max(maxProfit, profit)`
   - `minPrice = min(minPrice, arr[i])`
3. Return `maxProfit`

#### 💻 Code
```java
class Solution {
    public int stockBuySell(int[] arr, int n) {
        int minPrice = arr[0];
        int maxProfit = 0;
        for (int i = 0; i < n; i++) {
            int profit = arr[i] - minPrice;
            maxProfit = Math.max(maxProfit, profit);
            minPrice = Math.min(minPrice, arr[i]);
        }
        return maxProfit;
    }
}
```

#### 🧪 Dry Run — Normal Case

Input: `[10, 7, 5, 8, 11, 9]`

```
Start: minPrice=10, maxProfit=0

i=0: price=10 | profit=10-10=0  | maxProfit=0 | minPrice=min(10,10)=10
i=1: price=7  | profit=7-10=-3  | maxProfit=0 | minPrice=min(10,7)=7
i=2: price=5  | profit=5-7=-2   | maxProfit=0 | minPrice=min(7,5)=5
i=3: price=8  | profit=8-5=3    | maxProfit=3 | minPrice=min(5,8)=5
i=4: price=11 | profit=11-5=6   | maxProfit=6 | minPrice=min(5,11)=5
i=5: price=9  | profit=9-5=4    | maxProfit=6 | minPrice=min(5,9)=5

Return 6 ✅
```

#### 🧪 Dry Run — Descending Prices (No Profit)

Input: `[5, 4, 3, 2, 1]`

```
Start: minPrice=5, maxProfit=0

i=0: price=5 | profit=5-5=0  | maxProfit=0 | minPrice=5
i=1: price=4 | profit=4-5=-1 | maxProfit=0 | minPrice=4
i=2: price=3 | profit=3-4=-1 | maxProfit=0 | minPrice=3
i=3: price=2 | profit=2-3=-1 | maxProfit=0 | minPrice=2
i=4: price=1 | profit=1-2=-1 | maxProfit=0 | minPrice=1

Return 0 ✅  (no profitable trade possible)
```

> 💡 **Why check profit before updating `minPrice`?**
> If we updated `minPrice` first, we might compute `profit = arr[i] - arr[i] = 0` — effectively allowing buy and sell on the same day, which is not allowed. Checking profit first ensures we always sell on a day *after* the recorded buy price.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(1) |
| Optimal (Greedy) | **O(N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Kadane's Algorithm | A negative running sum is a burden — reset to 0 and start fresh. Initialize with `MIN_VALUE`, not `0` |
| Sort 0s 1s 2s | Dutch National Flag: 3 pointers, 1 pass. Don't advance `mid` after swapping with `high` — re-examine the swapped value |
| Stock Buy & Sell | Track minimum price seen so far. Max profit at each step = current price − minimum so far |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
