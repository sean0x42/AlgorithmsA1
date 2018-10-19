import io.seanbailey.railnetwork.Main;

/**
 * Entry point to the application. This essentially acts as an interface to 
 * @link{io.seanbailey.railnetwork.Main}.
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
 * @author Sean Bailey c3279343
 */
public class assign1 {

  /**
   * Entry point.
   * @param args An array of command line args from STDIN.
   */
  public static void main(String[] args) {
    Main.main(args);
  }
}
