package com.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.pojo.Battery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatteryMapper extends BaseMapper<Battery> {
}
