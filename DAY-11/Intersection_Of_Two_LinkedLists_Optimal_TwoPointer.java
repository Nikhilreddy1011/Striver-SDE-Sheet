// Approach: Two Pointer with List Redirect
// Time: O(M + N) | Space: O(1)
// When d1 reaches end, redirect to head2. When d2 reaches end, redirect to head1.
// Both pointers travel M+N total nodes and meet at the intersection (or null).

class Solution {
    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        ListNode d1 = head1;
        ListNode d2 = head2;

        while (d1 != d2) {
            d1 = (d1 == null) ? head2 : d1.next;
            d2 = (d2 == null) ? head1 : d2.next;
        }
        return d1; // intersection node or null
    }
}
