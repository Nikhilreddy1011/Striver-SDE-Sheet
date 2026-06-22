# 🚀 Day 22/45 – Heaps

## Problems Covered

1. Implement Max Heap
2. K-th Largest Element in an Array
3. Maximum Sum Combination

---

# Introduction to Heaps

A Heap is a Complete Binary Tree that follows a special ordering property.

### Max Heap

In a Max Heap:

```text
Parent >= Children
```

Example:

```text
        50
       /  \
      30   40
     / \
    10 20
```

The largest element is always present at the root.

---

## Why Array Representation?

A heap is stored using an array.

For index i:

```text
Parent = (i - 1) / 2

Left Child = 2*i + 1

Right Child = 2*i + 2
```

Example:

```text
Array = [50,30,40,10,20]

Index: 0 1 2 3 4
```

Tree:

```text
        50
       /  \
      30   40
     / \
    10 20
```

---

# 1. Implement Max Heap

## Problem Statement

Implement the following operations:

```text
insert(x)
getMax()
extractMax()
heapSize()
isEmpty()
changeKey(index,val)
initializeHeap()
```

---

## Core Concepts

### Heapify Up

Used when:

```text
Insertion
Increase Key
```

Move element upward until heap property is restored.

---

### Heapify Down

Used when:

```text
Extract Max
Decrease Key
```

Move element downward until heap property is restored.

---

## Insert Operation

### Idea

Insert at last position.

Heap property may break.

Perform Heapify Up.

---

### Dry Run

Insert:

```text
10
```

Heap:

```text
[10]
```

Insert:

```text
20
```

Heap:

```text
[10,20]
```

20 > 10

Swap

```text
[20,10]
```

---

### Complexity

Time Complexity:

```text
O(log N)
```

Space Complexity:

```text
O(1)
```

---

## Get Maximum

### Idea

Root always contains maximum value.

Return:

```text
arr[0]
```

### Complexity

Time Complexity:

```text
O(1)
```

Space Complexity:

```text
O(1)
```

---

## Extract Maximum

### Idea

Removing root directly causes shifting.

Instead:

1. Swap root with last element.
2. Remove last element.
3. Heapify Down.

---

### Dry Run

Heap:

```text
[50,30,40,10,20]
```

Swap:

```text
20,30,40,10,50
```

Delete last:

```text
20,30,40,10
```

Heapify Down:

```text
40,30,20,10
```

---

### Complexity

Time Complexity:

```text
O(log N)
```

Space Complexity:

```text
O(1)
```

---

## Change Key

### Idea

If new value is larger:

```text
Heapify Up
```

If new value is smaller:

```text
Heapify Down
```

---

### Example

Heap:

```text
40 30 20 10
```

Change:

```text
index=3

10 → 50
```

Heapify Up:

```text
50 40 20 30
```

---

### Complexity

Time Complexity:

```text
O(log N)
```

Space Complexity:

```text
O(1)
```

---

## Complete Complexity Analysis

| Operation   | Time    |
| ----------- | ------- |
| Insert      | O(logN) |
| Extract Max | O(logN) |
| Change Key  | O(logN) |
| Get Max     | O(1)    |
| Heap Size   | O(1)    |
| Is Empty    | O(1)    |

Overall Space:

```text
O(N)
```

---

## Key Learning

The two most important operations in a heap are:

```text
Heapify Up
Heapify Down
```

Every heap problem is built on top of these two concepts.

---

# 2. K-th Largest Element in an Array

## Problem Statement

Given an array nums and integer k.

Return the kth largest element.

---

### Example

Input:

```text
nums = [1,2,3,4,5]

k = 2
```

Sorted:

```text
5 4 3 2 1
```

Answer:

```text
4
```

---

# Approach 1: Sorting

## Idea

Sort array.

Return:

```text
nums[n-k]
```

---

### Dry Run

```text
1 2 3 4 5
```

Sort:

```text
1 2 3 4 5
```

k=2

Answer:

```text
4
```

---

### Complexity

Time Complexity:

```text
O(N log N)
```

Space Complexity:

```text
O(1)
```

---

# Approach 2: Min Heap

## Intuition

We only need the k largest elements.

Maintain a Min Heap of size k.

The smallest element inside the heap becomes the kth largest element.

---

### Algorithm

Insert first k elements.

For remaining elements:

```text
If current element > heap top

Remove top

Insert current element
```

---

### Dry Run

Array:

```text
1 2 3 4 5
```

k=2

Heap:

```text
1 2
```

Add 3:

```text
Remove 1

2 3
```

Add 4:

```text
Remove 2

3 4
```

Add 5:

```text
Remove 3

4 5
```

Top:

```text
4
```

Answer:

```text
4
```

---

### Complexity

Time Complexity:

```text
O(N log K)
```

Space Complexity:

```text
O(K)
```

---

# Approach 3: Quick Select

## Intuition

Quick Select is based on Quick Sort.

Instead of sorting entire array:

