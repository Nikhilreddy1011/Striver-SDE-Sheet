# 🚀 Day 5/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Search in a 2D Matrix](#1-search-in-a-2d-matrix) | 🔴 Hard | Arrays / Binary Search |
| 2 | [Pow(x, n)](#2-powx-n) | 🟢 Easy | Math / Recursion |
| 3 | [Majority Element-I](#3-majority-element-i) | 🟢 Easy | Arrays / Boyer-Moore |

---

## 1. Search in a 2D Matrix

### 🧩 Problem Statement

Given a 2D array `mat` where:
- Elements of each row are sorted in **non-decreasing order**
- The **first element of each row** is greater than the **last element of the previous row**

Determine if a `target` value exists in the matrix.

**Example 1:**
```
Input:  mat = [[1,2,3,4],[5,6,7,8],[9,10,11,12]], target = 8
Output: true
Explanation: target 8 exists at index (1,3)
```

**Example 2:**
```
Input:  mat = [[1,2,4],[6,7,8],[9,10,34]], target = 78
Output: false
```

**Example 3:**
```
Input:  mat = [[1,2,4],[6,7,8],[9,10,34]], target = 7
Output: true
```

**Constraints:**
- `1 <= m, n <= 100`
- `-10⁴ <= mat[i][j], target <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Linear Search)
**Time: O(N × M) | Space: O(1)**

#### 💡 Intuition
The simplest possible idea — traverse every cell of the matrix one by one. If any cell equals the target, return `true`. If we finish the entire traversal without finding it, return `false`.

#### 📝 Steps
1. Check edge case: if matrix is empty, return `false`
2. Two nested loops: outer `i` over rows, inner `j` over columns
3. If `mat[i][j] == target` → return `true`
4. After full traversal → return `false`

#### 💻 Code
```java
class Solution {
    public boolean searchMatrix(int[][] mat, int target) {
        if (mat.length == 0 || mat[0].length == 0) {
            return false;
        }
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == target) {
                    return true;
                }
            }
        }
        return false;
    }
}
```

#### 🧪 Dry Run

Input: `mat = [[1,2,3,4],[5,6,7,8],[9,10,11,12]], target = 8`

```
i=0: j=0→1, j=1→2, j=2→3, j=3→4  (no match)
i=1: j=0→5, j=1→6, j=2→7, j=3→8  → 8 == 8 → return true ✅
```

#### ⚠️ Limitation
Doesn't use the sorted property of the matrix at all. O(N×M) is the worst case — we check every element. We can do much better.

---

### 🟡 Approach 2 — Better (Row Identification + Binary Search)
**Time: O(N + log M) | Space: O(1)**

#### 💡 Intuition
Since each row is sorted, and rows are ordered (first element of row `i+1` > last element of row `i`), we can first **identify which row could contain the target**, then apply **binary search on that row only**.

A row can contain the target if: `mat[i][0] <= target <= mat[i][m-1]`

#### 📝 Steps
1. Loop through each row `i`
2. If `mat[i][0] <= target <= mat[i][m-1]` → this row might contain it
3. Apply binary search on `mat[i]`
4. Return the result from binary search
5. If no row qualifies, return `false`

#### 💻 Code
```java
class Solution {
    private boolean bS(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) return true;
            else if (nums[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return false;
    }

    public boolean searchMatrix(int[][] mat, int target) {
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < n; i++) {
            if (mat[i][0] <= target && target <= mat[i][m - 1]) {
                return bS(mat[i], target);
            }
        }
        return false;
    }
}
```

#### 🧪 Dry Run

Input: `mat = [[1,2,3,4],[5,6,7,8],[9,10,11,12]], target = 8`

```
Row 0: mat[0][0]=1 <= 8 <= mat[0][3]=4? → 8 <= 4? No
Row 1: mat[1][0]=5 <= 8 <= mat[1][3]=8? → Yes!
  Binary search on [5,6,7,8]:
  low=0, high=3
  mid=1: nums[1]=6 < 8 → low=2
  mid=2: nums[2]=7 < 8 → low=3
  mid=3: nums[3]=8 == 8 → return true ✅
