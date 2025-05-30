package com.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iot.mapper.BatteryMapper;
import com.iot.pojo.Battery;
import com.iot.service.BatteryService;
import org.springframework.stereotype.Service;

@Service
public class BatteryServiceImpl extends ServiceImpl<BatteryMapper, Battery> implements BatteryService {
}
