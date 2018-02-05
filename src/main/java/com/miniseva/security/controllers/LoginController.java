package com.miniseva.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private ProductRepository repoProduct;

    public LoginController(ProductRepository repoProduct) {
        this.repoProduct = repoProduct;
    }

    /**
     * Display the log in form. If the user has already signed in via a provider (FB, Twitter, LinkedIn, etc.) then
     * prepopulate the log in form.
     * <p>
     * If url == /login, then un/pw login; if url == /signin then social auth redirect.
     *
     * @param model
     * @param error
     * @param logout
     * @return
     */
    @GetMapping(value = { "/signin", "/login" })
    public String showLoginForm(Model model, HttpSession session,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        model.addAttribute("products",repoProduct.findAll());
                
        boolean hasError = false;
        String errorMessage = "";

        if (error != null) {
            hasError = true;
            errorMessage = "Invalid credentials provided."; // Default error message

            Throwable lastException = (Throwable) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            if (lastException instanceof DisabledException) {
                errorMessage = "Account is disabled.";
            }
            session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }

        if (logout != null)
            model.addAttribute("message", "Logout successful.");

        model.addAttribute("hasError", hasError);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("page", "login");

        model.addAttribute("pageLogin", true);
        return "security/login-form";

    }

}
