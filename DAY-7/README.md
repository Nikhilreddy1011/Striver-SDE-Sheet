# 🚀 Day 7/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Two Sum](#1-two-sum) | 🟢 Easy | Arrays / Hashing |
| 2 | [4 Sum](#2-4-sum) | 🟡 Medium | Arrays / Two Pointer |
| 3 | [Longest Consecutive Sequence](#3-longest-consecutive-sequence-in-an-array) | 🟡 Medium | Arrays / HashSet |

---

## 1. Two Sum

### 🧩 Problem Statement

Given an array of integers `nums` and an integer `target`, return the **indices (0-indexed)** of two elements that add up to `target`. Each input has exactly one solution. The same element cannot be used twice.

**Example 1:**
```
Input:  nums = [1, 6, 2, 10, 3], target = 7
Output: [0, 1]
Explanation: nums[0] + nums[1] = 1 + 6 = 7
```

**Example 2:**
```
Input:  nums = [1, 3, 5, -7, 6, -3], target = 0
Output: [1, 5]
Explanation: nums[1] + nums[5] = 3 + (-3) = 0
```

**Constraints:**
- `2 <= nums.length <= 10⁵`
- `-10⁴ <= nums[i] <= 10⁴`
- Exactly one valid answer exists

---

### 🔴 Approach 1 — Brute Force (Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
Check every possible pair `(i, j)` where `i < j`. If `nums[i] + nums[j] == target`, return `[i, j]`.

#### 💻 Code
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        int[] val = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    val[0] = i;
                    val[1] = j;
                    return val;
                }
            }
        }
        return new int[]{-1, -1};
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1, 6, 2, 10, 3], target = 7`

```
i=0 (val=1):
  j=1: 1+6=7 == 7 → return [0, 1] ✅
```

---

### 🟢 Approach 2 — Optimal (HashMap)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
For each element `num`, we need `target - num` to complete the pair. Instead of scanning the whole array again, store previously seen elements in a HashMap. For each new element, check if its complement already exists.

**The key insight:** We put elements into the map **after** checking — this ensures we never use the same index twice.

#### 📝 Steps
1. Create a `HashMap<Integer, Integer>` storing `(value → index)`
2. For each `nums[i]`:
   - Compute `rem = target - nums[i]`
   - If `rem` is in the map → return `[map.get(rem), i]`
   - Else → put `nums[i]` into the map with index `i`
3. Return `[-1, -1]` if no pair found

#### 💻 Code
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int rem = target - num;
            if (map.containsKey(rem)) {
                return new int[]{map.get(rem), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1, 6, 2, 10, 3], target = 7`

```
map = {}

i=0: num=1, rem=7-1=6 → map has 6? No → map={1:0}
i=1: num=6, rem=7-6=1 → map has 1? Yes! → return [map.get(1), 1] = [0, 1] ✅
```

**Second example:** `nums = [1, 3, 5, -7, 6, -3], target = 0`

```
map = {}

i=0: num=1,  rem=-1  → No → map={1:0}
i=1: num=3,  rem=-3  → No → map={1:0, 3:1}
i=2: num=5,  rem=-5  → No → map={1:0, 3:1, 5:2}
i=3: num=-7, rem=7   → No → map={..., -7:3}
i=4: num=6,  rem=-6  → No → map={..., 6:4}
i=5: num=-3, rem=3   → map has 3? Yes! → return [map.get(3), 5] = [1, 5] ✅
```

> 💡 **Why put AFTER checking?**
> If we put first and then check, `map.containsKey(rem)` could return the same index `i` (when `rem == nums[i]`), violating the "same element cannot be used twice" constraint.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(1) |
| HashMap | **O(N)** ✅ | O(N) |
| Two Pointer (sorted) | O(N log N) | O(N) |

> The Two Pointer approach (sort + two pointers) works when indices don't need to be returned. Since this problem asks for original indices, HashMap is the optimal choice.

---

## 2. 4 Sum

### 🧩 Problem Statement

Given an integer array `nums` and an integer `target`, return all **unique quadruplets** `[nums[a], nums[b], nums[c], nums[d]]` such that `a, b, c, d` are distinct indices and `nums[a] + nums[b] + nums[c] + nums[d] == target`.

**Example 1:**
```
Input:  nums = [1, -2, 3, 5, 7, 9], target = 7
Output: [[-2, 1, 3, 5]]
```

