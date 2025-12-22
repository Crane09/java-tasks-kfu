package list_realization;

import java.util.NoSuchElementException;
import java.util.Objects;


public class LinkedList<T> extends List<T> {

    private static final class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        checkIndex(index, size);
        return nodeAt(index).value;
    }

    @Override
    public void set(int index, T value) {
        checkIndex(index, size);
        nodeAt(index).value = value;
    }

    @Override
    public void add(T value) {
        Node<T> n = new Node<>(value);
        if (head == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    @Override
    public void add(int index, T value) {
        checkIndexForAdd(index, size);

        if (index == size) {
            add(value);
            return;
        }

        Node<T> n = new Node<>(value);

        if (index == 0) {
            n.next = head;
            head = n;
            if (tail == null) {
                tail = n;
            }
            size++;
            return;
        }

        Node<T> prev = nodeAt(index - 1);
        n.next = prev.next;
        prev.next = n;
        size++;
    }

    @Override
    public T removeAt(int index) {
        checkIndex(index, size);

        if (index == 0) {
            T removed = head.value;
            head = head.next;
            size--;
            if (size == 0) {
                tail = null;
            }
            return removed;
        }

        Node<T> prev = nodeAt(index - 1);
        Node<T> cur = prev.next;
        T removed = cur.value;
        prev.next = cur.next;
        if (cur == tail) {
            tail = prev;
        }
        size--;
        return removed;
    }

    @Override
    public boolean remove(T value) {
        if (head == null) {
            return false;
        }

        if (Objects.equals(head.value, value)) {
            removeAt(0);
            return true;
        }

        Node<T> prev = head;
        Node<T> cur = head.next;
        int idx = 1;
        while (cur != null) {
            if (Objects.equals(cur.value, value)) {
                removeAt(idx);
                return true;
            }
            prev = cur;
            cur = cur.next;
            idx++;
        }
        return false;
    }

    @Override
    public CustomIterator<T> iterator() {
        return new LinkedListIterator();
    }

    private Node<T> nodeAt(int index) {
        Node<T> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    private final class LinkedListIterator implements CustomIterator<T> {
        private Node<T> current;

        private LinkedListIterator() {
            reset();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T val = current.value;
            current = current.next;
            return val;
        }

        @Override
        public void reset() {
            current = head;
        }
    }
}
