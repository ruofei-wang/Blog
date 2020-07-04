package com.kkrepo.blog.common.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * HighChart.js Spline图表数据封装
 * @author WangRuofei
 * @create 2020-07-03 10:11 上午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@Accessors(chain = true)
public class SplineChart implements Serializable {

    private Date dayTime;

    private Long num;
}
