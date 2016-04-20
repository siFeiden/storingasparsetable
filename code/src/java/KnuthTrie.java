import java.util.*;

public class KnuthTrie<E> extends AbstractSet<E> {

  private final int rootBranchingFactor;
  private final int nodeBranchingFactor;
  private Node root;

  public KnuthTrie(int rootBranchingFactor, int nodeBranchingFactor) {
    this.rootBranchingFactor = rootBranchingFactor;
    this.nodeBranchingFactor = nodeBranchingFactor;
  }

  public boolean add(final E e) {
    Objects.requireNonNull(e);
    final int hashCode = e.hashCode();

    if ( root == null ) {
      root = new Node(e, hashCode, rootBranchingFactor);
      return true;
    }

    if ( root.hash == hashCode ) {
      return false;
    }

    int mod = hashCode % rootBranchingFactor;
    int div = hashCode / rootBranchingFactor;
    Node oldNode = this.root;
    Node nextNode = oldNode.children[mod];

    while ( nextNode != null ) {
      if ( nextNode.hash == hashCode ) {
        return false;
      } else {
        mod = div % nodeBranchingFactor;
        div = div / nodeBranchingFactor;
        oldNode = nextNode;
        nextNode = nextNode.children[mod];
      }
    }

    oldNode.children[mod] = new Node(e, hashCode, nodeBranchingFactor);

    return true;
  }

  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  public boolean contains(final Object o) {
    if ( o == null || root == null ) {
      return false;
    }

    final int hashCode = o.hashCode();

    if ( root.hash == hashCode ) {
      return true;
    }

    int mod = hashCode % rootBranchingFactor;
    int div = hashCode / rootBranchingFactor;
    Node nextNode = root.children[mod];

    while ( nextNode != null ) {
      if ( nextNode.hash == hashCode ) {
        return true;
      } else {
        mod = div % nodeBranchingFactor;
        div = div / nodeBranchingFactor;
        nextNode = nextNode.children[mod];
      }
    }

    return false;
  }

  @Override
  public int size() {
    int size = 0;
    for ( E ignored : this ) {
      size++;
    }

    return size;
  }

  @Override
  public Iterator<E> iterator() {
    return new KnuthTrieIterator<>(root);
  }

  @Override
  public void clear() {
    this.root = null;
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    throw new UnsupportedOperationException();
  }


  private static class Node {

    private final Object value;
    private final int hash;
    private final Node[] children;

    Node(Object value, int hash, int branchingFactor) {
      this.value = value;
      this.hash = hash;
      this.children = new Node[branchingFactor];
    }

    @Override
    public String toString() {
      return "Node{" +
          "value=" + value +
          ", children=" + children.length +
          '}';
    }
  }

  private static class KnuthTrieIterator<E> implements Iterator<E> {

    private final Deque<NodePointer> recursion;

    KnuthTrieIterator(Node root) {
      recursion = new ArrayDeque<>();

      // TODO find cleaner solution
      final Node dummy = new Node(null, 0, 1);
      dummy.children[0] = root;
      recursion.offerLast(new NodePointer(dummy, 0));
    }

    @Override
    public boolean hasNext() {
      NodePointer last = recursion.peekLast();

      while ( last != null && last.node != null ) {

        while ( last.childPointer < last.node.children.length ) {
          final Node maybeChildNode = last.node.children[last.childPointer];
          last.childPointer++;

          if ( maybeChildNode != null ) {
            recursion.offerLast(new NodePointer(maybeChildNode, 0));
            return true;
          }

        }

        recursion.pollLast();
        last = recursion.peekLast();
      }


      return false;
    }

    @Override
    public E next() {
      final NodePointer last = recursion.peekLast();

      if ( last == null ) {
        throw new NoSuchElementException();
      }

      return (E) last.node.value;
    }

    private class NodePointer {
      final Node node;
      int childPointer;

      public NodePointer(Node node, int childPointer) {
        this.node = node;
        this.childPointer = childPointer;
      }
    }
  }

  public static void main(String[] args) {
    final KnuthTrie<Integer> trie = new KnuthTrie<>(8, 4);
    Arrays.asList(150, 174, 190, 31).forEach(trie::add);

    System.out.println(trie.root);
    System.out.println(trie.root.children[6]);
    System.out.println(trie.root.children[6].children[3]);
    System.out.println(trie.root.children[7]);

    final Iterator<Integer> it = trie.iterator();

    System.out.println("print trie");
    trie.forEach(System.out::println);
  }
}
