class Solution {
    public List<Integer> floorCeilOfBST(TreeNode root, int key) {
        int floor = -1;
        int ceil = -1;
        TreeNode current = root;
        while (current != null) {
            if (current.data == key) {
                floor = current.data;
                break;
            } else if (current.data < key) {
                floor = current.data;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        current = root;
        while (current != null) {
            if (current.data == key) {
                ceil = current.data;
                break;
            }
            if (current.data > key) {
                ceil = current.data;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return Arrays.asList(floor, ceil);
    }
}
