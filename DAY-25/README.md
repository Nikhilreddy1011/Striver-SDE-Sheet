# 📘 DAY 25 -- Stacks (Part 2)

> **Topic:** Stack Data Structure, Monotonic Stack, Recursion on Stack\
> **Language:** Java\
> **Problems Solved:** 3

------------------------------------------------------------------------

# 📑 Table of Contents

1.  Introduction
2.  Learning Objectives
3.  Stack Concepts
4.  Balanced Parenthesis
5.  Next Greater Element
6.  Sort a Stack
7.  Complexity Comparison
8.  Interview Takeaways
9.  Day Summary

------------------------------------------------------------------------

# 🚀 Introduction

Day 25 focused on solving three classic stack problems that are among
the most frequently asked in coding interviews. Rather than implementing
the stack itself, these problems demonstrate **how the stack can be used
as a powerful problem-solving tool**.

The three problems covered completely different stack applications:

-   Validating nested expressions using LIFO.
-   Optimizing array problems using a Monotonic Stack.
-   Using recursion itself as an implicit stack to sort elements.

These concepts frequently appear in DSA interviews, compiler design,
expression evaluation, parsing, browser history, undo/redo systems,
recursion, and many advanced algorithms.

------------------------------------------------------------------------

# 🎯 Learning Objectives

After completing today's problems, I learned:

-   Why LIFO naturally solves matching-symbol problems.
-   How Monotonic Stacks reduce O(N²) solutions to O(N).
-   How recursion can replace an auxiliary stack.
-   Importance of edge-case handling before peek() and pop().
-   How every element in a monotonic stack is pushed and popped at most
    once.
-   Identifying stack patterns during interviews.

------------------------------------------------------------------------

# 📚 Stack Concepts Used Today

## LIFO Principle

Stack follows Last In First Out.

Operations: - Push - Pop - Peek / Top - isEmpty()

Applications: - Expression evaluation - Parenthesis matching - DFS -
Undo/Redo - Function call stack

------------------------------------------------------------------------

# ✅ Problem 1 -- Balanced Parenthesis

## Problem Statement

Given a string containing only (), {}, and \[\], determine whether every
opening bracket has the correct closing bracket in the correct order.

## Intuition

Every opening bracket waits for its corresponding closing bracket.

Since the **most recently opened bracket must close first**, Stack
perfectly models this behavior.

Whenever: - Opening bracket → Push. - Closing bracket → Compare with
stack top. - Mismatch → Invalid. - Match → Pop.

If the stack becomes empty after processing every character, the
expression is balanced.

## Brute Force Idea

Repeatedly remove: - () - {} - \[\]

until no replacement is possible.

If the string finally becomes empty → Valid.

### Time Complexity

O(N²)

### Space Complexity

O(N)

## Optimal Stack Approach

Algorithm

1.  Create an empty stack.
2.  Traverse every character.
3.  Push opening brackets.
4.  For closing brackets:
    -   Stack empty → False.
    -   Pop top.
    -   Compare brackets.
5.  Return stack.isEmpty().

## Dry Run

Input

()\[{}()\]

  Character   Stack   Action
  ----------- ------- --------
  (           (       Push
  )           Empty   Pop
  \[          \[      Push
  {           \[{     Push
  }           \[      Pop
  (           \[(     Push
  )           \[      Pop
  \]          Empty   Pop

Result → True

### Edge Cases

-   Empty string
-   Only opening brackets
-   Only closing brackets
-   Wrong nesting
-   Missing closing bracket

### Complexity

Time: O(N)

Space: O(N)

------------------------------------------------------------------------

# ✅ Problem 2 -- Next Greater Element

## Problem Statement

For every element, find the first greater element on its right.

If none exists return -1.

## Brute Force

For every index: - Traverse entire right side. - First larger element
becomes answer.

### Dry Run

Array

1 3 2 4

1 → 3

3 → 4

2 → 4

4 → -1

### Complexity

Time: O(N²)

Space: O(N)

------------------------------------------------------------------------

## Optimal Approach -- Monotonic Stack

### Key Observation

While traversing from right to left, every right-side element is already
processed.

Maintain a decreasing stack.

Remove all smaller elements because they can never become the answer.

### Algorithm

Traverse from right to left.

While stack top ≤ current: - Pop

If stack empty: - Answer = -1

Else: - Answer = Stack Top

Push current element.

## Dry Run

Input

1 3 2 4

Start from right.

4

Stack \[\]

Answer -1

Push 4

Stack

4

2

Top = 4

Answer = 4

Push 2

Stack

4

2

3

Pop 2

Top = 4

Answer = 4

Push 3

Stack

4

3

1

Top = 3

Answer = 3

Push 1

Final Answer

3 4 4 -1

### Why Monotonic Stack?

Every element enters stack once.

Every element leaves stack once.

Total operations = 2N.

Hence O(N).

### Complexity

Time: O(N)

Space: O(N)

------------------------------------------------------------------------

# ✅ Problem 3 -- Sort a Stack

## Problem Statement

Sort a stack using recursion only.

No loops.

No extra stack.

## Intuition

Instead of using another stack, recursion itself behaves like an
auxiliary stack.

Remove one element.

Sort remaining stack.

Insert removed element back into correct position.

## Recursive Functions

### sortStack()

Pop top.

Sort remaining stack.

Insert element back.

### insertSorted()

If: - Stack empty - Top ≤ current

Push current.

Else

Pop top.

Insert recursively.

Restore popped element.

## Dry Run

Initial Stack (Top)

2

3

1

4

Pop 2

Sort

3

1

4

Pop 3

Sort

1

4

Pop 1

Sort

4

Base Case

Insert 1

Insert 3

Insert 2

Final Stack

4

3

2

1

### Complexity

Time: O(N²)

Space: O(N)

------------------------------------------------------------------------

# 📊 Complexity Summary

  Problem                Brute Force   Optimal   Space
  ---------------------- ------------- --------- -------
  Balanced Parenthesis   O(N²)         O(N)      O(N)
  Next Greater Element   O(N²)         O(N)      O(N)
  Sort Stack             ---           O(N²)     O(N)

------------------------------------------------------------------------

# 💼 Interview Takeaways

-   Balanced Parenthesis is the standard application of Stack.
-   Next Greater Element introduces the Monotonic Stack pattern.
-   Traversing from right to left simplifies many stack problems.
-   Every monotonic stack problem guarantees each element is pushed and
    popped only once.
-   Recursive stack sorting demonstrates how recursion itself acts like
    an auxiliary stack.
-   Always check stack.isEmpty() before calling peek() or pop().

------------------------------------------------------------------------

# 🎯 Day 25 Summary

Today's session strengthened three major interview patterns:

-   Using Stack for symbol matching.
-   Using Monotonic Stack for array optimization.
-   Using Recursion for stack manipulation.

The biggest takeaway was understanding **when to use a stack** rather
than simply knowing stack operations. Recognizing these patterns makes
it easier to solve a wide range of interview problems efficiently.

------------------------------------------------------------------------

⭐ **Progress:** Day 25/45 -- Striver SDE Sheet Challenge
