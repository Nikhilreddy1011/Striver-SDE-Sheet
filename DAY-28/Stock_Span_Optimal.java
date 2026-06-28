class Solution {

    private int[] findPGE(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Stack<Integer> st = new Stack<>();

        for (int i = 0; i < n; i++) {
            int currEle = arr[i];

            while (!st.isEmpty() && arr[st.peek()] <= currEle) {
                st.pop();
            }

            if (st.isEmpty())
                ans[i] = -1;
            else
                ans[i] = st.peek();

            st.push(i);
        }

        return ans;
    }

    public int[] stockSpan(int[] arr, int n) {
        int[] PGE = findPGE(arr);
        int[] ans = new int[n];

        for (int i = 0; i < n; i++) {
            ans[i] = i - PGE[i];
        }

        return ans;
    }
}
