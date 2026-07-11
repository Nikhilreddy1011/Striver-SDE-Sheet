class Solution {
    public TreeNode bstFromPreorder(int[] preorder) {
        return bstFromPreorderHelper(preorder, Integer.MAX_VALUE, new int[]{0});
    }

    private TreeNode bstFromPreorderHelper(int[] preorder, int bound, int[] index) {
        if (index[0] == preorder.length || preorder[index[0]] > bound) {
            return null;
        }

        TreeNode root = new TreeNode(preorder[index[0]++]);

        root.left  = bstFromPreorderHelper(preorder, root.data, index);
        root.right = bstFromPreorderHelper(preorder, bound, index);

        return root;
    }
}
