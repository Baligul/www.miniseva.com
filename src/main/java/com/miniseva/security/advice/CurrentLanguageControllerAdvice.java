package com.miniseva.security.advice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Make the CSRF token available on every page.
 */
@ControllerAdvice
@Controller
public class CurrentLanguageControllerAdvice {
    @ModelAttribute
    public void enrichCurrentLanguage(HttpServletRequest request, Model model) {
        String currentLanguage = (String) request.getSession().getAttribute("currentLanguage");

        // if current language is null then set the default language to english
        if (currentLanguage == null || currentLanguage == "") {
            request.getSession().setAttribute("currentLanguage", "english");
            model.addAttribute("language", "english");
        } else {
            model.addAttribute("language", currentLanguage);
        }
    }
}