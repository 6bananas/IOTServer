package com.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.pojo.Car;
import com.iot.vo.CarVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarMapper extends BaseMapper<Car> {
    List<CarVO> selectDetailedCarInfoAll();

    CarVO selectDetailedCarInfo(Integer vid);
}
