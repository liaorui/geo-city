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

    @Test
    public void testCityCode() {
        // 防止SQL注入
       ChinaCity city = GeoCity.getCityCode("'%' or 1 = 1");
        System.out.println(city);
    }

    @Test
    public void testCityCode2() {
        ChinaCity city = GeoCity.getCityCode("广州");
        System.out.println(city);
    }

    @Test
    public void testCity() {
        ChinaCity city = GeoCity.getCity(440100);
        System.out.println(city);
    }
}
