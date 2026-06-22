class Solution {
private List<Integer> arr;
private int count;
public Solution(){
    arr = new ArrayList<>();
    count = 0 ;
}
private void heapifyUp(int ind) {
    int parentInd = (ind - 1) / 2;

    if (ind > 0 && arr.get(ind) > arr.get(parentInd)) {
        int temp = arr.get(ind);
        arr.set(ind, arr.get(parentInd));
        arr.set(parentInd, temp);

        heapifyUp(parentInd);
    }
}

private void heapifyDown(int ind) {
    int n = arr.size();

    int largestInd = ind;

    int leftChildInd = 2 * ind + 1;
    int rightChildInd = 2 * ind + 2;

    if (leftChildInd < n && arr.get(leftChildInd) > arr.get(largestInd))
        largestInd = leftChildInd;

    if (rightChildInd < n && arr.get(rightChildInd) > arr.get(largestInd))
        largestInd = rightChildInd;

    if (largestInd != ind) {
        int temp = arr.get(largestInd);
        arr.set(largestInd, arr.get(ind));
        arr.set(ind, temp);

        heapifyDown(largestInd);
    }
}
    public void initializeHeap() {
        arr.clear();
        count = 0;
    }

    public void insert(int key) {
        arr.add(key);
        heapifyUp(count);
        count++;
        return ;
    }

    public void changeKey(int index, int newVal) {

        if(arr.get(index) < newVal){
            arr.set(index,newVal);
            heapifyUp(index);
        }
        else{
            arr.set(index,newVal);
            heapifyDown(index);
        }
        return ;
    }

    public void extractMax() {
        int ele = arr.get(0);
        int temp = arr.get(count - 1);
        arr.set(count-1,arr.get(0));
        arr.set(0,temp);
        arr.remove(count-1);
        count--;
        if(count > 0 ){
            heapifyDown(0);
        }
    }

    public boolean isEmpty() {
        return (count == 0 );
        
    }

    public int getMax() {
       return arr.get(0);
    }

    public int heapSize() {
        return count;
    }
}