## 中国城市查询

将中国县级以上城市的经纬度做geo hash编码，输入目标经纬度，转换成geo hash后用SQL like查询，找出候选城市，然后根据距离排序，找出最近的城市。

数据来自 github [city-geo](https://github.com/88250/city-geo)，修正了部分错误的经纬度，增加了城市编码。

用法:  
1、根据经纬度查中国城市
```aidl
ChinaCity city = GeoCity.locate(42.74481320560531, 93.12168009045864);
System.out.println(city);
```

输出:
```aidl
ChinaCity{province='新疆维吾尔自治区', city='哈密市', area='null', lat=42.82582436811265, lon=93.52121554028504, geoHash='wpde8hhnd229'}
```

2、根据城市名称查询城市
```aidl
ChinaCity city = GeoCity.getCityCode("广州");
System.out.println(city);
```
如果没有找到，则返回null。 

3、根据城市编码查询城市
```aidl
ChinaCity city = GeoCity.getCity(440100);
System.out.println(city);
```
如果没有找到，则返回null。