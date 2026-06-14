// Approach: Length Difference - Advance the longer list by the difference, then traverse together
// Time: O(M + N) | Space: O(1)

class Solution {
    public int getDifference(ListNode head1, ListNode head2) {
        int len1 = 0, len2 = 0;
        while (head1 != null || head2 != null) {
            if (head1 != null) { len1++; head1 = head1.next; }
            if (head2 != null) { len2++; head2 = head2.next; }
        }
        return len1 - len2;
    }

    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        int diff = getDifference(head1, head2);

        // Advance the longer list by the difference
        if (diff < 0) {
            while (diff++ != 0) head2 = head2.next;
        } else {
            while (diff-- != 0) head1 = head1.next;
        }

        // Now traverse both together until they meet
        while (head1 != null) {
            if (head1 == head2) return head1;
            head1 = head1.next;
            head2 = head2.next;
        }
        return null;
    }
}
