class Solution {
    public List<List<Integer>> mergeOverlap(List<List<Integer>> intervals) {

        intervals.sort((a, b) -> a.get(0) - b.get(0));

        List<List<Integer>> ans = new ArrayList<>();

        for (List<Integer> curr : intervals) {

            if (ans.isEmpty() || curr.get(0) > ans.get(ans.size() - 1).get(1)) {

                ans.add(new ArrayList<>(curr));

            } else {

                List<Integer> last = ans.get(ans.size() - 1);

                last.set(1,Math.max(last.get(1), curr.get(1)));
            }
        }

        return ans;
    }
}