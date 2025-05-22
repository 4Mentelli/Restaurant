package it.unibs.test;

import it.unibs.se.model.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateTest {

    @BeforeEach
    void setUp() {
        var date = new Date("20/01/2025");
    }

    @Test
    void testDateCreation() {
        var date = new Date("20/01/2025");
        assertEquals(20, date.getDay());
        assertEquals(1, date.getMonth());
        assertEquals(2025, date.getYear());
    }
    @Test
    void testAfter() {
        var date1 = new Date("20/01/2025");
        var date2 = new Date("21/01/2025");
        assertTrue(date2.isAfter(date1));
    }
    @Test
    void testBefore() {
        var date1 = new Date("20/01/2025");
        var date2 = new Date("21/1/2025");
        var date3 = new Date("21/1/2025");
        assertTrue(date1.isBefore(date2));
        assertFalse(date2.isBefore(date3));
        assertFalse(date2.isBefore(date1));
    }
    @Test
    void testBetween() {
        var date1 = new Date("20/01/2025");
        var date2 = new Date("21/1/2025");
        var date3 = new Date("20/02/2025");
        assertTrue(date2.isBetween(date1, date3));
        assertTrue(date1.isBetween(date1, date3));
        assertTrue(date3.isBetween(date1, date3));
        assertFalse(date1.isBetween(date2, date3));
    }

}
