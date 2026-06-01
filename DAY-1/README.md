# 🚀 Day 1/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Set Matrix Zeroes](#1-set-matrix-zeroes) | 🟡 Medium | Arrays / Matrix |
| 2 | [Pascal's Triangle I](#2-pascals-triangle-i) | 🟢 Easy | Math / DP |
| 3 | [Next Permutation](#3-next-permutation) | 🟡 Medium | Arrays / Two Pointer |

---

## 1. Set Matrix Zeroes

### 🧩 Problem Statement

Given an `m × n` integer matrix, if any element is `0`, set its **entire row** and **entire column** to `0`. Must be done **in-place**.

**Example 1:**
```
Input:  [[1,1,1],
         [1,0,1],
         [1,1,1]]

Output: [[1,0,1],
         [0,0,0],
         [1,0,1]]
```
Element at `(1,1)` is `0` → entire row 1 and column 1 become `0`.

**Example 2:**
```
Input:  [[0,1,2,0],
         [3,4,5,2],
         [1,3,1,5]]

Output: [[0,0,0,0],
         [0,4,5,0],
         [0,3,1,0]]
```
Zeroes at `(0,0)` and `(0,3)` → row 0 fully zeroed, columns 0 and 3 fully zeroed.

---

### 🔴 Approach 1 — Brute Force  
**Space: O(m × n) | Time: O(m × n)**

#### 💡 Intuition

The naive idea is: *"When I find a `0`, zero out its row and column immediately."*

But there's a **trap** — if you zero out in the same matrix while scanning, newly written `0`s will incorrectly trigger more rows/columns to be zeroed.

**Fix:** Copy the matrix first, use the copy as reference while writing zeroes back into the original.

#### 📝 Steps
1. Create a `temp` copy of the matrix.
2. Scan the **original** matrix. Whenever `matrix[i][j] == 0`, zero out entire row `i` and column `j` in `temp`.
3. Copy `temp` back into `matrix`.

#### 💻 Code
```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] temp = new int[m][n];

        // Step 1: Copy matrix into temp
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                temp[i][j] = matrix[i][j];

        // Step 2: Use original to decide, write zeroes into temp
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    for (int r = 0; r < m; r++) temp[r][j] = 0; // zero column j
                    for (int c = 0; c < n; c++) temp[i][c] = 0; // zero row i
                }
            }
        }

        // Step 3: Copy temp back to matrix
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                matrix[i][j] = temp[i][j];
    }
}
```

#### 🧪 Dry Run

Input: `[[1,2,3,4],[5,6,0,8],[9,10,11,12]]`

**Step 1 – Copy to temp:**
```
temp = [[1,2,3,4],
        [5,6,0,8],
        [9,10,11,12]]
```

**Step 2 – Scan original. Find zero at `(1,2)`:**
- Zero entire column 2 in temp: `temp[0][2]=0`, `temp[1][2]=0`, `temp[2][2]=0`
- Zero entire row 1 in temp: `temp[1][0]=0`, `temp[1][1]=0`, `temp[1][2]=0`, `temp[1][3]=0`

```
temp after:
[[1,2,0,4],
 [0,0,0,0],
 [9,10,0,12]]
```

**Step 3 – Copy temp to matrix:**
```
Output: [[1,2,0,4],
         [0,0,0,0],
         [9,10,0,12]]  ✅
```

#### ⚠️ Why can't we zero out directly in the matrix?
If `matrix[1][2] = 0` → you zero row 1, setting `matrix[1][0] = 0`. Now when you reach `(1,0)`, you see a `0` and zero column 0 too — but that `0` was written by *you*, not in the original. This corrupts the result.

---

### 🟢 Approach 2 — Optimal  
**Space: O(m + n) | Time: O(m × n)**

#### 💡 Intuition

Instead of copying the whole matrix, just record **which rows and columns** have a zero. Two boolean/int arrays of size `m` and `n` are enough.

#### 📝 Steps
1. Create `row[m]` and `col[n]` arrays, initialized to `0`.
2. Scan the matrix. If `matrix[i][j] == 0`, mark `row[i] = 1` and `col[j] = 1`.
3. Scan again. If `row[i] == 1` OR `col[j] == 1`, set `matrix[i][j] = 0`.

#### 💻 Code
```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[] row = new int[m];
        int[] col = new int[n];

        // Step 1: Mark rows and columns that contain a zero
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (matrix[i][j] == 0) {
                    row[i] = 1;
                    col[j] = 1;
                }

        // Step 2: Set cells to zero based on markers
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (row[i] == 1 || col[j] == 1)
                    matrix[i][j] = 0;
    }
}
```

#### 🧪 Dry Run

Input: `[[1,2,3,4],[5,6,0,8],[9,10,11,12]]`

**Step 1 – Scan and mark:**

| Cell | Value | Action |
|------|-------|--------|
| (1,2) | 0 | `row[1] = 1`, `col[2] = 1` |

```
row = [0, 1, 0]
col = [0, 0, 1, 0]
```

**Step 2 – Apply zeroes:**

For every cell `(i, j)`:  check if `row[i] == 1` OR `col[j] == 1`

```
(0,0): row[0]=0, col[0]=0 → 1  (no change)
(0,1): row[0]=0, col[1]=0 → 2  (no change)
(0,2): row[0]=0, col[2]=1 → 0  ← zeroed by col
(0,3): row[0]=0, col[3]=0 → 4  (no change)

(1,0): row[1]=1 → 0  ← zeroed by row
(1,1): row[1]=1 → 0  ← zeroed by row
(1,2): row[1]=1 → 0  ← zeroed by both
(1,3): row[1]=1 → 0  ← zeroed by row

(2,0): row[2]=0, col[0]=0 → 9  (no change)
(2,1): row[2]=0, col[1]=0 → 10 (no change)
(2,2): row[2]=0, col[2]=1 → 0  ← zeroed by col
(2,3): row[2]=0, col[3]=0 → 12 (no change)
```

```
Output: [[1,2,0,4],
         [0,0,0,0],
         [9,10,0,12]]  ✅
```

#### 📊 Complexity

| | Brute Force | Optimal |
|--|------------|---------|
| Time | O(m × n) | O(m × n) |
| Space | O(m × n) | **O(m + n)** ✅ |

---

## 2. Pascal's Triangle I

### 🧩 Problem Statement

Given row `r` and column `c` (1-indexed), return the value at that position in Pascal's Triangle.

**Rules:**
- Every row starts and ends with `1`
- Interior element: `Pascal[r][c] = Pascal[r-1][c-1] + Pascal[r-1][c]`

```
Row 1:  1
Row 2:  1  1
Row 3:  1  2  1
Row 4:  1  3  3  1
Row 5:  1  4  6  4  1
```

**Example:** `r=5, c=3` → `6`

---

### 🔴 Approach 1 — Build the Triangle  
**Space: O(r²) | Time: O(r²)**

#### 💡 Intuition

Build the entire Pascal's Triangle up to row `r`, then return `arr[r-1][c-1]`. Straightforward to implement, just fill in each row using the previous row.

#### 💻 Code
```java
class Solution {
    public int pascalTriangleI(int r, int c) {
        int[][] arr = new int[r][r];

        for (int i = 0; i < r; i++) {
            arr[i][0] = 1;      // First element of every row = 1
            arr[i][i] = 1;      // Last element of every row = 1

            for (int j = 1; j < i; j++)
                arr[i][j] = arr[i-1][j-1] + arr[i-1][j]; // Sum of two above
        }

        return arr[r-1][c-1];
    }
}
```

#### 🧪 Dry Run

Input: `r = 5, c = 3`

Build 5 rows (0-indexed internally, so rows 0..4):

```
i=0: arr[0] = [1, 0, 0, 0, 0]   → set [0][0]=1, [0][0]=1 → [1,_,_,_,_]
i=1: arr[1][0]=1, arr[1][1]=1   → [1,1,_,_,_]
i=2: arr[2][0]=1, arr[2][2]=1
     j=1: arr[2][1] = arr[1][0] + arr[1][1] = 1+1 = 2
     → [1,2,1,_,_]
i=3: arr[3][0]=1, arr[3][3]=1
     j=1: arr[3][1] = arr[2][0] + arr[2][1] = 1+2 = 3
     j=2: arr[3][2] = arr[2][1] + arr[2][2] = 2+1 = 3
     → [1,3,3,1,_]
i=4: arr[4][0]=1, arr[4][4]=1
     j=1: arr[4][1] = arr[3][0] + arr[3][1] = 1+3 = 4
     j=2: arr[4][2] = arr[3][1] + arr[3][2] = 3+3 = 6   ← our answer!
     j=3: arr[4][3] = arr[3][2] + arr[3][3] = 3+1 = 4
     → [1,4,6,4,1]
```

Return `arr[4][2] = 6` ✅

---

### 🟢 Approach 2 — Combinatorics (nCr Formula)  
**Space: O(1) | Time: O(C)**

#### 💡 Intuition

A key mathematical property of Pascal's Triangle:

> The element at row `r`, column `c` equals `C(r-1, c-1)` — i.e., "r-1 choose c-1"

$$\binom{n}{r} = \frac{n!}{r! \cdot (n-r)!}$$

We don't need to compute full factorials. We can compute nCr iteratively:

$$\binom{n}{r} = \frac{n \cdot (n-1) \cdot (n-2) \cdots (n-r+1)}{1 \cdot 2 \cdot 3 \cdots r}$$

Multiply and divide step by step to keep numbers manageable and avoid overflow.

#### 💻 Code
```java
class Solution {
    public int pascalTriangleI(int r, int c) {
        return nCr(r - 1, c - 1);
    }

    // Computes n choose r iteratively
    private int nCr(int n, int r) {
        int ans = 1;
        for (int i = 0; i < r; i++) {
            ans = ans * (n - i); // Multiply numerator term
            ans = ans / (i + 1); // Divide denominator term immediately
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `r = 5, c = 3`

We compute `nCr(4, 2)`:

```
n = 4, r = 2

i=0: ans = 1 * (4 - 0) = 4
     ans = 4 / (0 + 1)  = 4

i=1: ans = 4 * (4 - 1) = 4 * 3 = 12
     ans = 12 / (1 + 1) = 12 / 2 = 6

Loop ends. Return 6. ✅
```

> 💡 **Why divide at each step?** Dividing immediately after each multiplication keeps the number small and guarantees the division is always exact (no remainder), because `n * (n-1) * ... * (n-k+1)` is always divisible by `k!`.

#### 📊 Complexity

| | Build Triangle | Combinatorics |
|--|--------------|--------------|
| Time | O(r²) | **O(C)** ✅ |
| Space | O(r²) | **O(1)** ✅ |

---

## 3. Next Permutation

### 🧩 Problem Statement

Given an array, rearrange it to its **next lexicographically greater permutation** in-place using O(1) extra space. If no greater permutation exists (array is sorted descending), rearrange to the smallest permutation (sorted ascending).

**Examples:**
```
[1,2,3]  →  [1,3,2]
[3,2,1]  →  [1,2,3]  (was last, wrap to first)
[1,1,5]  →  [1,5,1]
```

---

### 🔴 Approach 1 — Brute Force  
**Space: O(N × N!) | Time: O(N × N!)**

#### 💡 Intuition

Generate all permutations, sort them lexicographically, find the current one, return the next. Simple in concept but **extremely expensive** — we won't implement this for real.

> For `N=10`, that's `3,628,800` permutations. For `N=13`, it's `6 billion`. Not viable.

#### 📝 High-Level Steps
1. Generate all `N!` permutations using backtracking.
2. Sort them lexicographically.
3. Find current permutation's index via linear search.
4. Return index `+1` (or `0` if last).

**Not used in practice — included only for conceptual understanding.**

---

### 🟢 Approach 2 — Optimal (In-Place, 3 Steps)  
**Space: O(1) | Time: O(N)**

#### 💡 Intuition

Think about it this way — if you wanted to make a number just slightly larger by rearranging its digits, what would you do?

For `[1, 3, 5, 4, 2]`:
- The suffix `[5, 4, 2]` is already in **descending order** — any rearrangement of it gives something smaller.
- To get the *next* permutation, we need to **increase** something earlier in the array.
- The rightmost digit that can be increased is `3` (at index 1), because `3 < 5`.
- We swap `3` with the smallest digit to its right that is **still greater** than `3` → that's `4`.
- Array becomes `[1, 4, 5, 3, 2]`. But `[5, 3, 2]` is still in descending order — the biggest possible suffix.
- **Reverse** the suffix to make it the smallest: `[1, 4, 2, 3, 5]` ✅

#### 📝 3-Step Algorithm

```
Step 1: Find "pivot"
        Scan right to left, find the first index i where nums[i] < nums[i+1]
        → This is the rightmost "dip"

Step 2: Find swap partner
        Scan right to left from the end, find the first index j where nums[j] > nums[i]
        → Smallest element in the suffix greater than pivot
        → Swap nums[i] and nums[j]

Step 3: Reverse suffix
        Reverse everything from index i+1 to the end
        → Turns the descending suffix into ascending (smallest arrangement)
```

**Edge case:** If no pivot found (array is fully descending, e.g., `[3,2,1]`), just reverse the whole array.

#### 💻 Code
```java
class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int idx = -1;

        // Step 1: Find the pivot (first dip from the right)
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                idx = i;
                break;
            }
        }

        // Edge case: fully descending → reverse entire array
        if (idx == -1) {
            rev(nums, 0, n - 1);
            return;
        }

        // Step 2: Find the smallest element greater than pivot, swap
        for (int i = n - 1; i > idx; i--) {
            if (nums[i] > nums[idx]) {
                swap(nums, i, idx);
                break;
            }
        }

        // Step 3: Reverse the suffix after pivot
        rev(nums, idx + 1, n - 1);
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void rev(int[] arr, int first, int last) {
        while (first < last) {
            swap(arr, first, last);
            first++;
            last--;
        }
    }
}
```

#### 🧪 Dry Run — Example 1: `[1, 2, 3]`

**Step 1 – Find pivot (scan right to left):**
```
i=1: nums[1]=2, nums[2]=3 → 2 < 3 ✅ → idx = 1
```

**Step 2 – Find swap partner:**
```
Scan from right: first nums[j] > nums[1]=2
i=2: nums[2]=3 > 2 ✅ → swap(nums, 2, 1)

