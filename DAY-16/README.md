# 🚀 Day 16/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Minimum Coins](#1-minimum-coins) | 🔴 Hard | Dynamic Programming |
| 2 | [Assign Cookies](#2-assign-cookies) | 🟢 Easy | Greedy |
| 3 | [Subset Sum Equals Target](#3-subset-sum-equals-to-target) | 🔴 Hard | Recursion / DP |
| 4 | [Subsets II (with Duplicates)](#4-subsets-ii-with-duplicates) | 🟡 Medium | Backtracking |

---

## 1. Minimum Coins

### 🧩 Problem Statement

Given an array `coins[]` of distinct denominations and an integer `amount`, find the **fewest number of coins** needed to make up `amount`. You have infinite coins of each type. Return `-1` if impossible.

**Example 1:**
```
Input:  coins = [1,2,5], amount = 11
Output: 3  (5+5+1)
```

**Example 2:**
```
Input:  coins = [2,5], amount = 3
Output: -1  (impossible)
```

**Constraints:**
- `1 <= n <= 100`
- `1 <= coins[i], amount <= 10³`

---

### 🔴 Approach 1 — Memoization (Top-Down DP)
**Time: O(N × amount) | Space: O(N × amount)**

#### 💡 Intuition
At each index, decide: **take** the current coin (stay at same index since unlimited) or **not take** (move to previous index). Use `1e9` as infinity. Memoize to avoid recomputation.

**Base case:** Only `coins[0]` available — amount is achievable only if divisible by `coins[0]`.

#### 💻 Code
```java
class Solution {
    public int solve(int[] coins, int sum, int idx, int[][] dp) {
        if (idx == 0)
            return sum % coins[0] == 0 ? sum / coins[0] : (int) 1e9;
        if (dp[idx][sum] != -1) return dp[idx][sum];

        int nt = solve(coins, sum, idx - 1, dp);       // not take
        int t = (int) 1e9;
        if (coins[idx] <= sum)                          // take
            t = 1 + solve(coins, sum - coins[idx], idx, dp);

        return dp[idx][sum] = Math.min(nt, t);
    }

    public int MinimumCoins(int[] coins, int amount) {
        int n = coins.length;
        int[][] dp = new int[n][amount + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        int res = solve(coins, amount, n - 1, dp);
        return res == (int) 1e9 ? -1 : res;
    }
}
```

#### 🧪 Dry Run

Input: `coins = [1,2,5]`, `amount = 11`

```
solve(coins, 11, 2, dp):
  idx=2 (coin=5), sum=11
  nt = solve(coins, 11, 1, dp)       → uses coins [1,2] only
  t  = 1 + solve(coins, 6, 2, dp)   → take 5, sum=6

  solve(coins, 6, 2, dp):
    nt = solve(coins, 6, 1, dp)
    t  = 1 + solve(coins, 1, 2, dp)

    solve(coins, 1, 2, dp):
      nt = solve(coins, 1, 1, dp)
      t  = (coins[2]=5 > 1) → skip
      → reduces to solve with coins[1]=2 > 1, then solve with coins[0]=1
      → 1/1 = 1 coin → returns 1

  ... (memoization prevents recomputation)

Final chain: 5+5+1 → 3 coins ✅
```

---

### 🟡 Approach 2 — Tabulation (Bottom-Up DP)
**Time: O(N × amount) | Space: O(N × amount)**

#### 💡 Intuition
Build the DP table iteratively. `dp[i][j]` = min coins to make amount `j` using first `i+1` coins.

#### 💻 Code
```java
class Solution {
    public int MinimumCoins(int[] coins, int amount) {
        int n = coins.length;
        int[][] dp = new int[n][amount + 1];

        for (int i = 0; i <= amount; i++)
            dp[0][i] = i % coins[0] == 0 ? i / coins[0] : (int) 1e9;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= amount; j++) {
                int nt = dp[i - 1][j];
                int t = (int) 1e9;
                if (coins[i] <= j) t = 1 + dp[i][j - coins[i]];
                dp[i][j] = Math.min(nt, t);
            }
        }
        return dp[n - 1][amount] == (int) 1e9 ? -1 : dp[n - 1][amount];
    }
}
```

#### 🧪 Dry Run

Input: `coins = [1,2,5]`, `amount = 5`

```
Base row (coin=1):
dp[0] = [0,1,2,3,4,5]  (i/1 for all i)

Row i=1 (coin=2):
j=0: nt=dp[0][0]=0,  t=INF(2>0)    → 0
j=1: nt=dp[0][1]=1,  t=INF(2>1)    → 1
j=2: nt=dp[0][2]=2,  t=1+dp[1][0]=1 → 1
j=3: nt=dp[0][3]=3,  t=1+dp[1][1]=2 → 2
j=4: nt=dp[0][4]=4,  t=1+dp[1][2]=2 → 2
j=5: nt=dp[0][5]=5,  t=1+dp[1][3]=3 → 3

Row i=2 (coin=5):
j=5: nt=dp[1][5]=3, t=1+dp[2][0]=1 → 1

dp[2][5] = 1 ✅ (just take one 5-coin)
```

---

### 🟢 Approach 3 — Space Optimized (1D DP)
**Time: O(N × amount) | Space: O(amount)**

#### 💡 Intuition
Since `dp[i][j]` only depends on `dp[i][j - coins[i]]` (same row, earlier column) and `dp[i-1][j]` (previous row), we can collapse to 1D. Process left to right for each coin (unbounded knapsack).

#### 💻 Code
```java
class Solution {
    public int MinimumCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, (int) 1e9);
        dp[0] = 0;

        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] = Math.min(dp[j], dp[j - coin] + 1);
            }
        }
        return dp[amount] == (int) 1e9 ? -1 : dp[amount];
    }
}
```

#### 🧪 Dry Run

Input: `coins = [1,2,5]`, `amount = 7`

```
dp = [0, INF, INF, INF, INF, INF, INF, INF]

coin=1:
  j=1: dp[1]=min(INF, dp[0]+1)=1
  j=2: dp[2]=2, j=3: 3, j=4: 4, j=5: 5, j=6: 6, j=7: 7

coin=2:
  j=2: dp[2]=min(2, dp[0]+1)=1
  j=3: dp[3]=min(3, dp[1]+1)=2
  j=4: dp[4]=min(4, dp[2]+1)=2
  j=5: dp[5]=min(5, dp[3]+1)=3
  j=6: dp[6]=min(6, dp[4]+1)=3
  j=7: dp[7]=min(7, dp[5]+1)=4

coin=5:
  j=5: dp[5]=min(3, dp[0]+1)=1
  j=6: dp[6]=min(3, dp[1]+1)=2
  j=7: dp[7]=min(4, dp[2]+1)=2

dp[7] = 2 (5+2) ✅
```

> 💡 **Why iterate left to right for unbounded knapsack (not right to left)?**
> Left to right means when we compute `dp[j]`, `dp[j-coin]` has already been updated in the *current* coin's pass — meaning we can reuse the same coin multiple times. Right to left (0/1 knapsack) would prevent reuse by using the previous pass's value.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Memoization | O(N × amount) | O(N × amount) |
| Tabulation | O(N × amount) | O(N × amount) |
| Space Optimized | **O(N × amount)** ✅ | **O(amount)** ✅ |

---

## 2. Assign Cookies

### 🧩 Problem Statement

Given `student[]` (minimum cookie size needed) and `cookie[]` (cookie sizes), assign **at most one cookie per student**. Maximize the number of satisfied students.

**Example 1:**
```
Input:  student=[1,2,3], cookie=[1,1]
Output: 1  (only student needing size 1 can be satisfied)
```

**Example 2:**
```
Input:  student=[1,2], cookie=[1,2,3]
Output: 2  (all students satisfied)
```

**Constraints:**
- `1 <= student.length <= 3×10⁴`

---

### 🟢 Approach — Greedy (Sort + Two Pointers)
**Time: O(N log N + M log M) | Space: O(1)**

#### 💡 Intuition
**Greedy choice:** Give the smallest cookie that satisfies the least greedy student. Sort both arrays. Use two pointers — if the current cookie satisfies the current student, assign it (move both pointers). Otherwise the cookie is too small — move only the cookie pointer to try a larger one.

**Why smallest satisfying cookie?**
Giving a larger cookie to a student who needs a small one wastes the large cookie — it could satisfy a more demanding student instead.

#### 💻 Code
```java
class Solution {
    public int findMaximumCookieStudents(int[] Student, int[] Cookie) {
        int[] students = Student.clone();
        int[] cookies = Cookie.clone();
        Arrays.sort(students);
        Arrays.sort(cookies);

        int sp = 0, cp = 0, count = 0;

        while (sp < students.length && cp < cookies.length) {
            if (cookies[cp] >= students[sp]) {
                count++;
                sp++; // student satisfied
            }
            cp++; // always move to next cookie
        }
        return count;
    }
}
```

#### 🧪 Dry Run

Input: `student=[4,5,1]`, `cookie=[6,4,2]`

```
Sort students: [1,4,5]
Sort cookies:  [2,4,6]

sp=0, cp=0, count=0

cp=0 (cookie=2): 2 >= student[0]=1? Yes → count=1, sp=1, cp=1
cp=1 (cookie=4): 4 >= student[1]=4? Yes → count=2, sp=2, cp=2
cp=2 (cookie=6): 6 >= student[2]=5? Yes → count=3, sp=3, cp=3

sp=3 >= students.length → exit

Return 3 ✅
```

**Example where cookie is too small:**
`student=[1,2,3]`, `cookie=[1,1]`

```
Sort students: [1,2,3]
Sort cookies:  [1,1]

cp=0: cookie=1 >= student=1? Yes → count=1, sp=1, cp=1
cp=1: cookie=1 >= student=2? No → cp=2

cp=2 >= cookies.length → exit

Return 1 ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N log N + M log M)** ✅ | **O(1)** ✅ |

---

## 3. Subset Sum Equals to Target

### 🧩 Problem Statement

Given array `arr[]` and integer `target`, determine if any **subset** of the array has sum equal to `target`.

**Example 1:**
```
Input:  arr=[1,2,7,3], target=6
Output: True  (subset {1,2,3} sums to 6)
```

**Example 2:**
```
Input:  arr=[2,3,5], target=6
Output: False
```

**Constraints:**
- `1 <= n <= 100`, `1 <= arr[i] <= 100`, `0 <= target <= 5000`

---

### 🔴 Approach 1 — Bitmask (Brute Force)
**Time: O(2ᴺ × N) | Space: O(1)**

#### 💡 Intuition
There are `2^n` possible subsets. Represent each as a bitmask — if bit `i` is set, include `arr[i]`. Check if any subset's sum equals `target`.

#### 💻 Code
```java
class Solution {
    public boolean isSubsetSum(int[] arr, int target) {
        int n = arr.length;
        for (int mask = 0; mask < (1 << n); mask++) {
            int sum = 0;
            for (int i = 0; i < n; i++)
                if ((mask & (1 << i)) != 0) sum += arr[i];
            if (sum == target) return true;
        }
        return false;
    }
}
```

#### 🧪 Dry Run

Input: `arr=[1,2,3]`, `target=5`

```
mask=000 (binary): {} → sum=0
mask=001: {1} → sum=1
mask=010: {2} → sum=2
mask=011: {1,2} → sum=3
mask=100: {3} → sum=3
mask=101: {1,3} → sum=4
mask=110: {2,3} → sum=5 == target → return true ✅
```

---

### 🟡 Approach 2 — Pure Recursion
**Time: O(2ᴺ) | Space: O(N) stack**

#### 💡 Intuition
At each element (from end to 0), either **pick** it (reduce target by arr[i]) or **skip** it. Base case: `target==0` → found; `i<0` → not found.

#### 💻 Code
```java
class Solution {
    public boolean isSubsetSum(int[] arr, int target) {
        return solve(arr, arr.length - 1, target);
    }

    private boolean solve(int[] arr, int i, int target) {
        if (target == 0) return true;
        if (i < 0) return false;
        if (arr[i] > target) return solve(arr, i - 1, target);

        return solve(arr, i - 1, target - arr[i])  // pick
            || solve(arr, i - 1, target);            // skip
    }
}
```

#### 🧪 Dry Run

Input: `arr=[1,2,3]`, `target=5`

```
solve(arr, 2, 5):  i=2, arr[2]=3
  pick: solve(arr, 1, 2)  → i=1, arr[1]=2
          pick: solve(arr, 0, 0) → target==0 → TRUE ✅
```

> 💡 **Why process from the end (n-1 to 0)?**
> It makes the recursion align naturally with DP tabulation (where we build from index 0 upward). Both directions work, but this convention keeps it consistent with the DP versions of this problem.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Bitmask | O(2ᴺ × N) | O(1) |
| Recursion | O(2ᴺ) | O(N) |

---

## 4. Subsets II (with Duplicates)

### 🧩 Problem Statement

Given array `nums` (may contain duplicates), return the **power set** — all possible subsets with no duplicate subsets.

**Example 1:**
```
Input:  nums = [1,2,2]
Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
```

**Example 2:**
```
Input:  nums = [1,2]
Output: [[],[1],[2],[1,2]]
```

**Constraints:**
- `1 <= nums.length <= 10`
- `-10 <= nums[i] <= 10`

---

### 🔴 Approach 1 — Bitmask + HashSet
**Time: O(2ᴺ × N) | Space: O(2ᴺ × N)**

#### 💡 Intuition
Generate all `2^n` subsets via bitmask. Since input is sorted, equal elements always appear in the same order in any subset — so equal subsets are literally identical lists. HashSet handles deduplication automatically.

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer>> seen = new HashSet<>();
        int n = nums.length;

        for (int mask = 0; mask < (1 << n); mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++)
                if ((mask & (1 << i)) != 0) subset.add(nums[i]);
            seen.add(subset);
        }
        return new ArrayList<>(seen);
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1,2,2]` (after sort)

```
mask=000: []
mask=001: [1]
mask=010: [2]
mask=011: [1,2]
mask=100: [2]       ← duplicate of [2], HashSet removes it
mask=101: [1,2]     ← duplicate of [1,2], HashSet removes it
mask=110: [2,2]
mask=111: [1,2,2]

Result: [[],[1],[2],[1,2],[2,2],[1,2,2]] ✅
```

---

### 🟢 Approach 2 — Backtracking (Optimal)
**Time: O(2ᴺ × N) | Space: O(N) stack**

#### 💡 Intuition
Sort the array so duplicates are adjacent. During backtracking, at the **same recursion level** (same `start`), if the current element equals the previous one, skip it — we'd generate the exact same subtree and thus the same subsets.

**Key line:** `if (i > start && nums[i] == nums[i-1]) continue;`

This skips duplicates only at the same level. Different levels (different recursion depths) can still reuse the same value — that's what generates `[2]` and `[2,2]` as separate subsets.

#### 📝 Steps
1. Sort `nums`
2. Start backtracking with `start=0`, empty `current`
3. At each level, always add snapshot of `current` to result
4. Loop `i` from `start` to `n-1`:
   - If `i > start && nums[i] == nums[i-1]` → skip (duplicate at same level)
   - Add `nums[i]`, recurse with `start=i+1`, then remove `nums[i]`

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, nums, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int[] nums,
                           List<Integer> current,
                           List<List<Integer>> result) {
        result.add(new ArrayList<>(current));

        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue; // skip duplicate

            current.add(nums[i]);
            backtrack(i + 1, nums, current, result);
            current.remove(current.size() - 1);
        }
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1,2,2]`

```
backtrack(start=0, current=[]):
  result = [[]]

  i=0 (nums[0]=1): no skip
    current=[1], backtrack(1, [1]):
      result = [[],[1]]

      i=1 (nums[1]=2): no skip
        current=[1,2], backtrack(2, [1,2]):
          result = [[],[1],[1,2]]

          i=2 (nums[2]=2): no skip (i==start==2)
            current=[1,2,2], backtrack(3):
              result = [[],[1],[1,2],[1,2,2]]
            backtrack, current=[1,2]

        backtrack, current=[1]

      i=2 (nums[2]=2): i>start? 2>1, nums[2]==nums[1]? 2==2 → SKIP

    backtrack, current=[]

  i=1 (nums[1]=2): no skip (i==start? No, i=1, start=0, but nums[1]!=nums[0])
    current=[2], backtrack(2, [2]):
      result = [[],[1],[1,2],[1,2,2],[2]]

      i=2 (nums[2]=2): no skip (i==start==2)
        current=[2,2], backtrack(3):
          result = [[],[1],[1,2],[1,2,2],[2],[2,2]]
        backtrack, current=[2]

    backtrack, current=[]

  i=2 (nums[2]=2): i>start? 2>0, nums[2]==nums[1]? 2==2 → SKIP

Final: [[],[1],[1,2],[1,2,2],[2],[2,2]] ✅
```

> 💡 **Why `i > start` (not just `i > 0`) in the skip condition?**
> If we only checked `i > 0`, we'd skip duplicates even at the *first element of a new recursion level* — which would wrongly prevent `[2,2]` from being generated. `i > start` ensures we only skip duplicates at the *same level* of the tree, not across different levels.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Bitmask + HashSet | O(2ᴺ × N) | O(2ᴺ × N) |
| Backtracking | **O(2ᴺ × N)** ✅ | **O(N)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Minimum Coins | Unbounded knapsack — iterate left to right in 1D DP so same coin can be reused. Use `1e9` as infinity, return -1 if unreachable |
| Assign Cookies | Sort both. Smallest satisfying cookie → least greedy student. Don't waste big cookies on small demands |
| Subset Sum | Bitmask for brute; recursion = pick/skip at each index. DP (memoization) gives O(N × target) optimal |
| Subsets II | Backtracking + `i > start && nums[i] == nums[i-1]` skip condition. Avoids duplicate subsets without any HashSet |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
