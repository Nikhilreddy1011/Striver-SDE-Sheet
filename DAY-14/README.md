# 🚀 Day 14/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Trapping Rainwater](#1-trapping-rainwater) | 🔴 Hard | Arrays / Two Pointer |
| 2 | [Maximum Consecutive Ones](#2-maximum-consecutive-ones) | 🟢 Easy | Arrays |
| 3 | [Remove Duplicates from Sorted Array](#3-remove-duplicates-from-sorted-array) | 🟢 Easy | Arrays / Two Pointer |

---

## 1. Trapping Rainwater

### 🧩 Problem Statement

Given an array `height` representing elevation of ground, calculate the **total water trapped** after rain.

**Example 1:**
```
Input:  height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
```

**Example 2:**
```
Input:  height = [4,2,0,3,2,5]
Output: 9
Explanation: 2+4+1+2 = 9 units trapped
```

**Constraints:**
- `1 <= n <= 10⁵`
- `0 <= height[i] <= 10⁵`

---

### 🔴 Approach 1 — Brute Force (Prefix Max + Suffix Max)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Picture a side view after heavy rain. Water gets trapped in dips. The water level at any position is determined by the **shorter of the two barriers** — the tallest bar to its left and the tallest bar to its right.

**Formula:** `water[i] = min(leftMax[i], rightMax[i]) - height[i]` (if positive)

Build two arrays:
- `prefixMax[i]` = max height from `0` to `i`
- `suffixMax[i]` = max height from `i` to `n-1`

#### 💻 Code
```java
class Solution {
    private int[] prefix(int[] arr) {
        int n = arr.length;
        int[] prefixMax = new int[n];
        prefixMax[0] = arr[0];
        for (int i = 1; i < n; i++)
            prefixMax[i] = Math.max(prefixMax[i - 1], arr[i]);
        return prefixMax;
    }

    private int[] suffix(int[] arr) {
        int n = arr.length;
        int[] suffixMax = new int[n];
        suffixMax[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--)
            suffixMax[i] = Math.max(suffixMax[i + 1], arr[i]);
        return suffixMax;
    }

    public int trap(int[] height) {
        int n = height.length;
        int total = 0;
        int[] left = prefix(height);
        int[] right = suffix(height);

        for (int i = 0; i < n; i++) {
            if (height[i] < left[i] && height[i] < right[i]) {
                total += (Math.min(left[i], right[i]) - height[i]);
            }
        }
        return total;
    }
}
```

#### 🧪 Dry Run

Input: `[4, 2, 0, 3, 2, 5]`

```
prefixMax: [4, 4, 4, 4, 4, 5]
suffixMax: [5, 5, 5, 5, 5, 5]

i=0: height=4, left=4, right=5 → 4<4? No → 0
i=1: height=2, left=4, right=5 → 2<4 and 2<5 → min(4,5)-2 = 4-2 = 2
i=2: height=0, left=4, right=5 → min(4,5)-0 = 4
i=3: height=3, left=4, right=5 → min(4,5)-3 = 1
i=4: height=2, left=4, right=5 → min(4,5)-2 = 2
i=5: height=5, left=5, right=5 → 5<5? No → 0

Total = 2+4+1+2 = 9 ✅
```

#### ⚠️ Limitation
Two extra arrays of size N → O(N) space. Can be reduced to O(1).

---

### 🟢 Approach 2 — Optimal (Two Pointers)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
**Key realization:** At any position, only the **minimum** of left/right max matters. Instead of precomputing both arrays, traverse from both ends with two pointers, tracking `leftMax` and `rightMax` on the fly.

**Why does moving the smaller-height pointer work?**
If `height[left] <= height[right]`, then `rightMax` (whatever it is) is guaranteed to be `>= height[left]` — because there's at least one taller bar (`height[right]`) to the right. So the water level at `left` is determined entirely by `leftMax`. We can safely process `left` and move it forward — no need to know the exact `rightMax`.

#### 📝 Steps
1. `left=0, right=n-1, leftMax=0, rightMax=0, total=0`
2. While `left < right`:
   - If `height[left] <= height[right]`:
     - If `leftMax > height[left]` → `total += leftMax - height[left]`
     - Else → `leftMax = height[left]`
     - `left++`
   - Else (symmetric for right)

#### 💻 Code
```java
class Solution {
    public int trap(int[] height) {
        int n = height.length;
        int total = 0;
        int leftMax = 0, rightMax = 0;
        int left = 0, right = n - 1;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (leftMax > height[left]) {
                    total += leftMax - height[left];
                } else {
                    leftMax = height[left];
                }
                left++;
            } else {
                if (rightMax > height[right]) {
                    total += rightMax - height[right];
                } else {
                    rightMax = height[right];
                }
                right--;
            }
        }
        return total;
    }
}
```

#### 🧪 Dry Run

Input: `[4, 2, 0, 3, 2, 5]`

```
left=0, right=5, leftMax=0, rightMax=0, total=0

Step 1: height[0]=4 <= height[5]=5? Yes
  leftMax(0) > height[0]=4? No → leftMax=4
  left=1

Step 2: height[1]=2 <= height[5]=5? Yes
  leftMax(4) > height[1]=2? Yes → total += 4-2=2 → total=2
  left=2

Step 3: height[2]=0 <= height[5]=5? Yes
  leftMax(4) > height[2]=0? Yes → total += 4-0=4 → total=6
  left=3

Step 4: height[3]=3 <= height[5]=5? Yes
  leftMax(4) > height[3]=3? Yes → total += 4-3=1 → total=7
  left=4

Step 5: height[4]=2 <= height[5]=5? Yes
  leftMax(4) > height[4]=2? Yes → total += 4-2=2 → total=9
  left=5

left==right → exit loop

Return total = 9 ✅
```

> 💡 **Why `leftMax > height[left]` check (else update leftMax)?**
> If the current height is greater than or equal to `leftMax`, it becomes the new barrier — no water can sit on top of it (it IS the barrier). Only when current height is below the barrier does water accumulate.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Prefix + Suffix Max | O(N) | O(N) |
| Two Pointers | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Maximum Consecutive Ones

### 🧩 Problem Statement

Given a binary array `nums`, return the **maximum number of consecutive 1s**.

**Example 1:**
```
Input:  nums = [1,1,0,0,1,1,1,0]
Output: 3
```

**Example 2:**
```
Input:  nums = [0,0,0,0,0,0,0,0]
Output: 0
```

---

### 🟢 Approach — Single Pass Counter
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Maintain a running `count` of consecutive 1s. On every 1, increment and update `maxCount`. On every 0, reset `count` to 0.

#### 💻 Code
```java
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int n = nums.length;
        int count = 0;
        int maxCount = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                count++;
                maxCount = Math.max(count, maxCount);
            } else {
                count = 0;
            }
        }
        return maxCount;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 0, 1, 1, 1, 0, 1, 1, 1]`

```
i=0 (1): count=1, maxCount=1
i=1 (0): count=0
i=2 (1): count=1, maxCount=1
i=3 (1): count=2, maxCount=2
i=4 (1): count=3, maxCount=3
i=5 (0): count=0
i=6 (1): count=1, maxCount=3
i=7 (1): count=2, maxCount=3
i=8 (1): count=3, maxCount=3

Return 3 ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | **O(1)** ✅ |

---

## 3. Remove Duplicates from Sorted Array

### 🧩 Problem Statement

Given an array `nums` sorted in non-decreasing order, **remove duplicates in-place** so each unique element appears once. Return the count `k` of unique elements — the first `k` elements of `nums` must contain the unique values in original order.

**Example 1:**
```
Input:  nums = [0,0,3,3,5,6]
Output: 4
Resulting array = [0,3,5,6,_,_]
```

**Example 2:**
```
Input:  nums = [-2,2,4,4,4,4,5,5]
Output: 4
Resulting array = [-2,2,4,5,_,_,_,_]
```

**Constraints:**
- `1 <= nums.length <= 10⁵`
- Array is sorted in non-decreasing order

---

### 🔴 Approach 1 — Brute Force (TreeSet)
**Time: O(N log N) | Space: O(N)**

#### 💡 Intuition
A Set automatically eliminates duplicates. Use a `TreeSet` (sorted), add all elements, then write them back into `nums`.

#### 💻 Code
```java
class Solution {
    public int removeDuplicates(int[] nums) {
        Set<Integer> set = new TreeSet<>();
        for (int num : nums) set.add(num);

        int length = set.size();
        int index = 0;
        for (int num : set) {
            nums[index] = num;
            index++;
        }
        return length;
    }
}
```

#### 🧪 Dry Run

Input: `[1, 1, 2, 2, 2, 3, 3]`

```
TreeSet (sorted, unique): {1, 2, 3}
Write back: nums = [1, 2, 3, 2, 2, 3, 3]
                     ↑first 3 are unique values↑

Return 3 ✅
```

#### ⚠️ Limitation
O(N) extra space for the TreeSet, plus O(N log N) for insertion. The array is **already sorted** — we're not using that property at all.

---

### 🟢 Approach 2 — Optimal (Two Pointers)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
**The shelf analogy:** Imagine books sorted alphabetically, with duplicates. Walk through and whenever you find a book different from the last unique one you kept, place it right after.

Since the array is **sorted**, duplicates are always **adjacent**. Use pointer `i` to track the last position of a unique element, and `j` to scan forward. Whenever `nums[j] != nums[i]`, we've found a new unique value — increment `i` and copy `nums[j]` there.

#### 📝 Steps
1. `i = 0`
2. For `j` from 1 to n-1:
   - If `nums[i] != nums[j]` → `i++`, `nums[i] = nums[j]`
3. Return `i + 1`

#### 💻 Code
```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        int i = 0;

        for (int j = 1; j < n; j++) {
            if (nums[i] != nums[j]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }
}
```

#### 🧪 Dry Run

Input: `[0, 0, 3, 3, 5, 6]`

```
i=0 (nums[0]=0)

j=1: nums[1]=0, nums[i]=nums[0]=0 → same, skip
j=2: nums[2]=3, nums[i]=0 → different! i=1, nums[1]=3
     Array: [0,3,3,3,5,6]
j=3: nums[3]=3, nums[i]=nums[1]=3 → same, skip
j=4: nums[4]=5, nums[i]=3 → different! i=2, nums[2]=5
     Array: [0,3,5,3,5,6]
j=5: nums[5]=6, nums[i]=nums[2]=5 → different! i=3, nums[3]=6
     Array: [0,3,5,6,5,6]

Return i+1 = 4 ✅
First 4 elements: [0,3,5,6] ✅
```

> 💡 **Why does this work only because the array is sorted?**
> Sorted order guarantees duplicates are adjacent. `nums[i]` always holds the last confirmed unique value. Any `nums[j]` different from `nums[i]` must be strictly greater (sorted) — guaranteed unique. If the array weren't sorted, duplicates could appear anywhere, and this adjacent-comparison trick would fail.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| TreeSet | O(N log N) | O(N) |
| Two Pointers | **O(N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Trapping Rainwater | Only min(leftMax, rightMax) matters at each position. Two pointers track both maxes in one pass — no precomputed arrays needed |
| Maximum Consecutive Ones | Single counter, reset on 0, track max — the simplest possible O(N) pattern |
| Remove Duplicates | Sorted array → duplicates adjacent. Two pointers: i=last unique position, j=scanner. No extra space needed |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*