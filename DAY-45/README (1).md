# 🚀 Day 45/45 – #SDESheetChallenge ✅

> **45 days. Done.** Solving problems from Striver's SDE Sheet — every single day, no skips.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Distinct Numbers in Each Subarray](#1-distinct-numbers-in-each-subarray) | 🟡 Medium | Sliding Window / HashMap |
| 2 | [Kth Largest Element in an Array](#2-kth-largest-element-in-an-array) | 🟡 Medium | Heap / QuickSelect |
| 3 | [Flood Fill Algorithm](#3-flood-fill-algorithm) | 🟡 Medium | Graph / DFS |

---

## 1. Distinct Numbers in Each Subarray

### 🧩 Problem Statement

Given an integer array `nums` and integer `k`, return an array where each element is the count of distinct numbers in the corresponding window of size `k`.

**Example 1:**
```
nums=[1,2,3,2,2,1,3], k=3
Windows:  [1,2,3]=3, [2,3,2]=2, [3,2,2]=2, [2,2,1]=2, [2,1,3]=3
Output:   [3, 2, 2, 2, 3]
```

**Constraints:** `1 <= k <= nums.length <= 10⁵`

---

### 🔴 Approach 1 — Brute (HashSet per window)
**Time: O(N×K) | Space: O(K)**

#### 💡 Idea
For each window position, create a fresh HashSet, add all k elements, and record the set's size (duplicates are automatically ignored).

```java
class Solution {
    public List<Integer> distinctNumbers(int[] nums, int k) {
       int n = nums.length;
       List<Integer> res = new ArrayList<>();
       for (int i = 0; i <= n - k; i++) {
           Set<Integer> set = new HashSet<>();
           for (int j = i; j < i + k; j++) set.add(nums[j]);
           res.add(set.size());
       }
       return res;
    }
}
```

**Why inefficient?** Two nested loops — outer O(N-k+1) ≈ O(N), inner O(K). Total O(N×K). For N=10⁵ and K=10⁴, that's 10⁹ operations — too slow.

---

### 🟢 Approach 2 — Optimal (Sliding Window + HashMap)
**Time: O(N) | Space: O(K)**

#### 💡 Idea

Instead of rebuilding the window from scratch each time, **slide it**. When the window moves right by 1:
- One element **leaves** (the leftmost of the old window)
- One element **enters** (the new rightmost)

Track **frequencies** in a HashMap (not just presence). A key exists in the map = that element is currently in the window. `map.size()` = distinct count.

**When element leaves:** decrement its frequency. If frequency hits 0, remove it from map (no longer in window).
**When element enters:** increment its frequency (add to map if new).

This processes each element exactly once → O(N).

#### 💻 Code
```java
class Solution {
    public List<Integer> distinctNumbers(int[] nums, int k) {
        int n = nums.length;
        Map<Integer, Integer> freq = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        // Initialize first window
        for (int i = 0; i < k; i++)
            freq.put(nums[i], freq.getOrDefault(nums[i], 0) + 1);

        result.add(freq.size());  // distinct count for first window

        // Slide window
        for (int i = k; i < n; i++) {
            int outgoing = nums[i - k];                    // element leaving
            freq.put(outgoing, freq.get(outgoing) - 1);
            if (freq.get(outgoing) == 0) freq.remove(outgoing); // gone from window

            int incoming = nums[i];                        // element entering
            freq.put(incoming, freq.getOrDefault(incoming, 0) + 1);

            result.add(freq.size());
        }

        return result;
    }
}
```

#### 🧪 Dry Run

**nums=[1,2,3,2,2,1,3], k=3**

```
Init window [1,2,3]: freq={1:1, 2:1, 3:1}. size=3. result=[3]

i=3 (incoming=2, outgoing=nums[0]=1):
  freq[1]-- → 0 → remove. freq={2:1,3:1}
  freq[2]++ → freq={2:2,3:1}. size=2. result=[3,2]

i=4 (incoming=2, outgoing=nums[1]=2):
  freq[2]-- → 1. freq={2:1,3:1}
  freq[2]++ → freq={2:2,3:1}. size=2. result=[3,2,2]

i=5 (incoming=1, outgoing=nums[2]=3):
  freq[3]-- → 0 → remove. freq={2:2}
  freq[1]=1. freq={2:2,1:1}. size=2. result=[3,2,2,2]

i=6 (incoming=3, outgoing=nums[3]=2):
  freq[2]-- → 1. freq={2:1,1:1}
  freq[3]=1. freq={2:1,1:1,3:1}. size=3. result=[3,2,2,2,3] ✅
```

> 💡 **Why HashMap and not HashSet?**
> A Set only tells us if an element is present or absent. When an element appears multiple times in the window and the window moves out the first occurrence, we can't know if other occurrences still exist. The frequency count in HashMap lets us safely remove the key only when count reaches 0.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute (set per window) | O(N×K) | O(K) |
| Sliding Window + Freq Map | **O(N)** ✅ | O(K) |

---

## 2. Kth Largest Element in an Array

### 🧩 Problem Statement

Given an array `nums`, return the kth largest element (not kth distinct — duplicates count).

**Example:** nums=[1,2,3,4,5], k=2 → 4

**Constraints:** `1 <= nums.length <= 10⁵`, `1 <= k <= nums.length`

---

### 🟡 Approach 1 — Better (Min-Heap of size k)
**Time: O(N log K) | Space: O(K)**

Maintain a min-heap of size k with the k largest elements. Heap's top = kth largest. (Same as Day 43's Kth Largest in Stream — already mastered this!)

```java
class Solution {
    public int kthLargestElement(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < k; i++) pq.add(nums[i]);
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > pq.peek()) { pq.poll(); pq.add(nums[i]); }
        }
        return pq.peek();
    }
}
```

---

### 🟢 Approach 2 — Optimal (QuickSelect)
**Time: O(N) average | Space: O(1)**

#### 💡 Idea

Based on QuickSort's partition step. In QuickSort (descending), the pivot ends up at its sorted position. If pivot lands at index `k-1`, it's the kth largest. If pivot lands too far right (index > k-1), search left. If too far left, search right.

Unlike QuickSort, we only recurse on **one side** — the side containing the kth element. Average case: T(N) = N + N/2 + N/4 + ... = O(N).

**Why randomize the pivot?** Choosing leftmost or rightmost as pivot degrades to O(N²) on sorted arrays. Random pivot reduces worst-case probability to negligible.

#### 💻 Code
```java
class Solution {
    public int kthLargestElement(int[] nums, int k) {
        int left = 0, right = nums.length - 1;
        while (true) {
            int pivotIndex = randomIndex(left, right);
            pivotIndex = partitionAndReturn(nums, pivotIndex, left, right);
            if      (pivotIndex == k - 1) return nums[pivotIndex];
            else if (pivotIndex > k - 1)  right = pivotIndex - 1;
            else                           left  = pivotIndex + 1;
        }
    }

    private Random rand = new Random();
    private int randomIndex(int l, int r) { return rand.nextInt(r - l + 1) + l; }
    private void swap(int[] a, int i, int j) { int t=a[i]; a[i]=a[j]; a[j]=t; }

    private int partitionAndReturn(int[] arr, int pi, int left, int right) {
        int pivot = arr[pi];
        swap(arr, left, pi);        // move pivot to front
        int ind = left + 1;
        for (int i = left + 1; i <= right; i++) {
            if (arr[i] > pivot) { swap(arr, ind, i); ind++; } // bigger → left portion
        }
        swap(arr, left, ind - 1);   // place pivot at correct position
        return ind - 1;
    }
}
```

#### 🧪 Dry Run

**nums=[-5,4,1,2,-3], k=5 (want 5th largest = smallest)**

```
Partition step (say pivot chosen at index 2, value=1):
  After partition (descending order): [4, 2, 1, -3, -5]
  pivot=1 lands at index 2.

  k-1=4. pivotIndex=2 < 4 → search right. left=3.

Partition step in [index 3..4] = [-3,-5] (say pivot=-3):
  After partition: [-3, -5]
  pivot=-3 at index 3. k-1=4. 3 < 4 → search right. left=4.

Partition step in [4..4] = [-5]:
  pivot=-5 at index 4. 4 == k-1=4 → return -5 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Sort | O(N log N) | O(1) |
| Min-Heap of size k | O(N log K) | O(K) |
| QuickSelect | **O(N) average** ✅ | **O(1)** ✅ |

---

## 3. Flood Fill Algorithm

### 🧩 Problem Statement

Given a 2D image, a starting pixel `(sr, sc)`, and a `newColor`, flood-fill from the starting pixel — change the color of all 4-directionally connected pixels with the same initial color to `newColor`.

**Example:**
```
image = [[1,1,1],[1,1,0],[1,0,1]], sr=1, sc=1, newColor=2
Output: [[2,2,2],[2,2,0],[2,0,1]]
Only the 1s connected to (1,1) change; the bottom-right 1 is not 4-directionally connected.
```

**Constraints:** `1 <= n,m <= 50`

---

### 🟢 Approach — DFS with direction vectors
**Time: O(N×M) | Space: O(N×M)**

#### 💡 Idea

Think of the image as a graph where each pixel is a node connected to its 4 neighbors. Starting from `(sr, sc)`, DFS visits all pixels with the same initial color and recolors them.

**Key design decisions:**
1. **Copy the image** before modifying — good practice to not alter input
2. **Check `ans[nrow][ncol] != newColor`** — prevents revisiting already-colored pixels (avoids infinite loop when `iniColor == newColor`)
3. **Direction vectors** `delRow={-1,0,1,0}`, `delCol={0,1,0,-1}` — clean way to visit up/right/down/left

**Edge case:** If `iniColor == newColor`, nothing to do (pixels already have the target color). The `ans[nrow][ncol] != newColor` check handles this automatically.

#### 💻 Code
```java
class Solution {
    private int[] delRow = {-1, 0, 1, 0};
    private int[] delCol = {0, 1, 0, -1};

    private boolean isValid(int i, int j, int n, int m) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    private void dfs(int row, int col, int[][] ans, int[][] image, int newColor, int iniColor) {
        ans[row][col] = newColor;           // color this pixel
        int n = image.length, m = image[0].length;
        for (int i = 0; i < 4; i++) {
            int nrow = row + delRow[i];
            int ncol = col + delCol[i];
            if (isValid(nrow, ncol, n, m) &&
                image[nrow][ncol] == iniColor &&   // same original color
                ans[nrow][ncol] != newColor) {     // not already colored
                dfs(nrow, ncol, ans, image, newColor, iniColor);
            }
        }
    }

    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int iniColor = image[sr][sc];
        int[][] ans = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) ans[i] = Arrays.copyOf(image[i], image[i].length);
        dfs(sr, sc, ans, image, newColor, iniColor);
        return ans;
    }
}
```

#### 🧪 Dry Run

**image=[[1,1,1],[1,1,0],[1,0,1]], sr=1, sc=1, newColor=2, iniColor=1**

```
ans starts as copy of image.

