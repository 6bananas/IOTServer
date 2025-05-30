package com.iot.controller;

import com.iot.service.CarService;
import com.iot.service.FileService;
import com.iot.service.RecordService;
import com.iot.vo.CarVO;
import com.iot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 只在前端与服务器之间交互的请求
 */
@RestController
public class CommonController {
    @Autowired
    private CarService carService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private FileService fileService;

    /**
     * 前端查询在线车辆VID、PID、灯光状态、电车状态、电池电压、电池温度、电池剩余电量
     */
    @GetMapping("/queryAllCars")
    public Result queryAllCars() {
        List<CarVO> carList = carService.selectDetailedCarInfoAll();
        return Result.success(carList);
    }

    @GetMapping("/queryCar/{vid}")
    public Result queryCar(@PathVariable Integer vid) {
        CarVO carVO = carService.selectDetailedCarInfo(vid);
        return Result.success(carVO);
    }

    /**
     * 前端查询电池历史信息
     */
    @GetMapping("/queryBatteryRecord/{pid}")
    public Result queryBatteryRecord(@PathVariable Integer pid) {
        return recordService.selectRecords(pid);
    }

    /**
     * 前端上传地图文件
     */
    @PostMapping("/uploadMap")
    public Result uploadMap(MultipartFile map) throws IOException {
        fileService.upload(map);
        return Result.success();
    }

    /**
     * 前端读取地图文件
     */
    @GetMapping("/getMap/{map}")
    public Result downloadMap(@PathVariable String map) throws IOException {
        return fileService.download(map);
    }
}
