# 🚀 Day 18/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Permutations of a String](#1-permutations-of-a-string) | 🟡 Medium | Backtracking |
| 2 | [N Queens](#2-n-queens) | 🔴 Hard | Backtracking |
| 3 | [Sudoku Solver](#3-sudoku-solver) | 🔴 Hard | Backtracking |

---

## 1. Permutations of a String

### 🧩 Problem Statement

Given a string `s` (may contain duplicates), return all **unique permutations** in **lexicographic order**.

**Example 1:**
```
Input:  s = "abc"
Output: ["abc","acb","bac","bca","cab","cba"]
```

**Example 2:**
```
Input:  s = "aab"
Output: ["aab","aba","baa"]  (3 unique, not 6)
```

**Constraints:**
- `1 <= s.length <= 8`
- Lowercase letters only

---

### 🟢 Approach — Backtracking with `used[]` Array + Duplicate Skip
**Time: O(N × N!) | Space: O(N)**

#### 💡 Intuition
Sort the characters first so duplicates are adjacent. Use a `used[]` array to track which characters are currently in the path. At each step:
- If `used[i]` is true → skip (already in current permutation)
- If `arr[i] == arr[i-1]` and `!used[i-1]` → skip (duplicate at same level)

**Why the second condition prevents duplicates:**
If `arr[i] == arr[i-1]` and `used[i-1]` is false, it means we're starting a new branch with `arr[i]` while its identical sibling `arr[i-1]` hasn't been used. This would generate the same permutation as a branch that started with `arr[i-1]`. Skipping it avoids duplicates without a HashSet.

**Why `!used[i-1]` (not `used[i-1]`)?**
We only skip when the previous identical character is NOT in the current path. If it IS used, we're building a different path (e.g., second `a` after first `a` is already placed) — that's valid and should not be skipped.

#### 💻 Code
```java
class Solution {
    public List<String> permuteUnique(String s) {
        char[] arr = s.toCharArray();
        Arrays.sort(arr);

        List<String> result = new ArrayList<>();
        boolean[] used = new boolean[arr.length];
        backtrack(arr, used, new StringBuilder(), result);
        return result;
    }

    private void backtrack(char[] arr, boolean[] used,
                           StringBuilder path, List<String> result) {
        if (path.length() == arr.length) {
            result.add(path.toString());
            return;
        }

        for (int i = 0; i < arr.length; i++) {
            if (used[i]) continue;
            if (i > 0 && arr[i] == arr[i - 1] && !used[i - 1]) continue;

            used[i] = true;
            path.append(arr[i]);
            backtrack(arr, used, path, result);
            path.deleteCharAt(path.length() - 1);
            used[i] = false;
        }
    }
}
```

#### 🧪 Dry Run

Input: `s = "aab"` → sorted: `['a','a','b']`

```
backtrack(path="", used=[F,F,F]):

  i=0 (arr[0]='a'): not used, i=0 so no duplicate check
    used=[T,F,F], path="a"
    backtrack(path="a"):
      i=0: used[0]=T → skip
      i=1 (arr[1]='a'): arr[1]==arr[0] && !used[0]? No (used[0]=T) → proceed
        used=[T,T,F], path="aa"
        backtrack(path="aa"):
          i=0,1: used → skip
          i=2 ('b'): not used
            path="aab" → ADD "aab" ✅
          backtrack
        used=[T,F,F], path="a"
      i=2 ('b'): not used
        used=[T,F,T], path="ab"
        backtrack(path="ab"):
          i=0: used → skip
          i=1 ('a'): not used
            path="aba" → ADD "aba" ✅
          i=2: used → skip
        used=[T,F,F], path="a"
    used=[F,F,F], path=""

  i=1 (arr[1]='a'): arr[1]==arr[0] && !used[0]? Yes (used[0]=F) → SKIP ✅

  i=2 (arr[2]='b'): no duplicate issue
    used=[F,F,T], path="b"
    backtrack(path="b"):
      i=0 ('a'): not used
        path="ba"
        backtrack:
          i=1 ('a'): not used (used[0]=T, arr[1]==arr[0] but used[0]=T → not skipped)
            path="baa" → ADD "baa" ✅

Final result: ["aab", "aba", "baa"] ✅ (lexicographic order due to initial sort)
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N × N!) worst case | O(N) stack + used array |

---

## 2. N Queens

### 🧩 Problem Statement

Place `n` queens on an `n × n` chessboard such that **no two queens attack each other**. Return all valid board configurations. Queens attack along rows, columns, and both diagonals.

**Example 1:**
```
Input:  n=4
Output: [[".Q..","...Q","Q...","..Q."],
         ["..Q.","Q...","...Q",".Q.."]]
```

**Example 2:**
```
Input:  n=2
Output: [[]]  (no solution)
```

**Constraints:** `1 <= n <= 9`

---

### 🟢 Approach — Row-by-Row Backtracking
**Time: O(N!) | Space: O(N)**

#### 💡 Intuition
Since no two queens can share a row, place **exactly one queen per row**. For each row, try every column. Check if the placement is safe by scanning **upward only** — queens already placed in previous rows are the only threat to the current row.

**Three checks for safety (all upward):**
- **Same column** (straight up)
- **Upper-left diagonal**
- **Upper-right diagonal**

No need to check downward — future rows haven't been filled yet.

#### 📝 Steps
1. Initialize board as `n` rows of `"..."` (n dots)
2. For each row (starting from 0):
   - Try each column
   - If `isPlaced(board, row, col)` → place queen, recurse on `row+1`
   - If recursion returns → remove queen (backtrack), try next column
3. When `row == n`, all queens placed — add board snapshot to results

#### 💻 Code
```java
class Solution {
    private boolean isPlaced(List<String> board, int row, int col) {
        int r = row, c = col;

        // Upper-left diagonal
        while (r >= 0 && c >= 0) {
            if (board.get(r).charAt(c) == 'Q') return false;
            r--; c--;
        }

        // Straight up (same column)
        r = row; c = col;
        while (r >= 0) {
            if (board.get(r).charAt(c) == 'Q') return false;
            r--;
        }

        // Upper-right diagonal
        r = row; c = col;
        while (r >= 0 && c < board.size()) {
            if (board.get(r).charAt(c) == 'Q') return false;
            r--; c++;
        }

        return true;
    }

    private void func(int row, List<List<String>> ans, List<String> board) {
        if (row == board.size()) { ans.add(new ArrayList<>(board)); return; }

        for (int col = 0; col < board.size(); col++) {
            if (isPlaced(board, row, col)) {
                char[] rowArr = board.get(row).toCharArray();
                rowArr[col] = 'Q';
                board.set(row, new String(rowArr));

                func(row + 1, ans, board);

                rowArr[col] = '.';
                board.set(row, new String(rowArr));
            }
        }
    }

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        List<String> board = new ArrayList<>();
        for (int i = 0; i < n; i++) board.add(".".repeat(n));
        func(0, ans, board);
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `n=4`

```
Board initially:
....
....
....
....

func(row=0):
  col=0: isPlaced? upper-left=empty, up=empty, upper-right=empty → YES
    board[0] = "Q..."
    func(row=1):
      col=0: up col=0 has Q → NO
      col=1: upper-left (0,0) has Q → NO
      col=2: isPlaced? upper-right from (1,2): check (0,3)='.'; up: (0,2)='.'; upper-left: (0,1)='.' → YES
        board[1] = "..Q."
        func(row=2):
          col=0: up col=0 has Q → NO
          col=1: upper-right (1,2) has Q → wait, upper-right from (2,1) goes (1,2)=Q → NO
          col=2: up col=2 has Q (row 1) → NO
          col=3: upper-left (2,2),(1,1),(0,0)=Q → NO
          No valid column → backtrack ❌
        board[1] = "...."
      col=3: upper-right from (1,3): (0,4) out of bounds; up (0,3)='.'; upper-left (0,2)='.' → YES
        board[1] = "...Q"
        func(row=2):
          col=0: up col=0 has Q (row 0) → NO
          col=1: upper-left (1,0)='.', (0,? no). upper-right (1,2)='.', (0,3)=Q → NO
          col=2: upper-left (1,1)='.', (0,0)=Q → NO
          col=3: up col=3 has Q (row 1) → NO
          No valid → backtrack ❌
    board[0] = "...."
  col=1: isPlaced? → YES
    board[0] = ".Q.."
    func(row=1):
      col=3: isPlaced? YES
        board[1] = "...Q"
        func(row=2):
          col=0: isPlaced? YES
            board[2] = "Q..."
            func(row=3):
              col=2: isPlaced? YES
                board[3] = "..Q."
                func(row=4): row==4 → ADD SOLUTION ✅

Solution 1: [".Q..","...Q","Q...","..Q."] ✅
```

> 💡 **Why only check upward in `isPlaced`?**
> We place queens row by row from top to bottom. When checking if row `r` is safe, all rows below `r` are empty — no queens there yet. So threats can only come from rows 0 to r-1, i.e., upward. Checking downward is unnecessary.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N!) | O(N) stack + board |

---

## 3. Sudoku Solver

### 🧩 Problem Statement

Fill empty cells (`'.'`) in a 9×9 Sudoku board so that every row, column, and 3×3 sub-box contains digits `1-9` exactly once. Exactly one solution is guaranteed.

**Example (partial):**
```
Input:  5 3 . . 7 . . . .
        6 . . 1 9 5 . . .
        ...
Output: Fully solved board
```

**Constraints:**
- 9×9 board
- Exactly one valid solution exists

---

### 🟢 Approach — Recursive Backtracking
**Time: O(9^E) where E = empty cells ≤ 81 | Space: O(E)**

#### 💡 Intuition
Find the first empty cell. Try placing digits `'1'` through `'9'`. For each:
- Check if it's **valid** (not already in same row, column, or 3×3 box)
- If valid → place it and recursively solve the rest
- If recursion returns `true` → done!
- If recursion returns `false` → remove digit (backtrack), try next digit
- If no digit works → return `false` (signal failure to parent)

When no empty cell is found, the board is completely solved → return `true`.

**The 3×3 box check trick:**
For cell `(row, col)`, the 3×3 box index is:
```
r = 3*(row/3) + i/3
c = 3*(col/3) + i%3
```
This elegantly maps `i` from 0 to 8 to all 9 cells of the box using a single loop variable.

#### 💻 Code
```java
class Solution {
    public void solveSudoku(char[][] board) { solve(board); }

    private boolean solve(char[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == '.') {
                    for (char digit = '1'; digit <= '9'; digit++) {
                        if (isValid(board, row, col, digit)) {
                            board[row][col] = digit;
                            if (solve(board)) return true;
                            board[row][col] = '.'; // backtrack
                        }
                    }
                    return false; // no digit worked — dead end
                }
            }
        }
        return true; // no empty cell — solved!
    }

    private boolean isValid(char[][] board, int row, int col, char digit) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == digit) return false; // row check
            if (board[i][col] == digit) return false; // col check

            int r = 3 * (row / 3) + i / 3; // box check
            int c = 3 * (col / 3) + i % 3;
            if (board[r][c] == digit) return false;
        }
        return true;
    }
}
```

#### 🧪 Dry Run (Simplified)

Let's trace how a simple 3-cell section works:

```
board[0][2] = '.'  (first empty cell found)

Try '1': isValid? Check row 0, col 2, box (0,0) to (2,2)
  → Suppose '1' conflicts in row → skip
Try '2': isValid? Conflicts in box → skip
Try '3': isValid? No conflicts → place '3'
  board[0][2] = '3'
  solve(board) recursively tries next empty cell...

If recursion returns false (dead end downstream):
  board[0][2] = '.' (backtrack)
  Try '4', '5', ... until a digit works or all fail

If all '1'-'9' fail → return false (parent must backtrack its last placement)
```

**Box index formula trace for cell (row=4, col=4):**
```
Box top-left: (3*(4/3), 3*(4/3)) = (3,3)
i=0: r=3+0=3, c=3+0=3  → board[3][3]
i=1: r=3+0=3, c=3+1=4  → board[3][4]
i=2: r=3+0=3, c=3+2=5  → board[3][5]
i=3: r=3+1=4, c=3+0=3  → board[4][3]
...
i=8: r=3+2=5, c=3+2=5  → board[5][5]

Covers entire (3..5, 3..5) box ✅
```

> 💡 **Why `return false` immediately when no digit works?**
> Returning `false` signals to the parent call that the current board state has no solution. The parent then backtracks its own last placement and tries the next digit. Without this return, the algorithm would continue forward with an unsolvable board, wasting exponential time.

> 💡 **How is the box formula `3*(row/3) + i/3` derived?**
> `row/3` gives which "box row" (0, 1, or 2). Multiply by 3 to get the starting row of that box. `i/3` gives the offset within the box (0, 1, or 2 rows down). Together they traverse all 3 rows of the box. Same logic applies for columns with `col/3` and `i%3`.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(9^E), E = empty cells | O(E) recursion stack |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Permutations of String | Sort + `used[]` array. Skip duplicate at same level with `arr[i]==arr[i-1] && !used[i-1]`. Results come out in lexicographic order automatically |
| N Queens | One queen per row. Only check upward (3 directions) since lower rows are empty. Backtrack when no column is safe |
| Sudoku Solver | Try digits 1-9 per empty cell. Return true immediately on success. Return false when all digits fail (triggers parent backtrack). Box formula: `3*(row/3)+i/3`, `3*(col/3)+i%3` |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
