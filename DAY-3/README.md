# 🚀 Day 3/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Rotate Matrix by 90 Degrees](#1-rotate-matrix-by-90-degrees) | 🟡 Medium | Arrays / Matrix |
| 2 | [Merge Overlapping Subintervals](#2-merge-overlapping-subintervals) | 🟡 Medium | Arrays / Sorting |
| 3 | [Merge Two Sorted Arrays Without Extra Space](#3-merge-two-sorted-arrays-without-extra-space) | 🟡 Medium | Arrays / Two Pointer |

---

## 1. Rotate Matrix by 90 Degrees

### 🧩 Problem Statement

Given an `N × N` 2D integer matrix, rotate it **90 degrees clockwise in-place** — without using an extra matrix.

**Example 1:**
```
Input:
[[1, 2, 3],
 [4, 5, 6],
 [7, 8, 9]]

Output:
[[7, 4, 1],
 [8, 5, 2],
 [9, 6, 3]]
```

**Example 2:**
```
Input:
[[0, 1, 1, 2],
 [2, 0, 3, 1],
 [4, 5, 0, 5],
 [5, 6, 7, 0]]

Output:
[[5, 4, 2, 0],
 [6, 5, 0, 1],
 [7, 0, 3, 1],
 [0, 5, 1, 2]]
```

**Constraints:**
- `n == matrix.length == matrix[i].length`
- `1 <= n <= 100`
- `-10⁴ <= matrix[i][j] <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Extra Matrix)
**Time: O(N²) | Space: O(N²)**

#### 💡 Intuition
Create a new matrix and directly place each element at its rotated position. For a 90° clockwise rotation, the element at position `(i, j)` in the original matrix goes to position `(j, N-1-i)` in the result matrix.

**The formula:** `ans[j][N-1-i] = matrix[i][j]`

Think of it visually:
- Row 0 of original → becomes Column (N-1) of result
- Row 1 of original → becomes Column (N-2) of result
- Row i of original → becomes Column (N-1-i) of result

#### 📝 Steps
1. Create a new `N × N` matrix `ans`
2. For every cell `(i, j)`, place `matrix[i][j]` into `ans[j][N-1-i]`
3. Copy `ans` back into `matrix`

#### 💻 Code
```java
class Solution {
    public void rotateMatrix(int[][] matrix) {
        int n = matrix.length;
        int[][] ans = new int[n][n];

        // Place each element at its rotated position
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans[j][n - 1 - i] = matrix[i][j];
            }
        }

        // Copy result back to original matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = ans[i][j];
            }
        }
    }
}
```

#### 🧪 Dry Run

Input:
```
matrix = [[1, 2, 3],
           [4, 5, 6],
           [7, 8, 9]]
n = 3
```

Applying `ans[j][n-1-i] = matrix[i][j]`:

```
(i=0,j=0): ans[0][2] = 1
(i=0,j=1): ans[1][2] = 2
(i=0,j=2): ans[2][2] = 3

(i=1,j=0): ans[0][1] = 4
(i=1,j=1): ans[1][1] = 5
(i=1,j=2): ans[2][1] = 6

(i=2,j=0): ans[0][0] = 7
(i=2,j=1): ans[1][0] = 8
(i=2,j=2): ans[2][0] = 9
```

```
ans = [[7, 4, 1],
       [8, 5, 2],
       [9, 6, 3]]
```

Copy back → `matrix = [[7,4,1],[8,5,2],[9,6,3]]` ✅

#### ⚠️ Limitation
Uses O(N²) extra space. The problem asks for in-place rotation — the optimal approach does it without any extra matrix.

---

### 🟢 Approach 2 — Optimal (Transpose + Reverse)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
A 90° clockwise rotation can be broken into **two simpler in-place operations**:

**Step 1 — Transpose the matrix:**
Swap `matrix[i][j]` with `matrix[j][i]` for all `i < j`.
This flips the matrix along its main diagonal — rows become columns.

**Step 2 — Reverse each row:**
Reverse every row of the transposed matrix.

**Why does this work?**
- After transpose: element at `(i, j)` moves to `(j, i)`
- After reversing row `j`: element at `(j, i)` moves to `(j, N-1-i)`
- Final position `(j, N-1-i)` is exactly where a 90° clockwise rotation puts `(i, j)` ✅

#### 📝 Steps
1. **Transpose:** For `i` from 0 to N, for `j` from 0 to `i`, swap `matrix[i][j]` with `matrix[j][i]`
2. **Reverse rows:** For each row `i`, swap elements from both ends towards the center

#### 💻 Code
```java
class Solution {
    public void rotateMatrix(int[][] matrix) {
        int n = matrix.length;

        // Step 1: Transpose the matrix (swap across diagonal)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - 1 - j];
                matrix[i][n - 1 - j] = temp;
            }
        }
    }
}
```

#### 🧪 Dry Run

Input:
```
matrix = [[1, 2, 3],
           [4, 5, 6],
           [7, 8, 9]]
