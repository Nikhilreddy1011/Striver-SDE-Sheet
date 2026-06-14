// Approach: HashMap - Track visited nodes
// Time: O(N) | Space: O(N)
// If we visit the same node twice, a cycle exists

import java.util.*;

class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode temp = head;
        HashMap<ListNode, Integer> nodeMap = new HashMap<>();

        while (temp != null) {
            if (nodeMap.containsKey(temp)) return true; // visited before = cycle
            nodeMap.put(temp, 1);
            temp = temp.next;
        }
        return false;
    }
}
