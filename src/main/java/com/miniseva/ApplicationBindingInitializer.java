package com.miniseva;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * ref: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 */
@ControllerAdvice
@Controller
public class ApplicationBindingInitializer {

    /**
     * On POST, save empty textboxes as null not as empty string "". Importantly, this does not solve the problem of
     * input type="number" from causing a binding error during validation. The solution is to manually validate numeric
     * textboxes.
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
