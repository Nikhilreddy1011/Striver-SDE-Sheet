class Solution {
    public boolean checkChildrenSum(TreeNode root) {
        if (root == null) return true;
        if (root.left == null && root.right == null) return true;

        int leftVal = (root.left != null) ? root.left.val : 0;
        int rightVal = (root.right != null) ? root.right.val : 0;

        return (root.val == leftVal + rightVal)
                && checkChildrenSum(root.left)
                && checkChildrenSum(root.right);
    }
}
