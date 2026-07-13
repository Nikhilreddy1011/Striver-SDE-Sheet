class Solution {

    public String serialize(TreeNode root) {
        if (root == null) return "";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode curNode = q.poll();
            if (curNode == null) {
                sb.append("#,");
            } else {
                sb.append(curNode.data).append(",");
                q.offer(curNode.left);
                q.offer(curNode.right);
            }
        }
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        if (data.isEmpty()) return null;
        StringBuilder s = new StringBuilder(data);
        int commaIndex = s.indexOf(",");
        String str = s.substring(0, commaIndex);
        s.delete(0, commaIndex + 1);
        TreeNode root = new TreeNode(Integer.parseInt(str));
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            commaIndex = s.indexOf(",");
            str = s.substring(0, commaIndex);
            s.delete(0, commaIndex + 1);
            if (!str.equals("#")) {
                TreeNode leftNode = new TreeNode(Integer.parseInt(str));
                node.left = leftNode;
                q.offer(leftNode);
            }
            commaIndex = s.indexOf(",");
            str = s.substring(0, commaIndex);
            s.delete(0, commaIndex + 1);
            if (!str.equals("#")) {
                TreeNode rightNode = new TreeNode(Integer.parseInt(str));
                node.right = rightNode;
                q.offer(rightNode);
            }
        }
        return root;
    }
}
