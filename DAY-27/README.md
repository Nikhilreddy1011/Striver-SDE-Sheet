# 🚀 Day 27/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Largest Rectangle in Histogram](#1-largest-rectangle-in-histogram) | 🔴 Hard | Monotonic Stack |
| 2 | [Sliding Window Maximum](#2-sliding-window-maximum) | 🔴 Hard | Monotonic Deque |
| 3 | [Min Stack](#3-min-stack) | 🔴 Hard | Stack Design |
| 4 | [Rotten Oranges](#4-rotten-oranges) | 🟡 Medium | BFS / Graphs |

---

## 1. Largest Rectangle in Histogram

### 🧩 Problem Statement

Given an array `heights` representing histogram bar heights (each of width 1), find the **area of the largest rectangle** that can be formed.

**Example 1:**
```
Input:  heights = [2, 1, 5, 6, 2, 3]
Output: 10  (bar at index 2-3, height 5, width 2 → 5×2=10)
```

**Example 2:**
```
Input:  heights = [3, 5, 1, 7, 5, 9]
Output: 15  (bar at index 3-5, height 5, width 3 → 5×3=15)
```

**Constraints:** `1 <= heights.length <= 10⁵`, `0 <= heights[i] <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Precompute PSE + NSE)
**Time: O(5N) ≈ O(N) | Space: O(N)**

#### 💡 Idea
For each bar, the rectangle it can form spans from its **Previous Smaller Element (PSE)** to its **Next Smaller Element (NSE)**. The formula:

```
width  = nse[i] - pse[i] - 1
area   = heights[i] × width
```

Precompute `nse[]` (traversing right-to-left) and `pse[]` (traversing left-to-right) using a monotonic stack. Then iterate once to find the max area.

For PSE: if no smaller element on the left → `pse[i] = -1`
For NSE: if no smaller element on the right → `nse[i] = n`

#### 📝 Steps
1. `findNSE(arr)`: traverse right-to-left, monotonic stack of indices, pop while `arr[st.peek()] >= arr[i]`
2. `findPSE(arr)`: traverse left-to-right, same logic
3. For each `i`: `area = heights[i] × (nse[i] - pse[i] - 1)`, track max

#### 💻 Code
```java
class Solution {
    private int[] findNSE(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Stack<Integer> st = new Stack<>();
        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && arr[st.peek()] >= arr[i]) st.pop();
            ans[i] = !st.isEmpty() ? st.peek() : n;
            st.push(i);
        }
        return ans;
    }
    private int[] findPSE(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] >= arr[i]) st.pop();
            ans[i] = !st.isEmpty() ? st.peek() : -1;
            st.push(i);
        }
        return ans;
    }
    public int largestRectangleArea(int[] heights) {
       int n = heights.length;
       int[] nse = findNSE(heights);
       int[] pse = findPSE(heights);
       int largestArea = 0;
       for(int i = 0; i < n; i++)
           largestArea = Math.max(largestArea, heights[i] * (nse[i] - pse[i] - 1));
       return largestArea;
    }
}
```

#### 🧪 Dry Run

Input: `heights = [2, 1, 5, 6, 2, 3]`

```
PSE (previous smaller index):
  i=0 (2): stack=[]   → pse[0]=-1,  push 0. stack=[0]
  i=1 (1): pop 0(h=2>=1) → stack=[]. pse[1]=-1, push 1. stack=[1]
  i=2 (5): h[1]=1<5 → pse[2]=1, push 2. stack=[1,2]
  i=3 (6): h[2]=5<6 → pse[3]=2, push 3. stack=[1,2,3]
  i=4 (2): pop 3(h=6>=2), pop 2(h=5>=2) → h[1]=1<2 → pse[4]=1, push 4.
  i=5 (3): h[4]=2<3 → pse[5]=4, push 5.
  pse = [-1,-1,1,2,1,4]

