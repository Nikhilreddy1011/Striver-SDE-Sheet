class Solution {
    public int[] findMissingRepeatingNumbers(int[] nums) {
        int n = nums.length;
        int[] temp = new int[n+1];
        for(int i = 0 ; i < n; i++){
            temp[nums[i]]++;
        }
        int duplicate = -1, missing = -1;
        for(int i = 1;i<=n;i++){
            if(temp[i]==2){
                duplicate = i;
            }
            else if(temp[i] == 0){
                missing = i;
            }
            if(missing != -1 && duplicate != -1){
                break;
            }
        }
        return new int[]{duplicate,missing};

    }
}