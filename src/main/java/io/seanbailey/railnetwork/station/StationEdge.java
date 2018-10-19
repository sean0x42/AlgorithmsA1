package io.seanbailey.railnetwork.station;

/**
 * Represents an edge from one @link{io.seanbailey.railnetwork.station.Station}
 * to another.
 * @author Sean Bailey c3279343
 * @see io.seanbailey.railnetwork.station.Station
 */
public class StationEdge {

  private final Station station;
  private final int distance;

  /**
   * Constructs a new station edge.
   * @param station Station at end of edge.
   * @param distance Distance to this station.
   */
  public StationEdge(Station station, int distance) {
    this.station = station;
    this.distance = distance;
  }

  /**
   * Returns a representation of this station edge as a string.
   *
   * <p>
   * Note that we cannot call the
   * @link{io.seanbailey.railnetwork.station.Station#toString} method because
   * this would result in a circular call chain. Instead, we can just print the
   * name of the station.
   * </p>
   *
   * @return A representation of this station edge as a string.
   */
  @Override
  public String toString() {
    return "StationEdge{" + 
      "station: " + station.getName() + 
      ", distance: " + distance +
      "}";
  }

  public Station getStation() {
    return station;
  }

  public int getDistance() {
    return distance;
  }
}