NSE (next smaller index):
  i=5 (3): stack=[]   → nse[5]=6,  push 5.
  i=4 (2): pop 5(h=3>=2) → stack=[]. nse[4]=6, push 4.
  i=3 (6): h[4]=2<6 → nse[3]=4, push 3.
  i=2 (5): h[3]=6>5 → no pop, nse[2]=3? Wait: stack=[4,3], peek=3, h[3]=6>=5 → pop 3. stack=[4]. h[4]=2<5 → nse[2]=4, push 2.
  i=1 (1): pop 2(5>=1), pop 4(2>=1) → nse[1]=6, push 1.
  i=0 (2): pop 1(1<2)? No: h[1]=1<2 → nse[0]=1, push 0.
  nse = [1,6,4,4,6,6]

Areas:
  i=0: 2*(1-(-1)-1) = 2*1 = 2
  i=1: 1*(6-(-1)-1) = 1*6 = 6
  i=2: 5*(4-1-1) = 5*2 = 10 ← MAX
  i=3: 6*(4-2-1) = 6*1 = 6
  i=4: 2*(6-1-1) = 2*4 = 8
  i=5: 3*(6-4-1) = 3*1 = 3

Return 10 ✅
```

---

### 🟢 Approach 2 — Optimal (Single Pass Stack)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Instead of precomputing PSE and NSE separately, compute both **on the fly** during a single traversal:

When we pop a bar from the stack (because `heights[top] >= heights[i]`):
- `nse = i` (current index triggered the pop = first smaller on right)
- `pse = st.peek()` after pop (new top = first smaller on left)

After the array is fully traversed, remaining elements in the stack have **no NSE** → `nse = n`.

This is equivalent to the brute force but uses only 1 traversal (plus cleanup) instead of 3.

#### 💻 Code
```java
class Solution {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        Stack<Integer> st = new Stack<>();
        int largestArea = 0;
        int area, nse, pse;

        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && heights[st.peek()] >= heights[i]) {
                int ind = st.pop();
                pse = st.isEmpty() ? -1 : st.peek();
                nse = i;
                area = heights[ind] * (nse - pse - 1);
                largestArea = Math.max(largestArea, area);
            }
            st.push(i);
        }

        while (!st.isEmpty()) { 
            nse = n;
            int ind = st.pop();
            pse = st.isEmpty() ? -1 : st.peek();
            area = heights[ind] * (nse - pse - 1);
            largestArea = Math.max(largestArea, area);
        }

        return largestArea;
    }
}
```

#### 🧪 Dry Run

Input: `heights = [2, 1, 5, 6, 2, 3]`

```
i=0 (h=2): stack=[], push 0. stack=[0]
i=1 (h=1): h[0]=2>=1 → pop ind=0
  pse=-1, nse=1, area=2*(1-(-1)-1)=2. largestArea=2
  stack=[]. push 1. stack=[1]
i=2 (h=5): h[1]=1<5, push 2. stack=[1,2]
i=3 (h=6): h[2]=5<6, push 3. stack=[1,2,3]
i=4 (h=2): h[3]=6>=2 → pop ind=3
  pse=2, nse=4, area=6*(4-2-1)=6. largestArea=6
  h[2]=5>=2 → pop ind=2
  pse=1, nse=4, area=5*(4-1-1)=10. largestArea=10
  h[1]=1<2, stop. push 4. stack=[1,4]
i=5 (h=3): h[4]=2<3, push 5. stack=[1,4,5]

Cleanup (nse=6):
  pop ind=5: pse=4, area=3*(6-4-1)=3
  pop ind=4: pse=1, area=2*(6-1-1)=8. largestArea=10
  pop ind=1: pse=-1, area=1*(6-(-1)-1)=6

Return 10 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Precompute PSE+NSE | O(5N) | O(N) |
| Single Pass Stack | **O(N)** ✅ | **O(N)** ✅ |

---

## 2. Sliding Window Maximum

### 🧩 Problem Statement

Given array `arr` and window size `k`, return the **maximum element in each sliding window** as it moves from left to right.

**Example 1:**
```
Input:  arr = [4,0,-1,3,5,3,6,8], k=3
Output: [4,3,5,5,6,8]
```

**Example 2:**
```
Input:  arr = [20,25], k=2
Output: [25]
```

**Constraints:** `1 <= arr.length <= 10⁵`, `1 <= k <= arr.length`

---

