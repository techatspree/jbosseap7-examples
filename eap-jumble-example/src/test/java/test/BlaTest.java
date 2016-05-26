package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlaTest {
    @Test
    public void testBla() throws Exception {
        assertEquals(255, Integer.parseInt("FF", 16));
        assertEquals(16777215, Integer.parseInt("FFFFFF", 16));
    }
}
