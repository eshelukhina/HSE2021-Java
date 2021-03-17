# Data Structure Trie
- Data structure for storing the srting set, which is a suspended tree with symbols on the edges. Strings are obtained by writing sequentially all characters stored on the edges between the Trie root and the terminal vertex.
- More information on [Wikipedia](https://en.wikipedia.org/wiki/Trie)
## Implementation
- `add(String element)` - add element to a Trie
  - <tt>True</tt> if this set did not already contain the specified
  - Complexity: O(|element|)
  
- `contains(String element)` - check if the element is contained in the structure
  - <tt>True</tt> if contains
  - Complexity: O(|element|)

- `remove(String element)` - remove element from a Trie
  - <tt>True</tt> if this set contained the specified element
  - Complexity: O(|element|)
  
- `size()` - size of structure
  - Complexity: O(1)
  
- `howManyStartsWithPrefix(String prefix)` - number of lines in the structure starting with the prefix passed
  - Complexity: O(|prefix|)
  
- `nextString(String element, int k)` - Get String in trie, next after `element` up to k elements
  - Found String or null
  - Complexity: O(|trie depth|)
  