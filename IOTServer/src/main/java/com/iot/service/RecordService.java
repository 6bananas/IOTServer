package com.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iot.pojo.Record;
import com.iot.vo.Result;

public interface RecordService extends IService<Record> {
    Result selectRecords(Integer pid);
}