```

**Step 1 — Transpose (swap matrix[i][j] with matrix[j][i] for j < i):**

```
(i=1,j=0): swap matrix[1][0] and matrix[0][1] → swap 4 and 2
(i=2,j=0): swap matrix[2][0] and matrix[0][2] → swap 7 and 3
(i=2,j=1): swap matrix[2][1] and matrix[1][2] → swap 8 and 6
```

After transpose:
```
[[1, 4, 7],
 [2, 5, 8],
 [3, 6, 9]]
```

**Step 2 — Reverse each row:**
```
Row 0: [1, 4, 7] → reversed → [7, 4, 1]
Row 1: [2, 5, 8] → reversed → [8, 5, 2]
Row 2: [3, 6, 9] → reversed → [9, 6, 3]
```

Final matrix:
```
[[7, 4, 1],
 [8, 5, 2],
 [9, 6, 3]]  ✅
```

> 💡 **Why `j < i` for transpose (not `j < n`)?**
> If we loop `j` from 0 to n, we'd swap each pair twice — ending up back at the original. Looping `j < i` ensures each pair is swapped exactly once (only the lower triangle).

> 💡 **Why `j < n/2` for reverse?**
> We only need to go to the midpoint. Going further would swap elements back to their original positions.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (Extra Matrix) | O(N²) | O(N²) |
| Optimal (Transpose + Reverse) | **O(N²)** ✅ | **O(1)** ✅ |

---

## 2. Merge Overlapping Subintervals

### 🧩 Problem Statement

Given an array of intervals where `intervals[i] = [start_i, end_i]`, **merge all overlapping intervals** and return the array of non-overlapping intervals that cover all intervals in the input.

Two intervals overlap if one starts before the other ends: `curr.start <= last.end`

**Example 1:**
```
Input:  [[1,5],[3,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: [1,5] and [3,6] overlap → merged into [1,6]
```

**Example 2:**
```
Input:  [[5,7],[1,3],[4,6],[8,10]]
Output: [[1,3],[4,7],[8,10]]
Explanation: [4,6] and [5,7] overlap → merged into [4,7]
```

**Constraints:**
- `1 <= intervals.length <= 10⁵`
- `0 <= start_i <= end_i <= 10⁵`

---

### 🟢 Approach — Sort + Greedy (Optimal)
**Time: O(N log N) | Space: O(N)**

#### 💡 Intuition
The key question is: **when do two intervals overlap?**

Interval A `[a, b]` and Interval B `[c, d]` overlap if `c <= b` — i.e., B starts before A ends.

If we **sort intervals by start time**, overlapping intervals will always be adjacent. We never need to compare a later interval with something far back — because if they were supposed to overlap, sorting ensures they'd already be neighbors.

**Why sorting is the key:**
Before sorting, `[5,7]` and `[4,6]` might not be adjacent. After sorting by start: `[4,6]` comes before `[5,7]`, and we can detect the overlap cleanly.

**Merge logic:**
- If `curr.start <= last.end` → they overlap → merge by updating `last.end = max(last.end, curr.end)`
- If `curr.start > last.end` → no overlap → add `curr` as a new interval

#### 📝 Steps
1. Sort `intervals` by start time
2. Add the first interval to result
3. For each remaining interval:
   - If it overlaps with the last interval in result → merge (update end)
   - Otherwise → add it as a new entry
4. Return result

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> mergeOverlap(List<List<Integer>> intervals) {

        // Step 1: Sort by start time
        intervals.sort((a, b) -> a.get(0) - b.get(0));

        List<List<Integer>> ans = new ArrayList<>();

        for (List<Integer> curr : intervals) {

            // If result is empty OR no overlap with last interval
            if (ans.isEmpty() || curr.get(0) > ans.get(ans.size() - 1).get(1)) {
                ans.add(new ArrayList<>(curr));
            } else {
                // Overlap: merge by extending the end
                List<Integer> last = ans.get(ans.size() - 1);
                last.set(1, Math.max(last.get(1), curr.get(1)));
            }
        }

        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `[[1,5],[3,6],[8,10],[15,18]]`

**Step 1 — Sort by start time:**
```
Already sorted: [[1,5],[3,6],[8,10],[15,18]]
```

**Step 2 — Process each interval:**

```
ans = []

curr = [1,5]:
  ans is empty → add directly
  ans = [[1,5]]

curr = [3,6]:
  last = [1,5], curr.start=3 <= last.end=5 → OVERLAP
  Merge: last.end = max(5,6) = 6
  ans = [[1,6]]

curr = [8,10]:
  last = [1,6], curr.start=8 > last.end=6 → NO OVERLAP
  Add new interval
  ans = [[1,6],[8,10]]

curr = [15,18]:
  last = [8,10], curr.start=15 > last.end=10 → NO OVERLAP
  Add new interval
  ans = [[1,6],[8,10],[15,18]]
```

Output: `[[1,6],[8,10],[15,18]]` ✅

---

**Second Dry Run** — Input: `[[5,7],[1,3],[4,6],[8,10]]`

**Step 1 — Sort by start time:**
```
Sorted: [[1,3],[4,6],[5,7],[8,10]]
```

**Step 2 — Process:**
```
ans = []

curr = [1,3]:
  ans empty → add
  ans = [[1,3]]

curr = [4,6]:
  last=[1,3], curr.start=4 > last.end=3 → NO OVERLAP
  Add new
  ans = [[1,3],[4,6]]

curr = [5,7]:
  last=[4,6], curr.start=5 <= last.end=6 → OVERLAP
  Merge: last.end = max(6,7) = 7
  ans = [[1,3],[4,7]]

curr = [8,10]:
  last=[4,7], curr.start=8 > last.end=7 → NO OVERLAP
  Add new
  ans = [[1,3],[4,7],[8,10]]
```

Output: `[[1,3],[4,7],[8,10]]` ✅

> 💡 **Why `max(last.end, curr.end)` instead of just `curr.end`?**
> Consider `[[1,10],[2,3]]`. After sorting: `[1,10]` is last, `[2,3]` overlaps. But `curr.end=3 < last.end=10`. If we just assigned `curr.end`, we'd shrink the interval incorrectly. `max` ensures we always keep the furthest end.

> 💡 **Why can't we solve this without sorting?**
> Without sorting, `[8,10]` and `[1,5]` might be compared even though they don't overlap. Sorting guarantees that if `curr` doesn't overlap with `last`, it won't overlap with anything before `last` either — so we never need to look back.

#### 📊 Complexity

| Step | Time | Space |
|------|------|-------|
| Sorting | O(N log N) | O(1) |
| Single pass merge | O(N) | O(N) for result |
| **Total** | **O(N log N)** ✅ | **O(N)** ✅ |

---

## 3. Merge Two Sorted Arrays Without Extra Space

### 🧩 Problem Statement

Given two sorted arrays `nums1` (size `m+n`, first `m` elements valid, rest are 0s) and `nums2` (size `n`), merge them **in-place** into `nums1` in sorted order.

**Example 1:**
```
Input:  nums1 = [-5,-2,4,5,0,0,0], m=4   nums2 = [-3,1,8], n=3
Output: nums1 = [-5,-3,-2,1,4,5,8]
```

**Example 2:**
```
Input:  nums1 = [0,2,7,8,0,0,0], m=4   nums2 = [-7,-3,-1], n=3
Output: nums1 = [-7,-3,-1,0,2,7,8]
```

**Constraints:**
- `0 <= m, n <= 1000`
- `-10⁴ <= nums1[i], nums2[i] <= 10⁴`
- Both arrays sorted in non-decreasing order

---

### 🔴 Approach 1 — Brute Force (Extra Array)
**Time: O(M+N) | Space: O(M+N)**

#### 💡 Intuition
Classic merge step from Merge Sort. Use two pointers on both arrays, always pick the smaller element and place it into a third array. Copy the merged result back to `nums1`.

#### 📝 Steps
1. Declare `temp[m+n]`, pointers `i=0` (nums1), `j=0` (nums2), `k=0` (temp)
2. While both pointers valid: pick smaller of `nums1[i]` and `nums2[j]` into `temp[k]`
3. Copy remaining elements of whichever array isn't exhausted
4. Copy `temp` back to `nums1`

#### 💻 Code
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] temp = new int[m + n];
        int i = 0, j = 0, k = 0;

        // Step 1: Merge both arrays into temp
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                temp[k] = nums1[i];
                i++; k++;
            } else {
                temp[k] = nums2[j];
                k++; j++;
            }
        }

        // Step 2: Copy remaining elements
        while (i < m) temp[k++] = nums1[i++];
        while (j < n) temp[k++] = nums2[j++];

        // Step 3: Copy back to nums1
        for (int c = 0; c < m + n; c++) nums1[c] = temp[c];
    }
}
```

#### 🧪 Dry Run

`nums1 = [-5,-2,4,5]` (m=4), `nums2 = [-3,1,8]` (n=3)

```
i=0,j=0: nums1[0]=-5 <= nums2[0]=-3 → temp[0]=-5, i=1
i=1,j=0: nums1[1]=-2 >  nums2[0]=-3 → temp[1]=-3, j=1
i=1,j=1: nums1[1]=-2 <= nums2[1]=1  → temp[2]=-2, i=2
i=2,j=1: nums1[2]=4  >  nums2[1]=1  → temp[3]=1,  j=2
i=2,j=2: nums1[2]=4  <= nums2[2]=8  → temp[4]=4,  i=3
i=3,j=2: nums1[3]=5  <= nums2[2]=8  → temp[5]=5,  i=4

i=4 (exhausted), remaining nums2: j=2
temp[6] = nums2[2] = 8

temp = [-5,-3,-2,1,4,5,8]
Copy back → nums1 = [-5,-3,-2,1,4,5,8] ✅
```

---

### 🟡 Approach 2 — Two-Pointer Swap + Sort
**Time: O(M log M + N log N) | Space: O(1)**

#### 💡 Intuition
Use two pointers — `left` starting at the **end of valid nums1** (`m-1`) and `right` starting at the **beginning of nums2** (`0`). If `nums1[left] > nums2[right]`, swap them — pushing larger elements to the end of nums1 and smaller ones to the front of nums2. After all swaps, sort both arrays separately and copy nums2 into the tail of nums1.

#### 📝 Steps
1. `left = m-1`, `right = 0`
2. While `left >= 0` and `right < n`: if `nums1[left] > nums2[right]`, swap and move both pointers
3. If no swap possible, break (already in correct relative order)
4. Sort `nums1[0..m]` and `nums2`
5. Copy `nums2` into `nums1[m..m+n]`

#### 💻 Code
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int left = m - 1;
        int right = 0;

        // Swap larger elements of nums1 with smaller elements of nums2
        while (left >= 0 && right < n) {
            if (nums1[left] > nums2[right]) {
                int temp = nums1[left];
                nums1[left] = nums2[right];
                nums2[right] = temp;
                left--;
                right++;
            } else break;
        }

        // Sort both halves separately
        Arrays.sort(nums1, 0, m);
        Arrays.sort(nums2);

        // Copy nums2 into the tail of nums1
        for (int i = m; i < m + n; i++) {
            nums1[i] = nums2[i - m];
        }
    }
}
```

#### 🧪 Dry Run

`nums1 = [-5,-2,4,5,0,0,0]` (m=4), `nums2 = [-3,1,8]` (n=3)

Valid nums1 = `[-5,-2,4,5]`, left=3, right=0

```
left=3, right=0: nums1[3]=5 > nums2[0]=-3 → swap
  nums1 = [-5,-2,4,-3,...], nums2 = [5,1,8]
  left=2, right=1

