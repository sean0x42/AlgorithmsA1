package io.seanbailey.railnetwork.parser;

import io.seanbailey.railnetwork.exception.ParseException;
import io.seanbailey.railnetwork.exception.ValidationException;
import io.seanbailey.railnetwork.station.Station;
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

  private Station[] stations;

  /**
   * Parses the given file.
   * @param file File to parse.
   * @return An array of stations parsed from the XML file.
   * @throws ParseException if the file cannot be parsed for any reason.
   * @throws ValidationException of a node is invalid.
   */
  public Station[] parse(File file) throws ParseException, ValidationException {
    // Init
    Document document = getDocument(file);
    NodeList nodes = document.getElementsByTagName("Station");
    stations = new Station[nodes.getLength()];

    // Create stations from nodes
    for (int i = 0; i < nodes.getLength(); i++) {
      createStationFromNode(nodes.item(i));
    }

    return stations;
  }

  /**
   * Returns an XML document (DOM) from a given file.
   * @param file File to retrieve DOM from.
   * @return Document object.
   * @throws ParseException if the file cannot be parsed.
   */
  private Document getDocument(File file) throws ParseException {
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
   * <p>Note that adding adjacent stations will also be created whenever they
   * are encountered.</p>
   *
   * @param node Node to construct station from.
   * @throws ValidationException if a node is invalid.
   */
  private void createStationFromNode(Node node) throws ValidationException {
    
  }
}
