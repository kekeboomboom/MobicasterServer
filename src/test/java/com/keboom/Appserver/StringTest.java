package com.keboom.Appserver;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/22
 * {@code @description:}
 */
public class StringTest {

    @Test
    void userString() {
        String user = "abc/1";
        String replace = StringUtils.replace(user, "/", "%2F");
        System.out.println(replace);
    }
}
