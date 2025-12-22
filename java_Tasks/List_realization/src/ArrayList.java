package list_realization;

import java.util.NoSuchElementException;
import java.util.Objects;


public class ArrayList<T> extends List<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0");
        }
        int cap = Math.max(initialCapacity, DEFAULT_CAPACITY);
        this.data = new Object[cap];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index, size);
        return (T) data[index];
    }

    @Override
    public void set(int index, T value) {
        checkIndex(index, size);
        data[index] = value;
    }

    @Override
    public void add(T value) {
        ensureCapacity(size + 1);
        data[size++] = value;
    }

    @Override
    public void add(int index, T value) {
        checkIndexForAdd(index, size);
        ensureCapacity(size + 1);
        int move = size - index;
        if (move > 0) {
            System.arraycopy(data, index, data, index + 1, move);
        }
        data[index] = value;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T removeAt(int index) {
        checkIndex(index, size);
        T removed = (T) data[index];

        int move = size - index - 1;
        if (move > 0) {
            System.arraycopy(data, index + 1, data, index, move);
        }

        data[--size] = null;
        return removed;
    }

    @Override
    public boolean remove(T value) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], value)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public CustomIterator<T> iterator() {
        return new ArrayListIterator();
    }

    private void ensureCapacity(int needed) {
        if (needed <= data.length) {
            return;
        }
        int newCapacity = Math.max(needed, data.length * 2);
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private final class ArrayListIterator implements CustomIterator<T> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (T) data[cursor++];
        }

        @Override
        public void reset() {
            cursor = 0;
        }
    }
}
