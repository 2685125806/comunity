package com.twinkle.community.advice;

import com.twinkle.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle( Throwable ex, Model model) {
        if (ex instanceof CustomizeException){
            model.addAttribute("message",ex.getMessage());
        }else {
            model.addAttribute("message","服务器炸了,待会再试试");
        }
        System.out.println("CE---");
        return new ModelAndView("error");
    }


}
