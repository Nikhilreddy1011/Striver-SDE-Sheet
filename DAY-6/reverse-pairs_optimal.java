class Solution {
    private int mergeSort(int[] nums, int low, int high){
        if(low >=high) return 0;
        int mid = (low + high)/2;
        int count = 0;
        count += mergeSort(nums,low,mid);
        count += mergeSort(nums,mid+1,high);
        count += countPairs(nums,low,mid,high);
        mergeNum(nums,low,mid,high);
        return count;
    }
    private int countPairs(int[] nums,int low,int mid,int high){
        int right = mid+1, count = 0;
        for(int i = low ;i<=mid;i++){
            while(right <= high && (long) nums[i] > 2L * nums[right]){
                right++;
            }
            count += (right - (mid+1));
        }
        return count;
    }
    private void mergeNum(int[] nums,int low,int mid,int high){
        int left = low,right = mid+1;
        List<Integer> list = new ArrayList<>();
        while(left <= mid && right <= high){
            if(nums[left] <= nums[right]){
                list.add(nums[left]);
                left++;
            }
            else{
                list.add(nums[right]);
                right++;
            }
        }
        while(left <= mid){
    list.add(nums[left]);
    left++;
}

while(right <= high){
    list.add(nums[right]);
    right++;
}

        for(int i = low;i<=high;i++){
            nums[i] = list.get( i - low);
        }

    }
    public int reversePairs(int[] nums) {
        int n = nums.length;
        return mergeSort(nums,0,n-1);
    }
}