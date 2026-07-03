# 🚀 Day 32/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Valid Anagram](#1-valid-anagram) | 🟢 Easy | Strings / Hashing |
| 2 | [Count and Say](#2-count-and-say) | 🔴 Hard | Strings / Simulation |
| 3 | [Compare Version Numbers](#3-compare-version-numbers) | 🟡 Medium | Strings / Parsing |

---

## 1. Valid Anagram

### 🧩 Problem Statement

Given two strings `s` and `t`, return `true` if `t` is an **anagram** of `s` (same characters, same frequencies), else `false`.

**Example 1:**
```
Input:  s="anagram", t="nagaram"
Output: true
```

**Example 2:**
```
Input:  s="rat", t="car"
Output: false
```

**Constraints:** `1 <= s.length, t.length <= 5×10⁴`, lowercase letters only

---

### 🔴 Approach 1 — Brute Force (Sort and Compare)
**Time: O(N log N) | Space: O(N)**

#### 💡 Idea
Sort both strings. If they are anagrams, the sorted versions will be identical.

#### 💻 Code
```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        char[] a = s.toCharArray();
        char[] b = t.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }
}
```

#### 🧪 Dry Run

```
s="anagram" → sorted: "aaagmnr"
t="nagaram" → sorted: "aaagmnr"
Arrays.equals → true ✅

s="rat" → sorted: "art"
t="car" → sorted: "acr"
"art" != "acr" → false ✅
```

---

### 🟢 Approach 2 — Optimal (Frequency Array)
**Time: O(N) | Space: O(1) (fixed size 26)**

#### 💡 Idea
Use a single `freq[26]` array. Increment for each character in `s`, decrement for each character in `t` (in the same loop). If they are anagrams, every count cancels out to 0. Any non-zero entry means they differ.

**Why O(1) space?**
The array is always size 26 (fixed), independent of input size.

#### 💻 Code
```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] freq = new int[26];
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
            freq[t.charAt(i) - 'a']--;
        }
        for (int count : freq) {
            if (count != 0) return false;
        }
        return true;
    }
}
```

#### 🧪 Dry Run

Input: `s="rat"`, `t="car"`

```
freq = [0]*26

i=0: s[0]='r'(17)++ → freq[17]=1. t[0]='c'(2)-- → freq[2]=-1
i=1: s[1]='a'(0)++ → freq[0]=1. t[1]='a'(0)-- → freq[0]=0
i=2: s[2]='t'(19)++ → freq[19]=1. t[2]='r'(17)-- → freq[17]=0

Final freq: freq[2]=-1, freq[19]=1 (non-zero entries exist)
Return false ✅

Input: s="anagram", t="nagaram"
All increments and decrements cancel → all freq[i]=0 → return true ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Sort and Compare | O(N log N) | O(N) |
| Frequency Array | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Count and Say

### 🧩 Problem Statement

The **Count and Say** sequence is defined as:
- `n=1`: "1"
- `n=2`: "11" (one 1)
- `n=3`: "21" (two 1s)
- `n=4`: "1211" (one 2, then one 1)
- `n=5`: "111221" (one 1, one 2, two 1s)

Given `n`, return the nth term of the count-and-say sequence.

**Example 1:**
```
Input:  n=4
Output: "1211"
```

**Example 2:**
```
Input:  n=1
Output: "1"
```

---

### 🟢 Approach — Iterative Simulation
**Time: O(N × M) | Space: O(M)** where M is the length of the nth term

#### 💡 Idea
Start with `"1"`. For each step from 2 to n, describe the current string using run-length encoding: scan left to right, count consecutive identical characters, append `count + char` to the result. Repeat `n-1` times.

#### 📝 Steps
1. `result = "1"`
2. For `i` from 1 to n-1:
   - `j = 0`
   - While `j < result.length()`:
     - Record current char `ch`
     - Count consecutive `ch`s
     - Append `count` + `ch` to StringBuilder
   - `result = sb.toString()`
3. Return `result`

#### 💻 Code
```java
class Solution {
    public String countAndSay(int n) {
        String result = "1";
        for (int i = 1; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            int j = 0;
            while (j < result.length()) {
                char ch = result.charAt(j);
                int count = 0;
                while (j < result.length() && result.charAt(j) == ch) {
                    j++;
                    count++;
                }
                sb.append(count).append(ch);
            }
            result = sb.toString();
        }
        return result;
    }
}
```

#### 🧪 Dry Run

```
n=5, start with result="1"

Step 1 (i=1): describe "1"
  j=0: ch='1', count=1 → append "11"
  result = "11"

Step 2 (i=2): describe "11"
  j=0: ch='1', count=2 (both 1s) → append "21"
  result = "21"

Step 3 (i=3): describe "21"
  j=0: ch='2', count=1 → append "12"
  j=1: ch='1', count=1 → append "11"
  result = "1211"

Step 4 (i=4): describe "1211"
  j=0: ch='1', count=1 → append "11"
  j=1: ch='2', count=1 → append "12"
  j=2: ch='1', count=2 (two 1s) → append "21"
  result = "111221"

Return "111221" ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N × M) | O(M) |

---

## 3. Compare Version Numbers

### 🧩 Problem Statement

Given two version strings `version1` and `version2` (e.g., "1.2.10"), compare them:
- Return `1` if `version1 > version2`
- Return `-1` if `version1 < version2`
- Return `0` if equal

Each revision is compared as an **integer** (so "01" == "1", "10" > "9").

**Example 1:**
```
Input:  version1="1.2", version2="1.10"
Output: -1  (2 < 10)
```

**Example 2:**
```
Input:  version1="1.0.1", version2="1"
Output: 1  (missing revision = 0, so "1.0.1" > "1.0.0")
```

---

### 🟢 Approach — Split by Dot + Compare
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Split both version strings by `"\\."` (regex for literal dot). Compare corresponding revision integers one by one. If one version has fewer revisions, treat missing ones as `0`.

**Why `Integer.parseInt`?**
"01" and "1" are the same version. Parsing to integer handles leading zeros automatically.

#### 💻 Code
```java
class Solution {
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int n = Math.max(v1.length, v2.length);
        for (int i = 0; i < n; i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 > num2) return 1;
            if (num2 > num1) return -1;
        }
        return 0;
    }
}
```

#### 🧪 Dry Run

Input: `version1="1.2"`, `version2="1.10"`

```
v1 = ["1", "2"]
v2 = ["1", "10"]
n = max(2, 2) = 2

i=0: num1=1, num2=1 → equal, continue
i=1: num1=2, num2=10 → 2 < 10 → return -1 ✅
```

**Missing revision:** `version1="1.0.1"`, `version2="1"`

```
v1 = ["1","0","1"]
v2 = ["1"]
n = max(3,1) = 3

i=0: num1=1, num2=1 → equal
i=1: num1=0, num2=0 (missing → 0) → equal
i=2: num1=1, num2=0 (missing → 0) → 1>0 → return 1 ✅
```

**Leading zeros:** `version1="01.1"`, `version2="1.01"`

```
i=0: parseInt("01")=1, parseInt("1")=1 → equal
i=1: parseInt("1")=1, parseInt("01")=1 → equal
return 0 ✅
```

> 💡 **Why `"\\."` in split?**
> `split(".")` in Java splits on any character (`.` is a regex wildcard). We need `"\\."` to split on a literal dot.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | O(N) for split arrays |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Valid Anagram | Single freq[26] array — increment for s, decrement for t in same loop. All zeros at end = anagram. O(1) space |
| Count and Say | Iterative run-length encoding. For each step, count consecutive identical chars and describe them. n-1 steps total |
| Compare Version Numbers | Split by `"\\."`, parse each revision as integer (handles leading zeros). Treat missing revisions as 0 |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