dfs(1,1): ans[1][1]=2. Check 4 neighbors:
  Up(0,1):    image[0][1]=1==iniColor, ans[0][1]=1≠2 → dfs(0,1)
    dfs(0,1): ans[0][1]=2. Check neighbors:
      Up(-1,1): invalid
      Right(0,2): image[0][2]=1==iniColor, ans[0][2]=1≠2 → dfs(0,2)
        dfs(0,2): ans[0][2]=2. No valid uncolored neighbors of color 1.
      Down(1,1): image[1][1]=1==iniColor, ans[1][1]=2 (already colored, skip)
      Left(0,0): image[0][0]=1==iniColor, ans[0][0]=1≠2 → dfs(0,0)
        dfs(0,0): ans[0][0]=2. Check neighbors:
          Down(1,0): image[1][0]=1==iniColor, ans[1][0]=1≠2 → dfs(1,0)
            dfs(1,0): ans[1][0]=2. Check neighbors:
              Up(0,0): already colored (ans=2, skip)
              Right(1,1): already colored (skip)
              Down(2,0): image[2][0]=1==iniColor, ans[2][0]=1≠2 → dfs(2,0)
                dfs(2,0): ans[2][0]=2. Neighbors: (1,0) colored, (3,0) invalid, (2,1) image=0≠iniColor. STOP.
              Left(1,-1): invalid
  Right(1,2): image[1][2]=0≠iniColor → skip
  Down(2,1):  image[2][1]=0≠iniColor → skip
  Left(1,0):  image[1][0]=1==iniColor, ans[1][0]=2 (already colored, skip)

