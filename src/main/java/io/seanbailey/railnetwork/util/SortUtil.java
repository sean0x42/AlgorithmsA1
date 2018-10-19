package io.seanbailey.railnetwork.util;

import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.util.Logger;

/**
 * A utility class for performing sort operations on
 * @link{io.seanbailey.railnetwork.station.Station}.
 * @author Sean Bailey c3279343
 */
public class SortUtil {

  private static final Logger logger;

  static {
    logger = new Logger(System.out, System.err);
  }

  /**
   * Performs quick sort on the given station array.
   * @param stations An array of stations to sort.
   */
  public static void quickSort(Station[] stations) {
    quickSort(stations, 0, stations.length - 1);
  }

  /**
   * Performs a quick sort on the given partition.
   * @param stations An array of stations to sort.
   * @param i Start of partition.
   * @param j End of partition.
   */
  private static void quickSort(Station[] stations, int i, int j) {
    if (i < j) {
      int partition = partition(stations, i, j);
      quickSort(stations, i, partition - 1);
      quickSort(stations, partition + 1, j);
    }
  }

  /**
   * Moves a random element into place, then returns the index of this
   * partition/pivot point.
   * @param stations An array of stations to partition.
   * @param i Current start of partition.
   * @param j Current end of partition.
   * @return Partition/pivot point.
   */
  private static int partition(Station[] stations, int i, int j) {
    // Init
    logger.debug("Partitioning from %d to %d", i, j);
    int partition = i;
    Station station = stations[i];

    // Iterate to J, swapping as needed
    for (int k = i + 1; k <= j; k++) {
      if (stations[k].compareTo(station) < 0) {
        partition++;
        swap(stations, partition, k);
      }
    }

    // Move partition/pivot point into place
    swap(stations, partition, i);

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
