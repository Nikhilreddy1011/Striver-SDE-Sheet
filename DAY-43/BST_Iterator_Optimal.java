class BSTIterator {
    private Stack<TreeNode> stack = new Stack<>();

    public BSTIterator(TreeNode root) { pushAll(root); }
    public boolean hasNext() { return !stack.isEmpty(); }
    public int next() {
        TreeNode temp = stack.pop();
        pushAll(temp.right);
        return temp.data;
    }
    private void pushAll(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }
}
