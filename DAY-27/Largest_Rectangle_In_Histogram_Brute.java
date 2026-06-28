class Solution {
    private int[] findNSE(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Stack<Integer> st = new Stack<>();

        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && arr[st.peek()] >= arr[i]) {
                st.pop();
            }

            ans[i] = !st.isEmpty() ? st.peek() : n;
            st.push(i);
        }

        return ans;
    }

    private int[] findPSE(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Stack<Integer> st = new Stack<>();

        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] >= arr[i]) {
                st.pop();
            }

            ans[i] = !st.isEmpty() ? st.peek() : -1;
            st.push(i);
        }

        return ans;
    }

    public int largestRectangleArea(int[] heights) {
       int n = heights.length;
       int[] nse = findNSE(heights);
       int[] pse = findPSE(heights);
       int LargestArea = 0;
       int area;
       for(int i = 0 ; i< n ; i++ ){
        area = heights[i] * (nse[i] - pse[i]-1);
        LargestArea = Math.max(LargestArea,area);
       }
       return LargestArea;
    }
}
