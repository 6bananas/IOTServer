package com.iot.service;

import com.iot.dto.PositionDTO;
import com.iot.vo.Result;

/**
 * 前端与硬件交互
 */
public interface InteractService {
    Result setPosition(PositionDTO positionDTO);
    Result charge(Integer vid);
    Result led(Integer vid, Integer state);
    Result beep(Integer vid, Integer state);
}
