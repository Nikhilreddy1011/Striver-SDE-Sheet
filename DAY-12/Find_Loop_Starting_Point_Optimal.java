// Approach: Floyd's Cycle Detection + Mathematical reset to find loop start
// Time: O(N) | Space: O(1)
// Phase 1: Detect if cycle exists (slow and fast meet inside cycle)
// Phase 2: Reset one pointer to head, move both 1 step at a time
//          They meet at the loop starting point

class Solution {
    public ListNode findStartingPoint(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode fast = head, slow = head;

        // Phase 1: Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }

        // No cycle
        if (slow != fast) return null;

        // Phase 2: Find loop starting point
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }
}
