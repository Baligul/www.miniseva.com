package com.miniseva.security.advice;

import org.springframework.security.web.csrf.CsrfToken;
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
public class CsrfControllerAdvice {
    @ModelAttribute
    public void enrichCSRF(HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // Exit immediately when token is null as this indicates that the method is being called after the template has
        // been rendered.
        if (token == null)
            return;

        model.addAttribute("csrfParameterName", token.getParameterName());
        model.addAttribute("csrfToken", token.getToken());
    }
}
