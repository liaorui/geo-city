package cn.learning;

import static org.junit.Assert.assertTrue;

import cn.learning.po.ChinaCity;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test public void testLocate() {
        ChinaCity city = GeoCity.locate(42.74481320560531, 93.12168009045864);
        System.out.println(city);
    }
}
