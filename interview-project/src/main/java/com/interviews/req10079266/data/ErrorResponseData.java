package com.interviews.req10079266.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseData implements Serializable {
    private String customMessage;
    private Class cause;
}
