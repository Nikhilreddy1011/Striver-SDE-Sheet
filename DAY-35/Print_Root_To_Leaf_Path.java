class Solution {
    public List<List<Integer>> allRootToLeaf(TreeNode root) {
        List<List<Integer>> allPaths  = new ArrayList<>();
        List<Integer>       currentPath = new ArrayList<>();
        dfs(root, currentPath, allPaths);
        return allPaths;
    }

    private void dfs(TreeNode node, List<Integer> path, List<List<Integer>> allPaths) {
        if (node == null) return;

        path.add(node.data);

        if (node.left == null && node.right == null) {
            allPaths.add(new ArrayList<>(path));
        } else {
            dfs(node.left,  path, allPaths);
            dfs(node.right, path, allPaths);
        }

        path.remove(path.size() - 1);
    }
}
