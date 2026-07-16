class Solution {
    public int kthLargestElement(int[] nums, int k) {
        int n = nums.length;
        if (k > nums.length) return -1;
        int left = 0, right = n - 1;
        while (true) {
            int pivotIndex = randomIndex(left, right);
            pivotIndex = partionAndReturnIndex(nums, pivotIndex, left, right);
            if      (pivotIndex == k - 1) return nums[pivotIndex];
            else if (pivotIndex > k - 1)  right = pivotIndex - 1;
            else                           left  = pivotIndex + 1;
        }
    }

    private Random rand = new Random();

    private int randomIndex(int left, int right) {
        int length = right - left + 1;
        return rand.nextInt(length) + left;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int partionAndReturnIndex(int[] arr, int pivotIndex, int left, int right) {
        int pivot = arr[pivotIndex];
        swap(arr, left, pivotIndex);
        int index = left + 1;
        for (int i = left + 1; i <= right; i++) {
            if (arr[i] > pivot) {
                swap(arr, index, i);
                index++;
            }
        }
        swap(arr, left, index - 1);
        return index - 1;
    }
}
