package com.miniseva.app.admin.users;

import com.miniseva.security.account.Account;
import com.miniseva.security.role.Role;
import com.miniseva.security.role.RoleService;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;
import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

import org.springframework.dao.DataIntegrityViolationException;

@Controller
public class AddUserController {
    private static final Logger log = LoggerFactory.getLogger(AddUserController.class);

    private CustomerRepository repoCustomer;
    private AccountService srvAccount;
    private RoleService srvRole;
    private PasswordEncoder passwordEncoder;

    public AddUserController(CustomerRepository repoCustomer,
                             AccountService srvAccount,
                             RoleService srvRole,
                             PasswordEncoder passwordEncoder) {
        this.repoCustomer = repoCustomer;
        this.srvAccount = srvAccount;
        this.srvRole = srvRole;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"/app/admin/users/add" })
    public String getAddUserPage(HttpServletRequest request, Model model) {

        String baseUserUrl = "/app/admin/users";
        String formPostUrl = baseUserUrl + "/add";

        model.addAttribute("customer",repoCustomer.getRemainingCustomers());

        model.addAttribute("baseUserUrl", baseUserUrl);
        model.addAttribute("formPostUrl", formPostUrl);

        return "app/admin/users/add-user";
    }

    @RequestMapping(value = { "/app/admin/users/add" }, method = POST)
    public String postUsersPage(HttpServletRequest request, @Valid Account account, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            @RequestParam String confirmPassword,
            @RequestParam(value="isCustomer", required=false) Boolean isCustomer,
            @RequestParam(value="isDeliveryBoy", required=false) Boolean isDeliveryBoy,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Account saveAccount = null;
        Set<Role> roles = new HashSet<Role>();

        model.addAttribute("user",account);
        model.addAttribute("isCustomer", isCustomer);
         model.addAttribute("isDeliveryBoy", isDeliveryBoy);
        String baseUserUrl = "/app/admin/users";
        String formPostUrl = baseUserUrl + "/add";

        model.addAttribute("customer",repoCustomer.getRemainingCustomers());

        model.addAttribute("baseUserUrl", baseUserUrl);
        model.addAttribute("formPostUrl", formPostUrl);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("email");
                if (error != null) {
                    model.addAttribute("emailError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("name");
                if (error != null) {
                    model.addAttribute("nameError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("password");
                if (error != null) {
                    model.addAttribute("passwordError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("mobilePhone");
                if (error != null) {
                    model.addAttribute("mobilePhoneError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("customerId");
                if (error != null) {
                    model.addAttribute("customerError", error.getDefaultMessage());
                }

                log.info("Add Account validation errors.");
                return "app/admin/users/add-user";
            }

            // Check that passwords are the same
            if (!account.getPassword().equals(confirmPassword)) {
                log.info("Password and confirm password do not match.");
                model.addAttribute("confirmPasswordError", "Password and confirm password do not match");
                return "app/admin/users/add-user";
            }

            if (action.equals("save")) {
                account.setEnabled(true); // Enable the new account
                account.setUsername(account.getMobilePhone());
                account.setCreatedBy(userId);
                account.setCreatedOn(DateTime.now());

                // Encode the password
                account.setPassword(passwordEncoder.encode(account.getPassword()));

                // Set the roles for this account.
                if (isCustomer != null) {
                    roles.addAll(srvRole.findByRole("ROLE_CUSTOMER"));
                }
                if (isDeliveryBoy != null) {
                    roles.addAll(srvRole.findByRole("ROLE_DELIVERY_BOY"));
                }    
                account.setRoles(roles);

                try {
                    saveAccount = srvAccount.save(account);

                    log.info("Saved user to DB: " + saveAccount.toString());
                    model.addAttribute("successMsg", "Successfully saved " + saveAccount.getName() + ".");
                }  catch (DataIntegrityViolationException e) {
                        log.info("Cannot save user to DB: " + account.toString());
                        if (e.getMessage().contains("mobile_phone")) {
                            model.addAttribute("mobilePhoneError", "Mobile '" + account.getMobilePhone() + "' already exists in our database.");
                        }
                        if (e.getMessage().contains("customer_id")) {
                            model.addAttribute("customerError", "Customer '" + account.getCustomerId() + "' already assigned to different user.");
                        }
                        if (e.getMessage().contains("email")) {
                            model.addAttribute("emailError", "Email '" + account.getEmail() + "' already exists in our database.");
                        }
                        model.addAttribute("successMsg", account.getName() + " can not be saved.");
                }  catch (Exception e) {
                    log.info("Cannot save user to DB: " + account.toString());
                    model.addAttribute("successMsg", "Database Error.");
                    
                }
            }
        }

        return "app/admin/users/add-user";
    }
}