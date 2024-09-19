package com.interviews.req10079266.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class InvalidInputDateException extends RuntimeException {

    private String inputDate;

    @Override
    public String getMessage() {
        return String.format("Invalid input date: %s", this.inputDate);
    }
}
