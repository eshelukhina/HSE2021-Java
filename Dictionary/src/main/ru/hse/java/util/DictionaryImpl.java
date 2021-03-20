package ru.hse.java.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.util.AbstractCollection;

import static java.lang.Math.abs;

public class DictionaryImpl<K, V> extends AbstractMap<K, V> implements Dictionary<K, V> {

    private static final int RESIZE = 2;
    private static final int INITIAL_CAPACITY = 2;
    private static final double INITIAL_COEFFICIENT = 0.8;

    private int size;


    private int capacity;
    private final double coefficient;

    ArrayList<ArrayList<Entry<K, V>>> hashTable;

    DictionaryImpl() {
        size = 0;
        capacity = INITIAL_CAPACITY;
        coefficient = INITIAL_COEFFICIENT;
        hashTable = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            hashTable.add(new ArrayList<>());
        }

    }

    DictionaryImpl(double newCoefficient) {
        size = 0;
        capacity = INITIAL_CAPACITY;
        coefficient = newCoefficient;
        hashTable = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            hashTable.add(new ArrayList<>());
        }

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public V get(Object key) {
        int i = getHash(key);
        ArrayList<Entry<K, V>> list = hashTable.get(i);

        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        size++;

        if ((double) size >= (double) capacity * coefficient) {
            capacity *= RESIZE;
            rehashTable();
        }

        int i = getHash(key);
        ArrayList<Entry<K, V>> list = hashTable.get(i);

        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)) {
                size--;
                return entry.setValue(value);
            }
        }

        hashTable.get(i).add(new SimpleEntry<>(key, value));
        return null;
    }

    @Override
    public V remove(Object key) {
        int i = getHash(key);
        ArrayList<Entry<K, V>> list = hashTable.get(i);

        if ((double) size <= (double) capacity * (coefficient / (double) RESIZE)) {
            capacity = (int) ((double) capacity / (double) RESIZE);
            rehashTable();
        }

        V value = get(key);
        list.removeIf(el -> el.getKey().equals(key));

        if (value != null) {
            size--;
        }

        return value;
    }

    @Override
    public void clear() {
        hashTable.clear();
        size = 0;
        capacity = INITIAL_CAPACITY;
        hashTable = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            hashTable.add(new ArrayList<>());
        }
    }

    @Override
    public @NotNull AbstractSet<K> keySet() {
        return new KeySet();
    }

    @Override
    public @NotNull AbstractCollection<V> values() {
        return new ValuesCollection();
    }

    @Override
    public @NotNull AbstractSet<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private int getHash(Object key) {
        return abs(key.hashCode() % capacity);
    }

    private void rehashTable() {
        ArrayList<ArrayList<Entry<K, V>>> newHashTable = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            newHashTable.add(new ArrayList<>());
        }

        for (ArrayList<Entry<K, V>> list : hashTable) {
            for (Entry<K, V> entry : list) {
                int h = getHash(entry.getKey());
                newHashTable.get(h).add(entry);
            }
        }

        hashTable = newHashTable;
    }

    private class DictionaryIterator<T> implements Iterator<T> {
        private final int iterType;                                           // 0 -- entry; 1 -- key; 2 -- value;
        private final Iterator<ArrayList<AbstractMap.Entry<K, V>>> iterList;
        private Iterator<Entry<K, V>> iterEntry = null;

        DictionaryIterator(Iterator<ArrayList<AbstractMap.Entry<K, V>>> newIterList, int newIterType) {
            iterList = newIterList;
            if (newIterList.hasNext()) {
                iterEntry = newIterList.next().iterator();
            }
            iterType = newIterType;
        }

        @Override
        public boolean hasNext() {
            if (iterEntry == null) {
                return false;
            }
            if (iterEntry.hasNext()) {
                return true;
            }
            while (iterList.hasNext()) {
                iterEntry = iterList.next().iterator();
                if (iterEntry.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() throws NoSuchElementException {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (iterType == 0) {
                return (T) iterEntry.next();
            }
            if (iterType == 1) {
                return (T) iterEntry.next().getKey();
            }
            if (iterType == 2) {
                return (T) iterEntry.next().getValue();
            }
            return null;
        }

        @Override
        public void remove() throws IllegalStateException {
            iterEntry.remove();
            size--;
        }
    }

    private class KeySet extends AbstractSet<K> {
        @NotNull
        @Override
        public Iterator<K> iterator() {
            return new DictionaryIterator<>(hashTable.iterator(), 1); // 1 -- key;
        }

        @Override
        public int size() {
            return size;
        }
    }

    private class ValuesCollection extends AbstractCollection<V> {
        @NotNull
        @Override
        public Iterator<V> iterator() {
            return new DictionaryIterator<>(hashTable.iterator(), 2); // 2 -- value;
        }

        @Override
        public int size() {
            return size;
        }
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {
        @NotNull
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new DictionaryIterator<>(hashTable.iterator(), 0); // 0 -- entry
        }

        @Override
        public int size() {
            return size;
        }
    }
}
