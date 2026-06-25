class Solution {
    public int[] nextLargerElement(int[] arr) {
        int n = arr.length;
        int[] sol = new int[n];
        Stack<Integer> st = new Stack<>();
        for(int i = n-1 ; i >=0 ; i--){
            while(!st.isEmpty() && st.peek() <= arr[i]){
                st.pop();
            }
            if(st.isEmpty()) 
                sol[i] = -1;
            else
                sol[i] = st.peek();
            st.push(arr[i]);
        }
        return sol;
    }
}