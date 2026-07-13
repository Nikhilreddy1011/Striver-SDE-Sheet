class Solution {

    class NodeValue {
        int minNode, maxNode, maxSize;
        NodeValue(int minNode, int maxNode, int maxSize) {
            this.minNode = minNode;
            this.maxNode = maxNode;
            this.maxSize = maxSize;
        }
    }

    private NodeValue largestBSTSubtreeHelper(TreeNode node) {
        if (node == null) {
            return new NodeValue(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        }
        NodeValue left  = largestBSTSubtreeHelper(node.left);
        NodeValue right = largestBSTSubtreeHelper(node.right);

        if (left.maxNode < node.data && node.data < right.minNode) {
            return new NodeValue(
                Math.min(node.data, left.minNode),
                Math.max(node.data, right.maxNode),
                left.maxSize + right.maxSize + 1
            );
        }
        return new NodeValue(
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            Math.max(left.maxSize, right.maxSize)
        );
    }

    public int largestBST(TreeNode root) {
        return largestBSTSubtreeHelper(root).maxSize;
    }
}
