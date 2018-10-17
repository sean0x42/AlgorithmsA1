package io.seanbailey.railnetwork;

import java.io.File;

import io.seanbailey.railnetwork.util.Logger;

/**
 * Main entry point to the application. For an entry point that conforms to the 
 * assignment specification, please see @link{assign1}.
 * @see assign1#main
 * @author Sean Bailey sean@seanbailey.io
 */
public class Main {

  private static final Logger logger;

  static {
    logger = new Logger(System.out, System.err);
  }

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
    File file = new File(args[0]);
    String origin = args[1];
    String destination = args[2];

    // Validate file
    if (!isDataFileValid(file, args[0])) {
      return;
    }
  }

  /**
   * Prints usage information.
   */
  private static void printUsage() {
    logger.info("Usage: java assign1 <dataFile> <origin> <destination> " +
        "[criterion]");
  }

  /**
   * Determines whether the given file could be a valid data file. Note that 
   * this method does not perform any validation on the contents of the file.
   * @param file Data file to validate.
   * @param path Path to file. For error messages.
   * @return Whether the file is valid.
   */
  private static boolean isDataFileValid(File file, String path) {
    // Make sure file exists
    if (!file.exists()) {
      logger.error("File not found '%s'.", path);
      return false;
    }

    // Ensure file is not a directory
    if (file.isDirectory()) {
      logger.error("Data file should not be a directory.");
      return false;
    }

    // Ensure the file is readable
    if (!file.canRead()) {
      logger.error("Data file cannot be read.");
      return false;
    }

    return true;
  }
}
