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
}
