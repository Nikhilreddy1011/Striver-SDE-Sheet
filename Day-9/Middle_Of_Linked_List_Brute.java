// Approach: Count + Traverse
// Time: O(N) | Space: O(1)
// Count total nodes, then traverse to count/2 + 1

class Solution {
    public ListNode middleOfLinkedList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode temp = head;
        int count = 0;

        while (temp != null) {
            temp = temp.next;
            count++;
        }

        int mid = count / 2 + 1;
        temp = head;

        while (temp != null) {
            mid--;
            if (mid == 0) break;
            temp = temp.next;
        }
        return temp;
    }
}
