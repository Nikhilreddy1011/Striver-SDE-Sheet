class Solution {
    private int height(TreeNode node){
        if(node == null) return 0;
        return 1 + Math.max(height(node.left),height(node.right));
    }
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) return 0;
        int lHeight = height(root.left);
        int rHeight = height(root.right);
        int curDiameter = lHeight + rHeight;
        int leftDiameter = diameterOfBinaryTree(root.left);
        int rightDiameter = diameterOfBinaryTree(root.right);
        return Math.max(curDiameter,Math.max(leftDiameter,rightDiameter));
    }
}
