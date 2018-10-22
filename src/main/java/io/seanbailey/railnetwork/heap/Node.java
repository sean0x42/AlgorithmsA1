package io.seanbailey.railnetwork.heap;

/**
 * A node in a weight heap.
 * @see io.seanbailey.railnetwork.heap.WeightedHeap;
 * @author Sean Bailey c3279343
 */
public class Node<T> implements Comparable<Node<T>> {

  private T value;
  private int weight;

  /**
   * Constructs a new node.
   * @param value Value to store in node.
   */
  public Node(T value) {
    this(value, Integer.MAX_VALUE);
  }

  /**
   * Constructs a new node.
   * @param value Value to store in node.
   * @param weight Value's corresponding weight.
   */
  public Node(T value, int weight) {
    this.value = value;
    this.weight = weight;
  }

  /**
   * Compare this node to another node.
   * @param node Node to compare to.
   * @return The total order of the two nodes.
   */
  @Override
  public int compareTo(Node<T> node) {
    return weight - node.getWeight();
  }

  /**
   * @return a representation of this node as a string.
   */
  @Override
  public String toString() {
    return "Node{" +
      "weight: " + weight +
      ", value: " + value +
      "}";
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }
}
