// Approach: Floyd's Cycle Detection (Slow and Fast Pointers)
// Time: O(N) | Space: O(1)
// If slow and fast meet, a cycle exists. If fast reaches null, no cycle.

class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;

        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next; // move 2 steps
            slow = slow.next;      // move 1 step
            if (slow == fast) return true; // they met = cycle exists
        }
        return false;
    }
}
