class Solution {
    private void dfs(int node,int[] visited, ArrayList<ArrayList<Integer>> adj,Stack<Integer> st){
        visited[node]  = 1;
        for(int it : adj.get(node)){
            if(visited[it]== 0){
                dfs(it,visited,adj,st);
            }
        }

        st.push(node);
    }
    private void dfs3(int node, int[] visited,ArrayList<ArrayList<Integer>> aReverse){
        visited[node]= 1;
        for(int it : aReverse.get(node)){
            if(visited[it]==0){
                dfs3(it,visited,aReverse);
            }
        }
    }
    public int kosaraju(int V, ArrayList<ArrayList<Integer>> adj) {
        int[] visited = new int[V];
        Stack<Integer> st = new Stack<>();
        for(int i = 0 ; i < V; i++){
            if(visited[i] == 0){
                dfs(i,visited,adj,st);

            }
        }
        ArrayList<ArrayList<Integer>> aReverse = new ArrayList<>();
        for(int i = 0 ; i < V ; i++){
            aReverse.add(new ArrayList<>());
        }
        for (int i = 0; i< V ; i++){
            visited[i]=0;
            for(int it : adj.get(i)){
                aReverse.get(it).add(i);
            }
        }
        int scc = 0;
        while(!st.isEmpty()){
            int node = st.peek();
            st.pop();
            if(visited[node]==0){
                scc +=1;
                dfs3(node,visited,aReverse);
            }
        }
        return scc;
    }
}

