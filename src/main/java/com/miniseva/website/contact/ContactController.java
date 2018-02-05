package com.miniseva.website.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ContactController {
    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private ContactRepository repo;

    public ContactController(ContactRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/contact")
    public String page(HttpServletRequest request, Model model, Principal principal) {
        model.addAttribute("pageContact", true);
        model.addAttribute("page", "contact");
        return "website/contact/contact-form";
    }

    @PostMapping("/contact")
    public String save(HttpServletRequest request, Model model, @Valid Contact contact, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldError("fullname");
            if (error != null) {
                model.addAttribute("fullnameError", error.getDefaultMessage());
            }
            error = bindingResult.getFieldError("email");
            if (error != null) {
                model.addAttribute("emailError", error.getDefaultMessage());
            }
            error = bindingResult.getFieldError("note");
            if (error != null) {
                model.addAttribute("noteError", error.getDefaultMessage());
            }

            log.info("Add Contact validation errors.");

            return "website/contact/contact-form";
        }

        model.addAttribute("contact", contact);
        model.addAttribute("page", "contact");
        
        // TODO: Create a contactService then remove repo
        repo.save(contact);
        log.info("Saved contact: " + contact.toString());
        return "website/contact/contact-result";
    }
}
