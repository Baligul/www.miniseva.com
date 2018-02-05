package com.miniseva.common.newsletter_signup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Author (add) newsletter_signup.
 */
@Controller
public class NewsletterSignupController {
    private static final Logger log = LoggerFactory.getLogger(NewsletterSignupController.class);
    private NewsletterSignupRepository repo;

    public NewsletterSignupController(NewsletterSignupRepository repo) {
        this.repo = repo;
    }

    /**
     * Add a new email to the newsletter_signup table.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/newsletter/signup" }, method = POST)
    public String newsletterSignupSubmit(HttpServletRequest request, @Valid NewsletterSignup newsletterSignup,
            BindingResult bindingResult,Model model) {
                
        if (bindingResult.hasErrors()) {
            FieldError errorEmail = bindingResult.getFieldError("email");
            if (errorEmail != null) {
                log.info("Not a valid email.");
            }
            log.info("Add NewsletterSignup validation errors.");
        }

        // Check for duplicate emails (i.e. does email already exist in DB)
        int emailExists = repo.emailExists(newsletterSignup.getEmail());
        if (emailExists > 0) {
            log.info("Email already exists. [email: " + newsletterSignup.getEmail() + "]");
        }

        if (!bindingResult.hasErrors() && emailExists == 0) {
            NewsletterSignup savedNewsletterSignup = repo.save(newsletterSignup);
            log.info("Saved email to DB: " + savedNewsletterSignup.toString());
        }

        return "redirect:/";
    }
}