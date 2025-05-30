package com.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iot.mapper.RecordMapper;
import com.iot.pojo.Record;
import com.iot.service.RecordService;
import com.iot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public Result selectRecords(Integer pid) {
        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Record::getPid, pid);
        List<Record> records = recordMapper.selectList(lambdaQueryWrapper);
        return Result.success(records);
    }
}
