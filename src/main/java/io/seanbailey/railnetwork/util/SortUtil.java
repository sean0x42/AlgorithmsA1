package io.seanbailey.railnetwork.util;

import io.seanbailey.railnetwork.util.Logger;
import io.seanbailey.railnetwork.util.MinHeap;

/**
 * A utility class for performing sort operations on a
 * @link{io.seanbailey.railnetwork.util.MinHeap}.
 * @author Sean Bailey c3279343
 */
public class SortUtil {

  private static final Logger logger = new Logger();

  /**
   * Performs quick sort on the given heap.
   * @param heap A min heap of stations
   */
  public static <T extends Comparable<T>> void quickSort(MinHeap<T> heap) {
    quickSort(heap, 0, heap.getSize() - 1);
  }

  /**
   * Performs a quick sort on the given partition.
   * @param heap A min heap.
   * @param i Start of partition.
   * @param j End of partition.
   */
  private static <T extends Comparable<T>> void quickSort(MinHeap<T> heap, 
      int i, int j) {
    if (i < j) {
      int partition = partition(heap, i, j);
      quickSort(heap, i, partition - 1);
      quickSort(heap, partition + 1, j);
    }
  }

  /**
   * Moves a random element into place, then returns the index of this
   * partition/pivot point.
   * @param heap A min heap to partition.
   * @param i Current start of partition.
   * @param j Current end of partition.
   * @return Partition/pivot point.
   */
  private static <T extends Comparable<T>> int partition(MinHeap<T> heap, int i,
      int j) {
    // Init
    int partition = i;
    T[] nodes = heap.getNodes();
    T node = nodes[i];

    // Iterate to J, swapping as needed
    for (int k = i + 1; k <= j; k++) {
      if (nodes[k].compareTo(node) < 0) {
        partition++;
        heap.swap(partition, k);
      }
    }

    // Move partition/pivot point into place
    heap.swap(partition, i);

    return partition;
  }

  /**
   * Swaps two elements in an array.
   * @param array Array to swap elements in.
   * @param i Index of the first element.
   * @param j Index of the second element.
   */
  public static final <T> void swap(T[] array, int i, int j) {
    T temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
}
