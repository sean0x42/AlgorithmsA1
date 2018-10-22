package io.seanbailey.railnetwork;

import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.util.Logger;
import io.seanbailey.railnetwork.util.MinHeap;
import io.seanbailey.railnetwork.util.SearchUtil;

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

  private static final Logger logger = new Logger();
  private MinHeap<Station> heap;

  /**
   * Constructs a new rail network.
   * @param stations A min heap containing stations.
   */
  public RailNetwork(MinHeap<Station> stations) {
    this.heap = stations;
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
    if (!SearchUtil.containsName(heap.getNodes(), origin)) {
      throw new ValidationException("Origin '%s' not found.", origin);
    }

    // Ensure destination exists
    if (!SearchUtil.containsName(heap.getNodes(), destination)) {
      throw new ValidationException("Destination '%s' not found.", destination);
    }

    // Set distance of origin to zero
    for (int i = 0; i < heap.getSize(); i++) {
      Station station = heap.get(i);

      // Set distance to zero if we're dealing with an origin point.
      if (station.getName().equals(origin)) {
        station.setDistanceFromOrigin(0);
        logger.debug("Found origin point %s (%s)", station.getName(), station.getLine());
      }
    }

    // Heapify, since distances have changed
    heap.heapify();

    // Continue until we run out of nodes
    while (!heap.isEmpty()) {
      logger.debug("Top: %s", heap.pop());
    }
  }
}
