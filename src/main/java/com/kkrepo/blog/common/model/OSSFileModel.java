package com.kkrepo.blog.common.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * @author WangRuofei
 * @create 2020-07-10 9:25 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@Builder
public class OSSFileModel {

    private String name;

    private String url;

    private long size;

    private Date lastModifyTime;
}
