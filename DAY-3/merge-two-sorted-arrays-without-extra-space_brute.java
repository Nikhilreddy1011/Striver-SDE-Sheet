class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] temp = new int[m+n];
        int i = 0, j =0,k=0;
        while(i<m && j < n){
            if(nums1[i] <= nums2[j]){
                temp[k] = nums1[i];
                i++;
                k++;

            }
            else{
                temp[k] = nums2[j];
                k++;
                j++;
            }
        }
        while(i<m){
            temp[k++]= nums1[i++];
        }
        while(j<n){
            temp[k++]= nums2[j++];
        }
        for(int c = 0;c<m+n;c++){
            nums1[c] = temp[c];
        }
        
    }
}