left=2, right=1: nums1[2]=4 > nums2[1]=1 → swap
  nums1 = [-5,-2,1,-3,...], nums2 = [5,4,8]
  left=1, right=2

left=1, right=2: nums1[1]=-2 > nums2[2]=8? No → BREAK
```

After swaps:
```
nums1 valid part = [-5,-2,1,-3]
nums2 = [5,4,8]
```

Sort both:
```
nums1[0..4] sorted = [-5,-3,-2,1]
nums2 sorted = [4,5,8]
```

Copy nums2 to tail:
```
nums1 = [-5,-3,-2,1,4,5,8] ✅
```

---

### 🟢 Approach 3 — Optimal (Fill From Back)
**Time: O(M+N) | Space: O(1)**

#### 💡 Intuition
Since `nums1` has exactly `n` empty slots at the end, we can fill from the **back** — placing the largest element at the last position, then the second largest, and so on.

Use three pointers:
- `i = m-1` → points to last valid element of nums1
- `j = n-1` → points to last element of nums2
- `k = m+n-1` → points to last position in nums1 (fill here)

Compare `nums1[i]` and `nums2[j]`, place the larger one at `nums1[k]` and move that pointer back. Continue until all of nums2 is placed.

**Why loop only on `j >= 0`?**
If nums2 is exhausted (`j < 0`), nums1's remaining elements are already in the right place. If nums1 is exhausted, remaining nums2 elements just get placed directly.

#### 📝 Steps
1. `i = m-1`, `j = n-1`, `k = m+n-1`
2. While `j >= 0`:
   - If `i >= 0` and `nums1[i] >= nums2[j]`: place `nums1[i]` at `nums1[k]`, decrement `i` and `k`
   - Else: place `nums2[j]` at `nums1[k]`, decrement `j` and `k`

#### 💻 Code
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;       // Pointer at end of valid nums1
        int j = n - 1;       // Pointer at end of nums2
        int k = m + n - 1;   // Pointer at end of nums1 (fill position)

        while (j >= 0) {
            if (i >= 0 && nums1[i] >= nums2[j]) {
                nums1[k] = nums1[i];
                i--;
                k--;
            } else {
                nums1[k] = nums2[j];
                j--;
                k--;
            }
        }
    }
}
```

