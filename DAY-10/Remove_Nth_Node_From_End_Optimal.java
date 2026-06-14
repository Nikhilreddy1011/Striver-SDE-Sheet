// Approach: Fast and Slow Pointers + Dummy Node
// Time: O(N) | Space: O(1)
// Move fast n+1 steps ahead, then move both until fast reaches null
// slow will be just before the node to delete

class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return null;

        ListNode dummy = new ListNode(-1, head);
        ListNode fast = dummy;
        ListNode slow = dummy;

        // Move fast n+1 steps ahead
        for (int i = 0; i <= n; i++)
            fast = fast.next;

        // Move both until fast reaches null
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }

        // slow is just before the node to delete
        slow.next = slow.next.next;
        return dummy.next;
    }
}
