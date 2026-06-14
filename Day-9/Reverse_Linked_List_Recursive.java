// Approach: Recursive
// Time: O(N) | Space: O(N) recursion stack
// Go to end, reverse links on the way back

class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode newHead = reverseList(head.next);

        ListNode nextNode = head.next;
        nextNode.next = head;
        head.next = null;

        return newHead;
    }
}