#### 🧪 Dry Run

`nums1 = [-5,-2,4,5,0,0,0]` (m=4), `nums2 = [-3,1,8]` (n=3)

Initial: `i=3, j=2, k=6`

```
Step 1: nums1[i=3]=5 vs nums2[j=2]=8
  8 > 5 → nums1[k=6] = 8, j=1, k=5
  nums1 = [-5,-2,4,5,0,0,8]

Step 2: nums1[i=3]=5 vs nums2[j=1]=1
  5 >= 1 → nums1[k=5] = 5, i=2, k=4
  nums1 = [-5,-2,4,5,0,5,8]

Step 3: nums1[i=2]=4 vs nums2[j=1]=1
  4 >= 1 → nums1[k=4] = 4, i=1, k=3
  nums1 = [-5,-2,4,5,4,5,8]

Step 4: nums1[i=1]=-2 vs nums2[j=1]=1
  1 > -2 → nums1[k=3] = 1, j=0, k=2
  nums1 = [-5,-2,4,1,4,5,8]

Step 5: nums1[i=1]=-2 vs nums2[j=0]=-3
  -2 >= -3 → nums1[k=2] = -2, i=0, k=1
  nums1 = [-5,-2,-2,1,4,5,8]

Step 6: nums1[i=0]=-5 vs nums2[j=0]=-3
  -3 > -5 → nums1[k=1] = -3, j=-1, k=0
  nums1 = [-5,-3,-2,1,4,5,8]

j = -1 → loop ends ✅
```

