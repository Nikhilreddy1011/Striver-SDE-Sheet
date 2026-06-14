// Approach: HashSet - Store all nodes of list1, check list2 against it
// Time: O(M + N) | Space: O(M)

import java.util.*;

class Solution {
    public ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        Set<ListNode> st = new HashSet<>();

        // Add all nodes of list1 to the set
        while (head1 != null) {
            st.add(head1);
            head1 = head1.next;
        }

        // Check each node of list2 against the set
        while (head2 != null) {
            if (st.contains(head2)) return head2;
            head2 = head2.next;
        }
        return null;
    }
}
