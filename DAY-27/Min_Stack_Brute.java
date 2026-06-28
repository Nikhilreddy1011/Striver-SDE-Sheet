class MinStack {
    private Stack<int[]> st;

    public MinStack() {
        st = new Stack<>();
    }

    public void push(int value) {
        if (st.isEmpty()) {
            st.push(new int[]{value, value});
            return;
        }

        int mini = Math.min(getMin(), value);
        st.push(new int[]{value, mini});
    }

    public void pop() {
        st.pop();
    }

    public int top() {
        return st.peek()[0];
    }

    public int getMin() {
        return st.peek()[1];
    }
}
