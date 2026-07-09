class Solution {

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder.length != postorder.length)
            return null;

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }

        return buildTreePostIn(inorder, 0, inorder.length - 1,
                               postorder, 0, postorder.length - 1, map);
    }

    public TreeNode buildTreePostIn(int[] inorder, int is, int ie,
                                    int[] postorder, int ps, int pe,
                                    Map<Integer, Integer> map) {

        if (ps > pe || is > ie)
            return null;

        TreeNode root = new TreeNode(postorder[pe]);

        int inRoot = map.get(root.data);
        int leftSize = inRoot - is;

        root.left = buildTreePostIn(inorder, is, inRoot - 1,
                                    postorder, ps, ps + leftSize - 1, map);

        root.right = buildTreePostIn(inorder, inRoot + 1, ie,
                                     postorder, ps + leftSize, pe - 1, map);

        return root;
    }
}
