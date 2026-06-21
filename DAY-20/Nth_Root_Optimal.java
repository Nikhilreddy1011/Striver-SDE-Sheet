class Solution {
    private int func(int mid, int N, int M){
        long ans = 1, base = mid;
        while(N > 0){
            if(N % 2 == 1){
                ans *= base;
                if(ans > M) return 2;
                N--;
            }
            else{
                N /= 2;
                base *= base;
                if(base > M) return 2;
            }
        }
        if( ans == M) return 1;
        return 0;
    }
    public int NthRoot(int N, int M) {
        int low = 1, high = M;
        while(low <= high){
            int mid = low + (high-low)/2;
            int val = func(mid, N, M);
            if(val == 1) return mid;
            else if(val == 0 ) low = mid +1;
            else high = mid - 1;

        }
        return -1;
    }
}