Output: `[-5,-3,-2,1,4,5,8]` ✅

---

### 🟢 Approach 4 — Gap Method (Shell Sort Based)
**Time: O((M+N) × log(M+N)) | Space: O(1)**

#### 💡 Intuition
This approach treats `nums1` and `nums2` as a **single virtual array** of size `M+N`. It applies the Shell Sort gap technique — compare elements that are `gap` distance apart and swap if they're out of order. Repeatedly halve the gap (using `ceil(gap/2)`) until gap reaches 1 and the array is sorted.

**Gap calculation:** `gap = ceil((M+N) / 2)`

For each gap, two pointers `left=0` and `right=left+gap` slide through the virtual array. Three pointer-region cases:
- Both in nums1 → compare `nums1[left]` vs `nums1[right]`
- Left in nums1, right in nums2 → compare `nums1[left]` vs `nums2[right-m]`
- Both in nums2 → compare `nums2[left-m]` vs `nums2[right-m]`

#### 💻 Code
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int len = n + m;
        int gap = (len / 2) + (len % 2); // ceil(len/2)

        while (gap > 0) {
            int left = 0;
            int right = left + gap;

            while (right < len) {
                // Left in nums1, right in nums2
                if (left < m && right >= m) {
                    swapIfGreater(nums1, nums2, left, right - m);
                }
                // Both in nums2
                else if (left >= m) {
                    swapIfGreater(nums2, nums2, left - m, right - m);
                }
                // Both in nums1
                else {
                    swapIfGreater(nums1, nums1, left, right);
                }
                left++;
                right++;
            }

            if (gap == 1) break;
            gap = (gap / 2) + (gap % 2); // ceil(gap/2)
        }

        // Copy nums2 into tail of nums1
        for (int i = m; i < m + n; i++) {
            nums1[i] = nums2[i - m];
        }
    }

    private void swapIfGreater(int[] arr1, int[] arr2, int idx1, int idx2) {
        if (arr1[idx1] > arr2[idx2]) {
            int temp = arr1[idx1];
            arr1[idx1] = arr2[idx2];
            arr2[idx2] = temp;
        }
    }
}
```

#### 🧪 Dry Run

`nums1 = [-5,-2,4,5]` (m=4), `nums2 = [-3,1,8]` (n=3)

Virtual array (conceptual): `[-5, -2, 4, 5 | -3, 1, 8]`
Indices:                        `0    1   2   3    4  5  6`

`len = 7`, initial `gap = ceil(7/2) = 4`

**Gap = 4:**
```
left=0, right=4: nums1[0]=-5 vs nums2[0]=-3 → -5 < -3, no swap
left=1, right=5: nums1[1]=-2 vs nums2[1]=1  → -2 < 1, no swap
left=2, right=6: nums1[2]=4  vs nums2[2]=8  → 4 < 8, no swap
left=3, right=7: right >= len, stop
```
No changes. gap = ceil(4/2) = 2

**Gap = 2:**
```
left=0, right=2: nums1[0]=-5 vs nums1[2]=4  → -5 < 4, no swap
left=1, right=3: nums1[1]=-2 vs nums1[3]=5  → -2 < 5, no swap
left=2, right=4: nums1[2]=4  vs nums2[0]=-3 → 4 > -3, SWAP
  nums1=[−5,−2,−3,5], nums2=[4,1,8]
