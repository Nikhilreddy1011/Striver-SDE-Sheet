// Approach: Make circular, find new tail, break connection
// Time: O(N) | Space: O(1)
// Key insight: rotating by k is same as rotating by k % length
// New head is at position (length - k) from start

class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0)
            return head;

        // Step 1: Find length and tail
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
            length++;
        }

        // Step 2: Make circular
        tail.next = head;

        // Step 3: Effective rotations (k can be > length)
        k = k % length;

        // Step 4: Find new tail (length - k steps from head)
        int stepsToNewTail = length - k;
        ListNode newTail = head;
        for (int i = 1; i < stepsToNewTail; i++) {
            newTail = newTail.next;
        }

        // Step 5: New head is next of new tail, break the circle
        ListNode newHead = newTail.next;
        newTail.next = null;

        return newHead;
    }
}
