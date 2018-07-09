package com.miniseva.app.account;

import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.card.Card;
import com.miniseva.app.card.CardRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;
import com.miniseva.security.account.Account;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * Author (add, edit, delete) companies.
 */
@Controller
public class AuthorAccountController {
    private static final Logger log = LoggerFactory.getLogger(AuthorAccountController.class);
    private AccountService srvAccount;
    private CustomerRepository repoCustomer;
    private LeadRepository repoLead;
    private CardRepository repoCard;

    public AuthorAccountController(AccountService srvAccount,
                                   CustomerRepository repoCustomer,
                                   LeadRepository repoLead,
                                   CardRepository repoCard) {
        this.srvAccount = srvAccount;
        this.repoCustomer = repoCustomer;
        this.repoLead = repoLead;
        this.repoCard = repoCard;
    }

    /**
     * Edit an existing user.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/profile" }, method = GET)
    public String authorStudentForm(HttpServletRequest request, 
                                    Model model,
                                    @RequestParam(value="msg", required=false) String msg,
                                    Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);
        Account account = srvAccount.findById(userId);

        model.addAttribute("user", account);
        
        if(account.getCustomerId() != null) {
            Customer customer = repoCustomer.findById(account.getCustomerId());
            model.addAttribute("customer", customer);
            
            if(customer.getLeadId() != null) {
                Lead lead = repoLead.findById(customer.getLeadId());
                model.addAttribute("lead", lead);
            }

            if(customer.getCardNo() != null) {
                Card card = repoCard.findByCardNo(customer.getCardNo());
                model.addAttribute("card", card);
            }
        }

        model.addAttribute("successMsg", msg);

        return "app/user/user-profile";

    }

    @RequestMapping(value = { "/app/profile" }, method = POST, params="action")
    public String studentSubmit(HttpServletRequest request,
                                @Valid Account user, BindingResult bindingResult,
                                Model model, @RequestParam String action,
                                Authentication authentication) {

        AccountDetails curentUser = new AccountDetails(authentication);
        String username = curentUser.getUsername();
        Long userId = srvAccount.getUserId(username);
        Account account = srvAccount.findById(userId);
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

             if (validationError) { 
                log.info("Save Account validation errors.");
                model.addAttribute("user", user);
                
                return "app/user/user-profile";
             }
        }

        if (action.equals("save")) {
            account.setUpdatedBy(userId);
            account.setUpdatedOn(DateTime.now());
             account.setName(user.getName());
            account.setEmail(user.getEmail());
            account.setUsername(user.getMobilePhone());
            account.setMobilePhone(user.getMobilePhone());
            try {
                    account = srvAccount.save(account);
                    log.info("Saved user to DB: " + account.toString());
                    model.addAttribute("successMsg", "Successfully saved " + account.getName() + ".");
                } catch (DataIntegrityViolationException e) {
                    log.info("Cannot save user to DB: " + account.toString());
                    if (e.getMessage().contains("mobile_phone")) {
                        model.addAttribute("mobilePhoneError", "Mobile '" + account.getMobilePhone() + "' already exists in our database.");
                    }
                    if (e.getMessage().contains("email")) {
                        model.addAttribute("emailError", "Email '" + account.getEmail() + "' already exists in our database.");
                    }
                    model.addAttribute("successMsg", account.getName() + " can not be saved.");
                } catch (Exception e) {
                    log.info("Cannot save user to DB: " + account.toString());
                    model.addAttribute("successMsg", "Database Error.");
            }
        } else if (action.equals("cancel")) {
            return "redirect:/app";
        } else if (action.equals("changePassword")) {
            return "redirect:/app/profile/change-password";
        }

        model.addAttribute("user", account);        

        if(account.getCustomerId() != null) {
            Customer customer = repoCustomer.findById(account.getCustomerId());
            model.addAttribute("customer", customer);
        }

        return "app/user/user-profile";

    }
}