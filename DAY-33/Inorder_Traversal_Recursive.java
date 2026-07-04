class Solution {
    private void recursiveInorder(TreeNode root, List<Integer> arr) {
        if (root == null) return;

        recursiveInorder(root.left, arr);
        arr.add(root.data);
        recursiveInorder(root.right, arr);
    }

    public List<Integer> inorder(TreeNode root) {
        List<Integer> arr = new ArrayList<>();
        recursiveInorder(root, arr);
        return arr;
    }
}
