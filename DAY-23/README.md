# Day 23 – Heaps

## Overview

Day 23 focused on solving advanced Heap-based problems from Striver's SDE Sheet. The objective was to understand how Priority Queues (Heaps) can be used to efficiently solve problems involving frequencies, sorted streams, and dynamic data processing.

The major takeaway from today's session was learning different Heap patterns:

* Min Heap for maintaining Top K elements.
* K-Way Merge using Priority Queue.
* Two Heap approach for maintaining median in a data stream.

---

# Problems Covered

### 1. Top K Frequent Elements

**Difficulty:** Medium

### 2. Merge K Sorted Arrays

**Difficulty:** Medium

### 3. Find Median from Data Stream

**Difficulty:** Hard

---

# 1. Top K Frequent Elements

## Problem Statement

Given an integer array `nums` and an integer `k`, return the `k` most frequent elements present in the array.

### Example

```java
Input:
nums = [1,1,1,2,2,3]
k = 2

Output:
[1,2]
```

---

## Intuition

The most straightforward approach is to count the frequency of each element and then sort all elements based on their frequencies.

However, sorting all frequencies is unnecessary because we only need the top `k` frequent elements.

A Min Heap of size `k` helps us efficiently maintain the `k` most frequent elements seen so far.

---

# Brute Force Approach

## Idea

1. Count frequencies using HashMap.
2. Store all entries inside a list.
3. Sort the list according to frequencies in descending order.
4. Pick the first `k` elements.

---

## Dry Run

```text
nums = [1,1,1,2,2,3]

Frequency Map:

1 -> 3
2 -> 2
3 -> 1

After Sorting:

(1,3)
(2,2)
(3,1)

First 2 elements:

[1,2]
```

---

## Complexity Analysis

### Time Complexity

```text
O(N log N)
```

### Space Complexity

```text
O(N)
```

---

# Optimal Approach

## Idea

Instead of sorting all frequencies:

* Maintain a Min Heap of size `k`.
* The heap stores elements according to frequency.
* Whenever heap size exceeds `k`, remove the element having minimum frequency.

At the end, the heap contains the top `k` frequent elements.

---

## Algorithm

1. Build frequency map.
2. Create Min Heap based on frequencies.
3. Insert every frequency pair.
4. If heap size exceeds `k`, remove smallest frequency.
5. Extract remaining elements.

---

## Dry Run

```text
Frequency:

1 -> 3
2 -> 2
3 -> 1

Heap:

Insert (1,3)

[(1,3)]

Insert (2,2)

[(2,2),(1,3)]

Insert (3,1)

[(3,1),(1,3),(2,2)]

Size > 2

Remove smallest

[(2,2),(1,3)]

Answer:

[2,1]
```

---

## Complexity Analysis

### Time Complexity

```text
O(N log K)
```

### Space Complexity

```text
O(N)
```

---

# Key Learning

When only Top K elements are required, maintaining a Min Heap of size K is generally more efficient than sorting all elements.

---

# 2. Merge K Sorted Arrays

## Problem Statement

Given `k` sorted arrays, each of size `k`, merge all arrays into a single sorted array.

### Example

```java
Input:

[
 [1,2,3],
 [4,5,6],
 [7,8,9]
]

Output:

[1,2,3,4,5,6,7,8,9]
```

---

## Intuition

Every individual array is already sorted.

Instead of collecting all elements and sorting them again, we can use a Min Heap to always retrieve the smallest available element among all arrays.

This pattern is called **K-Way Merge**.

---

# Brute Force Approach

## Idea

1. Traverse every array.
2. Store all elements into a single list.
3. Sort the list.
4. Return sorted list.

---

## Dry Run

```text
Arrays:

[1,2,3]
[4,5,6]
[7,8,9]

Combined:

[1,2,3,4,5,6,7,8,9]

Sort

Result:

[1,2,3,4,5,6,7,8,9]
```

---

## Complexity Analysis

### Time Complexity

```text
O(K² log(K²))
```

### Space Complexity

```text
O(K²)
```

---

# Optimal Approach

## Idea

Maintain a Min Heap storing:

```java
[value,row,column]
```

Initially insert the first element of every array.

Whenever an element is removed:

* Add it to answer.
* Insert the next element from the same array.

This guarantees that the smallest available element is always processed first.

