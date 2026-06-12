// Approach: Collect values into ArrayList, use two pointers to check palindrome
// Time: O(N) | Space: O(N)

import java.util.*;

class Solution {
    private boolean isPalindrome(List<Integer> li) {
        int l = 0, h = li.size() - 1;
        while (l < h) {
            if (li.get(l) != li.get(h)) return false;
            l++;
            h--;
        }
        return true;
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null) return true;
        List<Integer> li = new ArrayList<>();
        ListNode temp = head;
        while (temp != null) {
            li.add(temp.val);
            temp = temp.next;
        }
        return isPalindrome(li);
    }
}
