package io.seanbailey.railnetwork;

import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.heap.Node;
import io.seanbailey.railnetwork.heap.WeightedHeap;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.station.StationList;
import io.seanbailey.railnetwork.util.Logger;

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

  private static final Logger logger;

  static {
    logger = new Logger(System.out, System.err);
  }

  private final StationList stations;
  private WeightedHeap<Station> heap;

  /**
   * Constructs a new rail network.
   * @param stations An array of stations, each with an adjacency list of
   *                 station edges.
   */
  public RailNetwork(StationList stations) {
    this.stations = stations;

    // Init heap
    heap = new WeightedHeap<>(stations.getLength());
    for (Station station : stations) {
      heap.insert(station);
    }
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

    // Set distance of origin to zero
    for (int i = 0; i < heap.getSize(); i++) {
      Node<Station> node = heap.get(i);
      Station station = node.getValue();

      // Set distance to zero if we're dealing with an origin point.
      if (station.getName().equals(origin) && node.getWeight() != 0) {
        node.setWeight(0);
        logger.debug("Found origin point %s (%s)", station.getName(), station.getLine());
      }
    }

    // Heapify, since weights have changed
    heap.heapify();

    // Continue until we run out of nodes
    while (!heap.isEmpty()) {
      logger.debug("Head: %s", heap.get(0));
      
    }
  }
}
