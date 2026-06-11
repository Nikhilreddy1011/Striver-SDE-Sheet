// Approach: Nested Loop - Check every node of list2 against all nodes of list1
// Time: O(M * N) | Space: O(1)

class Solution {
    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        while (head2 != null) {
            ListNode temp = head1;
            while (temp != null) {
                if (temp == head2) return head2; // same node reference = intersection
                temp = temp.next;
            }
            head2 = head2.next;
        }
        return null;
    }
}
