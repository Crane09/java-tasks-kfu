public class step_9 {
    /**
     * Merges two given sorted arrays into one
     *
     * @param a1 first sorted array
     * @param a2 second sorted array
     * @return new array containing all elements from a1 and a2, sorted
     */
    public static int[] mergeArrays(int[] a1, int[] a2) {
    int[] merged = new int[a1.length + a2.length];
    for (int i=0, j=0, k=0; k<merged.length; k++) {
        if (i >= a1.length) {
            merged[k] = a2[j++];
        } else if (j >= a2.length) {
            merged[k] = a1[i++];
        } else if (a1[i] < a2[j]) {
            merged[k] = a1[i++];
        } else {
            merged[k] = a2[j++];
        }
    }
    return merged;
}
public static void main(String[] args) {
    int[] a1 = {1, 3, 5, 7};
    int[] a2 = {2, 4, 6, 8, 10, 12};
    int[] merged = mergeArrays(a1, a2);
    for (int value : merged) {
        System.out.print(value + " ");
    }
}
}
