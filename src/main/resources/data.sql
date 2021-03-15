CREATE TABLE `china_city_geo` (
  `code` varchar(255) DEFAULT NULL COMMENT '城市编码',
  `area` varchar(255) DEFAULT NULL COMMENT '县（区）',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `lat` varchar(255) DEFAULT NULL COMMENT '纬度',
  `lon` varchar(255) DEFAULT NULL COMMENT '经度',
  `geo_hash` varchar(255) DEFAULT NULL COMMENT 'geo hash'
) as select * from CSVREAD('classpath:china_city_geo.csv');

CREATE INDEX idx_hash ON china_city_geo(geo_hash);