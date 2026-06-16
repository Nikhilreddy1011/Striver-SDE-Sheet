
import java.util.*;

class Solution {
    static class MeetingComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            return Integer.compare(a[1], b[1]); 
        }
    }

    public int maxMeetings(int[] start, int[] end) {
        int n = end.length;
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new int[]{start[i], end[i]});
        }

        Collections.sort(list, new MeetingComparator());

        int freeTime = list.get(0)[1]; // end time of last selected meeting
        int count = 1;

        for (int i = 1; i < n; i++) {
            // current meeting can be scheduled only if it starts after room is free
            if (list.get(i)[0] > freeTime) {
                freeTime = list.get(i)[1];
                count++;
            }
        }
        return count;
    }
}
