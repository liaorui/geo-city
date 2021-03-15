package cn.learning;

import cn.learning.po.ChinaCity;
import cn.learning.util.H2Conn;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author liaorui
 * @date 2021/3/1
 */
public class GeoCity {

    /**
     * 保持长连接，不关闭
     */
    private Connection h2Conn;
    private static volatile GeoCity geoCity;

    /**
     * 懒加载，获取h2连接
     */
    private GeoCity() {
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

    public Connection getH2Conn() {
        return h2Conn;
    }

    public static GeoCity getInstance() {
        if (geoCity == null) {
            synchronized (GeoCity.class) {
                if (geoCity == null) {
                    geoCity = new GeoCity();
                }
            }
        }
        return geoCity;
    }

    public static ChinaCity getCityCode(String cityName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from china_city_geo where city like ?";
            ps = GeoCity.getInstance().getH2Conn().prepareStatement(sql);
            ps.setString(1, cityName + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String province = rs.getString("province");
                String city = rs.getString("city");
                String area = rs.getString("area");
                Double latitude = Double.parseDouble(rs.getString("lat"));
                Double longitude = Double.parseDouble(rs.getString("lon"));
                String pointGeoHash = rs.getString("geo_hash");
                ChinaCity chinaCity = new ChinaCity();
                chinaCity.setCode(code);
                chinaCity.setProvince(province);
                chinaCity.setCity(city);
                chinaCity.setArea(area);
                chinaCity.setLat(latitude);
                chinaCity.setLon(longitude);
                chinaCity.setGeoHash(pointGeoHash);
                return chinaCity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
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
            String sql = "select * from china_city_geo where geo_hash like ?";
            PreparedStatement ps = GeoCity.getInstance().getH2Conn().prepareStatement(sql);
            ps.setString(1, target);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String province = rs.getString("province");
                String city = rs.getString("city");
                String area = rs.getString("area");
                Double latitude = Double.parseDouble(rs.getString("lat"));
                Double longitude = Double.parseDouble(rs.getString("lon"));
                String pointGeoHash = rs.getString("geo_hash");
                ChinaCity chinaCity = new ChinaCity();
                chinaCity.setCode(code);
                chinaCity.setProvince(province);
                chinaCity.setCity(city);
                chinaCity.setArea(area);
                chinaCity.setLat(latitude);
                chinaCity.setLon(longitude);
                chinaCity.setGeoHash(pointGeoHash);
                cityList.add(chinaCity);
            }
            rs.close();
            ps.close();
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
