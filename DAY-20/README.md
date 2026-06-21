# 🚀 Day 20/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Find Nth Root of a Number](#1-find-nth-root-of-a-number) | 🟡 Medium | Binary Search on Answers |
| 2 | [Matrix Median](#2-matrix-median) | 🔴 Hard | Binary Search on Answers |
| 3 | [Single Element in Sorted Array](#3-single-element-in-sorted-array) | 🟡 Medium | Binary Search |
| 4 | [Search in Rotated Sorted Array](#4-search-in-rotated-sorted-array) | 🟡 Medium | Binary Search |

---

## 1. Find Nth Root of a Number

### 🧩 Problem Statement

Given two integers `N` and `M`, find the Nth root of M. If the Nth root is not an integer, return `-1`.

**Example 1:**
```
Input:  N=3, M=27
Output: 3   (3^3 = 27)
```

**Example 2:**
```
Input:  N=4, M=69
Output: -1  (no integer 4th root)
```

**Constraints:** `1 <= N <= 30`, `1 <= M <= 10⁹`

---

### 🔴 Approach 1 — Brute Force (Linear Search)
**Time: O(M × log N) | Space: O(1)**

#### 💡 Intuition
Iterate from 1 to M. For each `i`, compute `i^N` using fast exponentiation. If `i^N == M`, return `i`. If `i^N > M`, break early — no solution exists beyond this point.

#### 💻 Code
```java
class Solution {
    private long pow(int a, int x ){
        long ans = 1; 
        long base = a;
        while(x > 0){
            if(x % 2 == 1){ x--; ans *= base; }
            else{ x /=2; base *= base; }
        }
        return ans;
    }
    public int NthRoot(int N, int M) {
        for(int i = 1; i<= M ; i++){
            long val = pow(i,N);
            if(val == M) return i;
            else if( val > M) break;
        }
        return -1;
    }
}
```

---

### 🟢 Approach 2 — Optimal (Binary Search on Answer)
**Time: O(log M × log N) | Space: O(1)**

#### 💡 Intuition
Binary search in range `[1, M]`. For `mid`, compute `mid^N` and compare with `M`:
- Return `1` if `mid^N == M` → found
- Return `0` if `mid^N < M` → search right
- Return `2` if `mid^N > M` → search left

**Critical:** During exponentiation, if the running value exceeds `M`, immediately return `2` (early exit) — this prevents integer overflow when `M = 10⁹` and `N` is large.

#### 💻 Code
```java
class Solution {
    private int func(int mid, int N, int M){
        long ans = 1, base = mid;
        while(N > 0){
            if(N % 2 == 1){
                ans *= base;
                if(ans > M) return 2;
                N--;
            }
            else{
                N /= 2;
                base *= base;
                if(base > M) return 2;
            }
        }
        if( ans == M) return 1;
        return 0;
    }
    public int NthRoot(int N, int M) {
        int low = 1, high = M;
        while(low <= high){
            int mid = low + (high-low)/2;
            int val = func(mid, N, M);
            if(val == 1) return mid;
            else if(val == 0 ) low = mid +1;
            else high = mid - 1;
        }
        return -1;
    }
}
```

#### 🧪 Dry Run

Input: `N=3, M=27`

```
low=1, high=27

mid=14: func(14,3,27)
  14^2=196 > 27 → return 2 (early exit)
  high=13

mid=7: 7^2=49>27 → return 2, high=6
mid=3: 3^2=9, 9*3=27==27 → return 1 → return 3 ✅

Input: N=4, M=69
mid=35: 35^2=1225>69 → 2, high=34
mid=17: 17^2=289>69 → 2, high=16
mid=8:  8^2=64, 64^? no — 8^4=4096>69 → 2, high=7
mid=4:  4^4=256>69 → 2, high=3
mid=2:  2^4=16<69 → 0, low=3
mid=3:  3^4=81>69 → 2, high=2
low>high → return -1 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Linear Search | O(M × log N) | O(1) |
| Binary Search | **O(log M × log N)** ✅ | **O(1)** ✅ |

---

## 2. Matrix Median

### 🧩 Problem Statement

Given a row-wise sorted `N×M` matrix (N×M is always odd), find the **median**.

**Example 1:**
```
Input:  [[1,4,9],[2,5,6],[3,7,8]]
Sorted: [1,2,3,4,5,6,7,8,9]
Output: 5
```

**Example 2:**
```
Input:  [[1,3,8],[2,3,4],[1,2,5]]
Output: 3
```

**Constraints:** `1 <= N*M <= 10⁶`, N*M is odd

---

### 🔴 Approach 1 — Brute Force (Flatten + Sort)
**Time: O(N×M × log(N×M)) | Space: O(N×M)**

#### 💡 Intuition
Collect all elements into a list, sort it, return the middle element at index `(N×M)/2`.

#### 💻 Code
```java
class Solution {
    public int findMedian(int[][] matrix) {
        int n = matrix.length, m = matrix[0].length;
        List<Integer> arr = new ArrayList<>();
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++) arr.add(matrix[i][j]);
        Collections.sort(arr);
        return arr.get((m*n)/2);
    }
}
```

---

### 🟢 Approach 2 — Optimal (Binary Search on Value + Upper Bound)
**Time: O(N × log M × log(max−min)) | Space: O(1)**

#### 💡 Intuition
The median is the smallest value `x` such that more than half the total elements are `≤ x`.

**Key insight:** Since each row is sorted, we can count elements `≤ mid` in a row in O(log M) using upper bound. Summing across all N rows gives total count.

Binary search on the **value range** `[min, max]`:
- If `count(elements ≤ mid) ≤ (N×M)/2` → median is to the right → `low = mid+1`
- Else → `high = mid-1`

When the loop ends, `low` is the median.

**Why `count ≤ req` (not `< req`)?**
We want the smallest value where more than half the elements are ≤ it. So we search right when `count ≤ req` (not enough elements yet) and left when count exceeds it.

#### 💻 Code
```java
class Solution { 
    private int uB(int[] nums, int x, int m) {
        int low=0, high=m-1, ans=m;
        while(low <= high){
            int mid = low + (high-low)/2;
            if(nums[mid]>x){ ans=mid; high=mid-1; }
            else low=mid+1; 
        }
        return ans;
    }
    private int blackBox(int[][] matrix, int n, int m, int mid){
        int count=0;
        for(int i=0; i<n; i++) count += uB(matrix[i],mid,m);
        return count;
    }
    public int findMedian(int[][] matrix) {
        int n=matrix.length, m=matrix[0].length;
        int low=Integer.MAX_VALUE, high=Integer.MIN_VALUE;
        for(int i=0; i<n; i++){
            low=Math.min(low,matrix[i][0]);
            high=Math.max(high,matrix[i][m-1]);
        }
        int req=(n*m)/2;
        while(low<=high){
            int mid=low+(high-low)/2;
            int smallNumber=blackBox(matrix,n,m,mid);
            if(smallNumber<=req) low=mid+1;
            else high=mid-1;
        }
        return low;
    }
}
```

#### 🧪 Dry Run

Input: `[[1,4,9],[2,5,6],[3,7,8]]`, N=3, M=3, total=9, req=4

```
min=1 (first col mins), max=9 (last col maxes)
low=1, high=9

mid=5: blackBox counts elements ≤5 in each row:
  row[0]=[1,4,9]: upper_bound(5)=index 2 (first >5 is 9 at idx2) → 2 elements ≤5
  row[1]=[2,5,6]: upper_bound(5)=index 2 (first >5 is 6 at idx2) → 2 elements ≤5
  row[2]=[3,7,8]: upper_bound(5)=index 1 (first >5 is 7 at idx1) → 1 element ≤5
  total=5 > req=4 → high=4

mid=2: 
  row[0]: 1 element ≤2, row[1]: 1, row[2]: 1 → total=3 ≤ req=4 → low=3

mid=3: row[0]: 1, row[1]: 1, row[2]: 2 → total=4 ≤ req=4 → low=4

mid=4: row[0]: 2 (1,4), row[1]: 1 (2), row[2]: 1 (3) → total=4 ≤ req=4 → low=5

low=5 > high=4 → exit
Return low=5 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Flatten + Sort | O(NM log NM) | O(NM) |
| Binary Search on Value | **O(N log M × log(max−min))** ✅ | **O(1)** ✅ |

---

## 3. Single Element in Sorted Array

### 🧩 Problem Statement

In a sorted array where every element appears exactly twice except one, find the **single element**.

**Example 1:**
```
Input:  [1,1,2,2,3,3,4,5,5,6,6]
Output: 4
```

**Example 2:**
```
Input:  [1,1,3,5,5]
Output: 3
```

**Constraints:** `1 <= n <= 10⁴`, n is always odd

---

### 🔴 Approach 1 — Brute Force (Linear Scan)
**Time: O(N) | Space: O(1)**

Check each element against its neighbors. Handle edge cases (first/last element) separately.

```java
class Solution {
    public int singleNonDuplicate(int[] nums) {
      int n = nums.length;
      if (n==1) return nums[0];
      for(int i = 0 ; i < n ;  i++){
        if(i==0) { if(nums[i] != nums[i+1]) return nums[i]; }
        else if(i == n-1){ if(nums[i] != nums[i-1]) return nums[i]; }
        else{ if(nums[i] != nums[i+1] && nums[i] != nums[i-1]) return nums[i]; }
      }
      return -1;
    }
}
```

---

### 🟡 Approach 2 — XOR
**Time: O(N) | Space: O(1)**

#### 💡 Intuition
XOR of identical numbers = 0. XOR everything — pairs cancel out, leaving only the single element.

```java
class Solution {
    public int singleNonDuplicate(int[] nums) {
      int xor = 0;
      for(int i = 0; i < nums.length; i++) xor ^= nums[i];
      return xor;
    }
}
```

---

### 🟢 Approach 3 — Binary Search
**Time: O(log N) | Space: O(1)**

#### 💡 Intuition
Before the single element: pairs occupy `(even, odd)` indices — `nums[even] == nums[even+1]`.
After the single element: pairs shift to `(odd, even)` indices.

Binary search on `[1, n-2]` (excluding boundaries, handled separately):
- If `mid` is the single element (doesn't match either neighbor) → return
- If `mid` is **even** and `nums[mid] == nums[mid+1]` → we're in the left part → `low = mid+1`
- If `mid` is **odd** and `nums[mid] == nums[mid-1]` → we're in the left part → `low = mid+1`
- Otherwise → `high = mid-1`

#### 💻 Code
```java
class Solution {
    public int singleNonDuplicate(int[] nums) {
      int n = nums.length;
      if(n==1) return nums[0];
      if(nums[0] != nums[1]) return nums[0];
      if(nums[n-1] != nums[n-2]) return nums[n-1];
      int low = 1, high = n-2;
      while(low <= high){
        int mid = low + (high-low)/2;
        if(nums[mid] != nums[mid+1] && nums[mid] != nums[mid-1]) return nums[mid];
        if((mid%2==1 && nums[mid]==nums[mid-1]) || (mid%2==0 && nums[mid]==nums[mid+1]))
            low = mid+1;
        else high = mid-1;
      }
      return -1;
    }
}
```

#### 🧪 Dry Run

Input: `[1,1,2,2,3,3,4,5,5,6,6]` (n=11)

```
Boundary: nums[0]=1==nums[1]=1 → not single; nums[10]=6==nums[9]=6 → not single
low=1, high=9

mid=5: nums[5]=3, nums[4]=3, nums[6]=4 → 3==3 (matches left) → odd mid, matches mid-1 → left part → low=6
mid=7: nums[7]=5, nums[6]=4, nums[8]=5 → 5 matches nums[8] → even mid matches mid+1 → left part → low=8
mid=8: nums[8]=5, nums[7]=5, nums[9]=6 → 5 matches nums[7] → odd mid matches mid-1 → left part → low=9
mid=9: nums[9]=6, nums[8]=5, nums[10]=6 → 6 matches nums[10] → even mid matches mid+1? No, mid+1=10, nums[10]=6 ✓
  But wait: mid=9 (odd), nums[9]=6==nums[8]=5? No. mid%2==1, nums[mid]=6==nums[mid-1]=5? No
  Neither condition → high=8
low=9 > high=8 → exit

Wait, let me retrace... mid=9: nums[9]=6, nums[8]=5 (not equal), nums[10]=6 → nums[mid] doesn't match both neighbors
  nums[9]!=nums[10]? 6==6 → matches! So not single element check fails
  mid=9 (odd): nums[9]==nums[8]? 6==5 No. → condition false
  mid=9 (odd) first branch failed; else → high=8

...Actually let's check mid=6: low=6, high=9 → mid=7 (above is correct)

Correct trace: single element is at index 6 (value 4)
At mid=6: nums[6]=4, nums[5]=3, nums[7]=5 → 4≠3 and 4≠5 → single! return 4 ✅

Wait, mid=6 isn't reached in my trace above. Let me redo:

low=1, high=9
mid=5: nums[5]=3==nums[4]=3. mid=5(odd), nums[5]==nums[4] → left part → low=6
low=6, high=9
mid=7: nums[7]=5==nums[8]=5. mid=7(odd), nums[7]==nums[6]=4? No. mid=7(odd), nums[7]==nums[6]? 5==4 No
  → else → high=6
low=6, high=6
mid=6: nums[6]=4, nums[5]=3, nums[7]=5 → 4≠3 and 4≠5 → return 4 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Linear Scan | O(N) | O(1) |
| XOR | O(N) | O(1) |
| Binary Search | **O(log N)** ✅ | **O(1)** ✅ |

---

## 4. Search in Rotated Sorted Array

### 🧩 Problem Statement

Given a rotated sorted array with distinct values and a target `k`, return the **index of `k`** or `-1` if not found.

**Example 1:**
```
Input:  nums=[4,5,6,7,0,1,2], k=0
Output: 4
```

**Example 2:**
```
Input:  nums=[4,5,6,7,0,1,2], k=3
Output: -1
```

**Constraints:** All values unique, `1 <= n <= 10⁴`

---

### 🔴 Approach 1 — Brute Force (Linear Search)
**Time: O(N) | Space: O(1)**

```java
class Solution {
    public int search(int[] nums, int k) {
       int n = nums.length;
       for(int i = 0; i < n; i++) if(nums[i]==k) return i;
       return -1;
    }
}
```

---

### 🟢 Approach 2 — Optimal (Binary Search)
**Time: O(log N) | Space: O(1)**

#### 💡 Intuition
In a rotated sorted array, at any `mid`, **one half is always sorted**. Use this to determine which half contains the target:

- If **left half sorted** (`nums[low] <= nums[mid]`):
  - If `k` is within `[nums[low], nums[mid]]` → search left (`high = mid-1`)
  - Else → search right (`low = mid+1`)
- If **right half sorted**:
  - If `k` is within `[nums[mid], nums[high]]` → search right (`low = mid+1`)
  - Else → search left (`high = mid-1`)

#### 💻 Code
```java
class Solution {
    public int search(int[] nums, int k) {
       int n = nums.length;
       int low = 0, high = n-1;
       while(low <= high){
        int mid = low + (high-low)/2;
        if(nums[mid] == k) return mid;
        if(nums[low] <= nums[mid]){
            if(nums[low] <= k && k <= nums[mid]) high = mid-1;
            else low = mid+1;
        }
        else{
            if(nums[mid] <= k && k <= nums[high]) low = mid+1;
            else high = mid-1;
        }
       }
       return -1;
    }
}
```

#### 🧪 Dry Run

Input: `nums=[4,5,6,7,0,1,2], k=0`

```
low=0, high=6

mid=3: nums[3]=7 ≠ 0
  Left sorted? nums[0]=4 <= nums[3]=7 → YES
  k=0 in [4,7]? No → low=4

mid=5: nums[5]=1 ≠ 0
  Left sorted? nums[4]=0 <= nums[5]=1 → YES
  k=0 in [0,1]? Yes → high=4

mid=4: nums[4]=0 == k=0 → return 4 ✅
```

**k=3 (not found):**

```
low=0, high=6
mid=3: nums[3]=7. Left sorted[4,7]. 3 in [4,7]? No → low=4
mid=5: nums[5]=1. Left sorted[0,1]. 3 in [0,1]? No → low=6
mid=6: nums[6]=2. Left sorted[2,2]. 3 in [2,2]? No → low=7
low>high → return -1 ✅
```

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Linear Search | O(N) | O(1) |
| Binary Search | **O(log N)** ✅ | **O(1)** ✅ |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Nth Root | Binary search on answer [1,M]. Helper returns 1/0/2 to avoid overflow — early exit when intermediate value exceeds M |
| Matrix Median | Binary search on value [min,max]. Count elements ≤ mid per row using upper bound. Smallest value where count > (N×M)/2 is the median |
| Single Element | After single element, pair indices flip (even,odd)→(odd,even). Use this parity to eliminate half in each step |
| Search Rotated | One half is always sorted. Check which half, then check if target lies in that range to decide direction |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
