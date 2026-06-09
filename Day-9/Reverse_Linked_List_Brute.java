// Approach: Stack
// Time: O(N) | Space: O(N)
// Push all values to stack, pop back into nodes

class Solution {
    public ListNode reverseList(ListNode head) {
        Stack<Integer> st = new Stack<>();
        ListNode temp = head;
        while (temp != null) {
            st.push(temp.val);
            temp = temp.next;
        }
        temp = head;
        while (temp != null) {
            temp.val = st.pop();
            temp = temp.next;
        }
        return head;
    }
}
