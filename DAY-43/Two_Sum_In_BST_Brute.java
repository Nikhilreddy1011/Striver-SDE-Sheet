class Solution {
    public boolean twoSumBST(TreeNode root, int k) {
        List<Integer> sortedElements = inorderTraversal(root);
        int left = 0;
        int right = sortedElements.size() - 1;
        while (left < right) {
            int currentSum = sortedElements.get(left) + sortedElements.get(right);
            if (currentSum == k)      return true;
            else if (currentSum < k)  left++;
            else                      right--;
        }
        return false;
    }
    private List<Integer> inorderTraversal(TreeNode node) {
        List<Integer> elements = new ArrayList<>();
        inorderHelper(node, elements);
        return elements;
    }
    private void inorderHelper(TreeNode node, List<Integer> elements) {
        if (node == null) return;
        inorderHelper(node.left, elements);
        elements.add(node.data);
        inorderHelper(node.right, elements);
    }
}
