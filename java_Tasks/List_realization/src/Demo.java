package list_realization;

public class Demo {
    public static void main(String[] args) {
        List<String> arr = new ArrayList<>();
        arr.add("A");
        arr.add("B");
        arr.add(1, "X");

        System.out.println("ArrayList size=" + arr.size());
        printWithIterator(arr);

        List<Integer> linked = new LinkedList<>();
        linked.add(10);
        linked.add(20);
        linked.add(0, 5);

        System.out.println("\nLinkedList size=" + linked.size());
        printWithIterator(linked);
    }

    private static <T> void printWithIterator(List<T> list) {
        CustomIterator<T> it = list.iterator();
        System.out.print("[");
        boolean first = true;
        while (it.hasNext()) {
            if (!first) System.out.print(", ");
            System.out.print(it.next());
            first = false;
        }
        System.out.println("]");

        it.reset();
        System.out.println("Iterator reset -> first element: " + (it.hasNext() ? it.next() : "<empty>"));
    }
}
