// Approach: Iterative group reversal with dummy node
// Time: O(N) | Space: O(1)
// For each group of k nodes: find kth node, reverse the group, reconnect
// If fewer than k nodes remain, leave them as is

class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;

        while (true) {
            // Find the kth node from groupPrev
            ListNode kth = getKthNode(groupPrev, k);
            if (kth == null) break; // fewer than k nodes left, stop

            ListNode groupNext = kth.next; // head of next group

            // Reverse the current k-group
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            for (int i = 0; i < k; i++) {
                ListNode temp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = temp;
            }

            // Connect previous group to this reversed group
            ListNode temp = groupPrev.next; // original head of group (now tail)
            groupPrev.next = kth;           // kth becomes new head of group
            groupPrev = temp;               // move groupPrev to tail of reversed group
        }

        return dummy.next;
    }

    // Returns the kth node from curr (or null if fewer than k nodes exist)
    private ListNode getKthNode(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }
        return curr;
    }
}
