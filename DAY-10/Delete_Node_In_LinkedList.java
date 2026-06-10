// Approach: Copy next node's value and skip next node
// Time: O(1) | Space: O(1)
// We don't have access to head or previous node
// Trick: copy next node's value into current node, then skip next node
// This effectively "deletes" the current node in-place

class Solution {
    public void deleteNode(ListNode node) {
        node.val = node.next.val;       // copy next node's value here
        node.next = node.next.next;     // skip the next node
    }
}
