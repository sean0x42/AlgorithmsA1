package io.seanbailey.railnetwork;

import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.station.StationEdge;
import io.seanbailey.railnetwork.util.Logger;
import io.seanbailey.railnetwork.util.MinHeap;
import io.seanbailey.railnetwork.util.SearchUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    // Step 0: Validate
    if (origin.equals(destination))
      throw new ValidationException("Origin and destination cannot be the same.");
    
    validate("Origin", origin);
    validate("Destination", destination);
    
    // Step 1: Set distance of origin to zero
    resetMatchingStations(origin);

    // Step 2: Calculate shortest distances to every point
    calculateDistancesFromOrigin();

    // Step 3: Get best destination
    Station bestDestination = findDestination(destination);

    // Step 4: Backtrack to find shortest path
    List<Station> path = findShortestPath(bestDestination);

    // Step 5: Print path
    output(path);
  }

  /**
   * Ensure that the given value corresponds to a station with the same name.
   * @param name Human friendly name for the value.
   * @param value Actual entered value.
   * @throws ValidationException if the value is invalid.
   */
  private void validate(String name, String value) throws ValidationException {
    if (!SearchUtil.containsName(heap.getNodes(), value)) {
      throw new ValidationException("%s '%s' not found.", name, value);
    }
  }

  /**
   * Searches for stations which match the given name, and sets their distance
   * to zero.
   * @param stationName Name of station to reset.
   */
  private void resetMatchingStations(String stationName) {
    for (int i = 0; i < heap.getSize(); i++) {
      Station station = heap.get(i);

      // Set distance to zero if we're dealing with an origin point.
      if (station.getName().equals(stationName)) {
        station.setDistance(0);
        logger.debug("Found origin point %s (%s)", station.getName(), station.getLine());
      }
    }

    // Heapify, since distances have changed
    heap.heapify();
  }

  /**
   * Calculates the distance to each point in the network from the origin, using
   * Dijkstra's algorithm.
   */
  private void calculateDistancesFromOrigin() {
    // Continue until we run out of nodes
    while (!heap.isEmpty()) {
      // Get next station
      Station station = heap.pop();
      logger.debug("Working with: %s", station);

      // Calculate distances to all edges
      for (StationEdge edge : station.getAdjacentStations()) {
        Station edgeStation = edge.getStation();

        // Update distance
        int distance = station.getDistance() + edge.getDistance();
        if (distance < edgeStation.getDistance()) {
          edgeStation.setDistance(distance);
          edgeStation.setPrevious(station);
        }

        logger.debug("Working with edge: %s", edge);
      }

      heap.heapify();
    }
  }

  /**
   * Locates stations which could be potential destinations. Then returns
   * whichever of these is closest to the origin.
   * @param destinationName Name of destination station.
   * @return A station.
   */
  private Station findDestination(String destinationName) {
    // Find all possible destinations
    Station[] stations = heap.getNodes();
    Station destination = null;

    for (int i = 0; i < stations.length; i++) {
      // Get next station
      Station station = stations[i];
      if (!station.getName().equals(destinationName)) {
        continue;
      }

      // Compare to previous best
      if (destination == null || destination.getDistance() > station.getDistance()) {
        destination = station;
      }
    }

    return destination;
  }

  /**
   * Back tracks from the destination, recording the shortest path from the
   * origin.
   * @param destination The final station.
   * @return A list of stations in traversal order.
   */
  private List<Station> findShortestPath(Station destination) {
    // Init
    List<Station> path = new ArrayList<>();
    Station station = destination;

    do {
      path.add(station);
      station = station.getPrevious();
    } while (station != null);

    // Reverse array, since we started from dest
    Collections.reverse(path);

    return path;
  }

  /**
   * Traverses the shortest path, and prints it in a human readable form.
   * @param path A list of stations in the order they are tarversed.
   */
  private void output(List<Station> path) {
    Station previous = null;
    boolean first = true;

    // Traverse path
    for (int i = 0; i < path.size(); i++) {
      Station current = path.get(i);
      logger.debug("Step %d: %s", i + 1, current);

      // Keep track of the last relevant station
      if (previous == null) {
        previous = current;
      }

      // Deal with final station
      if (i == path.size() - 1) {
        printSection(previous, current, first);
        logger.info("The total trip will take approximately %d minutes.", current.getDistance());
        break;
      }

      // Check for line change
      if (!current.getLine().equals(previous.getLine())) {
        Station station = path.get(i - 1);
        printSection(previous, station, first);
        first = false;
        previous = current;
        continue;
      }
    }
  }

  /**
   * Prints a humanm readable message, which tells the reader how to get from
   * the origin to the destination.
   *
   * <p>
   * Note that the origin and destination should be on the same line.
   * </p>
   *
   * @param origin Station to start from.
   * @param destination Station to end at.
   * @param first A flag which determines whether this is the first leg of the
   *              journey.
   */
  private static void printSection(Station origin, Station destination, 
      boolean first) { 
    if (first) {
      logger.info(
          "From %s, take line %s to station %s", 
          origin.getName(), 
          origin.getLine(),
          destination.getName()
      );
      return;
    }
    
    logger.info(
        "then change line to %s, and continue to %s",
        destination.getLine(),
        destination.getName()
    );
  }
}
