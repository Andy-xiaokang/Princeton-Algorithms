/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node sentinel;
    private int size;

    private class Node {
        private Item item;
        private Node prev;
        private Node next;

        // constructor for node
        public Node(Item i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }

        // constructor for sentinel
        public Node(Node p, Node n) {
            prev = p;
            next = n;
        }
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        sentinel = new Node(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        Node first = new Node(item, null, null);
        sentinel.next.prev = first;
        first.next = sentinel.next;
        sentinel.next = first;
        first.prev = sentinel;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        Node last = new Node(item, null, null);
        sentinel.prev.next = last;
        last.prev = sentinel.prev;
        sentinel.prev = last;
        last.next = sentinel;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Item i = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return i;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Item i = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return i;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // a deque Iterator
    private class DequeIterator implements Iterator<Item> {
        private Node curr;
        private int remains;

        public DequeIterator() {
            curr = sentinel.next;
            remains = size;
        }

        public boolean hasNext() {
            return remains > 0;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("there is no next");
            Item i = curr.item;
            curr = curr.next;
            remains--;
            return i;
        }

        public void remove() {
            throw new UnsupportedOperationException("unsupported operation remove");
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<>();
        for (int i = 0; i < 5; i++) {
            dq.addFirst("A" + i);
        }
        for (int i = 0; i < 5; i++) {
            dq.addLast("B" + i);
        }
        for (String s : dq) {
            System.out.println(s);
        }
        System.out.println("dq has " + dq.size() + " elements in total");
        for (int i = 0; i < 10; i++) {
            System.out.println(dq.removeFirst());
            System.out.println(dq.removeLast());
            System.out.println(dq.size());
        }
    }

}
