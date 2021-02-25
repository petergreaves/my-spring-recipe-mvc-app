package com.ibm.petergreaves.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParamFormatException extends RuntimeException{
    public ParamFormatException() {
        super();
    }

    public ParamFormatException(String message) {
        super(message);
    }

    public ParamFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
