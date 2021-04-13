package com.ibm.petergreaves.recipe.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

/*

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleParamFormatEx(Exception  exception ){

        log.error("Got a 400 error");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ex", exception);
        modelAndView.setViewName("400error");

        return modelAndView;
    }

*/
}
