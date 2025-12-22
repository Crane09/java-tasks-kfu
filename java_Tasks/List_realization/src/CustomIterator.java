package list_realization;


public interface CustomIterator<T> {
    boolean hasNext();

    T next();

    void reset();
}
