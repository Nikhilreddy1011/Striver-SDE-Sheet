class Solution {
    private void inorder(TreeNode node, List<Integer> val) {
        if (node != null) {
            inorder(node.left, val);
            val.add(node.data);
            inorder(node.right, val);
        }
    }

    public List<Integer> kLargesSmall(TreeNode root, int k) {
        List<Integer> val = new ArrayList<>();
        inorder(root, val);
        int ksmallest = val.get(k - 1);
        int klargest  = val.get(val.size() - k);
        List<Integer> ans = new ArrayList<>();
        ans.add(ksmallest);
        ans.add(klargest);
        return ans;
    }
}
