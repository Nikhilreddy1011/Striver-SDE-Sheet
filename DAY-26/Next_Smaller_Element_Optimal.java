class Solution {
    public int[] nextSmallerElements(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Arrays.fill(ans,-1);
        Stack<Integer> st = new Stack<>();
        for(int i = n -1; i >=0 ; i--){
            while(!st.isEmpty() && st.peek() >= arr[i]){
                st.pop();
            }
            if(!st.isEmpty()) 
                ans[i] = st.peek();
            st.push(arr[i]);
        }
        return ans;
    }
}
