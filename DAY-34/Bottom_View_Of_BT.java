class Solution {
    public List<Integer> bottomView(TreeNode root) {
        List<Integer> ans = new ArrayList<>();

        if (root == null) {
            return ans;
        }

        Map<Integer, Integer> map = new TreeMap<>();
        Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<>();

        q.offer(new AbstractMap.SimpleEntry<>(root, 0));

        while (!q.isEmpty()) {
            Map.Entry<TreeNode, Integer> curr = q.poll();

            TreeNode node = curr.getKey();
            int line = curr.getValue();

            map.put(line, node.data);

            if (node.left != null) {
                q.offer(new AbstractMap.SimpleEntry<>(node.left, line - 1));
            }

            if (node.right != null) {
                q.offer(new AbstractMap.SimpleEntry<>(node.right, line + 1));
            }
        }

        for (int val : map.values()) {
            ans.add(val);
        }

        return ans;
    }
}