### 🔴 Approach 1 — Brute Force
**Time: O((N-K)×K) | Space: O(N-K)**

#### 💡 Idea
For each window starting position `i`, scan all `k` elements and find the max. Simple but too slow for large inputs.

#### 💻 Code
```java
class Solution {
    public int[] maxSlidingWindow(int[] arr, int k) {
        int n = arr.length;
        int[] ans = new int[n-k+1];
        for(int i = 0; i <= n-k; i++){
            int max = arr[i];
            for(int j = i; j < i+k; j++) max = Math.max(max, arr[j]);
            ans[i] = max;
        }
        return ans;
    }
}
```

---

### 🟢 Approach 2 — Optimal (Monotonic Deque)
**Time: O(N) | Space: O(K)**

#### 💡 Idea
Use a **Deque (double-ended queue)** that stores **indices** in **decreasing order of their values**. At every step:

1. **Remove expired index** from front: if `dq.front <= i - k`, it's outside the window
2. **Maintain decreasing order** from back: pop all indices whose values are `≤ arr[i]` (they can never be the max while `arr[i]` is in the window)
3. **Push `i`** to back
4. **Record answer** when first full window is reached (`i >= k-1`): `ans = arr[dq.front]`

**Why deque and not stack?**
We need to remove from both ends — old indices from front (window expiry), smaller values from back (maintaining monotonic order). Only deque supports O(1) at both ends.

#### 💻 Code
```java
class Solution {
    public int[] maxSlidingWindow(int[] arr, int k) {
        int n = arr.length;
        int[] ans = new int[n-k+1];
        int ansIndex = 0;
        Deque<Integer> dq = new LinkedList<>();
        for(int i = 0; i < n; i++){
            if(!dq.isEmpty() && dq.peekFirst() <= (i-k)) dq.pollFirst();
            while(!dq.isEmpty() && arr[dq.peekLast()] <= arr[i]) dq.pollLast();
            dq.offerLast(i);
            if(i >= (k-1)) ans[ansIndex++] = arr[dq.peekFirst()];
        } 
        return ans;   
    }
}
```

#### 🧪 Dry Run

Input: `arr = [4,0,-1,3,5,3,6,8]`, k=3

```
i=0 (4): dq=[], push 0. dq=[0]. i<2, no ans.
i=1 (0): 0<=4, don't pop back. push 1. dq=[0,1]. i<2, no ans.
i=2 (-1): -1<=0, don't pop back. push 2. dq=[0,1,2]. i>=2 → ans[0]=arr[0]=4.
i=3 (3): front=0 <= 3-3=0 → pollFirst. dq=[1,2].
  arr[2]=-1<=3 → pollLast. arr[1]=0<=3 → pollLast. dq=[]. push 3. dq=[3].
  ans[1]=arr[3]=3.
i=4 (5): front=3 > 4-3=1, ok.
  arr[3]=3<=5 → pollLast. dq=[]. push 4. dq=[4].
  ans[2]=arr[4]=5.
i=5 (3): arr[4]=5>3, no pop. push 5. dq=[4,5]. ans[3]=arr[4]=5.
i=6 (6): front=4>6-3=3, ok.
  arr[5]=3<=6 → pollLast. arr[4]=5<=6 → pollLast. dq=[]. push 6. dq=[6].
  ans[4]=arr[6]=6.
i=7 (8): arr[6]=6<=8 → pollLast. push 7. dq=[7]. ans[5]=arr[7]=8.

Result: [4,3,5,5,6,8] ✅
```

> 💡 **Why store indices, not values?**
> To check if the front element has expired (left the window), we compare its **index** with `i - k`. If we stored values, we'd have no way to check this.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O((N-K)×K) | O(N-K) |
| Monotonic Deque | **O(N)** ✅ | **O(K)** ✅ |

---

## 3. Min Stack

### 🧩 Problem Statement

Design a stack supporting `push`, `pop`, `top`, and `getMin` — all in **O(1)** time.

**Example:**
```
push(-2), push(0), push(-3)
getMin() → -3
pop()
top()    → 0
getMin() → -2
```

**Constraints:** At most `5×10⁴` calls, `-10⁵ <= val <= 10⁵`

---

