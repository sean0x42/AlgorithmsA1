package io.seanbailey.railnetwork;

import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.station.StationList;

/**
 * Represents a rail network. 
 *
 * <p>
 * Each node is a @link{io.seanbailey.railnetwork.station.Station}, and each
 * weighted edge a connection between two stations.
 * </p>
 *
 * @author Sean Bailey c3279343
 */
public class RailNetwork {

  private final StationList stations;

  /**
   * Constructs a new rail network.
   * @param stations An array of stations, each with an adjacency list of
   *                 station edges.
   */
  public RailNetwork(StationList stations) {
    this.stations = stations;
  }
}
