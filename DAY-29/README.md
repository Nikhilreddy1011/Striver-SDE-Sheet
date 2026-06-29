# 🚀 Day 29/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Reverse Every Word in a String](#1-reverse-every-word-in-a-string) | 🟡 Medium | Strings / Two Pointer |
| 2 | [Longest Palindromic Substring](#2-longest-palindromic-substring) | 🟡 Medium | Strings / Expand Around Center |
| 3 | [Roman to Integer](#3-roman-to-integer) | 🟡 Medium | Strings / HashMap |

---

## 1. Reverse Every Word in a String

### 🧩 Problem Statement

Given a string `s` containing words separated by spaces (possibly multiple spaces, leading/trailing), return a string with words in **reverse order** separated by a single space.

**Example 1:**
```
Input:  "welcome to the jungle"
Output: "jungle the to welcome"
```

**Example 2:**
```
Input:  "  amazing coding skills  "
Output: "skills coding amazing"
```

**Constraints:** `1 <= s.length <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Extract + Reverse List)
**Time: O(N) | Space: O(N)**

#### 💡 Idea
Scan the string, skipping spaces, to extract each word into a list. Then build the result by iterating the list in reverse with single spaces between words.

**Steps:**
1. Use pointer `j` to skip spaces and find word boundaries (`left` to `right`)
2. Add each word to `list`
3. Build answer by appending `list` from last to first with spaces

#### 💻 Code
```java
class Solution {
    public String reverseWords(String s) {
        int n = s.length();
        List<String> list = new ArrayList<>();
        int j = 0; 
        while(j < n){
            while(j < n && s.charAt(j) == ' ') j++;
            if(j >= n) break;
            int left = j;
            while(j < n && s.charAt(j) != ' ') j++;
            list.add(s.substring(left, j));
        }     
        StringBuilder ans = new StringBuilder();
        for(int i = list.size()-1; i >= 0; i--){
            ans.append(list.get(i));
            if(i != 0) ans.append(' ');
        }   
        return ans.toString();
    }
}
```

#### 🧪 Dry Run

Input: `"  amazing coding skills  "`

```
j=0,1: spaces → skip. j=2
left=2, j scans: j=9 (space found). word="amazing". list=["amazing"]
j=9: space → skip. j=10
left=10, j scans to 16. word="coding". list=["amazing","coding"]
j=16: space → skip. j=17
left=17, j scans to 23. word="skills". list=["amazing","coding","skills"]
j=23,24: spaces → skip. j=25=n → break

Reverse: "skills coding amazing" ✅
```

---

### 🟢 Approach 2 — Optimal (In-Place: Reverse All + Reverse Each Word)
**Time: O(N) | Space: O(1)**

#### 💡 Idea
Three steps, all in-place on a `StringBuilder`:

1. **Reverse the entire string** → words are now in reverse order but each word's characters are backwards
2. **Extract each word** (skip spaces), copy it forward (compacts the string, removes extra spaces), then **reverse that word in-place** to restore correct character order
3. **Add single space** after each word, trim trailing space at end

**Example trace:**
```
"  amazing coding skills  "
After full reverse: "  slliks gnidoc gnizama  "
Process words: extract "slliks", reverse → "skills", add space
               extract "gnidoc", reverse → "coding", add space
               extract "gnizama", reverse → "amazing"
Result: "skills coding amazing"
```

#### 💻 Code
```java
class Solution {
    private void reverse(StringBuilder s, int start, int end){
        while(start < end){
            char temp = s.charAt(start);
            s.setCharAt(start, s.charAt(end));
            s.setCharAt(end, temp);
            start++; end--;
        }
    }
    public String reverseWords(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(s);
        reverse(sb, 0, n-1);
        int i = 0, j = 0, start, end;
        while(j < n){
            while(j < n && sb.charAt(j) == ' ') j++;
            if(j == n) break;
            start = i;
            while(j < n && sb.charAt(j) != ' '){
                if(i < sb.length()) sb.setCharAt(i++, sb.charAt(j++));
                else { sb.append(sb.charAt(j++)); i++; }
            }
            end = i - 1;
            reverse(sb, start, end);
            if(j < n){
                if(i < sb.length()) sb.setCharAt(i++, ' ');
                else { sb.append(' '); i++; }
            }
        }
        if(i > 0 && sb.charAt(i-1) == ' ') i--;
        return sb.substring(0, i);
    }
}
```

> 💡 **Why reverse the whole string first?**
> Reversing everything puts words in the correct final order, just with each word's characters backwards. Reversing each word individually then corrects the characters. This avoids needing a separate list.

> 💡 **Why copy words forward (pointer `i`)?**
> Multiple spaces between words need to be collapsed to single spaces. Copying words forward to position `i` while skipping spaces achieves this compaction in-place.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Extract + Reverse List | O(N) | O(N) |
| In-Place | **O(N)** ✅ | **O(1)** ✅ |

---

## 2. Longest Palindromic Substring

### 🧩 Problem Statement

Given a string `s`, return the **longest substring** that is a palindrome (reads the same forwards and backwards).

**Example 1:**
```
Input:  "babad"
Output: "bab"  (or "aba")
```

**Example 2:**
```
Input:  "cbbd"
Output: "bb"
```

**Constraints:** `1 <= s.length <= 1000`, alphanumeric characters

---

### 🟢 Approach — Expand Around Center
**Time: O(N²) | Space: O(1)**

#### 💡 Idea
Every palindrome has a center. For odd-length palindromes, the center is a single character. For even-length, the center is between two characters. Expand outward from each possible center and check how far the palindrome extends.

There are `2N - 1` possible centers (N for odd, N-1 for even). For each center, expand while left and right characters match. Track the longest found.

**`expandAroundCenter(s, left, right)`:**
- Starts with left and right as the initial center
- Expands while `s[left] == s[right]`
- Returns `{left+1, right-1}` (last valid palindrome indices after the loop overshoots)

#### 💻 Code
```java
class Solution {
    private int[] expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--; right++;
        }
        return new int[]{left + 1, right - 1};
    }

    public String longestPalindrome(String s) {
        int n = s.length();
        if (n == 0) return "";
        int start = 0, end = 0;

        for (int i = 0; i < n; i++) {
            int[] odd  = expandAroundCenter(s, i, i);      // odd center
            int[] even = expandAroundCenter(s, i, i + 1);  // even center

            if (odd[1] - odd[0] > end - start) { start = odd[0]; end = odd[1]; }
            if (even[1] - even[0] > end - start) { start = even[0]; end = even[1]; }
        }
        return s.substring(start, end + 1);
    }
}
```

#### 🧪 Dry Run

Input: `s = "babad"`

```
i=0 (b):
  odd  (0,0): expand: s[0]=b==s[0]=b (single). Try left=-1 → stop. returns {0,0}. len=1
  even (0,1): s[0]=b != s[1]=a → stop immediately. returns {1,0}. len=-1 (empty)
  update: start=0, end=0

