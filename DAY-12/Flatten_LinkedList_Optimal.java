// Approach: Recursive merge (like merge sort) using child pointers
// Time: O(N*M) | Space: O(N) recursion stack
// Recursively flatten head.next first, then merge current head with the result
// Merge two sorted child-linked lists using child pointers

class Solution {
    private ListNode merge(ListNode list1, ListNode list2) {
        ListNode dummyNode = new ListNode(-1);
        ListNode res = dummyNode;

        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                res.child = list1;
                res = list1;
                list1 = list1.child;
            } else {
                res.child = list2;
                res = list2;
                list2 = list2.child;
            }
            res.next = null; // clear next pointer, we only use child
        }

        if (list1 != null) res.child = list1;
        else res.child = list2;

        if (dummyNode.child != null) dummyNode.child.next = null;

        return dummyNode.child;
    }

    public ListNode flattenLinkedList(ListNode head) {
        if (head == null || head.next == null) return head;

        // Recursively flatten the rest of the list
        ListNode mergedHead = flattenLinkedList(head.next);

        // Merge current head's child list with the merged result
        return merge(head, mergedHead);
    }
}