---

## Algorithm

1. Insert first element from each array.
2. Remove minimum element.
3. Add it to result.
4. Insert next element from the same row.
5. Continue until heap becomes empty.

---

## Dry Run

```text
Arrays:

[1,5,9]
[2,6,10]
[3,7,11]

Heap:

1
2
3

Remove 1

Answer:

[1]

Insert 5

Heap:

2
3
5

Remove 2

Answer:

[1,2]

Insert 6

Heap:

3
5
6

Continue...

Final:

[1,2,3,5,6,7,9,10,11]
```

---

## Complexity Analysis

### Time Complexity

```text
O(K² log K)
```

### Space Complexity

```text
O(K)
```

---

# Key Learning

The K-Way Merge pattern appears frequently in:

* Merge K Sorted Lists
* External Sorting
* Database Merge Operations
* Streaming Data Processing

---

# 3. Find Median from Data Stream

## Problem Statement

Design a data structure supporting:

```java
addNum(num)
findMedian()
```

such that median can be returned efficiently at any point.

---

### Example

```java
addNum(1)
addNum(2)

Median = 1.5

addNum(3)

Median = 2
```

---

## Intuition

If we sort the entire list every time a median is requested, the solution becomes inefficient.

Instead:

* Keep smaller half of numbers in a Max Heap.
* Keep larger half of numbers in a Min Heap.

This ensures median is always available at the top of heaps.

---

# Brute Force Approach

## Idea

1. Store all elements.
2. Sort whenever median is requested.
3. Return middle value(s).

---

## Dry Run

```text
Insert:

1
2
3

Sorted:

[1,2,3]

Median:

2
```

---

## Complexity Analysis

### Time Complexity

```text
addNum -> O(1)

findMedian -> O(N log N)
```

### Space Complexity

```text
O(N)
```

---

# Optimal Approach

## Idea

Maintain:

### Max Heap

Stores smaller half

```text
[1,2,3]
```

Top = largest element in smaller half

---

### Min Heap

Stores larger half

```text
[6,7,8]
```

Top = smallest element in larger half

---

## Balancing Rule

```text
|maxHeap size - minHeap size| <= 1
```

Max Heap can contain at most one extra element.

---

## Algorithm

### addNum()

* Insert into appropriate heap.
* Rebalance if required.

### findMedian()

If both heaps have same size:

```java
(max.peek() + min.peek()) / 2
```

Otherwise:

```java
max.peek()
```

---

## Dry Run

### Insert 1

```text
Max Heap:

[1]

Median = 1
```

---

### Insert 6

```text
Max Heap:

[1]

Min Heap:

[6]

Median:

(1+6)/2

= 3.5
```

---

### Insert 3

```text
Max Heap:

[3,1]

Min Heap:

[6]

Median:

3
```

---

## Complexity Analysis

### Time Complexity

```text
addNum -> O(log N)

findMedian -> O(1)
```

### Space Complexity

```text
O(N)
```

---

# Key Learning

The Two Heap pattern is one of the most important Heap applications and is frequently asked in coding interviews.

Applications include:

* Real-time analytics
* Streaming data systems
* Stock market analysis
* Running median calculations

---

# Day 23 Summary

| Problem                      | Brute Force         | Optimal Pattern                 |
| ---------------------------- | ------------------- | ------------------------------- |
| Top K Frequent Elements      | Sorting Frequencies | Min Heap + HashMap              |
| Merge K Sorted Arrays        | Merge + Sort        | K-Way Merge using Min Heap      |
| Find Median from Data Stream | Sort Every Query    | Two Heaps (Max Heap + Min Heap) |

---

# Concepts Learned

### Heap Patterns Covered

✅ Min Heap

✅ Max Heap

✅ Priority Queue

✅ K-Way Merge

✅ Top K Elements

✅ Frequency Based Problems

✅ Streaming Median

✅ Two Heap Technique

---

# Day 23 Takeaway

Day 23 demonstrated how versatile Heaps are beyond simple priority-based operations.

The same Priority Queue concept was applied in three completely different scenarios:

* Maintaining Top K frequent elements.
* Merging multiple sorted arrays efficiently.
* Finding median dynamically in a continuously growing data stream.

Understanding these patterns significantly strengthens problem-solving skills for coding interviews and advanced data structure questions. 🚀
