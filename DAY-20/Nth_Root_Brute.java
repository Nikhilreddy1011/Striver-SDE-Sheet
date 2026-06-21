class Solution {
    private long pow(int a, int x ){
        long ans = 1; 
        long base = a;
        while(x > 0){
            if(x % 2 == 1){
                x--;
                ans *= base;
            }
            else{
                x /=2;
                base *= base;
            }
            
        }
        return ans;
    }
    public int NthRoot(int N, int M) {
        for(int i = 1; i<= M ; i++){
            long val = pow(i,N);
            if(val == M){
                return i;
            }
            else if( val > M){
                break;
            }
        }
        return -1;
        
    }
}
