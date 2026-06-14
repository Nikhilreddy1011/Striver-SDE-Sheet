// Approach: Interleave copy nodes in original list, set random pointers, separate lists
// Time: O(N) | Space: O(1) — no extra HashMap needed
// Step 1: Insert copy of each node right after the original node
// Step 2: Set random pointers of copied nodes using original's random
// Step 3: Separate original and copied lists

class Solution {
    public ListNode copyRandomList(ListNode head) {
        if (head == null) return null;

        // Step 1: Insert copied nodes between original nodes
        // 1 -> 1' -> 2 -> 2' -> 3 -> 3'
        ListNode temp = head;
        while (temp != null) {
            ListNode copy = new ListNode(temp.val);
            copy.next = temp.next;
            temp.next = copy;
            temp = copy.next;
        }

        // Step 2: Set random pointers for copied nodes
        // copy.random = original.random.next (which is the copied node)
        temp = head;
        while (temp != null) {
            if (temp.random != null) {
                temp.next.random = temp.random.next;
            }
            temp = temp.next.next;
        }

        // Step 3: Separate original and copied lists
        ListNode dummy = new ListNode(0);
        ListNode copyCurr = dummy;
        temp = head;

        while (temp != null) {
            ListNode copy = temp.next;
            copyCurr.next = copy;
            copyCurr = copy;
            temp.next = copy.next; // restore original list
            temp = temp.next;
        }

        return dummy.next;
    }
}
