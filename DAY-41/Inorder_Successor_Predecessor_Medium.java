class Solution {

    List<Integer> succPredBST(TreeNode root, int key) {
        int[] result = {-1, -1};
        TreeNode[] prev = {null};

        inorderTraversal(root, key, result, prev);

        return Arrays.asList(result[0], result[1]);
    }

    private void inorderTraversal(TreeNode node, int key, int[] res, TreeNode[] prev) {
        if (node == null) return;

        inorderTraversal(node.left, key, res, prev);

        if (prev[0] != null && prev[0].data < key) {
            res[0] = prev[0].data;
        }

        if (res[1] == -1 && node.data > key) {
            res[1] = node.data;
        }

        prev[0] = node;

        inorderTraversal(node.right, key, res, prev);
    }
}
