package com.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iot.vo.CarVO;
import com.iot.mapper.CarMapper;
import com.iot.pojo.Car;
import com.iot.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
    @Autowired
    private CarMapper carMapper;

    @Override
    public List<CarVO> selectDetailedCarInfoAll() {
        return carMapper.selectDetailedCarInfoAll();
    }

    @Override
    public CarVO selectDetailedCarInfo(Integer vid) {
        return carMapper.selectDetailedCarInfo(vid);
    }
}