**Example 2:**
```
Input:  nums = [7, -7, 1, 2, 14, 3], target = 9
Output: []
```

**Constraints:**
- `1 <= nums.length <= 200`
- `-10⁴ <= nums[i] <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (4 Nested Loops + Set)
**Time: O(N⁴) | Space: O(no. of quadruplets)**

#### 💡 Intuition
Try every possible combination of 4 indices `(i, j, k, l)`. If their sum equals `target`, sort the quadruplet and add to a set (to avoid duplicates). Return the set as a list.

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        int n = nums.length;
        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int l = k + 1; l < n; l++) {
                        long sum = nums[i] + nums[j];
                        sum += nums[k];
                        sum += nums[l];
                        if (sum == target) {
                            List<Integer> temp = Arrays.asList(nums[i], nums[j], nums[k], nums[l]);
                            Collections.sort(temp);
                            set.add(temp);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(set);
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1, -2, 3, 5], target = 7`

```
i=0,j=1,k=2,l=3: 1+(-2)+3+5=7 == 7
  → sort [-2,1,3,5] → add to set

set = {[-2,1,3,5]}
Return [[-2,1,3,5]] ✅
```

---

### 🟡 Approach 2 — Better (3 Loops + HashSet)
**Time: O(N³ × log M) | Space: O(N + quadruplets)**

#### 💡 Intuition
Fix `i` and `j` with two outer loops. Use a HashSet to find the fourth element efficiently — if `a + b + c + d = target`, then `d = target - a - b - c`. For each `k`, compute the needed fourth element and check if it exists in the HashSet. Add `nums[k]` to the HashSet after checking.

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> sol = new ArrayList<>();
        int n = nums.length;
        Set<List<Integer>> set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Set<Long> hashset = new HashSet<>();
                for (int k = j + 1; k < n; k++) {
                    long sum = (long) nums[i] + nums[j] + nums[k];
                    long rem = target - sum;
                    if (hashset.contains(rem)) {
                        List<Integer> list = Arrays.asList(nums[i], nums[j], nums[k], (int) rem);
                        Collections.sort(list);
                        set.add(list);
                    }
                    hashset.add((long) nums[k]);
                }
            }
        }
        sol.addAll(set);
        return sol;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1, -2, 3, 5], target = 7`

```
i=0 (val=1), j=1 (val=-2):
  hashset = {}
  k=2 (val=3): sum=1+(-2)+3=2, rem=7-2=5 → hashset has 5? No → add 3
  k=3 (val=5): sum=1+(-2)+5=4, rem=7-4=3 → hashset has 3? Yes!
    → quad=[1,-2,5,3] → sort → [-2,1,3,5] → add to set

Return [[-2,1,3,5]] ✅
```

---

### 🟢 Approach 3 — Optimal (Sort + Two Pointers)
**Time: O(N³) | Space: O(1) extra**

#### 💡 Intuition
Extension of the 3Sum two-pointer technique. Fix two pointers `i` and `j`, then use two moving pointers `k` and `l` to find the remaining two elements. Sorting first lets us:
- Skip duplicates for `i` and `j` using simple adjacent checks
- Adjust `k` and `l` based on whether the sum is too large or too small

#### 📝 Steps
1. Sort `nums`
2. Outer loop `i` from 0 to n-1, skip if `nums[i] == nums[i-1]`
3. Inner loop `j` from i+1 to n-1, skip if `nums[j] == nums[j-1]`
4. Two pointers `k = j+1`, `l = n-1`:
   - Sum == target → add quad, move both, skip duplicates
   - Sum < target → `k++`
   - Sum > target → `l--`

#### 💻 Code
```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> sol = new ArrayList<>();
        int n = nums.length;
        Arrays.sort(nums);

        for (int i = 0; i < n; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip i duplicates

            for (int j = i + 1; j < n; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue; // skip j duplicates

                int k = j + 1;
                int l = n - 1;

                while (k < l) {
                    long sum = (long) nums[i] + nums[j] + nums[k] + nums[l];
                    if (sum == target) {
                        sol.add(Arrays.asList(nums[i], nums[j], nums[k], nums[l]));
                        k++;
                        l--;
                        while (k < l && nums[k] == nums[k - 1]) k++; // skip k duplicates
                        while (k < l && nums[l] == nums[l + 1]) l--; // skip l duplicates
                    } else if (sum < target) {
                        k++;
                    } else {
                        l--;
                    }
                }
            }
        }
        return sol;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [1, -2, 3, 5, 7, 9], target = 7`

After sorting: `[-2, 1, 3, 5, 7, 9]`

```
i=0 (val=-2):
  j=1 (val=1):
    k=2, l=5: sum=-2+1+3+9=11 > 7 → l--
    k=2, l=4: sum=-2+1+3+7=9 > 7 → l--
    k=2, l=3: sum=-2+1+3+5=7 == 7 → add [-2,1,3,5] ✅
      k=3, l=2 → k>=l, exit while

  j=2 (val=3):
    k=3, l=5: sum=-2+3+5+9=15 > 7 → l--
    k=3, l=4: sum=-2+3+5+7=13 > 7 → l--
    k=3, l=3 → k>=l, exit

  j=3,4 similar — no valid quad found

