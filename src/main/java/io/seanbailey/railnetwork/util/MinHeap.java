package io.seanbailey.railnetwork.util;

/**
 * A min heap whereby @link{io.seanbailey.railnetwork.heap.Node nodes} are
 * managed according to an arbitrary weight.
 * @author Sean Bailey c3279343
 */
public class MinHeap<T extends Comparable<T>> {

  private static final Logger logger = new Logger();

  private T[] nodes;
  private int[] into;
  private int[] outof;
  private int size = 0;

  /**
   * Creates a new weighted heap.
   * @param size Size of heap.
   */
  public MinHeap(T[] nodes) {
    this.nodes = nodes;
    into = new int[nodes.length];
    outof = new int[nodes.length];
  }

  /**
   * Insert a node into the heap.
   * @param node Node to insert.
   */
  public void insert(T node) {
    // Find position to insert
    int i = size + 1; // 2
    T parent = null;
    while (i > 1) {
      // Get parent and compare
      int p = outof[(i / 2) - 1];
      parent = nodes[p];
      if (node.compareTo(parent) >= 0) {
        break;
      }

      // Move parent down
      outof[i - 1] = p;
      into[p] = i - 1;
      i = i / 2;
    }

    // Insert new value
    outof[i - 1] = size;
    into[size] = i - 1;
    nodes[size] = node;
    size++;
  }

  /**
   * Rebuilds the heap.
   *
   * <p>
   * Call this function if you make changes to the weights. Note that this
   * function is unusual in that all values are 1 indexed, so be sure to
   * subtract one whenever accessing into or outof.
   * </p>
   */
  public void heapify() {
    for (int i = size / 2; i >= 1; i--) {
      siftdown(i);
    }
  }

  /**
   * Sifts an element down to its correct position in the heap.
   * @param index Heap index of element to siftdown. 1 indexed.
   */
  @SuppressWarnings("unchecked")
  private void siftdown(int index) {
    T node = nodes[outof[index - 1]];
    int i = index;
    int valueIndex = outof[index - 1];

    // Climb down the heap until we run out of nodes
    while (2 * i <= size) {
      int child = 2 * i;

      // Check if right child exists
      if (child < size) {
        T left = nodes[outof[child - 1]];
        T right = nodes[outof[child]];
        
        // Use right child if it's smaller
        if (right.compareTo(left) < 0) {
          child++;
        }
      }

      // Compare current to child
      T childNode = nodes[outof[child - 1]];
      if (childNode.compareTo(node) >= 0) {
        break;
      }
      
      // Swap child and parent
      outof[i - 1] = outof[child - 1];
      into[outof[child - 1]] = i - 1;
      i = child;
    }

    // Put node in its new place
    outof[i - 1] = valueIndex;
    into[valueIndex] = i - 1;
  }

  /**
   * Removes the first node from the heap, and returns it.
   * @return First node.
   */
  public T pop() {
    T node = nodes[outof[0]];

    // Move last element in heap to top
    outof[0] = outof[size - 1];
    into[outof[size - 1]] = 0;

    // Reduce size and siftdown
    size--;
    siftdown(1);

    return node;
  }

  /**
   * Swap two elements (according to their array index).
   * @param i Index of element 1
   * @param j Index of element 2
   */
  void swap(int i, int j) {
    // Physically swap the nodes around
    T temp = nodes[i];
    nodes[i] = nodes[j];
    nodes[j] = temp;

    // Update outof
    int tempOutof = outof[into[i]];
    outof[into[i]] = outof[into[j]];
    outof[into[j]] = tempOutof;
    
    // Update into
    int tempInto = into[i];
    into[i] = into[j];
    into[j] = tempInto;
  }

  /**
   * @return the smallest weighted element in the heap.
   */
  public T first() {
    return nodes[outof[0]];
  }

  /**
   * Retrieves a node at the given index.
   * @param index Index of node to retrieve.
   * @return the node at the given index.
   */
  public T get(int index) {
    return nodes[outof[index]];
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

  public T[] getNodes() {
    return nodes;
  }

  int[] getInto() {
    return into;
  }

  int[] getOutof() {
    return outof;
  }
}
