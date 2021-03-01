package cn.learning;

import cn.learning.po.ChinaCity;
import cn.learning.util.H2Conn;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author liaorui
 * @date 2021/3/1
 */
public class GeoCity {

    private static Connection h2Conn;

    static {
        try {
            h2Conn = H2Conn.getInstance().getConn();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    h2Conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }));
            Statement st = h2Conn.createStatement();
            InputStream is = GeoCity.class.getResourceAsStream("/data.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            st.execute(builder.toString());
            st.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找指定经纬度所在的省市县
     *
     * @param lat
     * @param lon
     * @return
     */
    public static ChinaCity locate(double lat, double lon) {
        ArrayList<ChinaCity> cityList = new ArrayList<>();
        try {
            String geoHash = GeohashUtils.encodeLatLon(lat, lon);
            String target = geoHash.substring(0, 3) + "%";
            Statement st = h2Conn.createStatement();
            ResultSet rs = st.executeQuery("select * from china_city_geo where geo_hash like '" + target + "'");
            while (rs.next()) {
                String province = rs.getString("province");
                String city = rs.getString("city");
                String area = rs.getString("area");
                Double latitude = Double.parseDouble(rs.getString("lat"));
                Double longitude = Double.parseDouble(rs.getString("lon"));
                String pointGeoHash = rs.getString("geo_hash");
                ChinaCity chinaCity = new ChinaCity();
                chinaCity.setProvince(province);
                chinaCity.setCity(city);
                chinaCity.setArea(area);
                chinaCity.setLat(latitude);
                chinaCity.setLon(longitude);
                chinaCity.setGeoHash(pointGeoHash);
                cityList.add(chinaCity);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (cityList.size() > 0) {
            /**
             * 按距离排序
             */
            Collections.sort(cityList, (city1, city2) -> {
                SpatialContext geo = SpatialContext.GEO;
                double distance1 =
                    geo.calcDistance(geo.makePoint(city1.getLon(), city1.getLat()), geo.makePoint(lon, lat))
                        * DistanceUtils.DEG_TO_KM;
                double distance2 =
                    geo.calcDistance(geo.makePoint(city2.getLon(), city2.getLat()), geo.makePoint(lon, lat))
                        * DistanceUtils.DEG_TO_KM;
                if (distance1 < distance2) {
                    return -1;
                } else if (distance1 > distance2) {
                    return 1;
                }
                return 0;
            });
            return cityList.get(0);
        }
        return null;
    }

}
