class Solution {
    public TreeNode bstFromPreorder(int[] preorder) {
        List<Integer> inorder = new ArrayList<>();
        for (int val : preorder) inorder.add(val);
        Collections.sort(inorder);

        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < inorder.size(); i++) inMap.put(inorder.get(i), i);

        return buildTree(preorder, inMap, 0, preorder.length - 1, 0, inorder.size() - 1);
    }

    private TreeNode buildTree(int[] preorder, Map<Integer, Integer> inMap,
                               int preStart, int preEnd, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) return null;

        TreeNode root = new TreeNode(preorder[preStart]);
        int inRoot = inMap.get(root.data);
        int leftSize = inRoot - inStart;

        root.left  = buildTree(preorder, inMap, preStart + 1, preStart + leftSize, inStart, inRoot - 1);
        root.right = buildTree(preorder, inMap, preStart + leftSize + 1, preEnd, inRoot + 1, inEnd);

        return root;
    }
}
