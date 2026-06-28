# 🚀 Day 28/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Stock Span Problem](#1-stock-span-problem) | 🔴 Hard | Monotonic Stack |
| 2 | [Maximum of Minimums for Every Window Size](#2-maximum-of-minimums-for-every-window-size) | 🟡 Medium | Monotonic Stack |
| 3 | [Celebrity Problem](#3-celebrity-problem) | 🔴 Hard | Two Pointer / Matrix |

---

## 1. Stock Span Problem

### 🧩 Problem Statement

Given an array `arr` of stock prices, the **span** of day `i` is the number of consecutive previous days (including day `i`) where the price was ≤ `arr[i]`.

**Example 1:**
```
Input:  arr = [120, 100, 60, 80, 90, 110, 115]
Output: [1, 1, 1, 2, 3, 5, 6]
```

**Example 2:**
```
Input:  arr = [15, 13, 12, 14, 16, 20]
Output: [1, 1, 1, 3, 5, 6]
```

**Constraints:** `1 <= n <= 10⁵`, `1 <= arr[i] <= 10⁹`

---

### 🔴 Approach 1 — Brute Force
**Time: O(N²) | Space: O(1)**

#### 💡 Idea
For each day `i`, walk backwards as long as `arr[j] <= arr[i]`. Count the steps. Break when a larger price is found.

#### 💻 Code
```java
class Solution {
    public int[] stockSpan(int[] arr, int n) {
     int[] ans = new int[n];
     for(int i = 0; i < n; i++){
        int currSpan = 0;
        for(int j = i; j >= 0; j--){
            if(arr[j] <= arr[i]) currSpan++;
            else break;
        }
        ans[i] = currSpan;
     }
     return ans;
    }
}
```

#### 🧪 Dry Run

Input: `[120, 100, 60, 80, 90, 110, 115]`

```
i=0 (120): j=0: 120<=120 → span=1. break. ans[0]=1
i=1 (100): j=1: 100<=100 → 1. j=0: 120>100 → break. ans[1]=1
i=2 (60):  j=2: 60<=60 → 1. j=1: 100>60 → break. ans[2]=1
i=3 (80):  j=3: 80<=80 → 1. j=2: 60<=80 → 2. j=1: 100>80 → break. ans[3]=2
i=4 (90):  j=4→3→2→1(break): span=3. ans[4]=3
i=5 (110): j=5→4→3→2→1→0(break): span=5. ans[5]=5
i=6 (115): j=6→5→4→3→2→1→0(break): span=6. ans[6]=6

Result: [1,1,1,2,3,5,6] ✅
```

---

### 🟢 Approach 2 — Optimal (Previous Greater Element + Monotonic Stack)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
The span of day `i` = distance from `i` to the **Previous Greater Element (PGE)** index.

`span[i] = i - PGE[i]`

If no PGE exists (price is the highest so far), PGE index = `-1`, so `span = i - (-1) = i + 1`.

**Finding PGE:** traverse left to right with a monotonic stack storing indices in **decreasing order of values**. Pop while `arr[top] <= arr[i]` (not strictly greater).

#### 💻 Code
```java
class Solution {
    private int[] findPGE(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] <= arr[i]) st.pop();
            ans[i] = st.isEmpty() ? -1 : st.peek();
            st.push(i);
        }
        return ans;
    }

    public int[] stockSpan(int[] arr, int n) {
        int[] PGE = findPGE(arr);
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) ans[i] = i - PGE[i];
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `[120, 100, 60, 80, 90, 110, 115]`

```
Finding PGE (left to right):
i=0 (120): stack=[] → PGE[0]=-1, push 0. stack=[0]
i=1 (100): arr[0]=120>100, no pop → PGE[1]=0, push 1. stack=[0,1]
i=2 (60):  arr[1]=100>60, no pop → PGE[2]=1, push 2. stack=[0,1,2]
i=3 (80):  pop 2(60<=80) → arr[1]=100>80, stop → PGE[3]=1, push 3. stack=[0,1,3]
i=4 (90):  pop 3(80<=90) → arr[1]=100>90, stop → PGE[4]=1, push 4. stack=[0,1,4]
i=5 (110): pop 4(90<=110), pop 1(100<=110) → arr[0]=120>110 → PGE[5]=0, push 5.
i=6 (115): pop 5(110<=115) → arr[0]=120>115 → PGE[6]=0, push 6.

PGE = [-1, 0, 1, 1, 1, 0, 0]

Span = i - PGE[i]:
  [0-(-1), 1-0, 2-1, 3-1, 4-1, 5-0, 6-0]
= [1, 1, 1, 2, 3, 5, 6] ✅
```

> 💡 **Why pop when `arr[top] <= arr[i]` (not strictly less)?**
> We want the first element that is **strictly greater**. If `arr[top] == arr[i]`, it cannot be the PGE (span counts only strictly greater as the boundary). So we pop equal elements too.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(1) |
| Monotonic Stack (PGE) | **O(N)** ✅ | **O(N)** ✅ |

---

## 2. Maximum of Minimums for Every Window Size

### 🧩 Problem Statement

Given array `arr[]` of size `n`, for every window size `i` (1 to n), find the **maximum of all minimum values** of contiguous subarrays of size `i`.

**Example 1:**
```
Input:  arr = [10, 20, 30, 50, 10, 70, 30]
Output: [70, 30, 20, 10, 10, 10, 10]
  Window 1: max of [10,20,30,50,10,70,30] = 70
  Window 2: max of [min(10,20), min(20,30), ...] = max(10,20,20,10,10,30) = 30
  ...
```

**Constraints:** `1 <= n <= 10⁵`, `1 <= arr[i] <= 10⁶`

---

### 🟢 Approach — PSE + NSE + Backward Fill
**Time: O(N) | Space: O(N)**

#### 💡 Idea
For each element `arr[i]`, find the **largest window** for which it is the minimum:
- Left boundary: Previous Smaller Element index (`left[i]`), or `-1`
- Right boundary: Next Smaller Element index (`right[i]`), or `n`

Max window size where `arr[i]` is the minimum: `len = right[i] - left[i] - 1`

Update `ans[len] = max(ans[len], arr[i])`.

**Why backward fill?** If `arr[i]` is the minimum for a window of size 5, it's also a candidate for windows of size 1–4. Filling from right to left propagates larger values to smaller window sizes.

**PSE vs NSE duplicates:** PSE uses `>=` (pop equal), NSE uses `>` (don't pop equal). This prevents counting an element twice when duplicates exist.

#### 💻 Code
```java
class Solution {
    public int[] maxOfMin(int[] arr) {
        int n = arr.length;
        int[] left = new int[n], right = new int[n];
        Stack<Integer> st = new Stack<>();

        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] >= arr[i]) st.pop();
            left[i] = st.isEmpty() ? -1 : st.peek();
            st.push(i);
        }

        st.clear();

        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && arr[st.peek()] > arr[i]) st.pop();
            right[i] = st.isEmpty() ? n : st.peek();
            st.push(i);
        }

        int[] ans = new int[n + 1];
        Arrays.fill(ans, Integer.MIN_VALUE);

        for (int i = 0; i < n; i++) {
            int len = right[i] - left[i] - 1;
            ans[len] = Math.max(ans[len], arr[i]);
        }

        for (int i = n - 1; i >= 1; i--) ans[i] = Math.max(ans[i], ans[i + 1]);

        int[] res = new int[n];
        for (int i = 1; i <= n; i++) res[i - 1] = ans[i];
        return res;
    }
}
```

#### 🧪 Dry Run

Input: `arr = [6, 3, 5, 1, 12]` (n=5)

```
PSE (left[]):
i=0(6):  stack=[] → left[0]=-1, push 0
i=1(3):  pop 0(6>=3) → left[1]=-1, push 1
i=2(5):  arr[1]=3<5 → left[2]=1, push 2
i=3(1):  pop 2(5>=1), pop 1(3>=1) → left[3]=-1, push 3
i=4(12): arr[3]=1<12 → left[4]=3, push 4
left = [-1,-1,1,-1,3]

