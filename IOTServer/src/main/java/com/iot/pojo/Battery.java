package com.iot.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Battery {
    @TableId
    private Integer pid;
    private Double voltage;
    private Double temperature;
    private Double power;
}
