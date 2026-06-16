// Approach: Greedy - Sort both arrays, match smallest satisfying cookie to smallest student
// Time: O(N log N + M log M) | Space: O(1)
// Intuition: Give each student the smallest cookie that satisfies them.
// Sort both. Two pointers: if cookie[j] >= student[i], assign and move both pointers.
// Else, the cookie is too small — try the next (larger) cookie.

import java.util.*;

class Solution {
    public int findMaximumCookieStudents(int[] Student, int[] Cookie) {
        int[] students = Student.clone();
        int[] cookies = Cookie.clone();
        Arrays.sort(students);
        Arrays.sort(cookies);

        int studentPtr = 0, cookiePtr = 0, count = 0;

        while (studentPtr < students.length && cookiePtr < cookies.length) {
            if (cookies[cookiePtr] >= students[studentPtr]) {
                count++;
                studentPtr++; // student satisfied, move to next
            }
            cookiePtr++; // always try next cookie
        }
        return count;
    }
}
