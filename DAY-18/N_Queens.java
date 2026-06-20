// Approach: Backtracking row by row
// Time: O(N!) | Space: O(N)
// Place one queen per row. For each column, check if placement is safe by
// scanning upward (same column), upper-left diagonal, upper-right diagonal.
// If safe, place queen and recurse on next row. Backtrack if no valid column found.

import java.util.*;

class Solution {
    // Check if placing queen at (row, col) is safe
    private boolean isPlaced(List<String> board, int row, int col) {
        int r = row, c = col;

        // Check upper-left diagonal
        while (r >= 0 && c >= 0) {
            if (board.get(r).charAt(c) == 'Q') return false;
            r--; c--;
        }

        // Check directly upward (same column)
        r = row; c = col;
        while (r >= 0) {
            if (board.get(r).charAt(c) == 'Q') return false;
            r--;
        }

        // Check upper-right diagonal
        r = row; c = col;
        while (r >= 0 && c < board.size()) {
            if (board.get(r).charAt(c) == 'Q') return false;
            r--; c++;
        }

        return true; // safe to place
    }

    private void func(int row, List<List<String>> ans, List<String> board) {
        if (row == board.size()) {
            ans.add(new ArrayList<>(board)); // all rows filled — valid solution
            return;
        }

        for (int col = 0; col < board.size(); col++) {
            if (isPlaced(board, row, col)) {
                // Place queen
                char[] rowArr = board.get(row).toCharArray();
                rowArr[col] = 'Q';
                board.set(row, new String(rowArr));

                func(row + 1, ans, board); // recurse on next row

                // Remove queen (backtrack)
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
