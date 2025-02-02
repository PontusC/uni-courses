/**
 * QuickSort implementation with a fixed pivot. Sorts given array.
 * Switches over to Insertion sort when given range is small enough.
 *
 * @author Pontus
 * @version 2016-03
 */
public class QuickSortFixedPivotInsertion implements IntSorter{

    public QuickSortFixedPivotInsertion(){

    }

    /**
     * Sorts given array in ascending order.
     * @param array to be sorted.
     */
    public void sort(int[] array){
        if (isSorted(array))
            return;
        if (isReversed(array)){
            reverseArray(array);
            return;
        }
        quicksort(array, 0, array.length-1);
    }

    /**
     * Method that performs the sorting and recursively calls itself until sorting is completed.
     * @param array to be sorted.
     * @param lowerIndex the lower index of the range to sort.
     * @param higherIndex the upper index of the range to sort.
     */
    private void quicksort(int[] array, int lowerIndex, int higherIndex) {
        int index = sort(array, lowerIndex, higherIndex);
        if ((index - 1) - lowerIndex < 22) {
            insertionSort(array, lowerIndex, index - 1);
        } else {
            quicksort(array, lowerIndex, index - 1);
        }
        if (higherIndex - index < 22) {
            insertionSort(array, index, higherIndex);
        } else {
            quicksort(array, index, higherIndex);
        }
    }

    /**
     * Method that performs the actual quicksort swaps.
     * @param array to be sorted.
     * @param lowerIndex the lower index of the range to sort.
     * @param higherIndex the upper index of the range to sort.
     * @return the lower index, used to determine where to split the array for the next sort.
     */
    private int sort(int[] array, int lowerIndex, int higherIndex){
        int left = lowerIndex;
        int right = higherIndex;
        // Fixed pivot
        int pivot = array[(left+right)/2];

        // The actual sorting
        while (left <= right){
            while (array[left] < pivot)
                left++;
            while(array[right] > pivot)
                right--;
            if (left <= right) {
                // Swaps the elements
                int temp = array[left];
                array[left] = array[right];
                array[right] = temp;
                left++;
                right--;
            }
        }
        return left;
    }

    /**
     * Method that sorts an array following the insertion sort algorithm
     * @param array to be sorted
     */
    public void insertionSort(int[] array, int lowerIndex, int higherIndex)
    {
        for (int i = lowerIndex + 1; i < higherIndex + 1; i++) {
            int j = i;
            while (j > 0 && (array[j-1] > array[j])){
                int temp = array[j];
                array[j] = array[j-1];
                array[j-1] = temp;
                j --;
            }
        }
    }

    /**
     * Method that checks if the given array is sorted in ascending order, or full of the same values.
     * @param array to be checked.
     * @return false if not sorted, true if sorted.
     */
    private boolean isSorted(int[] array){
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i+1])
                return false;
        }
        return true;
    }

    /**
     * Method that checks if the given array is sorted in descending order.
     * @param array to be checked.
     * @return false if not in descending order, true if.
     */
    private boolean isReversed(int[] array){
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] < array[i+1])
                return false;
        }
        return true;
    }

    /**
     * Method to reverse an array.
     * @param array to be reversed
     */
    private void reverseArray(int[] array){
        int length = array.length;
        for (int i = 0; i < length/2; i++) {
            int temp = array[i];
            array[i] = array[length - i - 1];
            array[length - i - 1] = temp;
        }
    }
}
