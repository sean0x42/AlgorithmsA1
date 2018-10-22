package io.seanbailey.railnetwork.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;

public class HeapTest {

  private static final Logger logger = new Logger();

  @Test
  public void insertingIntoHeap() {
    // init
    MinHeap<String> heap = new MinHeap<>(new String[7]);
    String a = "a";
    String b = "b";
    String c = "c";
    String d = "d";
    String e = "e";
    String f = "f";
    String g = "g";

    heap.insert(b);
    heap.insert(c);
    heap.insert(d);
    heap.insert(e);
    heap.insert(f);
    heap.insert(g);
    heap.insert(a);
    assertEquals("Failure: a is not the first station.", heap.get(0), a);

    // Ensure we can survive a swap (e <-> a)
    log(heap);
    logger.debug("Swapping...");
    heap.swap(3, 6);
    log(heap);
    assertEquals("Stations were swapped but heap was not maintained.", heap.get(0), a);

    while (!heap.isEmpty()) {
      logger.debug(heap.pop());
      log(heap);
    }
  }

  private void log(MinHeap<String> heap) {
    logger.debug(Arrays.toString(heap.getNodes()));
    logger.debug(Arrays.toString(heap.getInto()));
    logger.debug(Arrays.toString(heap.getOutof()));
    logger.debug("Size: %d", heap.getSize());
  }
}
