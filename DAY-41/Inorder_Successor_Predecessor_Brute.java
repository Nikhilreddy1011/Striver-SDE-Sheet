class Solution {

    List<Integer> succPredBST(TreeNode root, int key) {
        List<Integer> sortedList = new ArrayList<>();
        inorderTraversal(root, sortedList);

        int predecessor = -1;
        int successor   = -1;

        for (int i = 0; i < sortedList.size(); i++) {
            if (sortedList.get(i) < key) {
                predecessor = sortedList.get(i);
            } else if (sortedList.get(i) > key) {
                successor = sortedList.get(i);
                break;
            }
        }

        List<Integer> result = new ArrayList<>();
        result.add(predecessor);
        result.add(successor);

        return result;
    }

    private void inorderTraversal(TreeNode node, List<Integer> sortedList) {
        if (node == null) return;
        inorderTraversal(node.left, sortedList);
        sortedList.add(node.data);
        inorderTraversal(node.right, sortedList);
    }
}
