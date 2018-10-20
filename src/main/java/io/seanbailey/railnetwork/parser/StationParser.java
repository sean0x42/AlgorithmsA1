package io.seanbailey.railnetwork.parser;

import io.seanbailey.railnetwork.exception.ParseException;
import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.station.StationList;
import io.seanbailey.railnetwork.util.Logger;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A utility class which parses the rail network XML file into an array of
 * @link{io.seanbailey.railnetwork.station.Station Stations}.
 * @author Sean Bailey c3279343
 */
public class StationParser {

  private static final Logger logger;

  static {
    logger = new Logger(System.out, System.err);
  }

  private StationList stations;

  /**
   * Parses the given file.
   *
   * <p>
   * The parsing process occurs over four main steps.
   * <ol>
   *   <li>Parse the given file into a DOM tree.</li>
   *   <li>Traverse the DOM finding stations. Create corresponding Java
   *   objects.</li>
   *   <li>Sort the list of stations for efficient searching.</li>
   *   <li>Traverse the DOM again to find station edges.</li>
   * </ol>
   * </p>
   *
   * @param file File to parse.
   * @return An array of stations parsed from the XML file.
   * @throws ParseException if the file cannot be parsed for any reason.
   * @throws ValidationException if a node is invalid.
   */
  public StationList parse(File file) throws ParseException, 
         ValidationException {
    // Step 1: Parse XML file to DOM
    Document document = getDocument(file);

    // Step 2: Create stations
    createStations(document);

    // Step 3: Sort station array
    stations.sort();

    // Step 4: Find station edges
    addStationEdges(document);

    // Print stations for debugging purposes
    if (Logger.ENABLE_DEBUG) {
      for (Station station : stations) { 
        logger.debug(station.toString());
      }
    }

    return stations;
  }

  /**
   * Returns an XML document (DOM) from a given file.
   * @param file File to retrieve DOM from.
   * @return Document object.
   * @throws ParseException if the file cannot be parsed.
   */
  private static Document getDocument(File file) throws ParseException {
    // Init
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document document;

    // Attempt to create builder
    try {
      builder = factory.newDocumentBuilder();
      document = builder.parse(file);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
      throw new ParseException("[Bug] An unforeseen exception has occurred. " +
          "Please review the above stack trace.");
    }

    return document;
  }

  /**
   * Creates an array of stations from the DOM.
   * @param document DOM tree.
   * @return An array of stations.
   * @throws Validation exception if a station is invalid.
   */
  private void createStations(Document document) throws ValidationException {
    // Init
    NodeList nodes = document.getElementsByTagName("Station");
    stations = new StationList(nodes.getLength());

    // Create stations from nodes
    for (int i = 0; i < nodes.getLength(); i++) {
      stations.put(i, createStationFromNode(nodes.item(i)));
    }
  }

  /**
   * Attempts to create a station object from a node.
   *
   * <p>
   * Note that this process does not account for adjacent stations in any way. 
   * These should be added on a second run through the DOM.
   * </p>
   *
   * @param node Node to construct station from.
   * @return A station object.
   * @throws ValidationException if a node is invalid.
   */
  private static Station createStationFromNode(Node node) 
      throws ValidationException {
    // Init
    NodeList children = node.getChildNodes();
    String name = null;
    String line = null;

    // Iterate over children
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      switch (child.getNodeName()) {
        case "Name":
          name = child.getTextContent();
          break;
        case "Line":
          line = child.getTextContent();
      }
    }

    // Ensure required values where found
    String error = null;
    if (name == null) {
      error = "No name was defined.";
    } else if (line == null) {
      error = "No line was defined.";
    }

    if (error != null) {
      throw new ValidationException("Invalid station in XML file. %s", error);
    }

    return new Station(name, line);
  }

  /**
   * Creates a station edge from a given node.
   * @param node Node which defines the edge.
   * @param station Station which node is attached to.
   * @throws ValidationException if the station edge is invalid.
   */
  private void createEdgeFromNode(Node node, Station station)
      throws ValidationException {
    // Init
    NodeList children = node.getChildNodes();
    String name = null;
    String line = null;
    int duration = Integer.MAX_VALUE;

    // Iterate over children
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      switch (child.getNodeName()) {
        case "Name":
          name = child.getTextContent();
          break;
        case "Line":
          line = child.getTextContent();
          break;
        case "Duration":
          duration = Integer.parseInt(child.getTextContent());
      }
    }

    // Ensure required values where found
    String error = null;
    if (name == null) {
      error = "No name was defined.";
    } else if (line == null) {
      error = "No line was defined.";
    } else if (duration == Integer.MAX_VALUE) {
      error = "No duration was defined.";
    } else if (duration <= 0) {
      error = "Duration must be a positive, non-zero value.";
    }

    if (error != null) {
      throw new ValidationException("Invalid edge in XML file. %s", error);
    }

    // Find adjacent station and add
    Station adjacent = stations.find(name, line);
    station.addAdjacentStation(adjacent, duration);
  }

  /**
   * Traverses the DOM and adds station edges to each station.
   * @param document DOM tree.
   * @throws ValidationException if an invalid station is encountered.
   */
  private void addStationEdges(Document document) throws ValidationException {
    // Init
    NodeList stationEdges = document.getElementsByTagName("StationEdges");
  
    // Iterate over each edge list
    for (int i = 0; i < stationEdges.getLength(); i++) {
      Node stationEdge = stationEdges.item(i);
      Station station = findStation(stationEdge.getParentNode());
      NodeList edges = stationEdge.getChildNodes();

      // Iterate over each edge
      for (int j = 0; j < edges.getLength(); j++) {
        Node edge = edges.item(j);
        
        // Skip over text nodes
        if (edge.getNodeName().equals("#text")) {
          continue;
        }

        createEdgeFromNode(edge, station);
      }
    }
  }

  /**
   * Searches for the corresponding station.
   * @param node Station node, which corresponds to an existing station.
   * @return The found station.
   * @throws ValidationException if a station is invalid.
   */
  private Station findStation(Node node) 
      throws ValidationException {
    Station station = createStationFromNode(node);
    Station found = stations.find(station);

    if (found == null) {
      throw new ValidationException("A node in the XML file referenced a " +
          "non-existent station.");
    }

    return found;
  }
}
