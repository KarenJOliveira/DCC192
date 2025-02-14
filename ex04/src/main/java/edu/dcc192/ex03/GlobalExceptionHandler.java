package edu.dcc192.ex03;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error"; // Nome da página no diretório "templates"
    }
}
