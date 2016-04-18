public class KnuthTrie {

  private final int rootBranchingFactor;
  private final int nodeBranchingFactor;
  private Node root;

  public KnuthTrie(int rootBranchingFactor, int nodeBranchingFactor) {
    this.rootBranchingFactor = rootBranchingFactor;
    this.nodeBranchingFactor = nodeBranchingFactor;
  }

  public boolean contains(final int n) {
    if ( root == null ) {
      return false;
    }

    if ( root.value == n ) {
      return true;
    }

    int mod = n % rootBranchingFactor;
    int div = n / rootBranchingFactor;
    Node nextNode = root.children[mod];

    while ( nextNode != null ) {
      if ( nextNode.value == n ) {
        return true;
      } else {
        mod = div % nodeBranchingFactor;
        div = div / nodeBranchingFactor;
        nextNode = nextNode.children[mod];
      }
    }

    return false;
  }

  public boolean insert(final int n) {
    if ( root == null ) {
      root = new Node(n, rootBranchingFactor);
      return true;
    }

    if ( root.value == n ) {
      return false;
    }

    int mod = n % rootBranchingFactor;
    int div = n / rootBranchingFactor;
    Node nextNode = root.children[mod];

    while ( nextNode != null ) {
      if ( nextNode.value == n ) {
        return false;
      } else {
        mod = div % nodeBranchingFactor;
        div = div / nodeBranchingFactor;
        final Node maybeNextNode = nextNode.children[mod];
        if ( maybeNextNode == null ) {
          nextNode.children[mod] = new Node(n, nodeBranchingFactor);
          return true;
        }
        nextNode = maybeNextNode;
      }
    }

    return false;
  }
  

  private static class Node {

    private final int value;
    private final Node[] children;

    Node(int value, int branchingFactor) {
      this.value = value;
      this.children = new Node[branchingFactor];
    }
  }
}
