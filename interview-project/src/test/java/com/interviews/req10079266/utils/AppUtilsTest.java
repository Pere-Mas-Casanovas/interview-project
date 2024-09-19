package com.interviews.req10079266.utils;

import com.interviews.req10079266.exceptions.InvalidInputDateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppUtilsTest {

    @Autowired
    private AppUtils appUtils;

    @Test
    public void shouldNormallyCheckInputDateFormat() {
        appUtils.checkInputDateFormat("2024-01-04");
    }

    @Test
    public void shouldThrowAnInvalidInputDateExceptionBecauseTheDateIsWrong() {
        Assertions.assertThrows(InvalidInputDateException.class, () -> { appUtils.checkInputDateFormat("wrong-date"); });
    }
}
