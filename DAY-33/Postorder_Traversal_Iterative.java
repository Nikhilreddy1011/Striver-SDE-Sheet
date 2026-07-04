class Solution {
    public List<Integer> postorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> nodeStack = new Stack<>();

        if (root != null) nodeStack.push(root);

        while (!nodeStack.isEmpty()) {
            TreeNode node = nodeStack.pop();
            result.add(node.data);

            if (node.left != null) nodeStack.push(node.left);
            if (node.right != null) nodeStack.push(node.right);
        }

        Collections.reverse(result);
        return result;
    }
}