### 🔴 Approach 1 — Pair Stack `{value, currentMin}`
**Time: O(1) | Space: O(2N)**

#### 💡 Idea
Each node in the stack stores a **pair** `{value, minSoFar}`. `minSoFar` at any node is `min(value, previous minSoFar)`. `getMin()` just reads the second element of the top pair.

#### 💻 Code
```java
class MinStack {
    private Stack<int[]> st;

    public MinStack() { st = new Stack<>(); }

    public void push(int value) {
        if (st.isEmpty()) { st.push(new int[]{value, value}); return; }
        int mini = Math.min(getMin(), value);
        st.push(new int[]{value, mini});
    }

    public void pop() { st.pop(); }

    public int top() { return st.peek()[0]; }

    public int getMin() { return st.peek()[1]; }
}
```

#### 🧪 Dry Run

Operations: push(-2), push(0), push(-3), getMin(), pop(), top(), getMin()

```
push(-2): stack=[{-2,-2}]
push(0):  min(0,-2)=-2 → stack=[{-2,-2},{0,-2}]
push(-3): min(-3,-2)=-3 → stack=[{-2,-2},{0,-2},{-3,-3}]
getMin(): peek()[1]=-3 ✅
pop():    stack=[{-2,-2},{0,-2}]
top():    peek()[0]=0 ✅
getMin(): peek()[1]=-2 ✅
```

---

### 🟢 Approach 2 — Formula-Based (Single Stack, O(N) Space)
**Time: O(1) | Space: O(N)**

#### 💡 Idea
Store only one value per push instead of a pair. Use a mathematical encoding to recover the previous minimum on pop.

**Push rule:** If `value <= mini`:
- Push `2*value - mini` (encoded value, always < mini)
- Update `mini = value`
- Else push `value` normally

**Pop rule:** If `top < mini` (encoded value):
- Recover old min: `mini = 2*mini - top`
- Else pop normally

**Top rule:** If `top < mini` (encoded), actual top = `mini`. Else actual top = `top`.

**Why `2*value - mini`?**
The encoded value `2v - m` is always `< mini` when `v <= m` (since `2v - m ≤ m` when `v ≤ m`). This lets us detect encoded values by comparing with `mini`.

#### 💻 Code
```java
class MinStack {
    private Stack<Integer> st;
    private int mini;

    public MinStack() { st = new Stack<>(); }

    public void push(int value) {
        if (st.isEmpty()) { mini = value; st.push(value); return; }
        if (value > mini) {
            st.push(value);
        } else {
            st.push(2 * value - mini);
            mini = value;
        }
    }

    public void pop() {
        if (st.isEmpty()) return;
        int x = st.pop();
        if (x < mini) mini = 2 * mini - x;
    }

    public int top() {
        if (st.isEmpty()) return -1;
        int x = st.peek();
        if (mini < x) return x;
        return mini;
    }

    public int getMin() { return mini; }
}
```

#### 🧪 Dry Run

push(5), push(3), pop(), getMin()

```
push(5): empty → mini=5, push 5. stack=[5]
push(3): 3<=mini=5 → push 2*3-5=1 (encoded), mini=3. stack=[5,1]
pop():   x=1, 1<mini=3 → mini=2*3-1=5. stack=[5]
getMin(): return mini=5 ✅ (correctly recovered)
top():   x=5, mini=5, mini < x? No → return mini=5 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Pair Stack | O(1) | O(2N) |
| Formula Stack | **O(1)** ✅ | **O(N)** ✅ |

---

## 4. Rotten Oranges

### 🧩 Problem Statement

Given an `n×m` grid where `2=rotten`, `1=fresh`, `0=empty`. Every minute, a rotten orange rots adjacent (4-directional) fresh oranges. Return the **minimum minutes** for all oranges to rot, or `-1` if impossible.

**Example 1:**
```
Input:  [[2,1,1],[0,1,1],[1,0,1]]
Output: -1  (bottom-left orange isolated)
```

**Example 2:**
```
Input:  [[2,1,1],[1,1,0],[0,1,1]]
Output: 4
```

**Constraints:** `1 <= n, m <= 500`

---

### 🟢 Approach — Multi-Source BFS
**Time: O(N×M) | Space: O(N×M)**

#### 💡 Idea
This is a classic **multi-source BFS** problem. All rotten oranges are sources that simultaneously spread rot each minute. BFS naturally processes level by level, and each level = 1 minute.

**Algorithm:**
1. Find all initially rotten oranges → add all to queue at once (multi-source)
2. Count `total` non-empty cells
3. BFS level by level: each level = 1 minute. For each rotten orange, rot its 4 fresh neighbors
4. Track `count` of oranges processed. After BFS: if `count == total` → return `time`, else `-1`

**Why `if(!q.isEmpty()) time++`?**
We increment time only if more oranges were added (more rotting will happen next minute). This avoids counting an extra minute at the end.

#### 💻 Code
```java
class Solution {
    private int[] delRow = {-1, 0, 1, 0};
    private int[] delCol = {0, 1, 0, -1};

