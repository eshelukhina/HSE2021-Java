package ru.hse.java.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class DictionaryTest {

    private Dictionary<String, Character> dictStrCh;
    private Dictionary<String, Integer> dictStrInt;
    private Dictionary<String, Integer> dictStrIntParam;
    private Dictionary<Integer, Character> dictIntCh;
    private HashMap<String, Character> mapStrChar;
    private HashMap<String, Integer> mapStrInt;
    private HashMap<String, Integer> mapStrIntParam;

    @BeforeEach
    void setUp() {
        dictStrCh = new DictionaryImpl<>();
        dictStrInt = new DictionaryImpl<>();
        dictStrIntParam = new DictionaryImpl<>(0.66);
        dictIntCh = new DictionaryImpl<>();

        mapStrChar = new HashMap<>();
        mapStrInt = new HashMap<>();
        mapStrIntParam = new HashMap<>();

    }

    @Test
    void testPut() {
        assertNull(dictStrCh.put("one", '1'));
        assertNull(dictStrCh.put("two", '2'));
        assertNull(dictStrCh.put("three", '3'));

        assertNull(dictStrInt.put("one", 1));
        assertNull(dictStrInt.put("two", 2));
        assertNull(dictStrInt.put("three", 3));

        assertNull(dictStrIntParam.put("one", 1));
        assertNull(dictStrIntParam.put("two", 2));
        assertNull(dictStrIntParam.put("three", 3));

        assertNull(dictIntCh.put(1, '1'));
        assertNull(dictIntCh.put(2, '2'));
        assertNull(dictIntCh.put(3, '3'));
    }

    @Test
    void testRemoveAndSizeIsEmpty() {

        assertTrue(dictStrCh.isEmpty());

        dictStrCh.put("one", '1');
        dictStrCh.put("two", '2');
        dictStrCh.put("three", '3');
        dictStrCh.put("four", '4');
        dictStrCh.put("five", '5');

        assertEquals(dictStrCh.size(), 5);

        assertEquals(dictStrCh.remove("three"), '3');
        assertEquals(dictStrCh.remove("five"), '5');
        assertEquals(dictStrCh.size(), 3);

        assertFalse(dictStrCh.isEmpty());

        assertEquals(dictStrCh.remove("one"), '1');
        assertEquals(dictStrCh.remove("two"), '2');
        assertEquals(dictStrCh.size(), 1);

        assertFalse(dictStrCh.isEmpty());

        assertEquals(dictStrCh.remove("four"), '4');
        assertEquals(dictStrCh.size(), 0);

        assertTrue(dictStrCh.isEmpty());
    }

    @Test
    void testGet() {
        dictStrIntParam.put("one", 1);
        dictStrIntParam.put("two", 2);
        dictStrInt.put("three", 3);
        dictStrCh.put("four", '4');
        dictStrCh.put("five", '5');

        assertEquals(dictStrIntParam.get("one"), 1);
        assertEquals(dictStrIntParam.get("two"), 2);
        assertEquals(dictStrInt.get("three"), 3);
        assertEquals(dictStrCh.get("four"), '4');
        assertEquals(dictStrCh.get("five"), '5');
    }

    @Test
    void testContainsKey() {
        dictStrIntParam.put("one", 1);
        dictStrIntParam.put("two", 2);
        dictStrInt.put("three", 3);
        dictStrCh.put("four", '4');
        dictStrCh.put("five", '5');

        assertTrue(dictStrIntParam.containsKey("one"));
        assertTrue(dictStrIntParam.containsKey("two"));
        assertFalse(dictStrInt.containsKey("one"));
        assertFalse(dictStrInt.containsKey("two"));
        assertTrue(dictStrInt.containsKey("three"));
        assertFalse(dictStrCh.containsKey("three"));
        assertTrue(dictStrCh.containsKey("four"));
        assertTrue(dictStrCh.containsKey("five"));

        dictStrIntParam.remove("one");
        assertFalse(dictStrIntParam.containsKey("one"));

        dictStrInt.remove("three");
        assertFalse(dictStrInt.containsKey("three"));

        dictStrCh.remove("five");
        assertFalse(dictStrCh.containsKey("five"));
    }

    @Test
    void testClear() {
        dictStrIntParam.put("one", 1);
        dictStrIntParam.put("two", 2);
        dictStrInt.put("three", 3);
        dictStrCh.put("four", '4');
        dictStrCh.put("five", '5');

        dictStrIntParam.clear();
        assertEquals(dictStrIntParam.size(), 0);

        dictStrInt.clear();
        assertEquals(dictStrInt.size(), 0);

        dictStrCh.clear();
        assertEquals(dictStrCh.size(), 0);
    }

    @Test
    void stressTestKeySet() {
        Random rand = new Random();

        for (int i = 0; i < 999; i++) {
            int rnd = rand.nextInt(56);
            mapStrInt.put(Integer.toString(rnd), i);
            dictStrInt.put(Integer.toString(rnd), i);
            mapStrIntParam.put(Integer.toString(rnd), i);
            dictStrIntParam.put(Integer.toString(rnd), i);
        }

        for (int i = 0; i < 999; i++) {
            int rnd = rand.nextInt(56);
            mapStrChar.put(Integer.toString(rnd), (char) i);
            dictStrCh.put(Integer.toString(rnd), (char) i);
        }

        for (String k : dictStrInt.keySet()) {
            assertEquals(dictStrInt.get(k), mapStrInt.get(k));
            assertTrue(dictStrInt.containsKey(k));
        }
        for (String k : dictStrIntParam.keySet()) {
            assertEquals(dictStrIntParam.get(k), mapStrIntParam.get(k));
            assertTrue(dictStrIntParam.containsKey(k));
        }

        for (String k : dictStrCh.keySet()) {
            assertEquals(dictStrCh.get(k), mapStrChar.get(k));
            assertTrue(dictStrCh.containsKey(k));
        }
    }

    @Test
    void stressTestValues() {
        Random rand = new Random();

        for (int i = 0; i < 999; i++) {
            int rnd = rand.nextInt(56);
            mapStrInt.put(Integer.toString(rnd), i);
            dictStrInt.put(Integer.toString(rnd), i);
            mapStrIntParam.put(Integer.toString(rnd), i);
            dictStrIntParam.put(Integer.toString(rnd), i);
        }

        for (int i = 0; i < 999; i++) {
            int rnd = rand.nextInt(56);
            mapStrChar.put(Integer.toString(rnd), (char) i);
            dictStrCh.put(Integer.toString(rnd), (char) i);
        }

        for (Integer v : dictStrInt.values()) {
            assertTrue(mapStrInt.containsValue(v));
        }

        for (Integer v : dictStrIntParam.values()) {
            assertTrue(mapStrIntParam.containsValue(v));
        }

        for (Character v : dictStrCh.values()) {
            assertTrue(mapStrChar.containsValue(v));
        }
    }

    @Test
    void stressTestEntrySet() {
        Random rand = new Random();

        for (int i = 0; i < 999; i++) {
            int rnd = rand.nextInt(56);
            mapStrInt.put(Integer.toString(rnd), i);
            dictStrInt.put(Integer.toString(rnd), i);
            mapStrIntParam.put(Integer.toString(rnd), i);
            dictStrIntParam.put(Integer.toString(rnd), i);
        }

        for (int i = 0; i < 999; i++) {
            int rnd = rand.nextInt(56);
            mapStrChar.put(Integer.toString(rnd), (char) i);
            dictStrCh.put(Integer.toString(rnd), (char) i);
        }

        for (Map.Entry<String, Integer> k : dictStrInt.entrySet()) {
            assertEquals(dictStrInt.get(k.getKey()), mapStrInt.get(k.getKey()));
            assertTrue(dictStrInt.containsKey(k.getKey()));
        }

        for (Map.Entry<String, Integer> k : dictStrIntParam.entrySet()) {
            assertEquals(dictStrIntParam.get(k.getKey()), mapStrIntParam.get(k.getKey()));
            assertTrue(dictStrIntParam.containsKey(k.getKey()));
        }

        for (Map.Entry<String, Character> k : dictStrCh.entrySet()) {
            assertEquals(dictStrCh.get(k.getKey()), mapStrChar.get(k.getKey()));
            assertTrue(dictStrCh.containsKey(k.getKey()));
        }
    }
}