package io.seanbailey.railnetwork.parser;

import io.seanbailey.railnetwork.exception.ParseException;
import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.util.Logger;
import io.seanbailey.railnetwork.util.MinHeap;
import io.seanbailey.railnetwork.util.SearchUtil;
import io.seanbailey.railnetwork.util.SortUtil;
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

  private static final Logger logger = new Logger();

  private MinHeap<Station> stations;

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
  public MinHeap<Station> parse(File file) throws ParseException, 
         ValidationException {
    // Step 1: Parse XML file to DOM
    Document document = getDocument(file);

    // Step 2: Create stations
    createStations(document);

    // Step 3: Sort station array
    SortUtil.quickSort(stations);

    // Step 4: Find station edges
    addStationEdges(document);

    // Print stations for debugging purposes
    //if (Logger.ENABLE_DEBUG) {
    //  for (Station station : stations.getNodes()) { 
    //    logger.debug(station.toString());
    //  }
    //}

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
    stations = new MinHeap<>(new Station[nodes.getLength()]);

    // Create stations from nodes
    for (int i = 0; i < nodes.getLength(); i++) {
      stations.insert(createStationFromNode(nodes.item(i)));
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
    Station adjacent = findStation(name, line);
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
   * @see #findStation(Station)
   * @param name Name of station.
   * @param line Station line.
   * @return Located station or null.
   */
  private Station findStation(String name, String line) {
    return findStation(new Station(name, line));
  }

  /**
   * Searches for the corresponding station.
   * @see #findStation(Station)
   * @param node Station node, which corresponds to an existing station.
   * @throws ValidationException If the node is invalid.
   */
  private Station findStation(Node node) throws ValidationException {
    Station station = findStation(createStationFromNode(node));

    // Ensure station was found
    if (station == null) {
      throw new ValidationException("A node in the XML file referenced a non-existent station.");
    }

    return station;
  }

  /**
   * Searches for the correspond station.
   *
   * <p>
   * This function should only be called once the station array is sorted, as it
   * makes use of a binary search for efficiency.
   * </p>
   *
   * @param station Station to search for
   * @return Located station or null if not found.
   */
  private Station findStation(Station station) {
    return SearchUtil.find(stations.getNodes(), station);
  }
}
