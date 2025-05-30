package com.iot.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.iot.mapper.BatteryMapper;
import com.iot.mapper.CarMapper;
import com.iot.mapper.RecordMapper;
import com.iot.pojo.Battery;
import com.iot.pojo.Car;
import com.iot.pojo.Record;
import com.iot.server.WebSocketServer;
import com.iot.service.HardwareService;
import com.iot.utils.ChannelManageUtils;
import com.iot.utils.ClientManageUtils;
import com.iot.utils.SystemUtils;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.iot.constants.SystemConstants.*;

@Service
public class HardwareServiceImpl implements HardwareService {
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private BatteryMapper batteryMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private WebSocketServer webSocketServer;
    
    private void updateAndInform(Integer vid, Integer state) throws IOException {
        // 修改电车状态
        Car car = carMapper.selectById(vid);
        car.setState(state);
        carMapper.updateById(car);
        // 通知前端
        HashMap<String, Object> map = new HashMap<>();
        map.put("VID", vid);
        map.put("state", state);
        webSocketServer.sendToAll(JSONObject.toJSONString(map));
    }

    @Override
    public void offline(Channel channel) throws IOException {
        // 移除Map
        String clientAddress = SystemUtils.getClientAddress(channel);
        Integer vid = ClientManageUtils.getVid(clientAddress);
        if (vid != null) { // 硬件可能刚连接就断开，此时两个Map中都不会有这条连接
            ClientManageUtils.removeClient(clientAddress);
            ChannelManageUtils.removeChannel(vid);
            // 修改电车状态并通知前端
            updateAndInform(vid, CAR_STATE_PAUSE);
        }
    }

    @Override
    public void reset(Integer vid, Channel channel) throws IOException {
        // 加入Map
        ChannelManageUtils.addChannel(vid, channel);
        ClientManageUtils.addClient(SystemUtils.getClientAddress(channel), vid);
        // 发送复位消息
        String systemTime = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN);
        HashMap<String, Object> map = new HashMap<>();
        map.put("VID", vid);
        map.put("clock", systemTime);
        channel.writeAndFlush(JSONObject.toJSONString(map));
        // 修改电车状态并通知前端
        updateAndInform(vid, CAR_STATE_NORMAL);
    }

    private void warn(Integer vid, Integer state) throws IOException {
        // 修改电车状态并通知前端
        updateAndInform(vid, state);
    }

    @Override
    public void parseMsg(String msg, Channel channel) throws IOException {
        Object parse = JSON.parse(msg);
        if (parse instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) parse;
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) object;
                Integer vid = json.getInteger("VID");
                String time = json.getString("localTime");
                Integer pid = json.getInteger("PID");
                Double temperature = json.getDouble("Temperature");
                Double voltage = json.getDouble("v");
                Double power = (voltage - MIN_VOLTAGE) / (MAX_VOLTAGE - MIN_VOLTAGE);
                power = Math.round(power * 1000.0) / 10.0; // 如0.7578947转换为75.8
                Integer x = json.getInteger("x");
                Integer y = json.getInteger("y");
                // 修改电池状态
                Battery battery = batteryMapper.selectById(pid);
                battery.setVoltage(voltage);
                battery.setTemperature(temperature);
                battery.setPower(power);
                batteryMapper.updateById(battery);
                // 修改车载电池
                Car car = carMapper.selectById(vid);
                car.setPid(pid);
                carMapper.updateById(car);
                // 记录消费记录
                Record record = new Record(LocalDateTimeUtil.parse(time, DatePattern.NORM_DATETIME_PATTERN), pid, vid, power, temperature);
                recordMapper.insert(record);
                // 转给前端电车坐标
                HashMap<String, Integer> map = new HashMap<>();
                map.put("VID", vid);
                map.put("x", x);
                map.put("y", y);
                webSocketServer.sendToAll(JSON.toJSONString(map));
            }
        } else {
            JSONObject json = (JSONObject) parse;
            Integer vid = json.getInteger("VID");
            String carState = json.getString("state");
            if (carState.equals("reset")) {
                reset(vid, channel);
            } else if (carState.equals("Temp Warning")) {
                warn(vid, CAR_STATE_TEMPERATURE_WARN);
            } else if (carState.equals("Volt Warning")) {
                warn(vid, CAR_STATE_BATTERY_LOW_POWER);
            } else if (carState.equals("Rescue Warning")) {
                warn(vid, CAR_STATE_NEED_RESCUE);
            } else if (carState.equals("Normal")) {
                warn(vid, CAR_STATE_NORMAL);
            }
        }
    }
}
