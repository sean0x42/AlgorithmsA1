package io.seanbailey.railnetwork.station;

import java.util.LinkedList;

import io.seanbailey.railnetwork.station.StationEdge;

/**
 * Represents a single station on the rail network. You could also think of
 * these as nodes on a graph.
 * @author Sean Bailey c3279343
 * @see io.seanbailey.railnetwork.station.StationEdge
 */
public class Station implements Comparable<Station> {

  private final String name;
  private final String line;
  private LinkedList<StationEdge> adjacentStations;

  /**
   * Constructs a new station.
   * @param name Station name.
   * @param line The line this station is attached to.
   */
  public Station(String name, String line) {
    this.name = name;
    this.line = line;
    adjacentStations = new LinkedList<>();
  }

  /**
   * Compares this station to another one.
   * @param station Station to compare to.
   * @return The total order of the stations.
   */
  @Override
  public int compareTo(Station station) {
    int nameDelta = name.compareTo(station.getName());

    // If names are identical, compare line
    if (nameDelta == 0) {
      return line.compareTo(station.getLine());
    }

    return nameDelta;
  }

  /**
   * @return a representation of this station as a string.
   */
  @Override
  public String toString() {
    return "Station{" + 
      "name: " + name + 
      ", line: " + line +
      ", adjacentStations: " + adjacentStations.toString() +
      "}";
  }

  /**
   * Adds a station to this stations adjacency list.
   * @param station Station to add.
   */
  public void addAdjacentStation(Station station, int distance) {
    adjacentStations.add(new StationEdge(station, distance));
  }

  public String getName() {
    return name;
  }

  public String getLine() {
    return line;
  }
}
