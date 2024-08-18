package com.sunyy.usercentor.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ovi
 * @since 2024/8/17
 */
public class RegUtil {
    private RegUtil() {
    }

    /**
     * 邮箱正则校验
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /**
     * 校验邮箱是否合法
     *
     * @param email 邮箱
     * @return true 合法 false 不合法
     */
    public static boolean checkEmail(String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.find();
    }

}
