package com.iot.controller;

import com.iot.dto.PositionDTO;
import com.iot.service.InteractService;
import com.iot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 需要与硬件交互的请求
 */
@RestController
public class InteractController {
    @Autowired
    private InteractService interactService;

    @PostMapping("/setPosition")
    public Result setPosition(@RequestBody PositionDTO positionDTO) {
        return interactService.setPosition(positionDTO);
    }

    @GetMapping("/charge/{vid}")
    public Result charge(@PathVariable Integer vid) {
        return interactService.charge(vid);
    }

    @GetMapping("/led/{vid}/{state}")
    public Result led(@PathVariable Integer vid, @PathVariable Integer state) {
        return interactService.led(vid, state);
    }

    @GetMapping("/beep/{vid}/{state}")
    public Result beep(@PathVariable Integer vid, @PathVariable Integer state) {
        return interactService.beep(vid, state);
    }
}