```

#### ⚠️ Limitation
Still O(N) for row scan + O(log M) for binary search = O(N + log M). We can combine both into a single binary search over the entire matrix.

---

### 🟢 Approach 3 — Optimal (Full Binary Search on Virtual 1D Array)
**Time: O(log(N×M)) | Space: O(1)**

#### 💡 Intuition
The key insight: since both rows and columns are sorted, and each row continues where the previous left off, the **entire matrix is effectively one sorted 1D array** of length `N×M`.

We can apply binary search on this virtual 1D array using index conversion:
- Virtual index `mid` → `row = mid / M`, `col = mid % M`
- So `mat[mid/M][mid%M]` gives us the element at virtual index `mid`

No extra space needed — we never actually flatten the matrix.

#### 📝 Steps
1. `low = 0`, `high = N*M - 1`
2. While `low <= high`:
   - `mid = (low + high) / 2`
   - `row = mid / M`, `col = mid % M`
   - If `mat[row][col] == target` → return `true`
   - If `mat[row][col] < target` → `low = mid + 1`
   - Else → `high = mid - 1`
3. Return `false`

#### 💻 Code
```java
class Solution {
    public boolean searchMatrix(int[][] mat, int target) {
        int n = mat.length;
        int m = mat[0].length;
        int low = 0, high = n * m - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int row = mid / m;
            int col = mid % m;

            if (mat[row][col] == target) return true;
            else if (mat[row][col] < target) low = mid + 1;
            else high = mid - 1;
        }
        return false;
    }
}
```

#### 🧪 Dry Run

Input: `mat = [[1,2,3,4],[5,6,7,8],[9,10,11,12]], target = 8`

`n=3, m=4` → virtual array has `3×4 = 12` elements (indices 0 to 11)

```
low=0, high=11

Step 1: mid=5 → row=5/4=1, col=5%4=1 → mat[1][1]=6
  6 < 8 → low=6

Step 2: mid=8 → row=8/4=2, col=8%4=0 → mat[2][0]=9
  9 > 8 → high=7

Step 3: mid=6 → row=6/4=1, col=6%4=2 → mat[1][2]=7
  7 < 8 → low=7

Step 4: mid=7 → row=7/4=1, col=7%4=3 → mat[1][3]=8
  8 == 8 → return true ✅
