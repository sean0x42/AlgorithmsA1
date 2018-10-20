package io.seanbailey.railnetwork;

import io.seanbailey.railnetwork.exception.ValidationException;
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

  /**
   * Finds and prints the shortest path between two points in the network.
   *
   * <p>
   * This is a modified version of Dijkstra's algorithm, that makes use of an 
   * adjacency list (in the form of a
   * @link{io.seanbailey.railnetwork.station.StationList}) and an indirect max
   * heap.
   * </p>
   *
   * @param origin Station name to start from.
   * @param destination Station name to finish at.
   * @throws ValidationException if the origin or destination are invalid.
   */
  public void findShortestPath(String origin, String destination)
      throws ValidationException {
    // Ensure source exists
    if (!stations.contains(origin)) {
      throw new ValidationException("Origin '%s' not found.", origin);
    }

    // Ensure destination exists
    if (!stations.contains(destination)) {
      throw new ValidationException("Destination '%s' not found.", destination);
    }
  }
}
