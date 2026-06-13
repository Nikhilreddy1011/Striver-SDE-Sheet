// Approach: Simulate k rotations one by one
// Time: O(N * k) | Space: O(1)
// Each rotation: traverse to second-last node, move last node to front

class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0)
            return head;

        for (int i = 0; i < k; i++) {
            ListNode curr = head;
            ListNode prev = null;

            // Traverse to last node
            while (curr.next != null) {
                prev = curr;
                curr = curr.next;
            }

            prev.next = null;   // detach last node
            curr.next = head;   // last node becomes new head
            head = curr;
        }
        return head;
    }
}
