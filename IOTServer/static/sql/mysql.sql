CREATE DATABASE `iotserver`;

USE `iotserver`;

-- 电池
CREATE TABLE `battery` (
    pid INT PRIMARY KEY,
    voltage DOUBLE NOT NULL, -- 电压
    temperature DOUBLE NOT NULL, -- 温度
    power DOUBLE NOT NULL -- 电量
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- 电车
CREATE TABLE `car` (
    vid INT PRIMARY KEY,
    pid INT,
    light INT NOT NULL, -- 灯光状态，1 开 0 关
    state INT NOT NULL -- 车辆状态，1 正常 2 暂停 3 温度异常 4 电量不足 5 需要救援
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- 电池消费记录
CREATE TABLE `record` (
    time DATETIME PRIMARY KEY,
    pid INT NOT NULL,
    vid INT NOT NULL,
    power DOUBLE NOT NULL,
    temperature DOUBLE NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO `battery`
VALUES
    (1001, 78.3, 32.1, 98.0),
    (1002, 45.7, 43.6, 80.0);

INSERT INTO `car`
VALUES
    (2001, 1001, 0, 0);