    private boolean isValid(int i, int j, int n, int m) {
        if (i < 0 || i >= n) return false;
        if (j < 0 || j >= m) return false;
        return true;
    }

    public int orangesRotting(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int time = 0, total = 0, count = 0;
        Queue<int[]> q = new LinkedList<>();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                if (grid[i][j] != 0) total++;
                if (grid[i][j] == 2) q.add(new int[]{i, j});
            }

        while (!q.isEmpty()) {
            int k = q.size();
            count += k;
            while (k-- > 0) {
                int[] cell = q.poll();
                int row = cell[0], col = cell[1];
                for (int i = 0; i < 4; i++) {
                    int nRow = row + delRow[i], nCol = col + delCol[i];
                    if (isValid(nRow, nCol, n, m) && grid[nRow][nCol] == 1) {
                        grid[nRow][nCol] = 2;
                        q.add(new int[]{nRow, nCol});
                    }
                }
            }
            if (!q.isEmpty()) time++;
        }

        return total == count ? time : -1;
    }
}
```

#### 🧪 Dry Run

Input: `[[2,1,1],[1,1,0],[0,1,1]]`

```
Initial rotten: (0,0). total=7 (non-zero cells).
Queue = [(0,0)]

Minute 0 → BFS Level 1:
  Process (0,0): rot neighbors (0,1)→2, (1,0)→2
  count=1, queue=[(0,1),(1,0)]
  queue not empty → time=1

Minute 1 → BFS Level 2:
  Process (0,1): rot (0,2)→2, (1,1)? already... wait (1,1)=1 → 2
  Process (1,0): neighbors checked, (0,0)=2 already, (2,0)=0 skip, (1,1) already queued
  count=1+2=3, queue=[(0,2),(1,1)]
  time=2

Minute 2 → BFS Level 3:
  Process (0,2): rot (1,2)? grid[1][2]=0 → skip
  Process (1,1): rot (2,1)→2
  count=3+2=5, queue=[(2,1)]
  time=3

Minute 3 → BFS Level 4:
  Process (2,1): rot (2,2)→2
  count=5+1=6, queue=[(2,2)]
  time=4

BFS Level 5:
  Process (2,2): no fresh neighbors
  count=6+1=7, queue=[]
  queue empty → time stays 4

total=7 == count=7 → return 4 ✅
```

> 💡 **Why multi-source BFS (all rotten at once) instead of BFS from each separately?**
> Separate BFS would give wrong answers because multiple rotten oranges spread simultaneously. Multi-source BFS naturally models parallelism by starting all sources in the queue from the beginning.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N×M)** ✅ | **O(N×M)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Largest Rectangle | For each bar, max width = nse[i] - pse[i] - 1. Precompute both with stack, or compute on-the-fly by noting: when you pop a bar, current index is NSE and new stack top is PSE |
| Sliding Window Max | Monotonic deque stores indices in decreasing value order. Remove expired front, pop smaller back, front is always the window max |
| Min Stack | Pair: store {val, minSoFar} per node. Formula: encode new min as 2v-m; recover old min on pop if x < mini |
| Rotten Oranges | Multi-source BFS: all initial rotten oranges enter queue together. Each BFS level = 1 minute. Count processed vs total to detect unreachable fresh oranges |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
