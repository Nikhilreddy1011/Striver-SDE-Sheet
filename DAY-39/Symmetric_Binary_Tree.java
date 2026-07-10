class Solution {
    public boolean isSymmetric(TreeNode root) {
        return root == null || isSame(root.left, root.right);
    }

    private boolean isSame(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        if (left.data != right.data) return false;
        return isSame(left.left, right.right) && isSame(left.right, right.left);
    }
}
