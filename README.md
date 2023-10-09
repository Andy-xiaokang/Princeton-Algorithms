# Princeton Algorithms
## week1 Percolation
Percolation.java union find  
难点是解决backwash的现象，如果直接按照课件上的方法新建两个虚拟点，然后直接调用unionfind 的API就会出现这种现象，方法是可以使用两个数组，一个用来记录方格是否open, 一个用来记录整体是否percolate.  
## week2 Deques and Randomized queues.
Deques 这个可以采用双向链表的方式处理首尾节点，此外为方便处理初始化空Deque时可以初始化一个sentinel节点方便处理。  
Randomized queues 主要注意deque方法，随机取出一个index对应的entry后与队尾entry交换,再更新。
## week3 colinear
BruteColinearPoints 建立好线段对应的数据类型ArrayList，注意好四层循环满足条件即添加对应的线段，不需要管是否重复。  
FastCollinearPoints 注意限制为n^2^logn, 排序nlogn, 最外层只能有一层循环。 难点在于添加线段时不重复，在满足最少四点成线的条件后可以再加入`p.compareTo(min(tmp, i, j - 1)) < 0` 的条件，以满足p点为线段的最低点，这样添加的线段才不会重复，然后再加入最长线段`lines.add(new LineSegment(p, max(tmp, i, j - 1)));`  
## week4 8puzzle
Board 主要难点在manhaton距离的求解上，对每一个元素，其曼哈顿距离等于与goal board 对应数值所在位置的deltax 与 deltay 绝对值之和。

```java
                deltaX = Math.abs((i - (tiles[i][j] - 1) / n));
                deltaY = Math.abs((j - (tiles[i][j] - 1) % n));
                manDistance += (deltaX + deltaY);
``` 
那个twin method 不太能理解，也不知道为什么不能使用`Std.RandomuniformedInt` 来取两对随机的tile交换。上传测试会报错。

Solver 参照的别人的解法，因为原先的处理方式只能对solvable 的puzzle 输出正确结果，对unsolvable 的puzzle会陷入死循环。参照别人的解法实在是太妙了。精彩绝伦，建议反复体会。
for initial board build a MinPQ, for initia.twin build a new MinPQ1  
for a selector between MinPQ and MinPQ1, constantly build the minheap and delMin. there must be one break the loop.  
optimization   

```java
            for (Board b : neighbors) {
                if (node.prev == null || !b.equals(node.prev.board)) {
                    selector.insert(new Node(b, node.moves + 1, node));
                }
            }
```
pay attention to when the initial node was deleted the node.prev is null, so node.prev.board will 
throw a NullPointerExpection. so you need to handle this paticular situation.  
