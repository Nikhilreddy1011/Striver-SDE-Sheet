// Approach: Slow and Fast Pointers (Tortoise and Hare)
// Time: O(N/2) | Space: O(1)
// fast moves 2 steps, slow moves 1 step
// when fast reaches end, slow is at the middle

class Solution {
    public ListNode middleOfLinkedList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }
}