i=1 (a):
  odd  (1,1): s[1]=a. expand: s[0]=b==s[2]=b? No wait: left=1,right=1. Expand: left=0,right=2: s[0]=b==s[2]=b → continue. left=-1: stop. returns {0,2}. len=3-0=2
  even (1,2): s[1]=a != s[2]=b → stop. returns {2,1}. empty.
  update: 2 > 0 → start=0, end=2

i=2 (b):
  odd  (2,2): expand: left=1,right=3: s[1]=a==s[3]=a → continue. left=0,right=4: s[0]=b==s[4]=d? No → stop. returns {1,3}. len=3-1=2
  Not > current end-start=2 → no update

i=3,4: no longer palindrome found

start=0, end=2 → s.substring(0,3) = "bab" ✅
```

> 💡 **Why `left+1, right-1` returned?**
> The loop exits when `s[left] != s[right]` (one step too far). So the last valid palindrome is `[left+1, right-1]` — the positions before the mismatch.

> 💡 **Why check both odd and even for each `i`?**
> "bab" has odd center at 'a'. "bb" has even center between the two 'b's. We need both types at every index or we'd miss half the cases.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N²)** ✅ | **O(1)** ✅ |

---

## 3. Roman to Integer

### 🧩 Problem Statement

Convert a Roman numeral string to an integer. Roman numerals: `I=1, V=5, X=10, L=50, C=100, D=500, M=1000`. Subtraction rule: if a smaller value appears before a larger one, subtract it (e.g., IV=4, IX=9, XL=40).

**Example 1:**
```
Input:  "III" → 3
```

**Example 2:**
```
Input:  "XLII" → 42  (XL=40, II=2)
```

**Constraints:** `1 <= s.length <= 15`, valid Roman numeral in [1, 3999]

---

### 🟢 Approach — HashMap + Left-to-Right with Lookahead
**Time: O(N) | Space: O(1)**

#### 💡 Idea
Store all 7 symbols and their values in a HashMap. Traverse left to right. At each character:
- If the **next character has a larger value** → subtract current (subtraction case: IV, IX, etc.)
- Otherwise → add current

This single-pass rule handles all subtraction cases naturally without special-casing each pair.

**Why left-to-right (not right-to-left)?**
Left-to-right with a lookahead is intuitive: "if the current symbol is smaller than the next, it's subtractive." Right-to-left works too (just subtract if current < running max), but this approach matches how we read Roman numerals.

#### 💻 Code
```java
class Solution {
    public int romanToInt(String s) {
        int n = s.length();
        HashMap<Character,Integer> map = new HashMap<>();
        map.put('I',1); map.put('V',5); map.put('X',10); map.put('L',50);
        map.put('C',100); map.put('D',500); map.put('M',1000);
        int ans = 0;
        for(int i = 0; i < n; i++){
            if(i+1 < n && map.get(s.charAt(i)) < map.get(s.charAt(i+1)))
                ans -= map.get(s.charAt(i));
            else
                ans += map.get(s.charAt(i));
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `"MCMXCIV"`

```
map: M=1000, C=100, X=10, I=1, V=5

i=0 (M=1000): next=C(100). 1000 < 100? No → ans += 1000. ans=1000
i=1 (C=100):  next=M(1000). 100 < 1000? Yes → ans -= 100. ans=900
i=2 (M=1000): next=X(10). 1000 < 10? No → ans += 1000. ans=1900
i=3 (X=10):   next=C(100). 10 < 100? Yes → ans -= 10. ans=1890
i=4 (C=100):  next=I(1). 100 < 1? No → ans += 100. ans=1990
i=5 (I=1):    next=V(5). 1 < 5? Yes → ans -= 1. ans=1989
i=6 (V=5):    no next. ans += 5. ans=1994

Return 1994 ✅
```

**DCCCXC = 890:**
```
D=500: next=C(100). 500<100? No → +500. ans=500
C=100: next=C(100). 100<100? No → +100. ans=600
C=100: next=C(100). No → +100. ans=700
C=100: next=X(10). 100<10? No → +100. ans=800
X=10:  next=C(100). 10<100? Yes → -10. ans=790
C=100: no next. +100. ans=890 ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N)** ✅ | **O(1)** (HashMap is fixed 7 entries) ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Reverse Words | Brute: extract words to list, append reverse. Optimal: reverse full string, then reverse each word in-place to compact spaces and fix word characters |
| Longest Palindrome | Expand Around Center: 2N-1 possible centers (odd + even). Expand while characters match. O(N²) time, O(1) space |
| Roman to Integer | HashMap for 7 symbols. Left-to-right: if current < next → subtract; else add. One pass handles all subtraction rules |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
