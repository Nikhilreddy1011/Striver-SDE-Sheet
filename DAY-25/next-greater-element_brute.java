class Solution {
    public int[] nextLargerElement(int[] arr) {
        int n = arr.length;
        int[] sol = new int[n];
        Arrays.fill(sol,-1);
        for(int i = 0 ; i < n ; i++){
            for(int j = i + 1 ; j < n ; j++){
                if(arr[j] > arr[i]){
                    sol[i] = arr[j];
                    break;
                }
            }
        }
        return sol;
    
    }
}