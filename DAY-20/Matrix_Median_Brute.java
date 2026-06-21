class Solution {
    public int findMedian(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
      List<Integer> arr = new ArrayList<>();
      for(int i = 0 ; i < n ; i++){
        for(int j = 0; j < m ; j++){
            arr.add(matrix[i][j]);
        }
      }
      Collections.sort(arr);
      return arr.get((m*n)/2);
    }
}
