// Approach: Collect + Sort + Rebuild
// Time: O((M+N) log(M+N)) | Space: O(M+N)
// Collect all values, sort them, build new linked list

import java.util.*;

class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        List<Integer> li = new ArrayList<>();
        ListNode temp = list1;

        while (temp != null) { li.add(temp.val); temp = temp.next; }
        temp = list2;
        while (temp != null) { li.add(temp.val); temp = temp.next; }

        Collections.sort(li);

        ListNode dummyNode = new ListNode(-1);
        temp = dummyNode;
        for (int ele : li) {
            temp.next = new ListNode(ele);
            temp = temp.next;
        }
        return dummyNode.next;
    }
}
