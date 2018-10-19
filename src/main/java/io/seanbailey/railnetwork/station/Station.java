package io.seanbailey.railnetwork.station;

import java.util.LinkedList;

import io.seanbailey.railnetwork.station.StationEdge;

/**
 * Represents a single station on the rail network. You could also think of
 * these as nodes on a graph.
 * @author Sean Bailey c3279343
 * @see io.seanbailey.railnetwork.station.StationEdge
 */
public class Station {

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

  public String getName() {
    return name;
  }

  public String getLine() {
    return line;
  }
}
