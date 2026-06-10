// Approach: Simulate addition digit by digit with carry
// Time: O(max(M, N)) | Space: O(max(M, N)) for result list
// Digits stored in reverse order so we process from least significant digit
// Handle carry after both lists are exhausted

class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode temp = dummy;
        int carry = 0;

        // Continue while either list has nodes or there's a carry left
        while (l1 != null || l2 != null || carry != 0) {
            int sum = 0;

            if (l1 != null) {
                sum += l1.data;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.data;
                l2 = l2.next;
            }

            sum += carry;
            carry = sum / 10;           // carry for next digit

            ListNode node = new ListNode(sum % 10);  // current digit
            temp.next = node;
            temp = temp.next;
        }
        return dummy.next;
    }
}
