package io.seanbailey.railnetwork.heap;

import io.seanbailey.railnetwork.heap.Node;
import io.seanbailey.railnetwork.util.Logger;
import java.lang.reflect.Array;

/**
 * A min heap whereby @link{io.seanbailey.railnetwork.heap.Node nodes} are
 * managed according to an arbitrary weight.
 * @author Sean Bailey c3279343
 */
public class WeightedHeap<T> {

  private static final Logger logger;

  static {
    logger = new Logger(System.out, System.err);
  }

  private Node<T>[] values;
  private int[] into;
  private int[] outof;
  private int size = 0;

  /**
   * Creates a new weighted heap.
   * @param length Length of heap
   */
  public WeightedHeap(int length) {
    into = new int[length];
    outof = new int[length];

    // Create generic array. A bit hacky, Java does not support generic arrays
    @SuppressWarnings("unchecked")
    final Node<T>[] values = (Node<T>[]) Array.newInstance(Node.class, length);
    this.values = values;
  }

  /**
   * Inserts a value into the heap.
   * @param value Value to insert.
   */
  public void insert(T value) {
    // Create new node
    Node<T> node = new Node<>(value);

    // Find position to insert
    int i = size + 1;
    Node<T> parent = null;
    while (i > 1) {
      // Get parent and compare
      int parentIndex = outof[(i / 2) - 1];
      parent = values[parentIndex];
      if (node.compareTo(parent) > 0) {
        break;
      }

      // Move parent down
      outof[i - 1] = parentIndex;
      into[parentIndex] = i - 1;
      i = i / 2;
    }

    // Put new value into heap 
    outof[i - 1] = size;
    into[size] = i - 1;
    values[size] = node;
    size++;
  }

  /**
   * Rebuilds the heap. Call this function if you make changes to the weights
   * withiout using @link{#updateWeight(int, int)}.
   */
  public void heapify() {
    for (int i = size / 2; i >= 1; i--) {
      siftdown(i - 1);
    }
  }

  /**
   * Sifts an element down to its correct position in the heap.
   * @param index Heap index of element to siftdown.
   */
  private void siftdown(int index) {
    int original = outof[index];
    int i;
    Node<T> node = values[original];

    // Climb down the heap until we run out of nodes
    for (i = index + 1; 2 * i <= size; i++) {
      int child = 2 * i;

      // Check if right child exists
      if (child < size) {
        Node<T> left = values[outof[child - 1]];
        Node<T> right = values[outof[child]];

        // Use right child if it's smaller
        if (right.compareTo(left) < 0) {
          child++;
        }
      }

      // Compare current to child
      Node<T> childNode = values[outof[child - 1]];
      if (childNode.compareTo(node) >= 0) {
        break;
      }
      
      // Swap child and parent
      outof[i - 1] = outof[child - 1];
      into[outof[child - 1]] = i - 1;
      i = child;
    }

    // Put node in its new place
    logger.debug("Sifted down from %d to %d", index + 1, i);
    outof[i - 1] = original;
    into[original] = i - 1;
  }

  /**
   * @return the smallest weighted element in the heap.
   */
  public T first() {
    return values[outof[0]].getValue();
  }

  /**
   * Retrieves a node at the given index.
   * @param index Index of node to retrieve.
   * @return the node at the given index.
   */
  public Node<T> get(int index) {
    return values[outof[index]];
  }

  /**
   * @return Whether the heap is empty.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  public int getSize() {
    return size;
  }
}