i=1 (val=1):
  j=2 (val=3):
    k=3, l=5: sum=1+3+5+9=18 > 7 → l--
    k=3, l=4: sum=1+3+5+7=16 > 7 → l--
    k=3, l=3 → k>=l, exit
  ... (remaining combos exceed 7)

Return [[-2,1,3,5]] ✅
```

> 💡 **Why `long` for sum?** `nums[i]` can be up to 10⁴. Adding four of them can reach 4 × 10⁴ which fits in `int`, but with the constraints extended to larger values, `long` prevents silent overflow. Always safe practice.

> 💡 **Why skip `nums[j] == nums[j-1]` only when `j > i+1`?** If `j == i+1`, it's the first `j` for this `i` — we must consider it. The check `j > i+1` ensures we only skip on the second or later occurrence of `j` for the same `i`.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| 4 Loops + Set | O(N⁴) | O(quadruplets) |
| 3 Loops + HashSet | O(N³ log M) | O(N) |
| Sort + Two Pointers | **O(N³)** ✅ | **O(1)** ✅ |

---

## 3. Longest Consecutive Sequence in an Array

### 🧩 Problem Statement

Given an array `nums` of `n` integers, return the **length of the longest sequence of consecutive integers**. The integers in the sequence can appear in any order in the array.

**Example 1:**
```
Input:  nums = [100, 4, 200, 1, 3, 2]
Output: 4
Explanation: [1, 2, 3, 4] is the longest consecutive sequence
```

**Example 2:**
```
Input:  nums = [0, 3, 7, 2, 5, 8, 4, 6, 0, 1]
Output: 9
Explanation: [0, 1, 2, 3, 4, 5, 6, 7, 8]
```

**Constraints:**
- `1 <= nums.length <= 10⁵`
- `-10⁹ <= nums[i] <= 10⁹`

---

### 🔴 Approach 1 — Brute Force (Linear Search)
**Time: O(N³) | Space: O(1)**

#### 💡 Intuition
For each element `x`, keep checking if `x+1`, `x+2`, ... exists in the array using linear search. Count the length of the sequence starting at `x`. Track the maximum.

#### 💻 Code
```java
class Solution {
    private boolean ls(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) return true;
        }
        return false;
    }

    public int longestConsecutive(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        int longest = 1;

        for (int i = 0; i < n; i++) {
            int x = nums[i];
            int count = 1;
            while (ls(nums, x + 1)) {
                x += 1;
                count++;
            }
            longest = Math.max(longest, count);
        }
        return longest;
    }
}
```

#### 🧪 Dry Run

Input: `[100, 4, 200, 1, 3, 2]`

```
i=0 (x=100): ls(101)? No → count=1
i=1 (x=4):   ls(5)?   No → count=1
i=2 (x=200): ls(201)? No → count=1
i=3 (x=1):   ls(2)?   Yes → x=2, count=2
              ls(3)?   Yes → x=3, count=3
              ls(4)?   Yes → x=4, count=4
              ls(5)?   No  → longest=4
i=4 (x=3):   ls(4)?   Yes → x=4, count=2
              ls(5)?   No  → count=2
i=5 (x=2):   ls(3)?   Yes → x=3, count=2
              ls(4)?   Yes → x=4, count=3
              ls(5)?   No  → count=3

