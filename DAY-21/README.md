# 🚀 Day 21/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem                           | Difficulty | Topic                    |
| - | --------------------------------- | ---------- | ------------------------ |
| 1 | Median of Two Sorted Arrays       | 🔴 Hard    | Binary Search Partition  |
| 2 | K-th Element of Two Sorted Arrays | 🔴 Hard    | Binary Search Partition  |
| 3 | Book Allocation Problem           | 🟡 Medium  | Binary Search on Answers |
| 4 | Aggressive Cows                   | 🟡 Medium  | Binary Search on Answers |

---

# 1. Median of Two Sorted Arrays

### 🧩 Problem Statement

Given two sorted arrays, return the median of the merged sorted array.

**Example 1:**

```text
Input: nums1 = [1,3], nums2 = [2]
Output: 2.0
```

**Example 2:**

```text
Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.5
```

---

### 🔴 Approach 1 — Merge Arrays

**Time: O(N+M) | Space: O(N+M)**

#### 💡 Intuition

Merge both sorted arrays and directly find the median from the merged array.

---

### 🟡 Approach 2 — Two Pointers

**Time: O(N+M) | Space: O(1)**

#### 💡 Intuition

Instead of storing the merged array, track only the median positions while merging.

---

### 🟢 Approach 3 — Binary Search Partition

**Time: O(log(min(N,M))) | Space: O(1)**

#### 💡 Intuition

Partition both arrays such that:

```text
Left Half Size = Right Half Size
```

and

```text
max(left) <= min(right)
```

Once a valid partition is found:

```text
Odd Length:
Median = max(left)

Even Length:
Median = (max(left) + min(right))/2
```

The key idea is that we binary search on the smaller array and derive the partition in the second array automatically.

#### 📊 Complexity Summary

| Approach                | Time             | Space  |
| ----------------------- | ---------------- | ------ |
| Merge Arrays            | O(N+M)           | O(N+M) |
| Two Pointers            | O(N+M)           | O(1)   |
| Binary Search Partition | O(log(min(N,M))) | O(1)   |

---

# 2. K-th Element of Two Sorted Arrays

### 🧩 Problem Statement

Given two sorted arrays and an integer K, return the K-th smallest element in the merged sorted order.

**Example 1:**

```text
Input:
A = [2,3,6,7,9]
B = [1,4,8,10]
K = 5

Output:
6
```

---

### 🔴 Approach 1 — Merge Arrays

**Time: O(N+M) | Space: O(N+M)**

#### 💡 Intuition

Merge both arrays and directly return the K-th element.

---

### 🟡 Approach 2 — Two Pointers

**Time: O(K) | Space: O(1)**

#### 💡 Intuition

Simulate the merge process and stop as soon as the K-th element is reached.

---

### 🟢 Approach 3 — Binary Search Partition

**Time: O(log(min(N,M))) | Space: O(1)**

#### 💡 Intuition

This problem is almost identical to Median of Two Sorted Arrays.

Instead of fixing the left partition size as:

```text
(N+M+1)/2
```

we fix it as:

```text
K
```

Partition:

```text
cut1 + cut2 = K
```

Valid partition:

```text
l1 <= r2
l2 <= r1
```

Answer:

```text
max(l1,l2)
```

#### 📊 Complexity Summary

| Approach                | Time             | Space  |
| ----------------------- | ---------------- | ------ |
| Merge Arrays            | O(N+M)           | O(N+M) |
| Two Pointers            | O(K)             | O(1)   |
| Binary Search Partition | O(log(min(N,M))) | O(1)   |

---

# 3. Book Allocation Problem

### 🧩 Problem Statement

Allocate books to M students such that:

* Every student gets at least one book.
* Books are assigned contiguously.
* Maximum pages assigned to a student is minimized.

Return the minimum possible value of the maximum pages.

**Example**

```text
Input:
Books = [25,46,28,49,24]
Students = 4

Output:
71
```

---

### 🔴 Approach 1 — Brute Force

**Time: O((sum-max) × N) | Space: O(1)**

#### 💡 Intuition

The answer lies between:

```text
max(bookPages)
```

