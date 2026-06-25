class Solution {
    private void insertSorted(Stack<Integer> st, int x){
        if(st.isEmpty() || st.peek() <=x){
            st.push(x);
            return;
        }
        int top = st.pop();
        insertSorted(st,x);
        st.push(top);
    }
    public void sortStack(Stack<Integer> st) {
        if(st.size() <= 1) return ;
        int x = st.pop();
        sortStack(st);
        insertSorted(st,x);
    }
}