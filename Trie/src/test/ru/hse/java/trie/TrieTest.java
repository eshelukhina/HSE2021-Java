package ru.hse.java.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {
    Trie trie;

    @BeforeEach
    void setUp() {
        trie = new TrieImpl();
    }

    @Test
    void testAdd() {
        assertTrue(trie.add("Asachildyouwouldwait"));
        assertTrue(trie.add("helloWorld"));
        assertTrue(trie.add(""));
        assertFalse(trie.add("helloWorld"));
        assertFalse(trie.add(""));
    }

    @Test
    void testContains() {
        trie.add("GotTheMusicInYouBaby");
        trie.add("TellMeWhy");
        trie.add("");
        assertTrue(trie.contains("GotTheMusicInYouBaby"));
        assertTrue(trie.contains("TellMeWhy"));
        assertTrue(trie.contains(""));
        assertFalse(trie.contains("hello"));
        assertFalse(trie.contains("WoRlD"));
    }

    @Test
    void stressTestContains() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();

        Set<String> dictionary = new HashSet<>();

        for (int j = 0; j < 100; j++) {
            StringBuilder sb = new StringBuilder(j);

            for (int i = 0; i < j; i++) {
                int index = (int) (AlphaNumericString.length() * Math.random());
                sb.append(AlphaNumericString.charAt(index));
            }
            String str = sb.toString();
            trie.add(str);
            dictionary.add(str);
        }

        for (int j = 0; j < 100; j++) {
            StringBuilder sb = new StringBuilder(j);

            for (int i = 0; i < j; i++) {
                int index = (int) (AlphaNumericString.length() * Math.random());
                sb.append(AlphaNumericString.charAt(index));
            }
            String str = sb.toString();
            if (dictionary.contains(str)) {
                assertTrue(trie.contains(str));
            } else assertFalse(trie.contains(str));
        }

        for (String value : dictionary) {
            assertTrue(trie.contains(value));
        }
    }


    @Test
    void testRemove() {
        trie.add("InColdLightOfMorning");
        trie.add("SomethingInTheWay");
        trie.add("");
        assertTrue(trie.remove("InColdLightOfMorning"));
        assertTrue(trie.remove(""));
        assertTrue(trie.remove("SomethingInTheWay"));
        assertFalse(trie.remove("SomethingInTheWay"));
        assertFalse(trie.remove("Remove"));
    }

    @Test
    void testSize() {
        assertEquals(trie.size(), 0);
        trie.add("WhereIsMyMind");
        trie.add("DrunkOnDestruction");
        trie.add("DrunkOnDestruction");
        trie.add("");
        assertEquals(trie.size(), 3);
        trie.remove("DrunkOnDestruction");
        trie.remove("JustCheck");
        assertEquals(trie.size(), 2);
    }

    @Test
    void testHowManyStartsWithPrefix() {
        trie.add("abacaba");
        trie.add("abcde");
        trie.add("abacabaabacabk");
        trie.add("abcde");
        trie.add("aaa");
        trie.add("");
        assertEquals(trie.howManyStartsWithPrefix("ab"), 3);
        assertEquals(trie.howManyStartsWithPrefix("a"), 4);
        assertEquals(trie.howManyStartsWithPrefix(""), 5);
        assertEquals(trie.howManyStartsWithPrefix("q"), 0);
    }

    @Test
    public void testNextString() {
        trie.add("abacaba");
        trie.add("abcde");
        trie.add("abacabaabacabk");
        trie.add("abcde");
        trie.add("aaa");
        trie.add("");
        assertEquals(trie.nextString("abc", 1), "abcde");
        assertNull(trie.nextString("ab", 5));
        assertEquals(trie.nextString("aaa", 1), "abacaba");
        assertNull(trie.nextString("null", 3));
        assertEquals(trie.nextString("", 4), "abcde");
        assertEquals(trie.nextString("", 0), "");
    }

    @Test
    void testOneOfThree() {
        Set<String> dictionary = new HashSet<>();
        Random rand = new Random(1000);
        for (int i = 0; i < 10000; i++) {
            StringBuilder string = new StringBuilder();
            int length = rand.nextInt(101);
            for (int j = 0; j < length; j++) {
                string.append((char) ('a' + rand.nextInt(26)));
            }
            int command = rand.nextInt(3);
            int helper = rand.nextInt(2);
            if (command == 0) {
                dictionary.add(string.toString());
                trie.add(string.toString());
                assertEquals(dictionary.contains(string.toString()), trie.contains(string.toString()));
            } else if (command == 1) {
                if (helper == 0) {
                    dictionary.add(string.toString());
                    trie.add(string.toString());
                }
                dictionary.remove(string.toString());
                trie.remove(string.toString());
                assertEquals(dictionary.contains(string.toString()), trie.contains(string.toString()));
            } else {
                if (helper == 0) {
                    dictionary.add(string.toString());
                    trie.add(string.toString());
                }
                assertEquals(dictionary.contains(string.toString()), trie.contains(string.toString()));
            }
        }
    }
}
