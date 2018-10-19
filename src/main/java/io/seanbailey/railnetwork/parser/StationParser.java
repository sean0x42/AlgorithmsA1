package io.seanbailey.railnetwork.parser;

import io.seanbailey.railnetwork.exception.ParseException;
import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.station.Station;
import io.seanbailey.railnetwork.util.Logger;
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

  private static final Logger logger;

  static {
    logger = new Logger(System.out, System.err);
  }

  /**
   * Parses the given file.
   * @param file File to parse.
   * @return An array of stations parsed from the XML file.
   * @throws ParseException if the file cannot be parsed for any reason.
   * @throws ValidationException of a node is invalid.
   */
  public static Station[] parse(File file) throws ParseException, 
         ValidationException {
    // Init
    Document document = getDocument(file);
    NodeList nodes = document.getElementsByTagName("Station");
    Station[] stations = new Station[nodes.getLength()];

    // Create stations from nodes
    for (int i = 0; i < nodes.getLength(); i++) {
      stations[i] = createStationFromNode(nodes.item(i));
    }

    // Sort station array, allowing us to efficiently search it later
    quickSort(stations);

    // Add adjacent stations
    NodeList edges = document.getElementsByTagName("StationEdge");
    // Cache the most recent station so we don't have to search thanks to the
    // principle of locality
    Station cache = null; 
    for (int i = 0; i < edges.getLength(); i++) {
      Node edge = edges.item(i);

    }

    // Print stations if debug is enabled
    if (Logger.ENABLE_DEBUG) {
      for (int i = 0; i < stations.length; i++) {
        logger.debug(stations[i].toString());
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
    if (name == null) {
      throw new ValidationException("Invalid station in XML file. No name was defined.");
    } else if (line == null) {
      throw new ValidationException("Invalid station in XML file. No line was defined.");
    }

    return new Station(name, line);
  }

  /**
   * Runs quick sort on the given station array.
   * 
   * <p>
   * Sorting the station array allows us to perform binary searches as needed.
   * This is especially useful when populating station edges, but has other
   * applications as well.
   * </p>
   *
   * @param stations An array of stations to sort.
   * @see io.seanbailey.railnetwork.util.Sorter#quickSort(Station[])
   */
  private static void quickSort(Station[] stations) {
    logger.debug("Performing quick sort on station array.");

    // Moved to external class to prevent bloat
    SortUtil.quickSort(stations);
  }
}
