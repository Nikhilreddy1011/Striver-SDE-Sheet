class Solution {

    int findMaxPathSum(TreeNode root, int[] maxi) {
        if (root == null) return 0;

        int left = Math.max(0, findMaxPathSum(root.left, maxi));
        int right = Math.max(0, findMaxPathSum(root.right, maxi));

        maxi[0] = Math.max(maxi[0], left + right + root.data);

        return root.data + Math.max(left, right);
    }

    public int maxPathSum(TreeNode root) {
        int[] maxi = {Integer.MIN_VALUE};
        findMaxPathSum(root, maxi);
        return maxi[0];
    }
}
