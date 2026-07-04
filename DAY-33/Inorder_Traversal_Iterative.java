class Solution {
    public List<Integer> inorder(TreeNode root) {
        Stack<TreeNode> st = new Stack<>();
        TreeNode node = root;
        List<Integer> inorder = new ArrayList<>();

        while (true) {
            if (node != null) {
                st.push(node);
                node = node.left;
            } else {
                if (st.isEmpty()) break;

                node = st.pop();
                inorder.add(node.data);
                node = node.right;
            }
        }

        return inorder;
    }
}
