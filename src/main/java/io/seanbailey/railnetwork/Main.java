package io.seanbailey.railnetwork;

import io.seanbailey.railnetwork.exception.ParseException;
import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.parser.StationParser;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.util.Logger;
import io.seanbailey.railnetwork.util.MinHeap;
import java.io.File;

/**
 * Main entry point to the application. For an entry point that conforms to the 
 * assignment specification, please see @link{assign1}.
 *
 * <p>
 * This program finds the shortest path (based on travel time) between a two
 * locations of a Rail Network. 
 *
 * Input:
 * <ul>
 *   <li><strong>dataFile:</strong> An XML file containing each station in the
 *   rail network, along with their connected stations and lines.</li>
 *   <li><strong>origin:</strong> Starting station.</li>
 *   <li><strong>destination:</strong> Ending station.</li>
 * </ul>
 * </p>
 *
 * @see assign1#main
 * @author Sean Bailey sean@seanbailey.io
 */
public class Main {

  private static final Logger logger = new Logger();

  /**
   * Main entry point.
   * @param args An array of command line args from STDIN.
   */
  public static void main(String[] args) {
    // Ensure we have enough args
    if (args.length < 3) {
      logger.error("Not enough arguments.");
      printUsage();
      return;
    }

    // Input
    File file;
    MinHeap<Station> stations;
    String origin = args[1];
    String destination = args[2];
    logger.debug("%s → %s", origin, destination);

    try {
      // Retrieve and validate file
      file = validateFile(args[0]);

      // Parse file and retrieve stations
      StationParser parser = new StationParser();
      stations = parser.parse(file);

      // Find shortest path
      RailNetwork network = new RailNetwork(stations);
      network.findShortestPath(origin, destination);
    } catch (ValidationException | ParseException exception) {
      logger.error(exception.getMessage());
      return;
    }
  }

  /**
   * Validates the given file path. 
   *
   * <p>
   * Note that this does not perform any validation on the contents of the file,
   * it only ensures that the file exists and is readable.
   * </p>
   *
   * @param path Path to file.
   * @return A valid file.
   * @throws ValidationException if the path is not valid.
   */
  private static File validateFile(String path) throws ValidationException {
    // init
    File file = new File(path);

    // Ensure file exists
    if (!file.exists()) {
      throw new ValidationException("File '%s' not found.", path);
    }

    // Ensure file is not a directory
    if (file.isDirectory()) {
      throw new ValidationException("'%s' is a directory. Must be a file.", path);
    }

    // Ensure file is readable
    if (!file.canRead()) {
      throw new ValidationException("Cannot read file '%s'.", path);
    }

    return file;
  }

  /**
   * Prints usage information.
   */
  private static void printUsage() {
    logger.info("Usage: java assign1 <dataFile> <origin> <destination> " +
        "[criterion]");
  }
}
