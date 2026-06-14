// Approach: Iterative - Three Pointers (prev, curr, next)
// Time: O(N) | Space: O(1)
// Reverse links in-place using three pointers

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;

        while (curr != null) {
            ListNode nextNode = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextNode;
        }
        return prev;
    }
}
