package com.iot.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.iot.dto.PositionDTO;
import com.iot.mapper.CarMapper;
import com.iot.pojo.Car;
import com.iot.service.InteractService;
import com.iot.utils.ChannelManageUtils;
import com.iot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.iot.constants.ResultConstants.CAR_OFFLINE;
import static com.iot.constants.SystemConstants.CONTROL_CHARGE;

@Service
public class InteractServiceImpl implements InteractService {
    @Autowired
    private CarMapper carMapper;

    @Override
    public Result setPosition(PositionDTO positionDTO) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("VID", positionDTO.getVid());
        map.put("x", positionDTO.getX());
        map.put("y", positionDTO.getY());
        boolean b = ChannelManageUtils.sendMessage(positionDTO.getVid(), JSONObject.toJSONString(map));
        return b == true ? Result.success() : Result.error(CAR_OFFLINE);
    }

    @Override
    public Result charge(Integer vid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("VID", vid);
        map.put("msg", CONTROL_CHARGE);
        boolean b = ChannelManageUtils.sendMessage(vid, JSONObject.toJSONString(map));
        return b == true ? Result.success() : Result.error(CAR_OFFLINE);
    }

    @Override
    public Result led(Integer vid, Integer state) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("VID", vid);
        map.put("LED", state);
        boolean b = ChannelManageUtils.sendMessage(vid, JSONObject.toJSONString(map));
        if (b == true) {
            Car car = carMapper.selectById(vid);
            car.setLight(state);
            carMapper.updateById(car);
            return Result.success();
        }
        return Result.error(CAR_OFFLINE);
    }

    @Override
    public Result beep(Integer vid, Integer state) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("VID", vid);
        map.put("BEEP", state);
        boolean b = ChannelManageUtils.sendMessage(vid, JSONObject.toJSONString(map));
        return b == true ? Result.success() : Result.error(CAR_OFFLINE);
    }
}
