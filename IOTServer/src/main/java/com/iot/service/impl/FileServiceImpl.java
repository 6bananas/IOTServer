package com.iot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.iot.service.FileService;
import com.iot.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.iot.constants.ResultConstants.MAP_NAME_EMPTY;
import static com.iot.constants.ResultConstants.MAP_NOT_FOUND;
import static com.iot.constants.SystemConstants.MAP_UPLOAD_DIR;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void upload(MultipartFile map) throws IOException {
        String filename = map.getOriginalFilename();
        map.transferTo(new File(System.getProperty("user.dir") + MAP_UPLOAD_DIR + filename));
    }

    @Override
    public Result download(String map) throws IOException {
        if (StrUtil.hasEmpty(map)) {
            return Result.error(MAP_NAME_EMPTY);
        }
        String path = System.getProperty("user.dir") + MAP_UPLOAD_DIR + map + ".txt";
        File file = new File(path);
        if (!file.exists()) {
            return Result.error(MAP_NOT_FOUND);
        }
        // 一次性读完文件内容，并转换为字符串形式
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        return Result.success(content);
    }
}
