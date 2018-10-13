package io.seanbailey.railnetwork;

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
  }

  /**
   * Prints usage information.
   */
  private static void printUsage() {
    logger.info("Usage: java assign1 <dataFile> <origin> <destination> " +
        "[criterion]");
  }
}
