class Solution {
    public List<Integer> majorityElementTwo(int[] nums) {
        int n = nums.length;
        List<Integer> list = new ArrayList<>();
        int count1 = 0, count2 = 0;
        int ele1 = 0, ele2 = 0;
        int min = n/3 + 1;
        for(int i = 0; i< n;i++){
            if(count1 == 0 && ele2 != nums[i]){
                ele1 = nums[i];
                count1++;
            }
            else if(count2 == 0 && ele1 != nums[i]){
                ele2 = nums[i];
                count2++;
            }
            else if(nums[i] == ele1) count1++;
            else if(nums[i] == ele2) count2++;
            else{
                count1--;
                count2--;
            }
        }
        int cnt1=0,cnt2 = 0;
        for(int i =0;i<n;i++){
            if(nums[i] == ele1){
                cnt1++;
            }
            if(nums[i] == ele2){
                cnt2++;
            }
        }
        if(cnt1 >= min){
            list.add(ele1);
        }
        if(cnt2 >= min && ele1 != ele2){
            list.add(ele2);

        }
        return list;

    }
}