class Solution {
    public List<Integer> majorityElementTwo(int[] nums) {
        int n = nums.length;
        List<Integer> list = new ArrayList<>();
        int min = n/3 ;

        for(int i =0;i<n;i++){
            if(list.size()==0 || list.get(0) != nums[i]){
            int count = 0 ;
            for(int j = 0;j<n;j++){
                if(nums[j] == nums[i]){
                    count++;
                }
                
            }
            if(count > min){
                list.add(nums[i]);
            }
        }
        if(list.size()== 2){
            break;
        }
        }
        return list;
    }
}