```

> 💡 **Why `mid / m` and `mid % m`?**
> Think of the matrix as rows of length `m`. Dividing by `m` tells you which row, and the remainder tells you the column within that row. For example with `m=4`: index 7 → row=7/4=1, col=7%4=3 → cell (1,3).

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (Linear) | O(N × M) | O(1) |
| Row ID + Binary Search | O(N + log M) | O(1) |
| Full Binary Search | **O(log(N×M))** ✅ | **O(1)** ✅ |

---

## 2. Pow(x, n)

### 🧩 Problem Statement

Implement `pow(x, n)` — calculate `x` raised to the power `n` (i.e., `xⁿ`).

**Example 1:**
```
Input:  x = 2.0000, n = 10
Output: 1024.0000
```

**Example 2:**
```
Input:  x = 2.0000, n = -2
Output: 0.2500
Explanation: 2^(-2) = 1/4 = 0.25
```

**Constraints:**
- `-100.0 <= x <= 100.0`
- `-2³¹ <= n <= 2³¹ - 1`
- Either `x != 0` or `n > 0`

---

### 🔴 Approach 1 — Brute Force (Linear Multiplication)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
Multiply `x` by itself `n` times. For negative `n`, invert `x` first (`x = 1/x`) and treat `n` as positive. Simple and correct — but very slow for large `n`.

#### 📝 Steps
1. Base case: if `n == 0` or `x == 1.0` → return `1`
2. If `n < 0` → set `x = 1/x`, `n = -n` (use `long` to avoid overflow)
3. Multiply `ans *= x` exactly `n` times
4. Return `ans`

#### 💻 Code
```java
class Solution {
    public double myPow(double x, int n) {
        if (n == 0 || x == 1.0) return 1;

        long temp = n;
        if (n < 0) {
            x = 1 / x;
            temp = -1L * n;
        }

        double ans = 1;
        for (long i = 0; i < temp; i++) {
            ans *= x;
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `x = 2.0, n = 10`

```
temp = 10
ans = 1

i=0: ans = 1 * 2 = 2
i=1: ans = 2 * 2 = 4
i=2: ans = 4 * 2 = 8
i=3: ans = 8 * 2 = 16
i=4: ans = 16 * 2 = 32
i=5: ans = 32 * 2 = 64
i=6: ans = 64 * 2 = 128
i=7: ans = 128 * 2 = 256
i=8: ans = 256 * 2 = 512
i=9: ans = 512 * 2 = 1024

Return 1024.0 ✅
```

**With negative n:** `x = 2.0, n = -2`
```
n < 0 → x = 1/2 = 0.5, temp = 2

i=0: ans = 0.5
i=1: ans = 0.25

Return 0.25 ✅
```

#### ⚠️ Limitation
For large `n` (up to 2³¹), this loop runs 2 billion times — complete TLE. We need a smarter approach.

> 💡 **Why `long temp = n`?**
> `int` can't hold `-Integer.MIN_VALUE` because `Integer.MIN_VALUE = -2147483648` and its positive version overflows `int`. Using `long` handles this safely.

---

### 🟢 Approach 2 — Optimal (Fast Power / Binary Exponentiation)
**Time: O(log N) | Space: O(log N) recursion stack**

#### 💡 Intuition
Instead of multiplying `x` one at a time, **square it and halve the exponent** at each step:

- If `n` is **even**: `xⁿ = (x²)^(n/2)` — one multiplication, half the work
- If `n` is **odd**: `xⁿ = x × x^(n-1)` — one multiplication, then even case

This is called **Binary Exponentiation** or **Fast Power**. Instead of N multiplications, we do only log₂(N) multiplications.

**Real-life analogy:** To walk 16 steps, instead of taking 16 individual steps, you could take 1 step → 2 steps → 4 steps → 8 steps → 16 steps. Just 4 moves instead of 16.

#### 📝 Steps
1. Base case: `n == 0` → return `1.0`
2. Base case: `n == 1` → return `x`
3. If `n` is even → return `power(x * x, n / 2)`
4. If `n` is odd → return `x * power(x, n - 1)`
5. In `myPow`: convert `n` to `long`, handle negative `n` by returning `1 / power(x, -n)`

#### 💻 Code
```java
class Solution {
    private double power(double x, long n) {
        if (n == 0) return 1.0;
        if (n == 1) return x;

        if (n % 2 == 0) {
            return power(x * x, n / 2);  // Even: square and halve
        }
        return x * power(x, n - 1);      // Odd: reduce by 1
    }

    public double myPow(double x, int n) {
        long num = n;
        if (num < 0) {
            return (1.0 / power(x, -num));
        }
        return power(x, num);
    }
}
```

#### 🧪 Dry Run

Input: `x = 2.0, n = 10`

```
myPow(2.0, 10) → num=10, positive → power(2.0, 10)

power(2.0, 10):  n=10, even → power(2.0*2.0, 5) = power(4.0, 5)
power(4.0,  5):  n=5,  odd  → 4.0 * power(4.0, 4)
power(4.0,  4):  n=4,  even → power(4.0*4.0, 2) = power(16.0, 2)
power(16.0, 2):  n=2,  even → power(16.0*16.0, 1) = power(256.0, 1)
power(256.0,1):  n=1,  base → return 256.0

Unwinding:
power(16.0, 2)  = 256.0
power(4.0,  4)  = 256.0
power(4.0,  5)  = 4.0 * 256.0 = 1024.0
power(2.0,  10) = 1024.0

Return 1024.0 ✅
```

**With negative n:** `x = 2.0, n = -2`
```
num = -2 < 0 → return 1.0 / power(2.0, 2)
power(2.0, 2): even → power(4.0, 1) = 4.0
1.0 / 4.0 = 0.25 ✅
```

> 💡 **Why is the recursion depth O(log N)?**
> Every even call halves `n`. So starting from `n`, we reach 0 in roughly log₂(n) steps. For `n = 10⁹`, that's only ~30 recursive calls instead of a billion.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (Loop) | O(N) | O(1) |
| Fast Power (Recursion) | **O(log N)** ✅ | O(log N) |

---

## 3. Majority Element-I

### 🧩 Problem Statement

Given an integer array `nums` of size `n`, return the **majority element** — the element that appears **more than n/2 times**. The array is guaranteed to always have a majority element.

**Example 1:**
```
Input:  nums = [7, 0, 0, 1, 7, 7, 2, 7, 7]
Output: 7
Explanation: 7 appears 5 times in a 9-element array (5 > 9/2 = 4.5)
```

**Example 2:**
```
Input:  nums = [1, 1, 1, 2, 1, 2]
Output: 1
Explanation: 1 appears 4 times in a 6-element array (4 > 6/2 = 3)
```

**Constraints:**
- `1 <= n <= 10⁵`
- `-10⁴ <= nums[i] <= 10⁴`
- Majority element always exists

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
For every element, count how many times it appears in the full array. If the count exceeds `n/2`, it's the majority element.

#### 💻 Code
```java
class Solution {
    public int majorityElement(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] == nums[i]) count++;
            }
            if (count > (n / 2)) return nums[i];
        }
        return -1;
    }
}
```

#### 🧪 Dry Run

Input: `[7, 0, 0, 1, 7, 7, 2, 7, 7]`, n=9

```
i=0 (val=7): count=5 → 5 > 4 → return 7 ✅
```

---

### 🟡 Approach 2 — Brute Force 2 (Sort + Middle Element)
**Time: O(N log N) | Space: O(1)**

#### 💡 Intuition
Since the majority element appears more than n/2 times, after sorting it **must occupy the middle index**. Whatever element sits at `nums[n/2]` is always the majority element.

#### 💻 Code
```java
class Solution {
    public int majorityElement(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }
}
```

#### 🧪 Dry Run

Input: `[7, 0, 0, 1, 7, 7, 2, 7, 7]`

After sorting: `[0, 0, 1, 2, 7, 7, 7, 7, 7]`
```
n=9, n/2=4 → nums[4] = 7

Return 7 ✅
```

> 💡 **Why does the middle always work?** The majority element appears more than n/2 times. In a sorted array of n elements, if an element occupies more than half the positions, it must cover the midpoint regardless of where it starts.

---

### 🟡 Approach 3 — Better (HashMap Frequency Count)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Use a HashMap to count the frequency of each element in one pass. Then scan the map to find the element with frequency > n/2.

#### 💻 Code
```java
class Solution {
    public int majorityElement(int[] nums) {
        int n = nums.length;
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > n / 2) {
                return entry.getKey();
            }
        }
        return -1;
    }
}
```

#### 🧪 Dry Run

Input: `[7, 0, 0, 1, 7, 7, 2, 7, 7]`, n=9

**Pass 1 — Build frequency map:**
```
map = {7:5, 0:2, 1:1, 2:1}
```

**Pass 2 — Find majority:**
```
7 → 5 > 4 → return 7 ✅
```

---

### 🟢 Approach 4 — Optimal (Boyer-Moore Voting Algorithm)
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
This is the **Boyer-Moore Voting Algorithm** — a beautifully clever approach.

**The party analogy:** Imagine a large party where each guest brought a dish. You want to find the most popular dish without counting everything. You start with no dish in mind (`count = 0`). For each dish you see:
- If you're not tracking any dish → start tracking this one, count = 1
- If it matches what you're tracking → count++
- If it doesn't match → count-- (they "cancel" each other out)

When the count hits 0, the previous tracked dish and the current dish have "cancelled" each other — the majority element can still survive because it has more than n/2 votes.

**Why it works:** The majority element appears more than n/2 times. Even if every other element "votes against" it, the majority element still has enough votes to survive and be the last element standing.

**Note:** After finding the candidate, a second pass verifies it's actually the majority (in case the guarantee isn't given). Since this problem guarantees a majority exists, the second pass just confirms.

#### 📝 Steps
1. Initialize `ele = 0`, `count = 0`
2. For each element in `nums`:
   - If `count == 0` → set `ele = nums[i]`, `count = 1`
   - If `ele == nums[i]` → `count++`
   - Else → `count--`
3. Verify: count how many times `ele` appears
4. If count > n/2 → return `ele`, else return -1

#### 💻 Code
```java
class Solution {
    public int majorityElement(int[] nums) {
        int n = nums.length;
        int ele = 0;
        int count = 0;

        // Phase 1: Find the candidate
        for (int i = 0; i < n; i++) {
            if (count == 0) {
                count = 1;
                ele = nums[i];
            } else if (ele == nums[i]) {
                count++;
            } else {
                count--;
            }
        }

        // Phase 2: Verify the candidate
        int count1 = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] == ele) count1++;
        }

        if (count1 > (n / 2)) return ele;
        return -1;
    }
}
```

#### 🧪 Dry Run

Input: `[7, 0, 0, 1, 7, 7, 2, 7, 7]`, n=9

**Phase 1 — Find candidate:**

```
i=0: count=0 → ele=7, count=1
i=1: nums[1]=0 ≠ 7 → count=0
i=2: count=0 → ele=0, count=1
i=3: nums[3]=1 ≠ 0 → count=0
i=4: count=0 → ele=7, count=1
i=5: nums[5]=7 == 7 → count=2
i=6: nums[6]=2 ≠ 7 → count=1
i=7: nums[7]=7 == 7 → count=2
i=8: nums[8]=7 == 7 → count=3

Candidate: ele=7
```

**Phase 2 — Verify:**
```
Count of 7 in array = 5
5 > 9/2 = 4 → return 7 ✅
```

**Another dry run:** `[2, 2, 1, 1, 1, 2, 2]`, n=7

```
i=0: count=0 → ele=2, count=1
i=1: 2==2 → count=2
i=2: 1≠2 → count=1
i=3: 1≠2 → count=0
i=4: count=0 → ele=1, count=1
i=5: 2≠1 → count=0
i=6: count=0 → ele=2, count=1

Candidate: ele=2
Verify: count of 2 = 4, 4 > 3 → return 2 ✅
```

> 💡 **Why does the candidate survive?**
> The majority element appears more than n/2 times. Even in the worst case where all non-majority elements "cancel" the majority one-for-one, the majority still has leftover votes that no other element can cancel. It will always be the last element standing.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Nested Loops | O(N²) | O(1) |
| Sort + Middle | O(N log N) | O(1) |
| HashMap | O(N) | O(N) |
| Boyer-Moore Voting | **O(N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Search in 2D Matrix | Treat the sorted matrix as a virtual 1D array — use `mid/m` for row and `mid%m` for column |
| Pow(x, n) | Square the base and halve the exponent each step — O(log N) instead of O(N) |
| Majority Element | Boyer-Moore: majority and minority elements cancel each other out — the majority always survives |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*