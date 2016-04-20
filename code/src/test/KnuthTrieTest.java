import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class KnuthTrieTest {

  @Test
  public void add() throws Exception {
    final KnuthTrie<Integer> trie = new KnuthTrie<>(8, 4);

    assertTrue(trie.add(4));
    assertTrue(trie.add(5));
    assertTrue(trie.add(6));

    assertFalse(trie.add(4));
    assertFalse(trie.add(5));
    assertFalse(trie.add(6));
  }

  @Test(expected = NullPointerException.class)
  public void addNull() throws Exception {
    final KnuthTrie<Integer> trie = new KnuthTrie<>(8, 4);
    trie.add(null);
  }

  @Test
  public void isEmptyAndClear() throws Exception {
    final KnuthTrie<Integer> trie = new KnuthTrie<>(8, 4);

    assertTrue(trie.isEmpty());

    fillTrie(trie, 4, 5, 4);
    trie.clear();

    assertTrue(trie.isEmpty());
  }

  @Test
  public void contains() throws Exception {
    final KnuthTrie<Integer> trie = new KnuthTrie<>(8, 4);
    fillTrie(trie, 4, 5, 6);

    assertTrue(trie.contains(4));
    assertTrue(trie.contains(5));
    assertTrue(trie.contains(6));

    assertFalse(trie.contains(7));
    assertFalse(trie.contains("foo"));
  }

  @Test
  public void size() throws Exception {
    final KnuthTrie<String> trie = new KnuthTrie<>(8, 4);
    assertEquals(trie.size(), 0);

    System.out.println("size 0 passed");

    fillTrie(trie, "foo", "bar", "baz");
    assertEquals(trie.size(), 3);

    System.out.println("size 0 passed");

    trie.clear();
    assertEquals(trie.size(), 0);
  }

  @Test
  public void iterator() throws Exception {
    final Integer[] testValues = { 150, 174, 190, 30 };
    final KnuthTrie<Integer> trie = new KnuthTrie<>(8, 4);
    fillTrie(trie, testValues);

    final Iterator<Integer> it = trie.iterator();
    final Integer[] iteratorValues = new Integer[4];

    for ( int i = 0; i < testValues.length; i++ ) {
      assertTrue(it.hasNext());

      final int n = it.next();
      assertThat(n, anyOf(is(150), is(174), is(190), is(30)));

      iteratorValues[i] = n;
    }

    assertFalse(it.hasNext());


    Arrays.sort(testValues);
    Arrays.sort(iteratorValues);
    assertArrayEquals(testValues, iteratorValues);
  }

  private static <T> void fillTrie(KnuthTrie<T> trie, T... values) {
    Arrays.asList(values).forEach(trie::add);
  }

}