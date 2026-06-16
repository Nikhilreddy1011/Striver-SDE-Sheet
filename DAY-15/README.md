# 🚀 Day 15/45 – #SDESheetChallenge

> Solving 3-4 problems a day from Striver's SDE Sheet. Documenting every approach, dry run, and intuition — so that anyone can follow along, whether you're a beginner or brushing up for interviews.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [N Meetings in One Room](#1-n-meetings-in-one-room) | 🟡 Medium | Greedy |
| 2 | [Minimum Platforms for a Railway](#2-minimum-number-of-platforms-required-for-a-railway) | 🟡 Medium | Greedy / Two Pointer |
| 3 | [Job Sequencing Problem](#3-job-sequencing-problem) | 🟡 Medium | Greedy |
| 4 | [Fractional Knapsack](#4-fractional-knapsack) | 🟡 Medium | Greedy |

---

## 1. N Meetings in One Room

### 🧩 Problem Statement

Given `start[]` and `end[]` arrays for N meetings in one meeting room, find the **maximum number of meetings** that can be accommodated, given only one meeting can be held at a time.

**Example 1:**
```
Input:  Start = [1,3,0,5,8,5], End = [2,4,6,7,9,9]
Output: 4
Meetings: (1,2), (3,4), (5,7), (8,9)
```

**Example 2:**
```
Input:  Start = [10,12,20], End = [20,25,30]
Output: 1
```

**Constraints:**
- `1 <= N <= 10⁵`
- `0 <= start[i] < end[i] <= 10⁵`

---

### 🟢 Approach — Greedy: Sort by End Time
**Time: O(N log N) | Space: O(N)**

#### 💡 Intuition
Between two meetings, prefer the one that **ends earlier** — it frees up the room sooner, leaving more room for future meetings. This is the classic interval scheduling greedy.

**Why end time, not start time?**
A meeting starting early but ending very late blocks the room longer. Sorting by end time guarantees we always pick the option that releases the room fastest.

#### 📝 Steps
1. Pair up `(start[i], end[i])` into a list
2. Sort the list by **end time** ascending
3. Always select the first meeting; `freeTime = end[0]`, `count = 1`
4. For each subsequent meeting, if `start > freeTime`, select it: update `freeTime` and increment `count`

#### 💻 Code
```java
class Solution {
    static class MeetingComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            return Integer.compare(a[1], b[1]); // sort by end time
        }
    }

    public int maxMeetings(int[] start, int[] end) {
        int n = end.length;
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(new int[]{start[i], end[i]});

        Collections.sort(list, new MeetingComparator());

        int freeTime = list.get(0)[1];
        int count = 1;

        for (int i = 1; i < n; i++) {
            if (list.get(i)[0] > freeTime) {
                freeTime = list.get(i)[1];
                count++;
            }
        }
        return count;
    }
}
```

#### 🧪 Dry Run

Input: `Start = [1,3,0,5,8,5]`, `End = [2,4,6,7,9,9]`

```
Pairs: (1,2), (3,4), (0,6), (5,7), (8,9), (5,9)

Sort by end time: (1,2), (3,4), (0,6), (5,7), (8,9), (5,9)
                  end=2   end=4   end=6   end=7   end=9   end=9

freeTime = 2, count = 1

i=1 (3,4): start=3 > freeTime=2? Yes → select. freeTime=4, count=2
i=2 (0,6): start=0 > freeTime=4? No → skip
i=3 (5,7): start=5 > freeTime=4? Yes → select. freeTime=7, count=3
i=4 (8,9): start=8 > freeTime=7? Yes → select. freeTime=9, count=4
i=5 (5,9): start=5 > freeTime=9? No → skip

Return count = 4 ✅
Selected meetings: (1,2), (3,4), (5,7), (8,9)
```

**New example:** `Start = [1,4,6,9]`, `End = [2,5,7,12]`

```
Pairs sorted by end: (1,2), (4,5), (6,7), (9,12)

freeTime=2, count=1
i=1: start=4 > 2? Yes → freeTime=5, count=2
i=2: start=6 > 5? Yes → freeTime=7, count=3
i=3: start=9 > 7? Yes → freeTime=12, count=4

Return 4 ✅
```

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N log N)** ✅ | O(N) |

---

## 2. Minimum Number of Platforms Required for a Railway

### 🧩 Problem Statement

Given arrival and departure times of trains, find the **minimum number of platforms** needed so no train waits.

**Example 1:**
```
Input:  Arrival = [900,940,950,1100,1500,1800]
        Departure = [910,1200,1120,1130,1900,2000]
Output: 3
```

**Example 2:**
```
Input:  Arrival = [900,1100,1235], Departure = [1000,1200,1240]
Output: 1
```

**Constraints:**
- `1 <= N <= 10⁵`
- `0000 <= Arrival[i] <= Departure[i] <= 2359`

---

### 🔴 Approach 1 — Brute Force (Count Overlaps)
**Time: O(N²) | Space: O(1)**

#### 💡 Intuition
For each train `i`, count how many other trains `j` are present at the station **when train i arrives** — i.e., `Arrival[j] <= Arrival[i] <= Departure[j]`. The maximum such count across all trains is the answer.

#### 💻 Code
```java
class Solution {
    public int findPlatform(int[] Arrival, int[] Departure) {
        int n = Arrival.length;
        int maxCount = 1;

        for (int i = 0; i < n; i++) {
            int count = 1;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if ((Arrival[i] >= Arrival[j]) && (Departure[j] >= Arrival[i])) {
                        count++;
                    }
                    maxCount = Math.max(count, maxCount);
                }
            }
        }
        return maxCount;
    }
}
```

#### 🧪 Dry Run

Input: `Arrival = [900,1000,1200]`, `Departure = [1000,1200,1240]`

```
i=0 (Arrival=900):
  j=1: Arrival[0]>=Arrival[1]? 900>=1000? No → skip
  j=2: 900>=1200? No → skip
  count=1, maxCount=1

i=1 (Arrival=1000):
  j=0: 1000>=900? Yes, Departure[0]=1000>=1000? Yes → count=2
  j=2: 1000>=1200? No
  maxCount=2

i=2 (Arrival=1200):
  j=0: 1200>=900? Yes, Departure[0]=1000>=1200? No
  j=1: 1200>=1000? Yes, Departure[1]=1200>=1200? Yes → count=2
  maxCount=2

Return 2 ✅
```

---

### 🟢 Approach 2 — Optimal (Sort + Two Pointers)
**Time: O(N log N) | Space: O(1)**

#### 💡 Intuition
Sort `Arrival[]` and `Departure[]` **independently**. Use two pointers `i` (arrivals) and `j` (departures). Walk through events in chronological order:
- If the next arrival happens **before or at the same time** as the earliest pending departure → a new platform is needed (`count++`)
- Otherwise → a platform frees up (`count--`)

Track the maximum `count` seen — that's the answer.

**Why sort independently (not as pairs)?**
We only care about the *count of trains present* at any moment in time, not which specific train is which. Sorting independently still correctly counts simultaneous arrivals vs departures.

#### 💻 Code
```java
class Solution {
    public int findPlatform(int[] Arrival, int[] Departure) {
        int n = Arrival.length;
        Arrays.sort(Arrival);
        Arrays.sort(Departure);

        int ans = 1;
        int count = 1;
        int i = 1, j = 0;

        while (i < n && j < n) {
            if (Arrival[i] <= Departure[j]) {
                count++;
                i++;
            } else {
                count--;
                j++;
            }
            ans = Math.max(ans, count);
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `Arrival = [900,940,950,1100,1500,1800]`, `Departure = [910,1200,1120,1130,1900,2000]`

```
Sorted Arrival:   [900, 940, 950, 1100, 1500, 1800]
Sorted Departure: [910, 1120, 1130, 1200, 1900, 2000]

ans=1, count=1, i=1, j=0

Step 1: Arrival[1]=940 <= Departure[0]=910? No → count--, count=0, j=1
        ans = max(1,0) = 1

Wait, let's redo — count starts at 1 (representing train at Arrival[0]=900):

i=1, j=0:
  Arrival[1]=940 <= Departure[0]=910? No → count--=0, j=1
  ans = max(1,0) = 1

i=1, j=1:
  Arrival[1]=940 <= Departure[1]=1120? Yes → count++=1, i=2
  ans = max(1,1) = 1

i=2, j=1:
  Arrival[2]=950 <= Departure[1]=1120? Yes → count++=2, i=3
  ans = max(1,2) = 2

i=3, j=1:
  Arrival[3]=1100 <= Departure[1]=1120? Yes → count++=3, i=4
  ans = max(2,3) = 3

i=4, j=1:
  Arrival[4]=1500 <= Departure[1]=1120? No → count--=2, j=2
  ans = max(3,2) = 3

... (remaining iterations don't exceed 3)

Return ans = 3 ✅
```

> 💡 **Why does `count--` represent "a platform freed up"?**
> If the next earliest departure happens before the next arrival, that train has left — its platform is now free for a future train. We don't need to track *which* platform; we only need the count.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Count Overlaps | O(N²) | O(1) |
| Sort + Two Pointers | **O(N log N)** ✅ | **O(1)** ✅ |

---

## 3. Job Sequencing Problem

### 🧩 Problem Statement

Given jobs with `[JobID, Deadline, Profit]`, each taking 1 unit of time, find the **number of jobs completed** and **maximum profit**, where a job's profit is earned only if completed by its deadline.

**Example 1:**
```
Input:  Jobs = [[1,4,20],[2,1,10],[3,1,40],[4,1,30]]
Output: 2 60
Explanation: Job 3 at t=1 (profit 40), Job 1 at t=2 (profit 20). Total = 60
```

**Example 2:**
```
Input:  Jobs = [[1,2,100],[2,1,19],[3,2,27],[4,1,25],[5,1,15]]
Output: 2 127
```

**Constraints:**
- `1 <= N <= 10⁴`
- `1 <= Deadline <= N`
- `1 <= Profit <= 500`

---

### 🟢 Approach — Greedy: Sort by Profit, Schedule as Late as Possible
**Time: O(N log N + N²) | Space: O(maxDeadline)**

#### 💡 Intuition
To maximize profit, **always consider higher-profit jobs first**. For each job, schedule it on the **latest available day** at or before its deadline.

**Why schedule as late as possible?**
A job with deadline 4 can run on day 1, 2, 3, or 4. Running it on day 4 keeps days 1-3 free for *other* jobs that might have tighter deadlines. This maximizes flexibility for remaining jobs.

#### 📝 Steps
1. Sort jobs by **profit descending**
2. Find `maxDeadline` across all jobs
3. Create a `hash[]` of size `maxDeadline`, all initialized to `-1` (free slot)
4. For each job (in profit order), scan backward from `deadline-1` to `0` — place the job in the **first free slot** found
5. If placed: increment `count`, add to `total profit`

#### 💻 Code
```java
class Solution {
    public int[] JobScheduling(int[][] Jobs) {
        int n = Jobs.length;
        Arrays.sort(Jobs, (a, b) -> b[2] - a[2]); // sort by profit descending

        int maxDeadline = -1;
        for (int[] num : Jobs) maxDeadline = Math.max(maxDeadline, num[1]);

        int[] hash = new int[maxDeadline];
        Arrays.fill(hash, -1);

        int count = 0, total = 0;

        for (int i = 0; i < n; i++) {
            for (int j = Jobs[i][1] - 1; j >= 0; j--) {
                if (hash[j] == -1) {
                    count++;
                    hash[j] = Jobs[i][0];
                    total += Jobs[i][2];
                    break;
                }
            }
        }
        return new int[]{count, total};
    }
}
```

#### 🧪 Dry Run

Input: `Jobs = [[1,4,20],[2,1,10],[3,1,40],[4,1,30]]`

```
Sort by profit desc: [3,1,40], [4,1,30], [1,4,20], [2,1,10]

maxDeadline = 4
hash = [-1,-1,-1,-1]  (slots for days 1,2,3,4 → indices 0,1,2,3)

Job [3,1,40]: deadline=1, scan j=0
  hash[0]=-1 → place! hash=[3,-1,-1,-1], count=1, total=40

Job [4,1,30]: deadline=1, scan j=0
  hash[0]=3 (occupied) → no more j to check (j=-1 stops) → can't place

Job [1,4,20]: deadline=4, scan j=3,2,1,0
  hash[3]=-1 → place! hash=[3,-1,-1,1], count=2, total=60

Job [2,1,10]: deadline=1, scan j=0
  hash[0]=3 (occupied) → can't place

Return [2, 60] ✅
```

**New example:** `Jobs = [[1,1,100],[2,2,200],[3,3,300],[4,4,400]]`

```
Sort by profit desc: [4,4,400], [3,3,300], [2,2,200], [1,1,100]
maxDeadline = 4
hash = [-1,-1,-1,-1]

Job [4,4,400]: deadline=4, scan j=3 → free → place. hash=[-1,-1,-1,4], total=400, count=1
Job [3,3,300]: deadline=3, scan j=2 → free → place. hash=[-1,-1,3,4], total=700, count=2
Job [2,2,200]: deadline=2, scan j=1 → free → place. hash=[-1,2,3,4], total=900, count=3
Job [1,1,100]: deadline=1, scan j=0 → free → place. hash=[1,2,3,4], total=1000, count=4

Return [4, 1000] ✅
```

> 💡 **Why scan backward from `deadline-1` to `0`, not forward?**
> Scanning backward finds the **latest** available slot first. If we scanned forward, we might fill an early slot that a tighter-deadline job needs later, blocking it unnecessarily.

#### 📊 Complexity

| Time | Space |
|------|-------|
| O(N log N + N²) | O(maxDeadline) |

---

## 4. Fractional Knapsack

### 🧩 Problem Statement

Given `n` items with `val[i]` and `wt[i]`, and a knapsack of `capacity`, **fractions of items are allowed**. Maximize total value, rounded to 6 decimal places.

**Example 1:**
```
Input:  val=[60,100,120], wt=[10,20,30], capacity=50
Output: 240.000000
Take item 0 (full), item 1 (full), 2/3 of item 2 → 60+100+80=240
```

**Example 2:**
```
Input:  val=[60,100], wt=[10,20], capacity=50
Output: 160.000000 (both fit entirely)
```

**Constraints:**
- `1 <= n <= 10⁵`
- `1 <= capacity <= 10⁹`
- `1 <= val[i], wt[i] <= 10000`

---

### 🟢 Approach — Greedy: Sort by Value/Weight Ratio
**Time: O(N log N) | Space: O(N)**

#### 💡 Intuition
Since we can take **fractions**, the goal is to maximize value per unit of weight used. Compute `ratio = value/weight` for each item, sort descending, and greedily fill the knapsack — taking whole items while they fit, and a fraction of the next item to exactly fill remaining capacity.

**Why does the greedy choice guarantee optimality here (unlike 0/1 knapsack)?**
Because fractions are allowed, there's no "wasted" capacity. Always picking the highest ratio first ensures every unit of capacity used contributes the maximum possible value — no better arrangement exists.

#### 📝 Steps
1. For each item, compute `ratio[i] = val[i] / wt[i]`, paired with index `i`
2. Sort `ratio[]` descending
3. For each item in sorted order:
   - If `wt[i] <= cap` → take it fully, `totalValue += val[i]`, `cap -= wt[i]`
   - Else → take fraction `cap/wt[i]`, `totalValue += val[i] * (cap/wt[i])`, break
4. Return `totalValue` rounded to 6 decimals

#### 💻 Code
```java
class Solution {
    public double fractionalKnapsack(int[] val, int[] wt, long cap) {
        int n = val.length;
        double[][] ratio = new double[n][2];

        for (int i = 0; i < n; i++)
            ratio[i] = new double[]{(double) val[i] / wt[i], i};

        Arrays.sort(ratio, (a, b) -> Double.compare(b[0], a[0]));

        double totalValue = 0.0;
        for (double[] r : ratio) {
            int i = (int) r[1];
            if (wt[i] <= cap) {
                totalValue += val[i];
                cap -= wt[i];
            } else {
                totalValue += val[i] * ((double) cap / wt[i]);
                break;
            }
        }
        return Math.round(totalValue * 1e6) / 1e6;
    }
}
```

#### 🧪 Dry Run

Input: `val=[60,100,120], wt=[10,20,30], capacity=50`

```
Compute ratios:
  item 0: 60/10 = 6.0
  item 1: 100/20 = 5.0
  item 2: 120/30 = 4.0

Sort descending: item0(6.0), item1(5.0), item2(4.0)

cap=50, totalValue=0

item0: wt=10 <= cap=50? Yes → totalValue=60, cap=50-10=40
item1: wt=20 <= cap=40? Yes → totalValue=60+100=160, cap=40-20=20
item2: wt=30 <= cap=20? No → take fraction
       totalValue += 120 * (20/30) = 120 * 0.6667 = 80
       totalValue = 160+80 = 240
       break (cap exhausted)

Return 240.000000 ✅
```

**Example 2:** `val=[60,100], wt=[10,20], capacity=50`

```
ratios: item0=6.0, item1=5.0
Sort: item0, item1

item0: wt=10<=50 → totalValue=60, cap=40
item1: wt=20<=40 → totalValue=160, cap=20

Loop ends (no more items)
Return 160.000000 ✅
```

> 💡 **Why `Math.round(totalValue * 1e6) / 1e6`?**
> This rounds to exactly 6 decimal places. Multiplying by `1e6` shifts the 6th decimal into the integer part, `Math.round` rounds it, then dividing by `1e6` shifts it back — avoiding floating-point precision artifacts in the output.

#### 📊 Complexity

| Time | Space |
|------|-------|
| **O(N log N)** ✅ | O(N) for ratio array |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| N Meetings | Sort by end time — earliest-ending meeting frees the room soonest, maximizing future options |
| Min Platforms | Sort arrival/departure independently. Two pointers count simultaneous trains — count++ on arrival, count-- on departure |
| Job Sequencing | Sort by profit descending. Schedule each job at its LATEST free slot ≤ deadline — preserves flexibility for tighter-deadline jobs |
| Fractional Knapsack | Sort by value/weight ratio descending. Fractions allowed → greedy is provably optimal, unlike 0/1 knapsack |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
