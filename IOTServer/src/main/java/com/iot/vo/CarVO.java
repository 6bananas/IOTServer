package com.iot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarVO {
    private Integer vid;
    private Integer pid;
    private Integer light;
    private Integer state;
    private Double voltage;
    private Double temperature;
    private Double power;
}
