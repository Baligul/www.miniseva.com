package com.miniseva.app.admin.users;

import com.miniseva.security.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Change account password.
 */
@Controller
public class AdminChangePasswordController {
    private static final Logger log = LoggerFactory.getLogger(AdminChangePasswordController.class);
    private AccountService srvAccount;
    private PasswordEncoder passwordEncoder;

    public AdminChangePasswordController(AccountService srvAccount,
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
    @RequestMapping(value = { "/app/admin/users/{userId}/change-password"}, method = GET)
    public String changePasswordForm(HttpServletRequest request, Model model, @PathVariable Long userId) {
        Account account = srvAccount.findById(userId);

        model.addAttribute("formPostUrl", "/app/admin/users/" + userId + "/change-password");
        model.addAttribute("user", account);

        return "app/admin/users/change-password";
    }

    @RequestMapping(value = { "/app/admin/users/{userId}/change-password"}, method = POST)
    public String changePasswordSubmit(HttpServletRequest request, @Valid Account account, 
            BindingResult bindingResult,Model model, @PathVariable Long userId, 
            @RequestParam String confirmPassword, Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long currentUserId = srvAccount.getUserId(username);

        Account savedAccount = srvAccount.findById(userId);
        model.addAttribute("user", savedAccount);
        
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldError("password");
            if (error != null) {
                model.addAttribute("passwordError", error.getDefaultMessage());
                log.info("Change Password validation errors.");
                return "app/admin/users/change-password";
            }
        }
        
        // Check that passwords are the same
        if (!account.getPassword().equals(confirmPassword)) {
            log.info("Password and confirm password do not match.");
            model.addAttribute("confirmPasswordError", "Password and confirm password do not match");
            return "app/admin/users/change-password";
        }

        // Encode the password
        savedAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        savedAccount.setUpdatedBy(currentUserId);
        savedAccount.setUpdatedOn(DateTime.now());
        savedAccount = srvAccount.save(savedAccount);

        log.info("Saved account password to DB: " + savedAccount.toString());

        return "redirect:/app/admin/users/details/" + userId;

    }
}