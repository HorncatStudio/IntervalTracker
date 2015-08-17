package com.horncatstudio.intervaltracker.app;

import junit.framework.TestCase;

/**
 * Unit test for the TimeInterval object.
 */
public class TimeIntervalTest extends TestCase {

    public void testConstructor() throws Exception {
        TimeInterval expectedInterval = new TimeInterval(0);
        expectedInterval.Minutes = 1;
        expectedInterval.Seconds = 20;

        final Integer timeInMiliseconds = 80000;
        TimeInterval actualInterval = new TimeInterval(timeInMiliseconds);

        assertEquals(expectedInterval.Minutes, actualInterval.Minutes);
        assertEquals(expectedInterval.Seconds, actualInterval.Seconds);
    }

    public void testToStringLargerThenAMinute() throws Exception {
        final Integer timeInMiliseconds = 80000;
        TimeInterval expectedInterval = new TimeInterval(timeInMiliseconds);

        String actualIntervalString = "1:20";

        assertEquals(expectedInterval.toString(), actualIntervalString);
    }

    public void testToStringLessThenTenSEconds() throws Exception {
        final long minutes = 0;
        final long seconds = 8;
        TimeInterval expectedInterval = new TimeInterval(minutes,seconds);

        String actualIntervalString = "0:08";

        assertEquals(expectedInterval.toString(), actualIntervalString);
    }

    public void testToMilliseconds() throws Exception {
        TimeInterval expectedInterval = new TimeInterval(0);
        expectedInterval.Minutes = 1;
        expectedInterval.Seconds = 20;

        final long timeInMiliseconds = 80000;

        assertEquals(expectedInterval.toMilliseconds(), timeInMiliseconds);
    }
}