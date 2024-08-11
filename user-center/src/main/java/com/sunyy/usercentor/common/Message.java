package com.sunyy.usercentor.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ovi
 * @since 2024/7/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 消息
     */
    private String msg;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * data
     */
    private Object data;

    public static Message ok(Object data) {
        return new Message("ok", true, data);
    }

    public static Message ok(String msg, Object data) {
        return new Message(msg, true, data);
    }

    public static Message error(String msg) {
        return new Message(msg, true, null);
    }

}