```text
Only search the side containing answer.
```

---

## Partition Concept

Choose Pivot.

Arrange:

```text
Greater Elements | Pivot | Smaller Elements
```

If pivot reaches position:

```text
k-1
```

Then pivot is kth largest element.

---

### Example

Array:

```text
7 2 10 4 6
```

Pivot:

```text
6
```

After Partition:

```text
10 7 | 6 | 4 2
```

Pivot Index:

```text
2
```

If:

```text
k=3
```

Answer:

```text
6
```

---

### Dry Run

Array:

```text
5 2 8 1 9
```

k=2

Partition around 8:

```text
9 | 8 | 5 2 1
```

Pivot index:

```text
1
```

k-1:

```text
1
```

Answer:

```text
8
```

---

### Complexity

Average Case:

```text
O(N)
```

Worst Case:

```text
O(N²)
```

Space Complexity:

```text
O(1)
```

---

## Comparison

| Approach     | TC       | SC   |
| ------------ | -------- | ---- |
| Sorting      | O(NlogN) | O(1) |
| Min Heap     | O(NlogK) | O(K) |
| Quick Select | O(N) Avg | O(1) |

---

## Key Learning

Quick Select is one of the most important interview algorithms.

Whenever you see:

```text
Kth Largest
Kth Smallest
Top K
Selection Problem
```

Think about:

```text
Quick Select
Heap
```

---

# 3. Maximum Sum Combination

## Problem Statement

Given:

```text
nums1
nums2
```

Find the maximum k pair sums.

Each pair consists of:

```text
One element from nums1
One element from nums2
```

---

### Example

Input:

```text
nums1 = [7,3]

nums2 = [1,6]

k=2
```

Possible sums:

```text
7+1 = 8

7+6 = 13

3+1 = 4

3+6 = 9
```

Sorted:

```text
13 9 8 4
```

Answer:

```text
[13,9]
```

---

# Brute Force Approach

## Idea

Generate every possible combination.

Store all sums.

Sort in descending order.

Take first k elements.

---

### Dry Run

nums1:

```text
7 3
```

nums2:

```text
1 6
```

Generated sums:

```text
8
13
4
9
```

Sort:

```text
13
9
8
4
```

Take first 2:

```text
13
9
```

---

### Complexity

Time Complexity:

```text
O(N² log N²)
```

Space Complexity:

```text
O(N²)
```

---

# Optimal Approach (Max Heap + Set)

## Intuition

Generating all sums is wasteful.

Largest sum always comes from:

```text
Largest element of nums1

+

Largest element of nums2
```

Sort arrays in descending order.

Use Max Heap to always process current largest sum.

---

## Why Set?

Without a Set:

```text
Same pair may be inserted multiple times.
```

Example:

```text
(i+1,j)

(i,j+1)
```

could reach same state.

Set avoids duplicates.

---

## Algorithm

### Step 1

Sort both arrays descending.

---

### Step 2

Insert:

```text
(nums1[0]+nums2[0],0,0)
```

into Max Heap.

---

### Step 3

Repeatedly:

```text
Extract maximum sum
```

Store answer.

---

### Step 4

Generate:

```text
(i+1,j)

(i,j+1)
```

Insert if not visited.

---

## Dry Run

nums1:

```text
7 3
```

nums2:

```text
6 1
```

Initial Heap:

```text
13
```

Result:

```text
13
```

Push:

```text
3+6 = 9

7+1 = 8
```

Heap:

```text
9
8
```

Take:

```text
9
```

Answer:

```text
13 9
```

---

## Complexity

Time Complexity:

```text
O(K log K)
```

Space Complexity:

```text
O(K)
```

---

## Key Learning

This problem introduced a powerful pattern:

```text
Max Heap + Visited Set
```

Used in:

* Top K Pair Sums
* K Closest Points
* Merge K Sorted Lists
* Best First Search Problems

---

# 🎯 Day 22 Takeaways

### Pattern 1: Heap Implementation

Important Concepts:

```text
Heapify Up
Heapify Down
Insert
Extract Max
```

---

### Pattern 2: Top K Elements

Problems:

```text
Kth Largest Element
Top K Frequent Elements
K Closest Points
```

Use:

```text
Min Heap
```

---

### Pattern 3: Selection Problems

Use:

```text
Quick Select
```

Examples:

```text
Kth Largest
Kth Smallest
Median
```

---

### Pattern 4: Maximum Combinations

Use:

```text
Max Heap + Set
```

---

# Final Learning

Day 22 was all about understanding how heaps work internally and where they can be applied.

Before today, heaps felt like a built-in data structure.

After implementing a Max Heap from scratch and solving heap-based problems, it became clear how:

* Heapify Up maintains order after insertion.
* Heapify Down restores order after deletion.
* Min Heaps help solve Top-K problems efficiently.
* Max Heaps help process the largest candidates first.

Understanding these fundamentals makes advanced heap problems much easier to approach.
