package com.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iot.vo.CarVO;
import com.iot.pojo.Car;

import java.util.List;

public interface CarService extends IService<Car> {
    List<CarVO> selectDetailedCarInfoAll();

    CarVO selectDetailedCarInfo(Integer vid);
}
