package com.iot.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @TableId
    private Integer vid;
    private Integer pid;
    private Integer light;
    private Integer state;
}
