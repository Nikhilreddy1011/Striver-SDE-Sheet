class Solution {
    public List<Integer> succPredBST(TreeNode root, int key) {
        TreeNode predecessor = null;
        TreeNode successor   = null;
        TreeNode curr        = root;

        while (curr != null) {
            if (key > curr.data) {
                predecessor = curr;
                curr = curr.right;
            } else if (key < curr.data) {
                successor = curr;
                curr = curr.left;
            } else {
                if (curr.left != null) {
                    TreeNode temp = curr.left;
                    while (temp.right != null) temp = temp.right;
                    predecessor = temp;
                }

                if (curr.right != null) {
                    TreeNode temp = curr.right;
                    while (temp.left != null) temp = temp.left;
                    successor = temp;
                }
                break;
            }
        }

        int predVal = (predecessor != null ? predecessor.data : -1);
        int succVal = (successor   != null ? successor.data   : -1);

        List<Integer> ans = new ArrayList<>();
        ans.add(predVal);
        ans.add(succVal);

        return ans;
    }
}
