class Solution {
    boolean getPath(TreeNode root, List<TreeNode> path, int x) {
        if (root == null) return false;

        path.add(root);

        if (root.data == x) return true;

        if (getPath(root.left, path, x) || getPath(root.right, path, x))
            return true;

        path.remove(path.size() - 1);
        return false;
    }

    public TreeNode lca(TreeNode root, int p, int q) {
        List<TreeNode> path1 = new ArrayList<>();
        List<TreeNode> path2 = new ArrayList<>();

        if (!getPath(root, path1, p) || !getPath(root, path2, q))
            return null;

        int i = 0;
        while (i < path1.size() && i < path2.size() && path1.get(i) == path2.get(i))
            i++;

        return path1.get(i - 1);
    }
}
