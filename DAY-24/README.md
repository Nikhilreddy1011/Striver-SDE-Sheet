# 🚀 Day 24/45 – #SDESheetChallenge

Today focused on understanding the implementation of fundamental data structures from scratch instead of relying on Java's built-in Stack and Queue classes.

The primary objective was to understand:

* Internal working of Stack
* Internal working of Queue
* Circular Queue implementation
* Implementing Stack using Queue
* Implementing Queue using Stack
* Handling overflow and underflow conditions
* Understanding FIFO and LIFO behaviors

---

# 📋 Problems Covered Today

| # | Problem                      | Difficulty | Topic         |
| - | ---------------------------- | ---------- | ------------- |
| 1 | Implement Stack using Arrays | 🟢 Easy    | Stack         |
| 2 | Implement Queue using Arrays | 🟢 Easy    | Queue         |
| 3 | Implement Stack using Queue  | 🟢 Easy    | Stack & Queue |
| 4 | Implement Queue using Stack  | 🟢 Easy    | Stack & Queue |

---

# 1. Implement Stack using Arrays

## 🧩 Problem Statement

Implement a Stack using an Array supporting the following operations:

* push(x)
* pop()
* top()
* isEmpty()

The Stack should follow the Last In First Out (LIFO) principle.

---

## 🔴 Intuition

A Stack behaves exactly like a stack of plates.

Whenever a new plate is placed, it goes on top.

Whenever a plate is removed, the topmost plate gets removed first.

The last inserted element becomes the first element removed.

To achieve this behavior efficiently:

* Use an array for storage
* Maintain a variable called top

Initially:

```java
top = -1
```

which indicates that the stack is empty.

---

## 📝 Approach

### Push Operation

Increase top by one and insert the element.

```java
top++;
arr[top] = value;
```

---

### Pop Operation

Return the element present at top and decrease top.

```java
return arr[top--];
```

---

### Top Operation

Simply return the current top element.

```java
return arr[top];
```

---

### IsEmpty Operation

```java
return top == -1;
```

---

## 🧪 Dry Run

### Push(5)

```text
[5]
top = 0
```

### Push(10)

```text
[5,10]
top = 1
```

### Top()

Returns:

```text
10
```

### Pop()

Returns:

```text
10
```

Stack becomes:

```text
[5]
top = 0
```

---

## ⚠️ Edge Cases

### Stack Overflow

Occurs when:

```java
top >= capacity - 1
```

No more elements can be inserted.

---

### Stack Underflow

Occurs when:

```java
top == -1
```

Trying to pop from an empty stack.

---

## 📊 Complexity Analysis

| Operation | Time |
| --------- | ---- |
| Push      | O(1) |
| Pop       | O(1) |
| Top       | O(1) |
| IsEmpty   | O(1) |

### Space Complexity

```text
O(N)
```

---

# 2. Implement Queue using Arrays

## 🧩 Problem Statement

Implement a Queue supporting:

* push()
* pop()
* peek()
* isEmpty()

Queue should follow First In First Out (FIFO).

---

## 🔴 Intuition

Think of people standing in a ticket counter queue.

The first person entering the queue leaves first.

To efficiently support insertions and deletions, we use:

* start pointer
* end pointer
* current size
* circular indexing

---

## Why Circular Queue?

Without circular indexing:

```text
[5,10,20]
```

After popping two elements:

```text
[_,_,20]
```

Space gets wasted.

Circular Queue reuses the free positions.

---

## 📝 Approach

### Push

Insert at rear.

```java
end = (end + 1) % maxSize;
```

---

### Pop

Remove from front.

```java
start = (start + 1) % maxSize;
```

---

### Peek

Return:

```java
arr[start]
```

---

## 🧪 Dry Run

Push(5)

```text
[5]
start=0
end=0
```

Push(10)

```text
[5,10]
start=0
end=1
```

Peek()

```text
5
```

Pop()

```text
5
```

Queue becomes:

```text
[10]
```

---

## 📊 Complexity Analysis

| Operation | Time |
| --------- | ---- |
| Push      | O(1) |
| Pop       | O(1) |
| Peek      | O(1) |
| IsEmpty   | O(1) |

Space:

```text
O(N)
```

---

# 3. Implement Stack using Queue

## 🧩 Problem Statement

Implement Stack operations using only one Queue.

---

## 🔴 Intuition

Queue follows FIFO.

Stack follows LIFO.

Their behavior is opposite.

The trick is to always keep the newest element at the front of the queue.

---

## 📝 Approach

Whenever a new element is inserted:

1. Insert it normally.
2. Rotate all previous elements behind it.

Example:

Push(4)

```text
[4]
```

Push(8)

```text
[4,8]
```

Rotate:

```text
[8,4]
```

Now front acts as stack top.

---

## 🧪 Dry Run

Push(4)

```text
[4]
```

Push(8)

```text
[8,4]
```

Pop()

Returns:

```text
8
```

Remaining:

```text
[4]
```

---

## 📊 Complexity Analysis

| Operation | Time |
| --------- | ---- |
| Push      | O(N) |
| Pop       | O(1) |
| Top       | O(1) |
| IsEmpty   | O(1) |

Space:

```text
O(N)
```

---

# 4. Implement Queue using Two Stacks

## 🧩 Problem Statement

Implement Queue operations using Stacks.

---

# 🔴 Approach 1 – Push Costly

## Intuition

Use two stacks.

Whenever a new element is pushed:

* Move everything to stack2
* Insert new element
* Move everything back

This maintains queue order.

---

## Complexity

Push:

```text
O(N)
```

Pop:

```text
O(1)
```

---

# 🟢 Approach 2 – Optimal

## Intuition

Use:

```java
input stack
output stack
```

Push directly into input.

Whenever output becomes empty:

Move all elements from input to output.

This reversal automatically preserves FIFO order.

---

## Dry Run

Push(1)

```text
input=[1]
```

Push(2)

```text
input=[1,2]
```

Push(3)

```text
input=[1,2,3]
```

Pop()

Transfer:

```text
output=[3,2,1]
```

Pop:

```text
1
```

Queue behavior achieved.

---

## 📊 Complexity Analysis

| Operation | Time           |
| --------- | -------------- |
| Push      | O(1)           |
| Pop       | O(1) Amortized |
| Peek      | O(1) Amortized |
| IsEmpty   | O(1)           |

Space:

```text
O(N)
```

---

# 📌 Key Learnings

### Stack

* Follows LIFO
* Uses top pointer
* Push and Pop happen at same end

### Queue

* Follows FIFO
* Uses front and rear pointers
* Circular Queue avoids memory wastage

### Stack using Queue

* Queue rotation technique
* Newest element kept in front

### Queue using Stacks

* Double reversal concept
* Amortized O(1) operations

---

🔗 Connect
Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering
