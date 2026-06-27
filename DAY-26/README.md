# 🚀 Day 26/45 – #SDESheetChallenge

> Solving problems from Striver's SDE Sheet every day. Documenting every approach, dry run, and intuition — so anyone can follow along.

---

## 📋 Problems Covered Today

| # | Problem | Difficulty | Topic |
|---|---------|-----------|-------|
| 1 | [Next Smaller Element](#1-next-smaller-element) | 🟡 Medium | Monotonic Stack |
| 2 | [LRU Cache](#2-lru-cache) | 🟡 Medium | HashMap + Doubly Linked List |
| 3 | [LFU Cache](#3-lfu-cache) | 🔴 Hard | HashMap + Doubly Linked List |

---

## 1. Next Smaller Element

### 🧩 Problem Statement

Given an array `arr`, for each element find the **first element to its right that is smaller**. If none exists, return `-1`.

**Example 1:**
```
Input:  arr = [4, 8, 5, 2, 25]
Output: [2, 5, 2, -1, -1]
```

**Example 2:**
```
Input:  arr = [10, 9, 8, 7]
Output: [9, 8, 7, -1]
```

**Constraints:** `1 <= arr.length <= 10⁵`, `-10⁹ <= arr[i] <= 10⁹`

---

### 🔴 Approach 1 — Brute Force
**Time: O(N²) | Space: O(N)**

#### 💡 Intuition
For each element, scan all elements to its right. The first one smaller than it is the NSE.

#### 💻 Code
```java
class Solution {
    public int[] nextSmallerElements(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Arrays.fill(ans,-1);
        for(int i = 0; i < n ; i++){
            for(int j = i+1; j < n ; j++){
                if(arr[j] < arr[i]){
                    ans[i] = arr[j]; 
                    break;
                }
            }
        }
        return ans;
    }
}
```

---

### 🟢 Approach 2 — Optimal (Monotonic Stack)
**Time: O(N) | Space: O(N)**

#### 💡 Intuition
Traverse **right to left**. Maintain a stack of candidates. For each element:
- Pop all stack elements **≥ current** — they can never be NSE for anything to the left
- Stack top (if non-empty) is the NSE
- Push current element

The stack stays strictly increasing from top to bottom at all times.

#### 💻 Code
```java
class Solution {
    public int[] nextSmallerElements(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        Arrays.fill(ans,-1);
        Stack<Integer> st = new Stack<>();
        for(int i = n -1; i >=0 ; i--){
            while(!st.isEmpty() && st.peek() >= arr[i]){
                st.pop();
            }
            if(!st.isEmpty()) 
                ans[i] = st.peek();
            st.push(arr[i]);
        }
        return ans;
    }
}
```

#### 🧪 Dry Run

Input: `[4, 8, 5, 2, 25]`

```
i=4 (25): stack=[]  → ans[4]=-1, push 25.  stack=[25]
i=3 (2):  pop 25    → stack=[]. ans[3]=-1,  push 2.   stack=[2]
i=2 (5):  2<5 no pop → ans[2]=2, push 5.  stack=[2,5]
i=1 (8):  5<8 no pop → ans[1]=5, push 8.  stack=[2,5,8]
i=0 (4):  pop 8, pop 5 → stack=[2]. ans[0]=2, push 4. stack=[2,4]

Result: [2, 5, 2, -1, -1] ✅
```

> 💡 **NSE vs NGE:** Identical pattern. NGE: pop elements **≤ current**. NSE: pop elements **≥ current**.

#### 📊 Complexity Summary

| Approach | Time | Space |
|---------|------|-------|
| Brute Force | O(N²) | O(N) |
| Monotonic Stack | **O(N)** ✅ | **O(N)** ✅ |

---

## 2. LRU Cache

### 🧩 Problem Statement

Design an **LRU Cache** with O(1) `get` and `put`.

- `get(key)` → value if exists else -1; mark as recently used
- `put(key, value)` → insert/update; on capacity overflow, evict **least recently used**

**Constraints:** `1 <= capacity <= 1000`, at most `10⁵` calls

---

### 🟢 Approach — HashMap + Doubly Linked List
**Time: O(1) | Space: O(capacity)**

#### 💡 Intuition
- **HashMap** `key → Node` for O(1) lookup
- **Doubly Linked List** maintains usage order — MRU at front (after dummy head), LRU just before dummy tail

**Why doubly linked?** Deleting an arbitrary node needs its previous pointer. Singly linked can't do that in O(1).

**`get`:** lookup node, delete it, reinsert at front → O(1)  
**`put`:** if exists → update + move to front. If new + full → delete tail.prev (LRU) → insert new at front.

#### 💻 Code
```java
class Node {
    public int key, val;
    public Node next, prev;
    Node() { key = val = -1; next = prev = null; }
    Node(int k, int value) { key = k; val = value; next = prev = null; }
}

class LRUCache {
    private Map<Integer, Node> map;
    private int cap;
    private Node head;
    private Node tail;

    private void deleteNode(Node node){
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    private void insertAfterHead(Node node){
        Node nextNode = head.next;
        head.next = node;
        nextNode.prev = node;
        node.prev = head;
        node.next = nextNode;
    }

    public LRUCache(int capacity) {
        cap = capacity;
        map = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
       if(!map.containsKey(key)) return -1;
        Node node = map.get(key);
        int val = node.val;
        deleteNode(node);
        insertAfterHead(node);
        return val;
    }

    public void put(int key, int value) {
      if(map.containsKey(key)){
        Node node = map.get(key);
        node.val = value;
        deleteNode(node);
        insertAfterHead(node);
        return;
      }
      if(map.size() == cap){
        Node node = tail.prev;
        map.remove(node.key);
        deleteNode(node);
      }
      Node newNode = new Node(key,value);
      map.put(key,newNode);
      insertAfterHead(newNode);
    }
}
```

#### 🧪 Dry Run

`LRUCache(2)` → put(1,1) → put(2,2) → get(1) → put(3,3) → get(2)

```
put(1,1): H <-> [1] <-> T
put(2,2): H <-> [2] <-> [1] <-> T

get(1):   delete [1], insertAfterHead → H <-> [1] <-> [2] <-> T  → return 1 ✅

put(3,3): full → evict tail.prev=[2], delete from map
          H <-> [1] <-> T
          insert [3] → H <-> [3] <-> [1] <-> T

get(2):   not in map → return -1 ✅
```

---

## 3. LFU Cache

### 🧩 Problem Statement

Design an **LFU Cache** with O(1) `get` and `put`. Eviction policy: remove the least **frequently** used key. On tie → remove **least recently** used among tied keys.

**Constraints:** `1 <= capacity <= 10³`, at most `10⁵` calls

---

### 🟢 Approach — Two HashMaps + Doubly Linked Lists per Frequency
**Time: O(1) | Space: O(capacity)**

#### 💡 Intuition
LFU needs to track frequency AND recency within the same frequency group.

Three structures:
- `keyNode` map → `key → Node` (O(1) access)
- `freqListMap` → `frequency → DoublyLinkedList` of nodes at that frequency (MRU at front)
- `minFreq` → always tracks the current minimum frequency

**`updateFreqListMap`:** On any access:
1. Remove node from `freqListMap[cnt]`
2. If that was the last node at `minFreq` → increment `minFreq`
3. `node.cnt++`, add to front of `freqListMap[cnt+1]`

**Eviction:** Remove `tail.prev` from `freqListMap[minFreq]`.

**New key:** Always set `minFreq = 1` (new key has freq=1, lowest possible).

#### 💻 Code
```java
class Node {
    int key, value, cnt;
    Node next, prev;
    Node(int _key, int _value) { key = _key; value = _value; cnt = 1; }
}

class List {
    int size;
    Node head, tail;
    List() {
        head = new Node(0, 0); tail = new Node(0, 0);
        head.next = tail; tail.prev = head; size = 0;
    }
    void addFront(Node node) {
        Node temp = head.next;
        node.next = temp; node.prev = head;
        head.next = node; temp.prev = node; size++;
    }
    void removeNode(Node delnode) {
        Node prevNode = delnode.prev, nextNode = delnode.next;
        prevNode.next = nextNode; nextNode.prev = prevNode; size--;
    }
}

class LFUCache {
    private Map<Integer, Node> keyNode;
    private Map<Integer, List> freqListMap;
    private int maxSizeCache, minFreq, curSize;

    public LFUCache(int capacity) {
        maxSizeCache = capacity; minFreq = 0; curSize = 0;
        keyNode = new HashMap<>(); freqListMap = new HashMap<>();
    }

    private void updateFreqListMap(Node node) {
        keyNode.remove(node.key);
        freqListMap.get(node.cnt).removeNode(node);
        if (node.cnt == minFreq && freqListMap.get(node.cnt).size == 0) minFreq++;
        List nextHigherFreqList = freqListMap.containsKey(node.cnt + 1)
            ? freqListMap.get(node.cnt + 1) : new List();
        node.cnt++;
        nextHigherFreqList.addFront(node);
        freqListMap.put(node.cnt, nextHigherFreqList);
        keyNode.put(node.key, node);
    }

    public int get(int key) {
        if (!keyNode.containsKey(key)) return -1;
        Node node = keyNode.get(key);
        int val = node.value;
        updateFreqListMap(node);
        return val;
    }

    public void put(int key, int value) {
        if (maxSizeCache == 0) return;
        if (keyNode.containsKey(key)) {
            Node node = keyNode.get(key);
            node.value = value;
            updateFreqListMap(node);
        } else {
            if (curSize == maxSizeCache) {
                List list = freqListMap.get(minFreq);
                keyNode.remove(list.tail.prev.key);
                freqListMap.get(minFreq).removeNode(list.tail.prev);
                curSize--;
            }
            curSize++;
            minFreq = 1;
            List listFreq = freqListMap.containsKey(minFreq)
                ? freqListMap.get(minFreq) : new List();
            Node node = new Node(key, value);
            listFreq.addFront(node);
            keyNode.put(key, node);
            freqListMap.put(minFreq, listFreq);
        }
    }
}
```

#### 🧪 Dry Run

`LFUCache(2)` → put(1,1) → put(2,2) → get(1) → put(3,3)

```
put(1,1): minFreq=1, freqListMap={1:[node1]}, keyNode={1}
put(2,2): minFreq=1, freqListMap={1:[node2,node1]}, keyNode={1,2}

get(1):   updateFreqListMap(node1):
  remove from freq=1 → freq=1:[node2], size=1 → minFreq stays 1
  node1.cnt=2, add to freq=2 → freqListMap={1:[node2], 2:[node1]}
  return 1 ✅

put(3,3): full → evict from freqListMap[minFreq=1].tail.prev = node2
  remove key=2, curSize=1
  minFreq=1, insert node3 with cnt=1
  freqListMap={1:[node3], 2:[node1]}, keyNode={1:cnt=2, 3:cnt=1}
```

> 💡 **Why `minFreq = 1` on every new put?**
> New keys always start at frequency 1. Even if `minFreq` was 3 before, the new key is at 1 — which is the lowest. Not resetting would cause eviction to look in the wrong frequency list.

> 💡 **LRU vs LFU:**
> LRU: only recency matters. LFU: frequency first, recency breaks ties. LFU is harder because it maintains one DLL per frequency group, not just one global list.

#### 📊 Complexity

| Time per op | Space |
|-------------|-------|
| **O(1)** ✅ | O(capacity) |

---

## 📌 Key Takeaways

| Problem | Key Insight |
|---------|-------------|
| Next Smaller Element | Monotonic stack right-to-left: pop ≥ current, stack top is NSE. Same as NGE with flipped comparison |
| LRU Cache | HashMap + DLL with dummy head/tail. delete + insertAfterHead on every access. LRU = tail.prev |
| LFU Cache | keyNode map + freqListMap. Move node from freq-k to freq-(k+1) DLL on access. Track minFreq. New keys always reset minFreq=1 |

---

## 🔗 Connect

Follow along as I tackle all 45 days of the SDE Sheet — daily problems, clean explanations, and full dry runs.

> ⭐ Star this repo if it helps you | Drop a comment if anything's unclear!

---

*#SDESheetChallenge #DSA #Java #LeetCode #CompetitiveProgramming #SoftwareEngineering*
