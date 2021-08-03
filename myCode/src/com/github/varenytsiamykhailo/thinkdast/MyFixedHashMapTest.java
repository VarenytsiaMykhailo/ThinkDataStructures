package com.github.varenytsiamykhailo.thinkdast;

import org.junit.Before;

/**
 * @author downey
 *
 */
public class MyFixedHashMapTest extends MyLinearMapTest {

    @Before
    public void setUp()  {
        map = new MyFixedHashMap<String, Integer>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put(null, 0);
    }
}

