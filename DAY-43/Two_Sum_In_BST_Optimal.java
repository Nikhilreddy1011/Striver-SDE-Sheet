class BSTIterator {
    private Stack<TreeNode> stack;
    private boolean reverse;

    public BSTIterator(TreeNode root, boolean isReverse) {
        stack = new Stack<>();
        reverse = isReverse;
        pushAll(root);
    }
    private void pushAll(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = reverse ? node.right : node.left;
        }
    }
    public boolean hasNext() { return !stack.isEmpty(); }
    public int next() {
        TreeNode node = stack.pop();
        if (!reverse) pushAll(node.right);
        else          pushAll(node.left);
        return node.data;
    }
}

class Solution {
    public boolean twoSumBST(TreeNode root, int k) {
        if (root == null) return false;
        BSTIterator l = new BSTIterator(root, false);
        BSTIterator r = new BSTIterator(root, true);
        int i = l.next();
        int j = r.next();
        while (i < j) {
            if      (i + j == k) return true;
            else if (i + j < k)  i = l.next();
            else                  j = r.next();
        }
        return false;
    }
}
