package io.seanbailey.railnetwork.util;

import io.seanbailey.railnetwork.station.Station;

/**
 * A utility class for performing binary searches on sorted arrays.
 * @author Sean Bailey c3279343
 */
public class SearchUtil {

  /**
   * Determines whether the given
   * @link{io.seanbailey.railnetwork.station.Station} array contains a station
   * with the given name.
   * @param stations An array of staitions.
   * @param name Name of station to look for.
   * @return Whether such a station was found.
   */
  public static boolean containsName(Station[] stations, String name) {
    // Keep track of our search range
    int i = 0;
    int j = stations.length - 1;

    // Continue until search range is zero or negative
    while (i <= j) {
      int k = (i + j) / 2; // Midway between i and j
      int delta = name.compareTo(stations[k].getName());

      if (delta == 0) {
        return true;
      } else if (delta < 0) {
        j = k - 1;
      } else {
        i = k + 1;
      }
    }

    return false;
  }

  /**
   * Finds a matching verion (according to compareTo(T)) of the given node in
   * the array.
   * @param array Array to search.
   * @param node Node to search for.
   * @return The located node or null.
   */
  public static <T extends Comparable<T>> T find(T[] array, T node) {
    // Keep track of our search range
    int i = 0;
    int j = array.length - 1;

    // Continue until search range is zero or negative
    while (i <= j) {
      int k = (i + j) / 2; // Midway between i and j
      int delta = node.compareTo(array[k]);

      if (delta == 0) {
        return array[k];
      } else if (delta < 0) {
        j = k - 1;
      } else {
        i = k + 1;
      }
    }

    return null;
  }
}
