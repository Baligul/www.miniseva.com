package com.miniseva.app.admin.users;

import com.miniseva.security.account.Account;
import com.miniseva.security.role.Role;
import com.miniseva.security.role.RoleService;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

/**
 * Author (add, edit, delete) users.
 */
@Controller
public class AuthorUserController {
    private static final Logger log = LoggerFactory.getLogger(AuthorUserController.class);    
    private CustomerRepository repoCustomer;
    private AccountService srvAccount;
    private RoleService srvRole;

    public AuthorUserController(CustomerRepository repoCustomer,
                                AccountService srvAccount,
                                RoleService srvRole) {
        this.repoCustomer = repoCustomer;
        this.srvAccount = srvAccount;
        this.srvRole = srvRole;
    }

    /**
     * Edit an existing account.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/users/details/{userId}" }, method = GET)
    public String authorUserForm(HttpServletRequest request, Model model,
                                @PathVariable Long userId) {

        model.addAttribute("baseUserUrl", "/app/admin/users");
        Account account = srvAccount.findById(userId);

        model.addAttribute("user", account);

        for(Role role : account.getRoles()) {
            if (role.getRole().equals("ROLE_CUSTOMER")) {
                model.addAttribute("isCustomer", true);
            }
        }

        List<Customer> customers = repoCustomer.getRemainingCustomers();
        customers.add(repoCustomer.findById(account.getCustomerId()));

        // Get the list of customers
        model.addAttribute("customer", customers);

        model.addAttribute("user",account);

        return "app/admin/users/user-details";

    }

    @RequestMapping(value = { "/app/admin/users/details/{userId}" }, method = POST)
    public String customerSubmit(HttpServletRequest request, 
                                @Valid Account account, BindingResult bindingResult,
                                Model model, @PathVariable Long userId, 
                                @RequestParam(value="action", required=false) String action,
                                @RequestParam(value="isCustomer", required=false) Boolean isCustomer,
                                Authentication authentication) {

        Account savedAccount = srvAccount.findById(userId);

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long currentUserId = srvAccount.getUserId(username);

        Set<Role> roles = new HashSet<Role>();

        List<Customer> customers = repoCustomer.getRemainingCustomers();
        customers.add(repoCustomer.findById(account.getCustomerId()));

        // Get the list of customers
        model.addAttribute("customer", customers);

        account.setEnabled(savedAccount.getEnabled());
        model.addAttribute("user", account);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                Boolean validationError = false;
                FieldError error = bindingResult.getFieldError("name");
                if (error != null) {
                    validationError = true;
                    model.addAttribute("nameError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("email");
                if (error != null) {
                    validationError = true;
                    model.addAttribute("emailError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("mobilePhone");
                if (error != null) {
                    validationError = true;
                    model.addAttribute("mobilePhoneError", error.getDefaultMessage());
                }
                error = bindingResult.getFieldError("customerId");
                if (error != null) {
                    validationError = true;
                    model.addAttribute("customerError", error.getDefaultMessage());
                }
                
                if (validationError) {
                    log.info("Save Account validation errors.");
                    return "app/admin/users/user-details";
                }
            }
            
            if (action.equals("save")) {
                savedAccount.setUpdatedBy(currentUserId);
                savedAccount.setUpdatedOn(DateTime.now());
                savedAccount.setName(account.getName());
                savedAccount.setEmail(account.getEmail());
                savedAccount.setUsername(account.getMobilePhone());
                savedAccount.setMobilePhone(account.getMobilePhone());
                savedAccount.setCustomerId(account.getCustomerId());

                // Set the roles for this account.
                if (isCustomer != null) {
                    roles.addAll(srvRole.findByRole("ROLE_CUSTOMER"));
                }
                
                savedAccount.setRoles(roles);
                savedAccount.setApproved(account.getApproved());
                try {
                        savedAccount = srvAccount.save(savedAccount);

                        log.info("Saved account to DB: " + savedAccount.toString());
                        model.addAttribute("successMsg", "Successfully saved " + account.getName() + ".");
                    }  catch (DataIntegrityViolationException e) {
                            log.info("Cannot save user to DB: " + savedAccount.toString());
                            if (e.getMessage().contains("mobile_phone")) {
                                model.addAttribute("mobilePhoneError", "Mobile '" + savedAccount.getMobilePhone() + "' already exists in our database.");
                            }
                            if (e.getMessage().contains("customer_id")) {
                                model.addAttribute("customerError", "Customer '" + savedAccount.getCustomerId() + "' already assigned to different user.");
                            }
                            if (e.getMessage().contains("email")) {
                                model.addAttribute("emailError", "Email '" + savedAccount.getEmail() + "' already exists in our database.");
                            }
                            model.addAttribute("successMsg", savedAccount.getName() + " can not be saved.");
                    }  catch (Exception e) {
                            log.info("Cannot save user to DB: " + savedAccount.toString());
                            model.addAttribute("successMsg", e.getMessage());
                    }
            } else if (action.equals("cancel")) {
                return "redirect:/app/admin/users";
            } else if (action.equals("changePassword")) {
                return "redirect:/app/admin/users/" + userId + "/change-password";
            } else if (action.equals("enableAccount")) {
                savedAccount = srvAccount.findById(userId);
                savedAccount.setEnabled(true);
                savedAccount.setUpdatedBy(currentUserId);
                savedAccount.setUpdatedOn(DateTime.now());
                savedAccount = srvAccount.save(savedAccount);
                log.info("Saved account to DB: " + savedAccount.toString());
            } else if (action.equals("disableAccount")) {
                savedAccount = srvAccount.findById(userId);
                savedAccount.setEnabled(false);
                savedAccount.setUpdatedBy(currentUserId);
                savedAccount.setUpdatedOn(DateTime.now());
                savedAccount = srvAccount.save(savedAccount);
                log.info("Saved account to DB: " + savedAccount.toString());
            }
            model.addAttribute("user", savedAccount);
            for(Role role : savedAccount.getRoles()) {
                if (role.getRole().equals("ROLE_CUSTOMER")) {
                    model.addAttribute("isCustomer", true);
                }
            }
        }

        model.addAttribute("baseUserUrl", "/app/admin/users");
        
        if (action == null) {
            model.addAttribute("user", account);
        }

        return "app/admin/users/user-details";
    }
}