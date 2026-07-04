class Solution {
    public List<Integer> preorder(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        func(root, ans);
        return ans;
    }

    private void func(TreeNode node, List<Integer> ans) {
        if (node == null) {
            return;
        }
        ans.add(node.data);
        func(node.left, ans);
        func(node.right, ans);
    }
}
