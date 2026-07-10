class Solution {
    private TreeNode prev = null;

    private void flattenHelper(TreeNode root) {
        if (root == null) return;

        flattenHelper(root.right);
        flattenHelper(root.left);

        root.right = prev;
        root.left = null;
        prev = root;
    }

    public void flatten(TreeNode root) {
        prev = null;
        flattenHelper(root);
    }
}
