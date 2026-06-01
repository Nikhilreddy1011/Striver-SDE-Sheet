class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] temp = new int[m][n];

        for(int i = 0;i<m;i++){
            for(int j=0;j<n;j++){
                temp[i][j] = matrix[i][j];
            }
        } 
        for(int i = 0;i<m;i++){
            for(int j=0;j<n;j++){
                if(matrix[i][j] == 0){
                    for(int r = 0;r < m; r++){
                        temp[r][j] = 0;
                    }
                    for(int c = 0; c < n;c++){
                        temp[i][c]= 0;
                    }
                }
            }
        }
        for(int i = 0;i<m;i++){
            for(int j=0;j<n;j++){
                matrix[i][j] = temp[i][j] ;
            }
        }


    }
}