class NodeState {
    TreeNode node;
    int state;
    NodeState(TreeNode node, int state) {
        this.node = node;
        this.state = state;
    }
}

class Solution {
    List<List<Integer>> treeTraversal(TreeNode root) {
       List<Integer> preOrder  = new ArrayList<>();
       List<Integer> inOrder   = new ArrayList<>();
       List<Integer> postOrder = new ArrayList<>();
       if (root == null) return Arrays.asList(inOrder, preOrder, postOrder);
       Stack<NodeState> st = new Stack<>();
       st.push(new NodeState(root, 1));
       while (!st.isEmpty()) {
           NodeState peek = st.pop();
           TreeNode node  = peek.node;
           int state      = peek.state;
           if (state == 1) {
               preOrder.add(node.data);
               st.push(new NodeState(node, 2));
               if (node.left != null) st.push(new NodeState(node.left, 1));
           } else if (state == 2) {
               inOrder.add(node.data);
               st.push(new NodeState(node, 3));
               if (node.right != null) st.push(new NodeState(node.right, 1));
           } else {
               postOrder.add(node.data);
           }
       }
       return Arrays.asList(inOrder, preOrder, postOrder);
    }
}
