package ru.hse.mit.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MultisetImpl<E> extends AbstractCollection<E> implements Multiset<E> {

    private final LinkedHashMap<E, Integer> map;
    private int size;

    MultisetImpl() {
        map = new LinkedHashMap<>();
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int count(Object element) {
        return map.getOrDefault((E) element, 0);
    }

    @Override
    public Set<E> elementSet() {
        return map.keySet();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return new entrySet();
    }

    private class entrySet extends AbstractSet<Entry<E>> {

        @Override
        public @NotNull Iterator<Entry<E>> iterator() {
            return new Iterator<>() {
                int capacity = 0;
                final Iterator<Map.Entry<E, Integer>> iterator = map.entrySet().iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Entry<E> next() {
                    Map.Entry<E, Integer> entry = iterator.next();
                    capacity = entry.getValue();
                    return new Entry<>() {
                        @Override
                        public E getElement() {
                            return entry.getKey();
                        }

                        @Override
                        public int getCount() {
                            return entry.getValue();
                        }
                    };
                }

                @Override
                public void remove() {
                    iterator.remove();
                    size -= capacity;
                }
            };
        }

        @Override
        public int size() {
            return map.size();
        }
    }

    @Override
    public @NotNull Iterator<E> iterator() throws IllegalStateException {
        return new Iterator<>() {
            private final Iterator<Map.Entry<E, Integer>> iterator = map.entrySet().iterator();
            private Map.Entry<E, Integer> entry;
            private int capacity = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext() || capacity != 0;
            }

            @Override
            public E next() throws NoSuchElementException {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                if (capacity == 0) {
                    entry = iterator.next();
                    capacity = entry.getValue();
                }
                capacity -= 1;
                return entry.getKey();
            }

            @Override
            public void remove() {
                int count = entry.getValue();
                if (count > 1) {
                    entry.setValue(count - 1);
                } else {
                    iterator.remove();
                }
                size -= 1;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        size += 1;
        map.put(e, count(e) + 1);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        int ind = count(element);
        if (ind == 0) {
            return false;
        } else if (ind == 1) {
            map.remove(element);
        } else {
            map.replace((E) element, ind - 1);
        }
        size -= 1;
        return true;
    }
}