Array: [1, 3, 2]
```

**Step 3 – Reverse suffix after idx=1:**
```
Reverse from index 2 to 2 → single element, no change

Final: [1, 3, 2] ✅
```

---

#### 🧪 Dry Run — Example 2: `[3, 2, 1]`

**Step 1 – Find pivot:**
```
i=1: nums[1]=2, nums[2]=1 → 2 > 1, not a dip
i=0: nums[0]=3, nums[1]=2 → 3 > 2, not a dip
idx = -1 (no pivot found)
```

**Edge case triggered → reverse entire array:**
```
[3, 2, 1] → reversed → [1, 2, 3] ✅
```

---

#### 🧪 Dry Run — Example 3: `[1, 3, 5, 4, 2]`

**Step 1 – Find pivot:**
```
i=3: nums[3]=4, nums[4]=2 → 4 > 2, skip
i=2: nums[2]=5, nums[3]=4 → 5 > 4, skip
i=1: nums[1]=3, nums[2]=5 → 3 < 5 ✅ → idx = 1
```

**Step 2 – Find swap partner (scan from right for first > nums[1]=3):**
```
i=4: nums[4]=2 > 3? No
i=3: nums[3]=4 > 3? Yes ✅ → swap(nums, 3, 1)

Array before swap: [1, 3, 5, 4, 2]
Array after swap:  [1, 4, 5, 3, 2]
```

**Step 3 – Reverse suffix from idx+1=2 to end:**
```
Subarray to reverse: [5, 3, 2] → reversed: [2, 3, 5]

Final: [1, 4, 2, 3, 5] ✅
```

#### 📊 Complexity

| | Brute Force | Optimal |
|--|------------|---------|
| Time | O(N × N!) | **O(N)** ✅ |
| Space | O(N × N!) | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Set Matrix Zeroes | Mark rows/cols first, then apply — never zero while scanning |
| Pascal's Triangle | Every element = `nCr(r-1, c-1)`, compute iteratively in O(C) |
| Next Permutation | Find pivot → swap with just-greater → reverse suffix |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*