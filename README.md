## 经纬度反查城市

将中国县级以上城市的经纬度做geo hash编码，输入目标经纬度，转换成geo hash后用SQL like查询，找出候选城市，然后根据距离排序，找出最近的城市。

数据来自 github [city-geo](https://github.com/88250/city-geo)

用法:
```aidl
ChinaCity city = GeoCity.locate(23.83534589812056, 100.03773867836253);
```