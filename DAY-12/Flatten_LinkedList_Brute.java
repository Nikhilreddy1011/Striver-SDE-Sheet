// Approach: Collect all values from all child lists, sort, rebuild via child pointer
// Time: O(N*M * log(N*M)) | Space: O(N*M)
// N = number of head nodes, M = avg child list length

import java.util.*;

class Solution {
    private ListNode convertArrToLinkedList(List<Integer> arr) {
        ListNode dummyNode = new ListNode(-1);
        ListNode temp = dummyNode;
        for (int val : arr) {
            temp.child = new ListNode(val);
            temp = temp.child;
        }
        return dummyNode.child;
    }

    public ListNode flattenLinkedList(ListNode head) {
        List<Integer> arr = new ArrayList<>();

        // Traverse all head nodes via next pointer
        while (head != null) {
            ListNode t2 = head;
            // Traverse each child list
            while (t2 != null) {
                arr.add(t2.val);
                t2 = t2.child;
            }
            head = head.next;
        }

        Collections.sort(arr);
        return convertArrToLinkedList(arr);
    }
}
