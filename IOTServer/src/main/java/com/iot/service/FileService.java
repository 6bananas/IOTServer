package com.iot.service;

import com.iot.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    void upload(MultipartFile map) throws IOException;
    Result download(String map) throws IOException;
}
