# 🚀 Day 8/45 – #SDESheetChallenge

> Solving 3 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Largest Subarray with Sum K](#1-largest-subarray-with-sum-k) | 🟡 Medium | Arrays / Prefix Sum |
| 2 | [Count Subarrays with Given XOR K](#2-count-subarrays-with-given-xor-k) | 🔴 Hard | Arrays / Prefix XOR |
| 3 | [Longest Substring Without Repeating Characters](#3-longest-substring-without-repeating-characters) | 🟡 Medium | Strings / Sliding Window |

---

## 1. Largest Subarray with Sum K

### 🧩 Problem Statement

Given an array `nums` of size `n` and an integer `k`, find the **length of the longest subarray** that sums to `k`. Return `0` if no such subarray exists.

**Example 1:**
```
Input:  nums = [10, 5, 2, 7, 1, 9], k = 15
Output: 4
Explanation: [5, 2, 7, 1] sums to 15 and has length 4
```

**Example 2:**
```
Input:  nums = [-3, 2, 1], k = 6
Output: 0
Explanation: No subarray sums to 6
```

**Example 3:**
```
Input:  nums = [-1, 1, 1], k = 1
Output: 3
Explanation: [-1, 1, 1] sums to 1, length 3
```

**Constraints:**
- `1 <= n <= 10⁵`
- `-10⁵ <= nums[i] <= 10⁵`
- `-10⁹ <= k <= 10⁹`

---

### 🔴 Approach 1 — Brute Force (3 Nested Loops)
**Time: O(N³) | Space: O(1)**

#### 💡 Intuition
Try every possible subarray `[i..j]`. For each pair, compute the sum from scratch using a third inner loop. If sum equals `k`, update max length.

#### 💻 Code
```java
class Solution {
    public int longestSubarray(int[] nums, int k) {
        int n = nums.length;
        int maxLength = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int currentSum = 0;
                for (int x = i; x <= j; x++) {
                    currentSum += nums[x];
                }
                if (currentSum == k) {
                    maxLength = Math.max(maxLength, j - i + 1);
                }
            }
        }
        return maxLength;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [10, 5, 2, 7, 1, 9], k = 15`

```
i=0: j=0: sum=10, j=1: sum=15 == 15 → len=2, maxLen=2
     j=2: sum=17, j=3: sum=24, ...

i=1: j=1: sum=5, j=2: sum=7, j=3: sum=14, j=4: sum=15 == 15 → len=4, maxLen=4
     j=5: sum=24

i=2,3,4,5: no subarray of length > 4 sums to 15

Return 4 ✅
```

---

### 🟡 Approach 2 — Better (2 Nested Loops with Running Sum)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
Instead of recomputing the sum from scratch for every `[i..j]`, maintain a **running sum** as `j` extends. This eliminates the inner loop.

#### 💻 Code
```java
class Solution {
    public int longestSubarray(int[] nums, int k) {
        int n = nums.length;
        int longest = 0;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += nums[j];
                if (sum == k) {
                    longest = Math.max(longest, j - i + 1);
                }
            }
        }
        return longest;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [-1, 1, 1], k = 1`

```
i=0:
  j=0: sum=-1
  j=1: sum=0
  j=2: sum=1 == 1 → len=3, longest=3

i=1:
  j=1: sum=1 == 1 → len=1
  j=2: sum=2

i=2:
  j=2: sum=1 == 1 → len=1

Return 3 ✅
```

---

### 🟢 Approach 3 — Optimal (Prefix Sum + HashMap)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
The key mathematical observation:

If the prefix sum up to index `i` is `sum`, and we want a subarray ending at `i` with sum `k`, then the **prefix sum of the part before that subarray** must be `sum - k`.

So:
- If `sum == k` → the entire subarray from index 0 to `i` has sum `k` → length = `i + 1`
- If `map.containsKey(sum - k)` → there's a previous index where prefix sum was `sum - k`. The subarray between that index and `i` has sum `k`. Length = `i - map.get(sum - k)`

**Why store only the FIRST occurrence?**
We want the LONGEST subarray. The earlier the prefix sum appeared, the longer the subarray ending at `i`. If we update the map with later occurrences, we'd get shorter subarrays.

#### 📝 Steps
1. Initialize `map = {}`, `sum = 0`, `maxLen = 0`
2. For each index `i`:
   - `sum += nums[i]`
   - If `sum == k` → `maxLen = max(maxLen, i+1)`
   - If `map.containsKey(sum - k)` → `maxLen = max(maxLen, i - map.get(sum-k))`
   - If `sum` is NOT in map → `map.put(sum, i)` (first occurrence only)
3. Return `maxLen`

#### 💻 Code
```java
class Solution {
    public int longestSubarray(int[] nums, int k) {
        int n = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            sum += nums[i];

            // Entire prefix sums to k
            if (sum == k) {
                maxLen = Math.max(maxLen, i + 1);
            }

            // Check if (sum - k) exists as a previous prefix sum
            int rem = sum - k;
            if (map.containsKey(rem)) {
                int len = i - map.get(rem);
                maxLen = Math.max(maxLen, len);
            }

            // Only store first occurrence
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }
        return maxLen;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [10, 5, 2, 7, 1, 9], k = 15`

```
map = {}, sum = 0, maxLen = 0

i=0: sum=10 | 10≠15 | rem=10-15=-5, not in map | map={10:0}
i=1: sum=15 | 15==15 → maxLen=2 | rem=0, not in map | map={10:0, 15:1}
i=2: sum=17 | 17≠15 | rem=2, not in map | map={10:0, 15:1, 17:2}
i=3: sum=24 | 24≠15 | rem=9, not in map | map={..., 24:3}
i=4: sum=25 | 25≠15 | rem=10, map has 10 at index 0! → len=4-0=4, maxLen=4 | map={..., 25:4}
i=5: sum=34 | 34≠15 | rem=19, not in map | map={..., 34:5}

Return 4 ✅
```

**With negatives:** `nums = [-1, 1, 1], k = 1`

```
map = {}, sum = 0, maxLen = 0

i=0: sum=-1 | -1≠1 | rem=-2, not in map | map={-1:0}
i=1: sum=0  | 0≠1  | rem=-1, map has -1 at 0! → len=1-0=1, maxLen=1 | map={-1:0, 0:1}
i=2: sum=1  | 1==1 → maxLen=max(1,2+1)=3 | map={-1:0, 0:1, 1:2}

Return 3 ✅
```

> 💡 **Why doesn't the Two Pointer approach work here?**
> Two pointers work for non-negative arrays because shrinking the window always reduces the sum. With negative numbers, removing an element might actually increase the sum — so we can't decide whether to shrink from left. Prefix sum + HashMap handles negative numbers correctly.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| 3 Loops | O(N³) | O(1) |
| 2 Loops (running sum) | O(N²) | O(1) |
| Prefix Sum + HashMap | **O(N)** ✅ | O(N) |

---

## 2. Count Subarrays with Given XOR K

### 🧩 Problem Statement

Given an array `nums` and an integer `k`, return the **total number of subarrays whose XOR equals `k`**.

**Example 1:**
```
Input:  nums = [4, 2, 2, 6, 4], k = 6
Output: 4
Subarrays: [4,2], [4,2,2,6,4], [2,2,6], [6]
```

**Example 2:**
```
Input:  nums = [5, 6, 7, 8, 9], k = 5
Output: 2
Subarrays: [5], [5,6,7,8,9]
```

**Constraints:**
- `1 <= nums.length <= 10⁵`
- `1 <= nums[i] <= 10⁹`
- `1 <= k <= 10⁹`

---

### 🔴 Approach 1 — Brute Force (2 Nested Loops)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
For every starting index `i`, maintain a running XOR as `j` extends. If the running XOR equals `k` at any `j`, increment count. This avoids the third loop by using the property: `XOR(i..j) = XOR(i..j-1) ^ nums[j]`.

#### 💻 Code
```java
class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
        int n = nums.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            int xor = 0;
            for (int j = i; j < n; j++) {
                xor ^= nums[j];
                if (xor == k) count++;
            }
        }
        return count;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [4, 2, 2, 6, 4], k = 6`

```
i=0:
  j=0: xor=4
  j=1: xor=4^2=6 == 6 → count=1  (subarray [4,2])
  j=2: xor=6^2=4
  j=3: xor=4^6=2
  j=4: xor=2^4=6 == 6 → count=2  (subarray [4,2,2,6,4])

i=1:
  j=1: xor=2
  j=2: xor=2^2=0
  j=3: xor=0^6=6 == 6 → count=3  (subarray [2,2,6])
  j=4: xor=6^4=2

i=2:
  j=2: xor=2
  j=3: xor=2^6=4
  j=4: xor=4^4=0

i=3:
  j=3: xor=6 == 6 → count=4  (subarray [6])
  j=4: xor=6^4=2

i=4:
  j=4: xor=4

Return 4 ✅
```

---

### 🟢 Approach 2 — Optimal (Prefix XOR + HashMap)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
This is the XOR analogue of the prefix sum technique.

**Key XOR property:** If `xr` is the prefix XOR up to index `i`, and we want a subarray `[j+1..i]` with XOR = `k`, then:

```
XOR(j+1..i) = xr ^ XOR(0..j) = k
→ XOR(0..j) = xr ^ k
```

So, at every index `i`, we need to know **how many previous prefix XORs equal `xr ^ k`**. That's exactly what the map stores.

**Why initialize `map.put(0, 1)`?**
If `xr == k` at some index `i`, the subarray from `0` to `i` has XOR `k`. The formula gives `xr ^ k = 0`. If we don't pre-seed `0 → 1` in the map, this valid subarray would be missed.

#### 📝 Steps
1. Initialize `map = {0: 1}`, `xr = 0`, `count = 0`
2. For each element:
   - `xr = xr ^ nums[i]`
   - `x = xr ^ k` (the prefix XOR we're looking for)
   - `count += map.getOrDefault(x, 0)`
   - `map.put(xr, map.getOrDefault(xr, 0) + 1)`
3. Return `count`

#### 💻 Code
```java
class Solution {
    public int subarraysWithXorK(int[] nums, int k) {
        int n = nums.length;
        int xr = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // seed: empty prefix XOR = 0, seen once
        int count = 0;

        for (int i = 0; i < n; i++) {
            xr = xr ^ nums[i];      // prefix XOR till index i
            int x = xr ^ k;         // prefix XOR we need before this subarray
            count += map.getOrDefault(x, 0);
            map.put(xr, map.getOrDefault(xr, 0) + 1);
        }
        return count;
    }
}
```

#### 🧪 Dry Run

Input: `nums = [4, 2, 2, 6, 4], k = 6`

```
map = {0:1}, xr = 0, count = 0

i=0: xr=0^4=4  | x=4^6=2  | map has 2? No → count=0  | map={0:1, 4:1}
i=1: xr=4^2=6  | x=6^6=0  | map has 0? Yes(1) → count=1 | map={0:1, 4:1, 6:1}
i=2: xr=6^2=4  | x=4^6=2  | map has 2? No → count=1  | map={0:1, 4:2, 6:1}
i=3: xr=4^6=2  | x=2^6=4  | map has 4? Yes(2) → count=3 | map={0:1, 4:2, 6:1, 2:1}
i=4: xr=2^4=6  | x=6^6=0  | map has 0? Yes(1) → count=4 | map={0:1, 4:2, 6:2, 2:1}

Return 4 ✅
```

**Tracing which subarrays are counted:**
```
i=1 (count += 1): xr=6, x=0 found → subarray [0..1] = [4,2]
i=3 (count += 2): xr=2, x=4 found twice (at i=0 and i=2)
  → subarray [1..3] = [2,2,6]
  → subarray [0..3] = [4,2,2,6] → wait, XOR = 4^2^2^6 = 2, not 6
  Actually: xr at i=0 was 4, xr at i=2 was also 4
  Subarray after i=0: [1..3] = [2,2,6], XOR = 6 ✅
  Subarray after i=2: [3..3] = [6], XOR = 6 ✅
i=4 (count += 1): xr=6, x=0 found → subarray [0..4] = [4,2,2,6,4], XOR=6 ✅

Total = 4 ✅
```

> 💡 **Why use `map.getOrDefault(xr, 0) + 1` (not just +1)?**
> The same prefix XOR value can occur at multiple indices. We need to count how many times it has appeared. Each occurrence represents a different starting point for a subarray ending at the current `i`.

> 💡 **Why does XOR ^ k work as a "remainder"?**
> XOR is its own inverse: `a ^ a = 0` and `a ^ 0 = a`. So if `xr ^ k = x`, then `x ^ k = xr`. This is the XOR equivalent of subtraction in prefix sum problems.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (2 loops) | O(N²) | O(1) |
| Prefix XOR + HashMap | **O(N)** ✅ | O(N) |

---

## 3. Longest Substring Without Repeating Characters

### 🧩 Problem Statement

Given a string `S`, find the **length of the longest substring without any repeating characters**.

**Example 1:**
```
Input:  S = "abcddabac"
Output: 4
Explanation: "abcd" has length 4
```

**Example 2:**
```
Input:  S = "aaabbbccc"
Output: 2
Explanation: "ab" or "bc", both have length 2
```

**Example 3:**
```
Input:  S = "aaaa"
Output: 1
```

**Constraints:**
- `1 <= S.length <= 5 × 10⁴`
- `S` contains only English lowercase letters

---

### 🔴 Approach 1 — Brute Force (2 Nested Loops + Hash Array)
**Time: O(N²) | Space: O(256)**

#### 💡 Intuition
For every starting index `i`, extend as far right as possible without hitting a repeated character. Use a `hash[256]` array to track which characters are in the current window. When a repeat is found, break and start from the next `i`.

#### 💻 Code
```java
class Solution {
    public int longestNonRepeatingSubstring(String s) {
        int n = s.length();
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int[] hash = new int[256];
            Arrays.fill(hash, 0);

            for (int j = i; j < n; j++) {
                if (hash[s.charAt(j)] == 1) break; // repeat found
                hash[s.charAt(j)] = 1;
                int len = j - i + 1;
                maxLen = Math.max(len, maxLen);
            }
        }
        return maxLen;
    }
}
```

#### 🧪 Dry Run

Input: `S = "abcddabac"`

```
i=0 (start='a'):
  j=0: 'a' not seen → hash['a']=1, len=1
  j=1: 'b' not seen → hash['b']=1, len=2
  j=2: 'c' not seen → hash['c']=1, len=3
  j=3: 'd' not seen → hash['d']=1, len=4, maxLen=4
  j=4: 'd' seen! → break

i=1 (start='b'):
  j=1: 'b', j=2: 'c', j=3: 'd', j=4: 'd' seen → break, len=3

i=2 (start='c'):
  j=2: 'c', j=3: 'd', j=4: 'd' seen → break, len=2

... (no window longer than 4 found)

Return 4 ✅
```

---

### 🟢 Approach 2 — Optimal (Sliding Window + Last Seen Index)
**Time: O(N) | Space: O(256)**

#### 💡 Intuition
Use two pointers `l` (left) and `r` (right) defining a sliding window `[l..r]` with no repeating characters.

Instead of a 0/1 hash, store the **last seen index** of each character. When `r` finds a character that was previously seen **inside the current window** (`hash[char] >= l`), we don't need to shrink one step at a time — we can **jump `l` directly** to `hash[char] + 1`, skipping past the repeat in one move.

**Key distinction from brute force:**
The brute force restarts the window from scratch at each `i`. The sliding window **never moves `l` backward** — it only moves forward, giving O(N) total.

#### 📝 Steps
1. Initialize `hash[256]` with `-1`, `l = 0`, `r = 0`, `maxLen = 0`
2. While `r < n`:
   - If `hash[s.charAt(r)] >= l` → character is in current window → `l = max(hash[char]+1, l)`
   - `len = r - l + 1`, update `maxLen`
   - `hash[s.charAt(r)] = r` (store last seen index)
   - `r++`
3. Return `maxLen`

#### 💻 Code
```java
class Solution {
    public int longestNonRepeatingSubstring(String s) {
        int n = s.length();
        int[] hash = new int[256];
        Arrays.fill(hash, -1);
        int l = 0, r = 0, maxLen = 0;

        while (r < n) {
            // If character was seen inside current window [l..r]
            if (hash[s.charAt(r)] != -1) {
                l = Math.max(hash[s.charAt(r)] + 1, l);
            }
            int len = r - l + 1;
            maxLen = Math.max(len, maxLen);
            hash[s.charAt(r)] = r; // update last seen index
            r++;
        }
        return maxLen;
    }
}
```

#### 🧪 Dry Run

Input: `S = "abcddabac"`

```
hash = [-1 for all], l=0, r=0, maxLen=0

r=0 ('a'): hash['a']=-1, no repeat | len=1, maxLen=1 | hash['a']=0 | r=1
r=1 ('b'): hash['b']=-1, no repeat | len=2, maxLen=2 | hash['b']=1 | r=2
r=2 ('c'): hash['c']=-1, no repeat | len=3, maxLen=3 | hash['c']=2 | r=3
r=3 ('d'): hash['d']=-1, no repeat | len=4, maxLen=4 | hash['d']=3 | r=4
r=4 ('d'): hash['d']=3 >= l=0 → l=max(3+1,0)=4
           len=4-4+1=1 | hash['d']=4 | r=5
r=5 ('a'): hash['a']=0, but 0 < l=4 → not in window, no shift
           len=5-4+1=2 | hash['a']=5 | r=6
r=6 ('b'): hash['b']=1, but 1 < l=4 → not in window, no shift
           len=6-4+1=3 | hash['b']=6 | r=7
r=7 ('a'): hash['a']=5 >= l=4 → l=max(5+1,4)=6
           len=7-6+1=2 | hash['a']=7 | r=8
r=8 ('c'): hash['c']=2, but 2 < l=6 → not in window, no shift
           len=8-6+1=3 | hash['c']=8 | r=9

r=9 >= n → stop

Return maxLen = 4 ✅
```

> 💡 **Why `l = Math.max(hash[char]+1, l)` and not just `l = hash[char]+1`?**
> Consider `"abba"`. When we hit the second `b` (index 2), `hash['b'] = 1`, so `l` would move to 2. Then when we hit the second `a` (index 3), `hash['a'] = 0`. If we just set `l = 0+1 = 1`, we'd move `l` BACKWARD to 1, which is wrong — we'd include `b` again. The `max` ensures `l` never moves backward.

> 💡 **Why check `hash[char] >= l` (not just `!= -1`)?**
> A character might have been seen before but outside the current window. For example, in `"abcabc"`, when `r` hits the second `a` at index 3, `hash['a'] = 0`. But if `l = 2`, then `0 < l` — `a` is no longer in the current window `[2..3]`. No need to shrink. The `>= l` check handles this correctly.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force (2 Loops) | O(N²) | O(256) |
| Sliding Window (Last Index) | **O(N)** ✅ | **O(256)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Largest Subarray Sum K | Prefix sum + HashMap: if `sum - k` was seen before at index `j`, subarray `[j+1..i]` sums to `k`. Store only first occurrence for longest length |
| Count XOR Subarrays | Prefix XOR + HashMap: if `xr ^ k` was seen before, those subarrays have XOR `k`. Pre-seed map with `{0:1}` to handle subarrays starting from index 0 |
| Longest Non-Repeating Substring | Sliding window + last-seen index: jump `l` directly past the repeated character. Never move `l` backward with `Math.max` |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*