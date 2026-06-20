// Approach: Backtracking - try each digit 1-9 for each empty cell
// Time: O(9^E) where E = number of empty cells (at most 81) | Space: O(E) stack
// Find first empty cell, try digits 1-9. If valid per Sudoku rules, place it
// and recurse. If recursion returns true, solution found. If false (dead end),
// reset cell and try next digit. If no digit works, return false (backtrack).

class Solution {
    public void solveSudoku(char[][] board) {
        solve(board);
    }

    private boolean solve(char[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == '.') { // found empty cell
                    for (char digit = '1'; digit <= '9'; digit++) {
                        if (isValid(board, row, col, digit)) {
                            board[row][col] = digit; // place digit

                            if (solve(board)) return true; // solved!

                            board[row][col] = '.'; // backtrack
                        }
                    }
                    return false; // no digit worked — dead end, trigger backtrack
                }
            }
        }
        return true; // no empty cell found — board complete
    }

    // Check if digit can be placed at (row, col) per Sudoku rules
    private boolean isValid(char[][] board, int row, int col, char digit) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == digit) return false; // same row
            if (board[i][col] == digit) return false; // same column

            // Check 3x3 sub-box
            int r = 3 * (row / 3) + i / 3;
            int c = 3 * (col / 3) + i % 3;
            if (board[r][c] == digit) return false;
        }
        return true;
    }
}
