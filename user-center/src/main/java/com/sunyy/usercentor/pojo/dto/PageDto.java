package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ovi
 * @since 2024/8/17
 */
@Data
@ToString
public class PageDto {

    /**
     * 页码，从1开始
     */
    private Long pageNum;

    /**
     * 每页数量
     */
    private Long pageSize;
}