left=3, right=5: nums1[3]=5  vs nums2[1]=1  → 5 > 1, SWAP
  nums1=[−5,−2,−3,1], nums2=[4,5,8]
left=4, right=6: nums2[0]=4  vs nums2[2]=8  → 4 < 8, no swap
```
gap = ceil(2/2) = 1

**Gap = 1:**
```
left=0, right=1: nums1[0]=-5 vs nums1[1]=-2 → no swap
left=1, right=2: nums1[1]=-2 vs nums1[2]=-3 → -2 > -3, SWAP
  nums1=[−5,−3,−2,1]
left=2, right=3: nums1[2]=-2 vs nums1[3]=1  → no swap
left=3, right=4: nums1[3]=1  vs nums2[0]=4  → 1 < 4, no swap
left=4, right=5: nums2[0]=4  vs nums2[1]=5  → no swap
left=5, right=6: nums2[1]=5  vs nums2[2]=8  → no swap
```
gap == 1 → break

Copy nums2 to tail:
```
nums1 = [-5,-3,-2,1,4,5,8] ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space | Notes |
|---------|------|-------|-------|
| Extra Array (Merge Sort step) | O(M+N) | O(M+N) | Simplest |
| Two-Pointer Swap + Sort | O(M log M + N log N) | O(1) | Uses sorting |
| Fill From Back | **O(M+N)** ✅ | **O(1)** ✅ | Best overall |
| Gap Method (Shell Sort) | O((M+N) log(M+N)) | O(1) | No extra space, elegant |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Rotate Matrix 90° | Transpose + Reverse rows = 90° clockwise rotation, in-place O(1) space |
| Merge Overlapping Intervals | Sort by start time first — overlapping intervals will always be adjacent |
| Merge Two Sorted Arrays | Fill from back (largest to smallest) avoids overwriting valid data in nums1 |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*+