and

```text
sum(bookPages)
```

Try every possible answer and check whether allocation is possible.

---

### 🟢 Approach 2 — Binary Search on Answers

**Time: O(N log(sum)) | Space: O(1)**

#### 💡 Intuition

Search Space:

```text
low = max(bookPages)
high = sum(bookPages)
```

For a candidate answer:

```text
mid
```

Count how many students are required if no student can exceed `mid` pages.

If:

```text
studentsNeeded <= M
```

then allocation is possible.

Try a smaller answer.

Else:

```text
increase answer
```

#### 🧪 Dry Run

```text
Books = [25,46,28,49,24]
M = 4

low = 49
high = 172
```

Eventually:

```text
Answer = 71
```

Allocation:

```text
[25,46]
[28]
[49]
[24]
```

Maximum pages:

```text
71
```

#### 📊 Complexity Summary

| Approach      | Time             | Space |
| ------------- | ---------------- | ----- |
| Brute Force   | O((sum-max) × N) | O(1)  |
| Binary Search | O(N log(sum))    | O(1)  |

---

# 4. Aggressive Cows

### 🧩 Problem Statement

Given positions of stalls and K cows, place the cows such that the minimum distance between any two cows is maximized.

Return that maximum possible minimum distance.

**Example**

```text
Input:
stalls = [1,2,4,8,9]
k = 3

Output:
3
```

---

### 🔴 Approach 1 — Brute Force

**Time: O((max-min) × N) | Space: O(1)**

#### 💡 Intuition

The answer can range from:

```text
1
```

to

```text
stalls[n−1] − stalls[0]
```

Try every distance and check whether K cows can be placed.

---

### 🟢 Approach 2 — Binary Search on Answers

**Time: O(N log(maxDistance)) | Space: O(1)**

#### 💡 Intuition

Observe:

```text
If distance = D is possible,
then every smaller distance is also possible.
```

and

```text
If distance = D is not possible,
then every larger distance is also not possible.
```

This gives a monotonic search space:

```text
TTTTTTFFFFF
```

Perfect for Binary Search.

Search:

```text
low = 1
high = stalls[n−1] − stalls[0]
```

For every distance:

```text
Can we place K cows?
```

If yes:

```text
Try larger distance
```

Else:

```text
Try smaller distance
```

#### 🧪 Dry Run

```text
stalls = [1,2,4,8,9]
k = 3
```

After sorting:

```text
[1,2,4,8,9]
```

Binary Search:

```text
mid = 4 -> Not Possible
mid = 2 -> Possible
mid = 3 -> Possible
```

Answer:

```text
3
```

#### 📊 Complexity Summary

| Approach      | Time                  | Space |
| ------------- | --------------------- | ----- |
| Brute Force   | O((max-min) × N)      | O(1)  |
| Binary Search | O(N log(maxDistance)) | O(1)  |

---

## 📌 Key Takeaways

| Problem                           | Key Insight                                                        |
| --------------------------------- | ------------------------------------------------------------------ |
| Median of Two Sorted Arrays       | Partition both arrays such that left and right halves are balanced |
| K-th Element of Two Sorted Arrays | Same partition logic as median, but left partition size = K        |
| Book Allocation                   | Minimize maximum pages → Binary Search on Answers                  |
| Aggressive Cows                   | Maximize minimum distance → Binary Search on Answers               |

---

## 🎯 Day 21 Takeaway

Today reinforced two of the most important Binary Search patterns:

### 1️⃣ Binary Search Partition

Used in:

* Median of Two Sorted Arrays
* K-th Element of Two Sorted Arrays

Instead of searching indices, we search for a valid partition that satisfies specific conditions.

### 2️⃣ Binary Search on Answers

Used in:

* Book Allocation
* Aggressive Cows

Whenever:

```text
Minimize the Maximum
```

or

```text
Maximize the Minimum
```

appears in a problem, Binary Search on Answers should immediately come to mind.

Recognizing these patterns is often more important than memorizing the code.

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #BinarySearch #TakeUForward #Striver #CompetitiveProgramming #SoftwareEngineering*