NSE (right[]):
i=4(12): stack=[] → right[4]=5, push 4
i=3(1):  arr[4]=12>1, no pop → right[3]=4, push 3
i=2(5):  arr[3]=1<5 → right[2]=3, push 2  (wait: 1 NOT > 5, so no pop. right[2]=3)
i=1(3):  arr[2]=5>3 → pop 2. arr[3]=1<3 → right[1]=3, push 1
i=0(6):  arr[1]=3<6 → right[0]=1. Wait: 3 NOT > 6, so right[0]=1? No: stack=[3,1], peek=1, arr[1]=3. 3>6? No → right[0]=1.
Hmm, let me redo: stack after i=1 push 1 = [3,1]. peek=1, arr[1]=3. 3 > arr[0]=6? No → right[0]=1. push 0.
right = [1, 3, 3, 4, 5]

len = right[i] - left[i] - 1:
  i=0: 1-(-1)-1=1, ans[1]=max(MIN,6)=6
  i=1: 3-(-1)-1=3, ans[3]=max(MIN,3)=3
  i=2: 3-1-1=1,   ans[1]=max(6,5)=6  (6 stays)
  i=3: 4-(-1)-1=4, ans[4]=max(MIN,1)=1
  i=4: 5-3-1=1,   ans[1]=max(6,12)=12

ans = [MIN, 12, MIN, 3, 1, MIN]  (indices 0..5)

Backward fill (i from 4 to 1):
  ans[4]=max(1,MIN)=1
  ans[3]=max(3,1)=3
  ans[2]=max(MIN,3)=3
  ans[1]=max(12,3)=12

