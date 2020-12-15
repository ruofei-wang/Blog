package com.kkrepo.blog.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WangRuofei
 * @date 2020-11-30 9:21 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {
    private long id;
    private String name;
}
