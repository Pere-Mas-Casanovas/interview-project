package com.interviews.req10079266.resources;

import com.interviews.req10079266.exceptions.InvalidInputDateException;
import com.interviews.req10079266.data.ErrorResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice(annotations = RestController.class)
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ InvalidInputDateException.class})
    public ResponseEntity<ErrorResponseData> handleInvalidReportingFormatException(InvalidInputDateException ex) {
        return new ResponseEntity<>(new ErrorResponseData(ex.getMessage(), ex.getClass()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponseData> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponseData(ex.getMessage(), ex.getClass()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
