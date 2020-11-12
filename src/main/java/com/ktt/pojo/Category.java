package com.ktt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    // 视频类别id
    private String categoryId;
    // 视频类别编号
    private Integer number;
    // 视频类型名称
    private String typeName;
}
