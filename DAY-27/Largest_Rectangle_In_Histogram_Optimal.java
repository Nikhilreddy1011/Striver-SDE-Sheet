class Solution {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        Stack<Integer> st = new Stack<>();

        int largestArea = 0;
        int area, nse, pse;

        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && heights[st.peek()] >= heights[i]) {
                int ind = st.pop();
                pse = st.isEmpty() ? -1 : st.peek();
                nse = i;
                area = heights[ind] * (nse - pse - 1);
                largestArea = Math.max(largestArea, area);
            }
            st.push(i);
        }

        while (!st.isEmpty()) { 
            nse = n;
            int ind = st.pop();
            pse = st.isEmpty() ? -1 : st.peek();
            area = heights[ind] * (nse - pse - 1);
            largestArea = Math.max(largestArea, area);
        }

        return largestArea;
    }
}
