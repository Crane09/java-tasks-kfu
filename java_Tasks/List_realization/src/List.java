package list_realization;


public abstract class List<T> {

    public abstract int size();

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract void clear();

    public abstract T get(int index);

    public abstract void set(int index, T value);

    public abstract void add(T value);

    public abstract void add(int index, T value);

    public abstract T removeAt(int index);

    public abstract boolean remove(T value);

    public abstract CustomIterator<T> iterator();

    protected static void checkIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    protected static void checkIndexForAdd(int index, int size) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }
}
