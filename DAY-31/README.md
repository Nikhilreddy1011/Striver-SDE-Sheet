# 🚀 Day 31/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [KMP Algorithm / LPS Array](#1-kmp-algorithm--lps-array) | 🔴 Hard | Strings / Pattern Matching |
| 2 | [Minimum Insertions to Make String Palindrome](#2-minimum-insertions-to-make-string-palindrome) | 🔴 Hard | DP on Strings |

---

## 1. KMP Algorithm / LPS Array

### 🧩 Problem Statement

Given `text` and `pattern`, find the **starting indices of all occurrences** of `pattern` in `text` using the KMP algorithm.

**Example 1:**
```
Input:  text="abracadabra", pattern="abra"
Output: [0, 7]
```

**Example 2:**
```
Input:  text="abcabcabc", pattern="abc"
Output: [0, 3, 6]
```

**Constraints:** `1 <= text.length, pattern.length <= 5×10⁴`

---

### 🔑 Pre-Requisite: LPS Array

The **Longest Prefix Suffix (LPS)** array stores for each index `i`, the length of the longest **proper prefix** of the string that is also a **suffix** of `s[0..i]`.

**Example:** `s = "ababac"`
```
i=0 (a):      no proper prefix = suffix → LPS[0]=0
i=1 (ab):     no prefix = suffix → LPS[1]=0
i=2 (aba):    "a" is prefix and suffix → LPS[2]=1
i=3 (abab):   "ab" is prefix and suffix → LPS[3]=2
i=4 (ababa):  "aba" is prefix and suffix → LPS[4]=3
i=5 (ababac): no prefix = suffix → LPS[5]=0
LPS = [0, 0, 1, 2, 3, 0]
```

---

### 🔴 Approach 1 — Brute Force LPS
**Time: O(N³) | Space: O(N)**

#### 💡 Idea
Form combined string `s = pattern + '$' + text`. Compute LPS by checking all possible prefix-suffix pairs at each index using substring comparison. If `LPS[i] == pattern.length`, a match is found.

**Why the `$` delimiter?**
The `$` character (not in the input alphabet) prevents the LPS from matching across the pattern-text boundary. Without it, the prefix could incorrectly extend into the text portion.

#### 💻 Code
```java
class Solution {
    private int[] computeLPS(String s) {
        int n = s.length();
        int[] LPS = new int[n];
        for (int i = 1; i < n; i++) {
            for (int len = 1; len < i; len++) {
                if (s.substring(0, len).equals(s.substring(i - len + 1, i + 1)))
                    LPS[i] = len;
            }
        }
        return LPS;
    }
    public List<Integer> search(String pattern, String text) {
        String s = pattern + '$' + text;
        int[] LPS = computeLPS(s);
        int m = pattern.length();
        List<Integer> ans = new ArrayList<>();
        for (int i = m + 1; i < s.length(); i++)
            if (LPS[i] == m) ans.add(i - 2 * m);
        return ans;
    }
}
```

---

### 🟢 Approach 2 — KMP Optimal (Two-Pointer LPS)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Compute the LPS array efficiently using two pointers `i` and `j`:
- `i`: current index being processed
- `j`: length of the longest prefix-suffix match so far

**Two scenarios:**

**Case 1 — `s[i] == s[j]`:**
The prefix-suffix match extends by one character. Set `LPS[i] = j+1`, advance both `i` and `j`.

**Case 2 — `s[i] != s[j]`:**
Don't reset `j` to 0. Instead backtrack: `j = LPS[j-1]`. This reuses the previously computed LPS to jump to the next best possible matching position — the key insight that gives O(N) time.

**Why `j = LPS[j-1]` instead of `j = 0`?**
`LPS[j-1]` tells us the longest prefix of the pattern (ending at `j-1`) that is also a suffix. That prefix is already matched with the text — so we jump `j` there and try extending from that shorter match, avoiding redundant comparisons.

**Finding matches:**
After computing LPS for `s = pattern + '$' + text`, any position in the text part where `LPS[i] == pattern.length` means the pattern ends there. The starting index in text = `i - 2*m` (accounting for pattern length + delimiter).

#### 💻 Code
```java
class Solution {
    private int[] computeLPS(String s) {
        int n = s.length();
        int[] LPS = new int[n];
        int i = 1, j = 0;
        while (i < n) {
            if (s.charAt(i) == s.charAt(j)) {
                LPS[i] = j + 1; i++; j++;
            } else {
                while (j > 0 && s.charAt(i) != s.charAt(j)) j = LPS[j - 1];
                if (s.charAt(i) == s.charAt(j)) { LPS[i] = j + 1; j++; }
                i++;
            }
        }
        return LPS;
    }
    public List<Integer> search(String pattern, String text) {
        String s = pattern + '$' + text;
        int[] LPS = computeLPS(s);
        int m = pattern.length();
        List<Integer> ans = new ArrayList<>();
        for (int i = m + 1; i < s.length(); i++)
            if (LPS[i] == m) ans.add(i - 2 * m);
        return ans;
    }
}
```

#### 🧪 Dry Run

**LPS computation for `"abab$abcabab"`** (pattern="abab", text="abcabab"):

```
Combined s = "abab$abcabab"
              0123456789...

LPS[0]=0 (base)
i=1(b), j=0(a): 'b'!='a', j=0 already, no match → LPS[1]=0
i=2(a), j=0(a): match → LPS[2]=1, i=3, j=1
i=3(b), j=1(b): match → LPS[3]=2, i=4, j=2
i=4($), j=2(a): '$'!='a', j>0 → j=LPS[1]=0. '$'!='a' still → LPS[4]=0, i=5
i=5(a), j=0(a): match → LPS[5]=1, i=6, j=1
i=6(b), j=1(b): match → LPS[6]=2, i=7, j=2
i=7(c), j=2(a): 'c'!='a', j>0 → j=LPS[1]=0. 'c'!='a' → LPS[7]=0, i=8
i=8(a), j=0(a): match → LPS[8]=1, i=9, j=1
i=9(b), j=1(b): match → LPS[9]=2, i=10, j=2
i=10(a), j=2(a): match → LPS[10]=3, i=11, j=3
i=11(b), j=3(b): match → LPS[11]=4, i=12, j=4

LPS = [0,0,1,2,0,1,2,0,1,2,3,4]

m=4. Check i from m+1=5 to 11:
  i=11: LPS[11]=4==m → match at i-2*m = 11-8 = 3 → pattern starts at index 3 in "abcabab"... 
  Wait: text="abcabab", pattern at index 3: text[3..6]="abab" ✅
```

**Example 1:** `text="abcabcabc"`, `pattern="abc"` → Output: `[0, 3, 6]` ✅

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute LPS | O(N³) | O(N) |
| KMP (Two-Pointer) | **O(N)** ✅ | **O(N)** ✅ |

---

## 2. Minimum Insertions to Make String Palindrome

### 🧩 Problem Statement

Given a string `s`, find the **minimum number of characters to insert** to make it a palindrome.

**Example 1:**
```
Input:  s = "abcaa"
Output: 2  (insert 'c' and 'b' → "abcacba")
```

**Example 2:**
```
Input:  s = "ba"
Output: 1  (insert 'a' → "aba")
```

**Constraints:** `1 <= s.length <= 1000`

---

### 💡 Key Insight

**Why does `min insertions = n - LPS(s)`?**

If we keep the longest palindromic subsequence intact (don't touch it), we only need to add characters for the remaining non-palindromic portion.

**Steps to derive:**
1. Find the **Longest Palindromic Subsequence (LPS)** of length `k`
2. Answer = `n - k`

**How to find LPS?**
LPS of `s` = LCS of `s` and `reverse(s)`. This is because any palindromic subsequence reads the same forwards and backwards, so it appears in both `s` and its reverse.

---

### 🟡 Approach 1 — Tabulation (2D DP)
**Time: O(N²) | Space: O(N²)**

#### 💡 LCS DP Recurrence:
```
if s1[i] == s2[j]: dp[i][j] = 1 + dp[i-1][j-1]
else:               dp[i][j] = max(dp[i-1][j], dp[i][j-1])
```

#### 💻 Code
```java
class Solution {
    private int lcs(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int ind1 = 1; ind1 <= n; ind1++) {
            for (int ind2 = 1; ind2 <= m; ind2++) {
                if (s1.charAt(ind1 - 1) == s2.charAt(ind2 - 1))
                    dp[ind1][ind2] = 1 + dp[ind1 - 1][ind2 - 1];
                else
                    dp[ind1][ind2] = Math.max(dp[ind1 - 1][ind2], dp[ind1][ind2 - 1]);
            }
        }
        return dp[n][m];
    }
    private int longestPalindromeSubsequence(String s) {
        return lcs(s, new StringBuilder(s).reverse().toString());
    }
    public int minInsertion(String s) {
        return s.length() - longestPalindromeSubsequence(s);
    }
}
```

#### 🧪 Dry Run

Input: `s = "abcaa"`

```
reverse = "aacba"
LCS("abcaa", "aacba"):

     ""  a  a  c  b  a
  ""  0  0  0  0  0  0
  a   0  1  1  1  1  1
  b   0  1  1  1  2  2
  c   0  1  1  2  2  2
  a   0  1  2  2  2  3
  a   0  1  2  2  2  3

LPS = 3 (palindromic subsequence: "aaa" or "aca")
min insertions = 5 - 3 = 2 ✅
```

---

### 🟢 Approach 2 — Space Optimized (Two 1D Arrays)
**Time: O(N²) | Space: O(N)**

#### 💡 Idea
The 2D DP only uses the previous row `dp[ind1-1]` to compute the current row `dp[ind1]`. Replace with `prev` and `cur` arrays. After each row, `prev = cur`.

#### 💻 Code
```java
class Solution {
    private int lcs(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[] prev = new int[m + 1], cur = new int[m + 1];
        for (int ind1 = 1; ind1 <= n; ind1++) {
            for (int ind2 = 1; ind2 <= m; ind2++) {
                if (s1.charAt(ind1 - 1) == s2.charAt(ind2 - 1))
                    cur[ind2] = 1 + prev[ind2 - 1];
                else
                    cur[ind2] = Math.max(prev[ind2], cur[ind2 - 1]);
            }
            System.arraycopy(cur, 0, prev, 0, m + 1);
        }
        return prev[m];
    }
    private int longestPalindromeSubsequence(String s) {
        return lcs(s, new StringBuilder(s).reverse().toString());
    }
    public int minInsertion(String s) {
        return s.length() - longestPalindromeSubsequence(s);
    }
}
```

#### 🧪 Dry Run

Input: `s = "ba"`, reverse = "ab"

```
LCS("ba", "ab"):
prev = [0,0,0]

ind1=1 (b):
  ind2=1 (a): b!=a → cur[1]=max(prev[1]=0, cur[0]=0)=0
  ind2=2 (b): b==b → cur[2]=1+prev[1]=1
  prev = [0,0,1]

ind1=2 (a):
  ind2=1 (a): a==a → cur[1]=1+prev[0]=1
  ind2=2 (b): a!=b → cur[2]=max(prev[2]=1, cur[1]=1)=1
  prev = [0,1,1]

LPS = prev[2] = 1
min insertions = 2 - 1 = 1 ✅
```

> 💡 **Why `System.arraycopy(cur, 0, prev, 0, m+1)` instead of `prev = cur`?**
> `prev = cur` would make both references point to the same array. Modifying `cur` in the next iteration would also modify `prev`. `System.arraycopy` creates a proper copy so they remain independent.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Tabulation (2D) | O(N²) | O(N²) |
| Space Optimized | **O(N²)** ✅ | **O(N)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| KMP Algorithm | Build combined string `pattern + '$' + text`. Compute LPS with two-pointer approach — backtrack `j = LPS[j-1]` on mismatch (not j=0). LPS[i]==m means pattern found at index `i-2m` in text |
| Min Insertions Palindrome | min insertions = n - LPS(s). LPS(s) = LCS(s, reverse(s)). The palindromic subsequence stays intact; only the rest needs insertion |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
