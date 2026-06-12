// Approach: Find mid, reverse second half, compare both halves
// Time: O(N) | Space: O(1)
// Step 1: Find middle using slow-fast pointers
// Step 2: Reverse the second half
// Step 3: Compare first half and reversed second half

class Solution {
    private ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode nextNode = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextNode;
        }
        return prev;
    }

    private ListNode findMid(ListNode head) {
        ListNode slow = head, fast = head;
        // Use fast.next!=null && fast.next.next!=null to get FIRST middle for even length
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        ListNode mid = findMid(head);
        ListNode secondHalf = mid.next;
        mid.next = null; // split the list

        ListNode rev = reverse(secondHalf);
        ListNode temp = head;

        while (rev != null) {
            if (temp.val != rev.val) return false;
            temp = temp.next;
            rev = rev.next;
        }
        return true;
    }
}
