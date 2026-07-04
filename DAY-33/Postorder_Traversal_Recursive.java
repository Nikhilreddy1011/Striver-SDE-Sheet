class Solution {
    private void recursivePostorder(TreeNode root, List<Integer> arr) {
        if (root == null) return;

        recursivePostorder(root.left, arr);
        recursivePostorder(root.right, arr);
        arr.add(root.data);
    }

    public List<Integer> postorder(TreeNode root) {
        List<Integer> arr = new ArrayList<>();
        recursivePostorder(root, arr);
        return arr;
    }
}
