# Day 46 — Graph: Clone Graph & Traversal Techniques

## 1. Clone Graph

**Difficulty:** Medium

**Problem Statement**
You're given a reference to a node in a connected undirected graph. Each node has:
- a unique integer value `val` (1 to 100)
- a list of `neighbors`

Return a deep copy (clone) of the graph — same structure and values, but entirely new node objects.

**Constraints**
- Number of nodes: `[0, 100]`
- `1 <= Node.val <= 100`
- Each node's value is unique
- No duplicate edges or self-loops
- Graph is connected and fully reachable from node 1

**Intuition**
The graph may contain cycles, so a naive recursive copy would loop forever. The fix: keep a map from original node → cloned node. Before cloning a node, check the map — if it's already been cloned, reuse that clone instead of creating a new one. This guarantees each node is cloned exactly once and cycles are handled correctly.

**Approach**
1. If the input node is `null`, return `null`.
2. Maintain a `Map<Node, Node>` from original → clone.
3. DFS from the given node:
   - If the node is already in the map, return its clone.
   - Otherwise, create a new clone, store it in the map, then recursively clone each neighbor and attach it to the current clone's neighbor list.
4. Return the clone of the starting node.

**Complexity**
- Time: `O(V + E)` — every node and edge is processed once.
- Space: `O(V)` — for the map and the recursion stack.

---

## 2. Traversal Techniques (DFS & BFS)

**Difficulty:** Medium

**Problem Statement**
Given an undirected connected graph with `V` vertices (`0` to `V-1`) represented as a list of edges `[u, v]`, implement both:
- Depth First Search (DFS) starting from vertex 0
- Breadth First Search (BFS) starting from vertex 0

**Constraints**
- `1 <= V, E <= 10^4`

**Intuition**
DFS explores as deep as possible along a branch before backtracking (stack-based / recursive). BFS explores all neighbors at the current depth before moving to the next level (queue-based). Both need a `visited` array to avoid revisiting nodes and infinite loops in cyclic graphs.

**Approach — DFS**
1. Build an adjacency list from the edge list (undirected → add both directions).
2. Mark the current node visited and add it to the result.
3. Recurse into every unvisited neighbor.

**Approach — BFS**
1. Build the same adjacency list.
2. Push the start node into a queue, mark it visited.
3. While the queue isn't empty: pop the front node, add to result, push all unvisited neighbors and mark them visited.

**Complexity (both)**
- Time: `O(V + E)`
- Space: `O(V)` — visited array + queue/recursion stack.
