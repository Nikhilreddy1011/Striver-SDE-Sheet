// Approach: Two Pointers + Dummy Node
// Time: O(M+N) | Space: O(1)
// Compare heads of both lists, attach the smaller node
// Dummy node avoids edge case handling for empty result

class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummyNode = new ListNode(-1);
        ListNode temp = dummyNode, temp1 = list1, temp2 = list2;

        while (temp1 != null && temp2 != null) {
            if (temp1.val <= temp2.val) {
                temp.next = temp1;
                temp1 = temp1.next;
            } else {
                temp.next = temp2;
                temp2 = temp2.next;
            }
            temp = temp.next;
        }

        while (temp1 != null) {
            temp.next = temp1;
            temp1 = temp1.next;
            temp = temp.next;
        }
        while (temp2 != null) {
            temp.next = temp2;
            temp2 = temp2.next;
            temp = temp.next;
        }

        return dummyNode.next;
    }
}