Final ans:
Row 0: [2, 2, 2]
Row 1: [2, 2, 0]
Row 2: [2, 0, 1]  ← (2,2) has image=1 but not 4-connected to start ✅
```

> 💡 **Why check `image[nrow][ncol] == iniColor` instead of `ans[nrow][ncol] == iniColor`?**
> We use the ORIGINAL image to decide if a neighbor should be colored. Once we start coloring `ans`, previously colored cells show `newColor`, not `iniColor`. Using `image` keeps decisions based on the original state.

> 💡 **Why `ans[nrow][ncol] != newColor` as the visited check?**
> This prevents revisiting already-colored cells. A `visited[][]` array would also work, but `ans[nrow][ncol] != newColor` doubles as the visited check for free — if it's already `newColor`, it was already processed.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N×M)** — each pixel visited at most once | O(N×M) — ans array + recursion stack |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Distinct Numbers in Subarray | Sliding window + frequency map. Slide = remove outgoing (decrement freq, delete if 0) + add incoming. One pass O(N) |
| Kth Largest in Array | Min-heap of size k = O(N log K). QuickSelect = O(N) average. Randomize pivot to avoid O(N²) worst case |
| Flood Fill | DFS on 2D grid. Direction vectors for 4-connectivity. Check original image for same-color condition. Use `ans[r][c] != newColor` as visited check |

---

## 🏁 45-Day SDE Sheet Challenge — Complete!

45 days. 150+ problems. Arrays → Linked Lists → Greedy → Recursion → Binary Search → Stacks → Strings → Binary Trees → BST → Graphs. Every single day, documented with full dry runs and clean code.

The challenge wasn't just about solving problems — it was about building the habit of thinking through problems systematically, every day.

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
