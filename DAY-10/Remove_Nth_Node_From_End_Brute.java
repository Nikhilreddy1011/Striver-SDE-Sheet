// Approach: Count + Traverse
// Time: O(N) + O(N) | Space: O(1)
// Count total nodes, then traverse to (count - n)th node and delete the next

class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return null;

        int cnt = 0;
        ListNode temp = head;

        // Pass 1: count total nodes
        while (temp != null) {
            cnt++;
            temp = temp.next;
        }

        // Edge case: remove the head node
        if (cnt == n) return head.next;

        int res = cnt - n;
        temp = head;

        // Pass 2: traverse to the node just before the one to delete
        while (temp != null) {
            res--;
            if (res == 0) break;
            temp = temp.next;
        }

        // Skip the nth node from end
        temp.next = temp.next.next;
        return head;
    }
}
