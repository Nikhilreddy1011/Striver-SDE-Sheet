class Solution {
    private TreeNode prev = null;
    private TreeNode head = null;

    private void inorder(TreeNode node) {
        if (node == null) return;

        inorder(node.left);

        if (prev == null) {
            head = node;
        } else {
            prev.right = node;
            node.left = prev;
        }

        prev = node;

        inorder(node.right);
    }

    public TreeNode bToDLL(TreeNode root) {
        prev = null;
        head = null;
        inorder(root);
        return head;
    }
}
