package cn.learning.po;

/**
 * @author liaorui
 * @date 2021/3/1
 */
public class ChinaCity {
    /**
     * 城市编码
     */
    private String code;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县（区）
     */
    private String area;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * geo hash
     */
    private String geoHash;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    @Override public String toString() {
        return "ChinaCity{" + "code='" + code + '\'' + ", province='" + province + '\'' + ", city='" + city + '\''
            + ", area='" + area + '\'' + ", lat=" + lat + ", lon=" + lon + ", geoHash='" + geoHash + '\'' + '}';
    }
}