Return 4 ✅
```

#### ⚠️ Limitation
For each element, the while loop runs O(N) times, and each `ls()` call is O(N) — total O(N³). TLE on large inputs.

---

### 🟡 Approach 2 — Better (Sort)
**Time: O(N log N) | Space: O(1)**

#### 💡 Intuition
Sort the array. Consecutive numbers will be adjacent. Scan through and extend the current sequence if `nums[i] - 1 == lastSeen`. Reset when the sequence breaks.

**Edge case:** Skip duplicate values (`nums[i] == lastSeen`).

#### 💻 Code
```java
class Solution {
    public int longestConsecutive(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int longest = 1;
        int lastSeen = Integer.MIN_VALUE;
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] - 1 == lastSeen) {
                count += 1;
                lastSeen = nums[i];
            }
            if (nums[i] != lastSeen) {
                count = 1;
                lastSeen = nums[i];
            }
            longest = Math.max(longest, count);
        }
        return longest;
    }
}
```

#### 🧪 Dry Run

Input: `[100, 4, 200, 1, 3, 2]`

After sorting: `[1, 2, 3, 4, 100, 200]`

```
lastSeen=MIN, count=0

i=0 (val=1):  1-1=0 ≠ MIN → second if: 1≠MIN → count=1, lastSeen=1 | longest=1
i=1 (val=2):  2-1=1 == 1  → count=2, lastSeen=2 | longest=2
i=2 (val=3):  3-1=2 == 2  → count=3, lastSeen=3 | longest=3
i=3 (val=4):  4-1=3 == 3  → count=4, lastSeen=4 | longest=4
i=4 (val=100):100-1=99≠4 → count=1, lastSeen=100 | longest=4
i=5 (val=200):200-1=199≠100 → count=1, lastSeen=200 | longest=4

Return 4 ✅
```

---

### 🟢 Approach 3 — Optimal (HashSet)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
The key observation: **only start counting from the beginning of a sequence.** An element `x` is the start of a sequence only if `x-1` does NOT exist in the set.

If we start counting from every element, we do redundant work. By skipping elements where `x-1` exists, we only start sequences from their true beginning — making the inner `while` loop run O(1) amortized across all elements.

#### 📝 Steps
1. Add all elements to a `HashSet`
2. For each element `num` in the set:
   - If `num-1` is NOT in the set → `num` is a sequence start
   - Count consecutive elements starting from `num`
   - Update `longest`

#### 💻 Code
```java
class Solution {
    public int longestConsecutive(int[] nums) {
        int n = nums.length;
        int longest = 1;
        Set<Integer> set = new HashSet<>();
        if (n == 0) return 0;

        for (int i = 0; i < n; i++) set.add(nums[i]);

        for (int num : set) {
            if (!set.contains(num - 1)) { // num is a sequence start
                int count = 1;
                int x = num;
                while (set.contains(x + 1)) {
                    x = x + 1;
                    count += 1;
                }
                longest = Math.max(longest, count);
            }
        }
        return longest;
    }
}
```

#### 🧪 Dry Run

Input: `[100, 4, 200, 1, 3, 2]`

```
set = {100, 4, 200, 1, 3, 2}

Iterating over set:

num=100: set has 99? No → START
  x=100, count=1
  set has 101? No → done
  longest=1

num=4: set has 3? Yes → SKIP (not a start)

num=200: set has 199? No → START
  x=200, count=1
  set has 201? No → done
  longest=1

num=1: set has 0? No → START
  x=1, count=1
  set has 2? Yes → x=2, count=2
  set has 3? Yes → x=3, count=3
  set has 4? Yes → x=4, count=4
  set has 5? No → done
  longest=4

num=3: set has 2? Yes → SKIP
num=2: set has 1? Yes → SKIP

Return 4 ✅
```

> 💡 **Why is the total time O(N) and not O(N²)?**
> Each element is visited at most twice — once when we check if it's a sequence start, and once inside the `while` loop when it's counted as part of a sequence. The `while` loop only runs for sequence starts, so across all elements the total iterations of all while loops = total elements in all sequences = N.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (Linear Search) | O(N³) | O(1) |
| Sort | O(N log N) | O(1) |
| HashSet | **O(N)** ✅ | O(N) |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Two Sum | HashMap stores complement lookups — check before inserting to avoid using same index twice |
| 4 Sum | Sort + fix two pointers + two-pointer scan = O(N³). Skip duplicates at each of the 4 levels |
| Longest Consecutive | HashSet + only start from sequence beginnings (`num-1` not in set) = O(N) amortized |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*