res = [ans[1], ans[2], ans[3], ans[4], ans[5]] = [12, 3, 3, 1, MIN→1]
Result: [12, 3, 3, 1, 1] ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | **O(N)** ✅ |

---

## 3. Celebrity Problem

### 🧩 Problem Statement

Given an `N×N` matrix `M` where `M[i][j]=1` means person `i` knows person `j`. A **celebrity** is known by everyone but knows nobody. Return the celebrity index or `-1`.

**Example 1:**
```
Input:  M = [[0,1,1,0],[0,0,0,0],[1,1,0,0],[0,1,1,0]]
Output: 1  (person 1 knows nobody, everyone knows person 1)
```

**Example 2:**
```
Input:  M = [[0,1],[1,0]]
Output: -1  (both know each other)
```

**Constraints:** `1 <= N <= 3000`

---

### 🔴 Approach 1 — Brute Force (Count Rows and Columns)
**Time: O(N²) | Space: O(N)**

#### 💡 Idea
For each person, count how many people know them (`knowMe[j]++`) and how many they know (`Iknow[i]++`). Celebrity: `knowMe[i] == n-1` AND `Iknow[i] == 0`.

#### 💻 Code
```java
class Solution {
    public int celebrity(int[][] M) {
        int n = M.length;
        int[] knowMe = new int[n], Iknow = new int[n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (M[i][j] == 1) { knowMe[j]++; Iknow[i]++; }
        for (int i = 0; i < n; i++)
            if (knowMe[i] == n - 1 && Iknow[i] == 0) return i;
        return -1;
    }
}
```

#### 🧪 Dry Run

`M = [[0,1,1,0],[0,0,0,0],[1,1,0,0],[0,1,1,0]]`

```
After matrix traversal:
knowMe = [1, 3, 2, 0]  (col sums)
Iknow  = [2, 0, 2, 2]  (row sums)

i=0: knowMe=1≠3 → no
i=1: knowMe=3==3, Iknow=0==0 → return 1 ✅
```

---

### 🟢 Approach 2 — Optimal (Two Pointer Elimination)
**Time: O(N) | Space: O(1)**

#### 💡 Idea
Use `top=0` and `down=n-1`. At each step, one of them cannot be the celebrity:

- If `M[top][down] == 1`: top knows down → **top** cannot be celebrity → `top++`
- If `M[down][top] == 1`: down knows top → **down** cannot be celebrity → `down--`
- If neither knows the other: **both** are eliminated → `top++; down--`

When `top >= down`, the candidate is `top`. Verify it fully with one O(N) pass.

**Why does this work?** A celebrity cannot know anyone, and must be known by everyone. Each comparison eliminates at least one person in O(1).

#### 💻 Code
```java
class Solution {
    public int celebrity(int[][] M) {
        int n = M.length;
        int top = 0, down = n - 1;
        while (top < down) {
            if (M[top][down] == 1) top++;
            else if (M[down][top] == 1) down--;
            else { top++; down--; }
        }
        if (top > down) return -1;
        for (int i = 0; i < n; i++) {
            if (i == top) continue;
            if (M[top][i] == 1 || M[i][top] == 0) return -1;
        }
        return top;
    }
}
```

#### 🧪 Dry Run

`M = [[0,1,1,0],[0,0,0,0],[1,1,0,0],[0,1,1,0]]`, n=4

```
top=0, down=3

Step 1: M[0][3]=0, M[3][0]=0 → both eliminated → top=1, down=2
Step 2: M[1][2]=0, M[2][1]=1 → down(2) knows top(1) → down=1

top=1, down=1 → exit loop (top not > down)

Verify top=1:
  i=0: M[1][0]=0 ✓ (1 doesn't know 0), M[0][1]=1 ✓ (0 knows 1)
  i=2: M[1][2]=0 ✓, M[2][1]=1 ✓
  i=3: M[1][3]=0 ✓, M[3][1]=1 ✓

Return 1 ✅
```

**Impossible case:** `M = [[0,1],[1,0]]`

```
top=0, down=1: M[0][1]=1 → top++ → top=1
top=1 >= down=1 → exit. top not > down.

Verify top=1:
  i=0: M[1][0]=1 → top knows 0 → return -1 ✅
```

> 💡 **Why verify after elimination?**
> The two-pointer elimination only guarantees that all others are NOT the celebrity. The remaining candidate still needs to be confirmed — it might not be a valid celebrity either (e.g., in the case of no celebrity at all).

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Count Rows/Cols | O(N²) | O(N) |
| Two Pointer | **O(N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Stock Span | Span[i] = i - PGE[i]. Finding PGE with monotonic stack eliminates the backward scan entirely |
| Max of Minimums | For each element, the largest window where it's the min = right[i] - left[i] - 1. Assign to ans[len], then backward fill to propagate down to smaller windows |
| Celebrity Problem | Two pointers eliminate non-celebrities in O(1) per step. One final O(N) verification confirms the candidate |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
