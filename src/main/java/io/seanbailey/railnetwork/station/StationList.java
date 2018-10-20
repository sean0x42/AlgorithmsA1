package io.seanbailey.railnetwork.station;

import java.util.Iterator;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.util.SortUtil;

/**
 * A special list of stations which can be quickly and efficiently searched.
 * @author Sean Bailey c3279343
 */
public class StationList implements Iterable<Station> {

  private Station[] stations;

  /**
   * Constructs a new station list.
   * @param size Length of list.
   */
  public StationList(int size) {
    stations = new Station[size];
  }

  /**
   * Places a station at a particular index.
   * @param i Index to place station at.
   * @param station Station to add.
   */
  public void put(int i, Station station) {
    stations[i] = station;
  }

  /**
   * Retrieves a station at a particular index.
   * @param i Index of station.
   * @return Station at index.
   */
  public Station get(int i) {
    return stations[i];
  }

  /**
   * @return the number of stations in the list.
   */
  public int getLength() {
    return stations.length;
  }

  /**
   * Sorts the internal array.
   */
  public void sort() {
    SortUtil.quickSort(stations);
  }

  /**
   * Constructs and returns an iterator.
   * @return A station iterator.
   */
  public Iterator<Station> iterator() {
    return new Iterator<Station>() {
      private int i = 0;

      public boolean hasNext() {
        return i < stations.length - 1;
      }

      public Station next() {
        return stations[i++];
      }
    };
  }

  /**
   * Searches for and retrieves a station.
   * @param name Station name.
   * @param line Station line.
   * @return Corresponding station (from station list) or null.
   */
  public Station find(String name, String line) {
    return find(new Station(name, line));
  }

  /**
   * Searches for and retrieves a station.
   *
   * <p>
   * Note that the station given as a parameter is only used for searching
   * purposes.
   * </p>
   *
   * @param station Station to search for.
   * @return Corresponding station (from station list) or null.
   */
  public Station find(Station station) {
    // Keep track of the current searching range
    int i = 0;
    int j = stations.length - 1;

    while (i <= j) {
      // Check halfway between i and j
      int midway = (i + j) / 2;
      int delta = station.compareTo(get(midway));

      // Compare
      if (delta == 0) {
        return get(midway);
      } else if (delta < 0) {
        j = midway - 1;
      } else {
        i = midway + 1;
      }
    }

    // Station not found
    return null;
  }

  /**
   * Determines whether a station with this name exists.
   * @param name Name of station.
   * @return Whether such a station exists.
   */
  public boolean contains(String name) {
    // Keep track of the current search range
    int i = 0;
    int j = stations.length - 1;

    while (i <= j) {
      int midway = (i + j) / 2;
      int delta = name.compareTo(get(midway).getName());

      // Compare
      if (delta == 0) {
        return true;
      } else if (delta < 0) {
        j = midway - 1;
      } else {
        i = midway + 1;
      }
    }

    return false;
  }
}
