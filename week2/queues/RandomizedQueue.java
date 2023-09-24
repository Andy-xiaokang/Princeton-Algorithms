/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private static final int INIT_CAPACITY = 8;
    private int capacity;
    private int size;  // number of elements in Queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[INIT_CAPACITY];
        size = 0;
        capacity = INIT_CAPACITY;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        capacity = newSize;
        array = newArray;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");
        if (size == capacity) resize(capacity * 2);
        array[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        int index = StdRandom.uniformInt(size);
        Item item = array[index];
        array[index] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0 && size == capacity / 4) resize(capacity / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        return array[StdRandom.uniformInt(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int remain;
        private Item[] copy;

        public RandomizedQueueIterator() {
            copy = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copy[i] = array[i];
            }
            StdRandom.shuffle(copy);
            remain = size;
        }

        public boolean hasNext() {
            return remain > 0;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("don't have next");
            return copy[--remain];
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
