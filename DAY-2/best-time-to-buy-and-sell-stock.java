class Solution {
    public int stockBuySell(int[] arr, int n) {
       int minprice = arr[0];
       int maxprofit = 0 ;
       for(int i = 0;i<n;i++){
        int profit = arr[i] - minprice;
        maxprofit = Math.max(maxprofit,profit);
        minprice = Math.min(minprice,arr[i]);
        
       }
       return maxprofit;

       
    }
}