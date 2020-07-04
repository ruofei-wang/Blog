package com.kkrepo.blog.common.util;

import com.kkrepo.blog.common.model.Tree;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建评论树的工具类
 * @author WangRuofei
 * @create 2020-05-25 10:06 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class TreeUtil {

    public static <T> List<Tree<T>> build(List<Tree<T>> nodes) {
        if (nodes== null) {
            return new ArrayList<>();
        }

        List<Tree<T>> tree = new ArrayList<>();
        nodes.forEach(node -> {
            Long pId = node.getPId();
            if (pId == null || pId.equals(0L)) {
                //父节点
                tree.add(node);
                return;
            }
            for (Tree<T> c : nodes) {
                Long id = c.getId();
                if (id != null && id.equals(pId)) {
                    c.getChildren().add(node);
                    return;
                }
            }
        });
        return tree;
    }
}
