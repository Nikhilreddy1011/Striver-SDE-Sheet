// Approach: HashSet - track visited nodes, first repeated node is loop start
// Time: O(N) | Space: O(N)

import java.util.*;

class Solution {
    public ListNode findStartingPoint(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        ListNode temp = head;
        while (temp != null) {
            if (set.contains(temp)) return temp; // already visited = loop start
            set.add(temp);
            temp = temp.next;
        }
        return null;
    }
}
