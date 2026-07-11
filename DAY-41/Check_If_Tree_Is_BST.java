class Solution {
    public boolean isBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;

        if (node.data <= min || node.data >= max) return false;

        boolean leftIsValid  = validate(node.left,  min,       node.data);
        boolean rightIsValid = validate(node.right, node.data, max);

        return leftIsValid && rightIsValid;
    }
}
