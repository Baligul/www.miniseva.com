package com.miniseva.app.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniseva.security.account.Account;

/**
 * Change account password.
 */
@Controller
public class AccountChangePasswordController {
    private static final Logger log = LoggerFactory.getLogger(AccountChangePasswordController.class);
    private AccountService srvAccount;
    private PasswordEncoder passwordEncoder;

    public AccountChangePasswordController(AccountService srvAccount,
                                           PasswordEncoder passwordEncoder) {
        this.srvAccount = srvAccount;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Change a password of an account.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/profile/change-password"}, method = GET)
    public String changePasswordForm(HttpServletRequest request, Model model) {

        model.addAttribute("formPostUrl", "/app/profile/change-password");
        return "app/user/change-password";
    }

    @RequestMapping(value = { "/app/profile/change-password"}, method = POST)
    public String changePasswordSubmit(HttpServletRequest request, @Valid Account account,
            BindingResult bindingResult,Model model, @RequestParam String confirmPassword, 
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldError("password");
            if (error != null) {
                model.addAttribute("passwordError", error.getDefaultMessage());
                log.info("Change Password validation errors.");
                return "app/user/change-password";
            }
        }

        Account savedAccount = srvAccount.findById(userId);

        // Check that passwords are the same
        if (!account.getPassword().equals(confirmPassword)) {
            log.info("Password and confirm password do not match.");
            model.addAttribute("confirmPasswordError", "Password and confirm password do not match");
            return "app/user/change-password";
        }

        // Encode the password
        savedAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        savedAccount.setUpdatedBy(userId);
        savedAccount.setUpdatedOn(DateTime.now());
        savedAccount = srvAccount.save(savedAccount);

        String successMsg = savedAccount.getName() + " password has been changed successfully.";

        log.info("Saved account password to DB: " + savedAccount.toString());

        return "redirect:/app/profile?msg=" + successMsg;

    }

}