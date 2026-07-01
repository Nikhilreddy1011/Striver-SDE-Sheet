# 🚀 Day 30/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Longest Common Prefix](#1-longest-common-prefix) | 🟢 Easy | Strings / Sorting |
| 2 | [String to Integer (ATOI)](#2-string-to-integer-atoi) | 🟡 Medium | Strings / Simulation |
| 3 | [Rabin Karp Algorithm](#3-rabin-karp-algorithm) | 🔴 Hard | Strings / Hashing |

---

## 1. Longest Common Prefix

### 🧩 Problem Statement

Find the longest common prefix string among an array of strings. If none exists, return `""`.

**Example 1:**
```
Input:  ["flowers","flow","fly","flight"]
Output: "fl"
```

**Example 2:**
```
Input:  ["dog","cat","animal","monkey"]
Output: ""
```

**Constraints:** `1 <= str.length <= 200`, `1 <= str[i].length <= 200`

---

### 🟢 Approach — Sort + Compare First and Last
**Time: O(N × M × log N) | Space: O(M)**

#### 💡 Idea
When you sort an array of strings lexicographically, the first and last strings in the sorted order differ the most from each other. Therefore, the common prefix of the **first and last** strings after sorting is guaranteed to be the common prefix of **all** strings in the array.

**Why does this work?**
Any character that matches between the lexicographically smallest and largest strings must also match in every string in between — because all middle strings are bounded within those two extremes character by character.

#### 💻 Code
```java
class Solution {    
    public String longestCommonPrefix(String[] str) {
        int n = str.length;
        Arrays.sort(str);
        String first = str[0], last = str[n-1];
        StringBuilder ans = new StringBuilder();
        for(int i = 0; i < Math.min(first.length(), last.length()); i++){
            if(first.charAt(i) != last.charAt(i)) return ans.toString();
            ans.append(first.charAt(i));
        }
        return ans.toString();
    }
}
```

#### 🧪 Dry Run

Input: `["flowers","flow","fly","flight"]`

```
After sort: ["flight","flow","flowers","fly"]
first = "flight", last = "fly"

i=0: 'f'=='f' → append 'f'. ans="f"
i=1: 'l'=='l' → append 'l'. ans="fl"
i=2: 'i' != 'y' → return "fl" ✅
```

**No common prefix:** `["dog","cat","animal"]`
```
After sort: ["animal","cat","dog"]
first="animal", last="dog"
i=0: 'a' != 'd' → return "" ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N × M × log N) | O(M) |

---

## 2. String to Integer (ATOI)

### 🧩 Problem Statement

Implement `myAtoi(s)` which converts a string to a 32-bit signed integer, following these rules:
1. Skip leading whitespaces
2. Read optional sign (`+` or `-`)
3. Read digits, stop on non-digit
4. Clamp to `[-2147483648, 2147483647]`

**Example 1:**
```
Input:  " -12345"
Output: -12345
```

**Example 2:**
```
Input:  "4193 with words"
Output: 4193
```

**Constraints:** `0 <= s.length <= 200`

---

### 🟢 Approach — Simulation
**Time: O(N) | Space: O(1)**

#### 💡 Idea
Simulate the conversion step by step. Use a `long` to accumulate the result and check for overflow inside the digit-parsing loop (not after). Checking inside the loop prevents the `long` from itself overflowing on very large inputs.

**Steps:**
1. Skip spaces with a while loop
2. Read optional sign
3. Parse digits: `res = res * 10 + digit`. After each digit, check if `res * sign` overflows INT range
4. Return `(int)(res * sign)`

#### 💻 Code
```java
class Solution {
    public int myAtoi(String input) {
        int i = 0, n = input.length();
        while(i < n && input.charAt(i) == ' ') i++;
        int sign = 1;
        if(i < n && input.charAt(i) == '-'){ sign = -1; i++; }
        else if(i < n && input.charAt(i) == '+') i++;
        long res = 0;
        while(i < n && Character.isDigit(input.charAt(i))){
            res = res * 10 + (input.charAt(i) - '0');
            i++;
            if(res * sign >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if(res * sign <= Integer.MIN_VALUE) return Integer.MIN_VALUE;
        }
        return (int)(res * sign);
    }
}
```

#### 🧪 Dry Run

Input: `" -12345"`

```
Skip spaces: i=1 (first non-space)
sign: '-' found → sign=-1, i=2

Digits:
  i=2: '1' → res=1. 1*(-1)=-1 → no overflow
  i=3: '2' → res=12. -12 → no overflow
  i=4: '3' → res=123. -123 → no overflow
  i=5: '4' → res=1234. -1234 → no overflow
  i=6: '5' → res=12345. -12345 → no overflow
  i=7: not digit → exit loop

return (int)(12345 * -1) = -12345 ✅
```

**Overflow case:** `"99999999999"`

```
res grows: 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999...
At some point res * sign = 999999999 > INT_MAX → return Integer.MAX_VALUE ✅
```

> 💡 **Why use `long` for `res` and check overflow inside the loop?**
> If we used `int`, the accumulation itself would overflow silently before we could check. `long` has enough range to hold intermediate values, and we check against INT bounds after each digit to exit early.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | **O(1)** ✅ |

---

## 3. Rabin Karp Algorithm

### 🧩 Problem Statement

Given `text` and `pattern`, find the **starting indices of all occurrences** of `pattern` in `text` using Rabin-Karp.

**Example 1:**
```
Input:  text="ababcabcababc", pattern="abc"
Output: [2, 5, 10]
```

**Example 2:**
```
Input:  text="hello", pattern="ll"
Output: [2]
```

**Constraints:** `1 <= text.length, pattern.length <= 5×10⁴`

---

### 🔴 Approach 1 — Brute Force
**Time: O(M × N) | Space: O(K)**

#### 💡 Idea
For every starting position `i` in `text`, compare `pattern` character by character with `text[i..i+n-1]`. Use a `flag` — set it false on first mismatch and break.

#### 💻 Code
```java
class Solution {
    public List<Integer> search(String pat, String txt) {
        int m = pat.length(), n = txt.length();
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i <= n - m; i++){
            boolean flag = true;
            for(int j = 0; j < m; j++){
                if(pat.charAt(j) != txt.charAt(i+j)){ flag = false; break; }
            }
            if(flag) list.add(i);
        }
        return list;
    }
}
```

---

### 🟢 Approach 2 — Rabin-Karp (Rolling Hash)
**Time: O(M) average | Space: O(K)**

#### 💡 Idea — The Black Box Analogy
Instead of comparing strings character by character, convert each string to a **hash value** through a "black box" function. If two strings have equal hashes, they're likely equal — verify with a direct comparison to handle hash collisions.

**Hash Function:**
```
hash(S) = (S[0]×p⁰ + S[1]×p¹ + ... + S[n-1]×pⁿ⁻¹) % mod
```
where `p=7` and `mod=101` (both prime, to minimize collisions).

**Rolling Hash — The Key Optimization:**
Instead of recomputing the hash from scratch at each window position (O(N) per position = O(M×N) total), update it in O(1):
- **Remove** leftmost character's contribution: `hashText -= char[i] × pLeft`
- **Add** new rightmost character's contribution: `hashText += char[i+n] × pRight`

This gives O(M) total for sliding the window across the entire text.

**Why `+ mod) % mod` when removing?**
Subtraction can give a negative result in modular arithmetic. Adding `mod` before taking `% mod` ensures the result stays non-negative.

**Hash collision handling:**
When `hashPat == hashText`, perform `txt.substring(i, i+n).equals(pat)` to confirm. This prevents false positives from hash collisions.

#### 💻 Code
```java
class Solution {
    public List<Integer> search(String pat, String txt) {
        int n = pat.length(), m = txt.length();
        int p = 7, mod = 101;
        int hashPat = 0, hashText = 0;
        int pRight = 1, pLeft = 1;

        // Compute initial hash for pattern and first window of text
        for (int i = 0; i < n; i++) {
            hashPat = (hashPat + ((pat.charAt(i) - 'a' + 1) * pRight) % mod) % mod;
            hashText = (hashText + ((txt.charAt(i) - 'a' + 1) * pRight) % mod) % mod;
            pRight = (pRight * p) % mod;
        }

        List<Integer> ans = new ArrayList<>();

        for (int i = 0; i <= m - n; i++) {
            if (hashPat == hashText) {
                if (txt.substring(i, i + n).equals(pat)) ans.add(i);
            }
            if (i < m - n) {
                // Remove leftmost char, add new rightmost char
                hashText = (hashText - ((txt.charAt(i) - 'a' + 1) * pLeft) % mod + mod) % mod;
                hashText = (hashText + ((txt.charAt(i + n) - 'a' + 1) * pRight) % mod) % mod;
                hashPat = (hashPat * p) % mod;
                pLeft = (pLeft * p) % mod;
                pRight = (pRight * p) % mod;
            }
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `txt="dabcabc"`, `pat="abc"`, `p=7`, `mod=101`

```
Pattern hash: a=1, b=2, c=3
hashPat = (1×7⁰ + 2×7¹ + 3×7²) % 101
        = (1 + 14 + 147) % 101
        = 162 % 101 = 61

Initial window "dab":
hashText = (4×7⁰ + 1×7¹ + 2×7²) % 101
         = (4 + 7 + 98) % 101
         = 109 % 101 = 8

i=0: hashPat=61 != hashText=8 → no match
     Roll: remove 'd'(4), add 'c'(3) at pRight position
     hashText updates to hash of "abc"...
     → eventually hashText=61

i=1: hashPat=61 == hashText=61
     verify: txt.substring(1,4)="abc" == "abc" → ans.add(1) ✅
```

**Full example:** `txt="ababcabcababc"`, `pat="abc"`
- Pattern found at indices [2, 5, 10] ✅

> 💡 **Why multiply `hashPat` by `p` in the rolling step?**
> The hash function uses weighted powers. As the window slides right by one position, every character's power increases by 1 (multiplied by `p`). The pattern's hash must track this same shifting to stay comparable with the text window hash.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(M × N) | O(K) |
| Rabin-Karp | **O(M) average** ✅ | **O(K)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Longest Common Prefix | Sort → compare only first and last. If first and last share a prefix, all strings in between do too |
| ATOI | Simulation: skip spaces, read sign, accumulate digits as `long`, clamp inside the loop to catch overflow early |
| Rabin-Karp | Rolling hash: update in O(1) by removing leftmost and adding rightmost contribution. Verify on hash match to handle collisions |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
