class Solution {
    public Node connect(Node root) {

        if (root == null) return null;

        Node levelStart = root;

        while (levelStart.left != null) {
            Node curr = levelStart;

            while (curr != null) {
                curr.left.next = curr.right;

                if (curr.next != null) {
                    curr.right.next = curr.next.left;
                }

                curr = curr.next;
            }

            levelStart = levelStart.left;
        }

        return root;
    }
}
