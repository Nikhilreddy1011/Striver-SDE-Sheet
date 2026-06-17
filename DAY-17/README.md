# 🚀 Day 17/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Combination Sum](#1-combination-sum) | 🟡 Medium | Recursion / Backtracking |
| 2 | [Combination Sum II](#2-combination-sum-ii) | 🟡 Medium | Backtracking |
| 3 | [Palindrome Partitioning](#3-palindrome-partitioning) | 🔴 Hard | Backtracking |
| 4 | [Permutation Sequence](#4-permutation-sequence) | 🟡 Medium | Math / Combinatorics |

---

## 1. Combination Sum

### 🧩 Problem Statement

Given an array of **unique** integers `candidates` and a `target`, find all combinations where numbers sum to `target`. Each number can be used **unlimited times**.

**Example 1:**
```
Input:  candidates = [2,3,5,4], target = 7
Output: [[2,2,3],[3,4],[5,2]]
```

**Example 2:**
```
Input:  candidates = [2], target = 1
Output: []
```

**Constraints:**
- `1 <= candidates.length <= 30`
- `2 <= candidates[i] <= 40`
- All candidates are distinct

---

### 🟢 Approach — Recursion / Backtracking (Unbounded)
**Time: O(K × N^(target/min)) | Space: O(target/min) stack depth**

#### 💡 Intuition
At each index `i`, make one of two choices:
- **Skip** the current element → move to `i-1`
- **Take** the current element → stay at `i`, reduce `sum` by `candidates[i]`

The key to **unlimited usage**: when we "take", we don't move forward — we recurse with the **same index** `i`. Only when we skip do we move to `i-1`.

When `sum == 0`, a valid combination is found. When `sum < 0` or `i < 0`, backtrack.

#### 💻 Code
```java
class Solution {
    public void func(List<Integer> v, int i, int sum,
                     List<Integer> v2, List<List<Integer>> ans) {
        if (sum == 0) { ans.add(new ArrayList<>(v2)); return; }
        if (sum < 0 || i < 0) return;

        func(v, i - 1, sum, v2, ans);        // skip
        v2.add(v.get(i));
        func(v, i, sum - v.get(i), v2, ans); // take (stay at same i)
        v2.remove(v2.size() - 1);             // backtrack
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> v = new ArrayList<>();
        for (int num : candidates) v.add(num);
        func(v, v.size() - 1, target, new ArrayList<>(), ans);
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `candidates = [2,3,5]`, `target = 5`

```
func(v, 2, 5, []):
  Skip 5 → func(v, 1, 5, []):
    Skip 3 → func(v, 0, 5, []):
      Skip 2 → func(v,-1,5) → return (i<0)
      Take 2 → func(v, 0, 3, [2]):
        Take 2 → func(v, 0, 1, [2,2]):
          Take 2 → func(v, 0,-1) → return (sum<0)
          Skip 2 → func(v,-1,1) → return
        Skip 2 → func(v,-1,3) → return
    Take 3 → func(v, 1, 2, [3]):
      Skip 3 → func(v, 0, 2, [3]):
        Take 2 → func(v, 0, 0, [3,2]) → sum==0 → ADD [3,2] ✅
  Take 5 → func(v, 2, 0, [5]) → sum==0 → ADD [5] ✅

(Also generates [2,3] along the way via other branches)
```

> 💡 **Why stay at `i` when taking?**
> If we moved to `i-1` after taking, we'd never use the same element twice. Staying at `i` lets us take it again on the next recursive call — that's what gives us combinations like `[2,2,3]`.

---

## 2. Combination Sum II

### 🧩 Problem Statement

Given an array `candidates` (may have **duplicates**) and a `target`, find all **unique** combinations where each element is used **at most once**.

**Example 1:**
```
Input:  candidates = [2,1,2,7,6,1,5], target = 8
Output: [[1,1,6],[1,2,5],[1,7],[2,6]]
```

**Example 2:**
```
Input:  candidates = [2,5,2,1,2], target = 5
Output: [[1,2,2],[5]]
```

**Constraints:**
- `1 <= candidates.length <= 100`
- Each element used at most once

---

### 🟢 Approach — Backtracking with Duplicate Skipping
**Time: O(2ᴺ × N) | Space: O(N)**

#### 💡 Intuition
Sort the array so duplicates are adjacent. At each step:
1. **Pick** `candidates[index]` → recurse with `index+1`
2. **Skip** `candidates[index]` → but jump past all duplicates of the same value

The duplicate-jumping in step 2 is what prevents identical combinations. Without it, picking `[1(at pos 0), 7]` and `[1(at pos 5), 7]` would both appear as `[1,7]`.

#### 💻 Code
```java
class Solution {
    private void func(int index, int sum, List<Integer> nums,
                      int[] candidates, List<List<Integer>> ans) {
        if (sum == 0) { ans.add(new ArrayList<>(nums)); return; }
        if (sum < 0 || index == candidates.length) return;

        nums.add(candidates[index]);
        func(index + 1, sum - candidates[index], nums, candidates, ans);
        nums.remove(nums.size() - 1); // backtrack

        // Skip all duplicates of candidates[index] before the next call
        for (int i = index + 1; i < candidates.length; i++) {
            if (candidates[i] != candidates[index]) {
                func(i, sum, nums, candidates, ans);
                break;
            }
        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(candidates);
        func(0, target, new ArrayList<>(), candidates, ans);
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `candidates = [2,1,1,5]` (sorted: `[1,1,2,5]`), `target = 5` (partial)

```
Sorted: [1, 1, 2, 5]
         0  1  2  3

func(0, 5, []):
  Pick candidates[0]=1:
    func(1, 4, [1]):
      Pick candidates[1]=1:
        func(2, 3, [1,1]):
          Pick candidates[2]=2:
            func(3, 1, [1,1,2]): 5>1 → sum<target but 5>1 → eventually fail
          ...
          Pick candidates[3]=5: sum-5=3-5 < 0 → fail
          Skip:  no next distinct → return
      Skip candidates[1]=1 → candidates[2]=2 (different!) → func(2, 4, [1]):
        Pick 2: func(3, 2, [1,2]): Pick 5 → sum-5<0 → fail
        Skip to func(3, 4, [1]):
          Pick 5: func(4, -1, [1,5]) → sum<0 fail
  Skip candidates[0]=1 → candidates[2]=2 (jump past duplicate at index 1):
    func(2, 5, []):
      Pick 2: func(3, 3, [2]) → Pick 5: sum-5<0 fail
      Skip → func(3, 5, []): Pick 5 → sum==0 → ADD [5] ✅
```

> 💡 **Combination Sum vs Combination Sum II:**
> | Feature | Sum I | Sum II |
> |---------|-------|--------|
> | Reuse elements? | Yes (unlimited) | No (once each) |
> | Duplicates in input? | No | Yes |
> | Duplicate handling | Not needed | Skip via sort + jump |
> | Direction on take | Stay at `i` | Move to `i+1` |

---

## 3. Palindrome Partitioning

### 🧩 Problem Statement

Given string `s`, return all possible ways to **partition** it such that every substring in the partition is a **palindrome**.

**Example 1:**
```
Input:  s = "aabaa"
Output: [["a","a","b","a","a"],["a","a","b","aa"],["a","aba","a"],
         ["aa","b","a","a"],["aa","b","aa"],["aabaa"]]
```

**Example 2:**
```
Input:  s = "baa"
Output: [["b","a","a"],["b","aa"]]
```

**Constraints:**
- `1 <= s.length <= 16`
- Lowercase letters only

---

### 🟢 Approach — Backtracking + Palindrome Check
**Time: O(N × 2ᴺ) | Space: O(N)**

#### 💡 Intuition
At each `index`, try every possible ending position `i` (from `index` to `n-1`). If `s[index..i]` is a palindrome:
- Add it to `temp`
- Recurse with `index = i+1`
- Backtrack (remove it)

When `index == s.length()`, every character has been covered — save the current partition.

The palindrome check uses two pointers from both ends — O(N) per check.

#### 💻 Code
```java
class Solution {
    private boolean isPalindrome(String s, int left, int right) {
        while (left <= right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++; right--;
        }
        return true;
    }

    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs(0, s, new ArrayList<>(), res);
        return res;
    }

    private void dfs(int index, String s,
                     List<String> temp, List<List<String>> res) {
        if (index == s.length()) { res.add(new ArrayList<>(temp)); return; }

        for (int i = index; i < s.length(); i++) {
            if (isPalindrome(s, index, i)) {
                temp.add(s.substring(index, i + 1));
                dfs(i + 1, s, temp, res);
                temp.remove(temp.size() - 1); // backtrack
            }
        }
    }
}
```

#### 🧪 Dry Run

Input: `s = "baa"`

```
dfs(index=0):
  i=0: s[0..0]="b" → palindrome? Yes
    temp=["b"], dfs(1):
      i=1: s[1..1]="a" → palindrome? Yes
        temp=["b","a"], dfs(2):
          i=2: s[2..2]="a" → palindrome? Yes
            temp=["b","a","a"], dfs(3):
              index==3 → ADD ["b","a","a"] ✅
            backtrack → temp=["b","a"]
        backtrack → temp=["b"]
      i=2: s[1..2]="aa" → palindrome? Yes (a==a)
        temp=["b","aa"], dfs(3):
          index==3 → ADD ["b","aa"] ✅
        backtrack → temp=["b"]
    backtrack → temp=[]
  i=1: s[0..1]="ba" → palindrome? b≠a → No, skip
  i=2: s[0..2]="baa" → palindrome? b≠a → No, skip

Result: [["b","a","a"], ["b","aa"]] ✅
```

> 💡 **Why check `isPalindrome` before recursing?**
> We only recurse on valid palindromic substrings. This prunes the recursion tree early — we never recurse into a dead-end path where some substring is already not a palindrome.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N × 2ᴺ) | O(N) stack |

---

## 4. Permutation Sequence

### 🧩 Problem Statement

Given integers `n` and `k`, return the **k-th permutation** of numbers `[1..n]` in lexicographic order. Solve **without generating all permutations**.

**Example 1:**
```
Input:  n=3, k=3
Output: "213"
Permutations: 123, 132, 213*, 231, 312, 321
```

**Example 2:**
```
Input:  n=3, k=5
Output: "312"
```

**Constraints:**
- `1 <= n <= 9`
- `1 <= k <= n!`

---

### 🟢 Approach — Mathematical Block Division
**Time: O(N²) | Space: O(N)**

#### 💡 Intuition
For `n` numbers, there are `n!` total permutations. They are split into `n` blocks of `(n-1)!` each — one block per starting digit.

So: the first `(n-1)!` permutations start with `nums[0]`, the next `(n-1)!` start with `nums[1]`, etc.

Given `k` (0-indexed): `index = k / (n-1)!` tells which digit comes first. Remove that digit, update `k = k % (n-1)!`, and repeat with `n-1` remaining digits.

#### 📝 Steps
1. Build `nums = [1..n]`, compute `fact = (n-1)!`
2. `k--` (convert to 0-indexed)
3. While `nums` is not empty:
   - `index = k / fact` → pick `nums[index]`, append to answer, remove from `nums`
   - `k %= fact` → remaining k within the block
   - `fact /= nums.size()` → shrink factorial for next position

#### 💻 Code
```java
class Solution {
    public String getPermutation(int n, int k) {
        List<Integer> nums = new ArrayList<>();
        int fact = 1;

        for (int i = 1; i < n; i++) {
            fact *= i;
            nums.add(i);
        }
        nums.add(n);
        k--;

        StringBuilder ans = new StringBuilder();

        while (!nums.isEmpty()) {
            int index = k / fact;
            ans.append(nums.get(index));
            nums.remove(index);

            if (nums.isEmpty()) break;

            k %= fact;
            fact /= nums.size();
        }
        return ans.toString();
    }
}
```

#### 🧪 Dry Run

Input: `n=4, k=9`

```
nums = [1,2,3,4], fact = 3! = 6, k = 9-1 = 8

Step 1: index = 8/6 = 1 → pick nums[1]=2, remove 2
  ans = "2", nums=[1,3,4], k=8%6=2, fact=6/3=2

Step 2: index = 2/2 = 1 → pick nums[1]=3, remove 3
  ans = "23", nums=[1,4], k=2%2=0, fact=2/2=1

Step 3: index = 0/1 = 0 → pick nums[0]=1, remove 1
  ans = "231", nums=[4], k=0%1=0, fact=1/1=1

Step 4: index = 0/1 = 0 → pick nums[0]=4, remove 4
  ans = "2314", nums=[] → break

Return "2314" ✅

Verification: permutations of [1,2,3,4] starting with 2:
  2123 block starts at perm 7, our k=9 → 9th = 2314 ✅
```

**Dry Run for Example 1:** `n=3, k=3`

```
nums=[1,2,3], fact=2!=2, k=3-1=2

Step 1: index=2/2=1 → pick 2, nums=[1,3], k=2%2=0, fact=2/2=1
Step 2: index=0/1=0 → pick 1, nums=[3],  k=0%1=0, fact=1/1=1
Step 3: index=0/1=0 → pick 3, nums=[]  → break

Return "213" ✅
```

> 💡 **Why `k--` at the start?**
> The problem uses 1-indexed `k`. Division-based block selection works with 0-indexed math. Decrementing once converts it without needing special cases throughout.

> 💡 **Why `fact /= nums.size()` (not `nums.size()-1`)?**
> After removing the chosen digit, `nums.size()` has already decreased by 1. So `nums.size()` is exactly `(new n - 1)` — the size before the next pick. Dividing by it gives the correct `(new n - 1)!` for the next iteration.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N²) (list removal is O(N)) | O(N) |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Combination Sum | Unlimited use: stay at same index when taking. No sorting needed since no duplicates |
| Combination Sum II | Single use: move to i+1 when taking. Sort + jump past duplicate values in skip branch |
| Palindrome Partitioning | Try all substrings at each index; only recurse when it's a palindrome. Two-pointer check is O(N) per substring |
| Permutation Sequence | Block division: k/(n-1)! gives which digit comes next. Remove it, update k, shrink factorial. No need to generate all permutations |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
