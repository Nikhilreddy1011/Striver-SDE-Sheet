class BSTIterator {
    private List<Integer> values;
    private int index;

    public BSTIterator(TreeNode root) {
        values = new ArrayList<>();
        inorderTraversal(root);
        index = -1;
    }
    public boolean hasNext() { return index + 1 < values.size(); }
    public int next()        { return values.get(++index); }
    private void inorderTraversal(TreeNode node) {
        if (node == null) return;
        inorderTraversal(node.left);
        values.add(node.data);
        inorderTraversal(node.right);
    }
}
