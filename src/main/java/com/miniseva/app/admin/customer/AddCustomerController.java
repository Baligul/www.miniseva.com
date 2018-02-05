package com.miniseva.app.admin.customer;

import com.miniseva.security.account.Account;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.block.BlockRepository;

import com.miniseva.app.card.Card;
import com.miniseva.app.card.CardRepository;

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

@Controller
public class AddCustomerController {
    private static final Logger log = LoggerFactory.getLogger(AddCustomerController.class);

    private CustomerRepository repoCustomer;
    private AccountService srvAccount;
    private LeadRepository repoLead;
    private BlockRepository repoBlock;
    private CardRepository repoCard;

    public AddCustomerController(CustomerRepository repoCustomer,
                                 AccountService srvAccount,
                                 LeadRepository repoLead,
                                 BlockRepository repoBlock,
                                 CardRepository repoCard) {
        this.repoCustomer = repoCustomer;
        this.srvAccount = srvAccount;
        this.repoLead = repoLead;
        this.repoBlock = repoBlock;
        this.repoCard = repoCard;
    }

    @GetMapping(value = {"/app/admin/customer/add" })
    public String getAddCustomerPage(HttpServletRequest request, 
                                    Model model) {

        model.addAttribute("card", repoCard.getRemainingCardsForCustomer());
        model.addAttribute("lead", repoLead.findAll());

        return "app/admin/customer/add-customer";
    }

    @RequestMapping(value = { "/app/admin/customer/add" }, method = POST)
    public String postAddCustomerPage(HttpServletRequest request, 
            @Valid Customer customer, 
            BindingResult bindingResult, Model model,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="leadId", required=false) Long leadId,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Customer saveCustomer = null;
        log.info("Customer Found: " + customer.toString());
        model.addAttribute("lead", repoLead.findAll());
        model.addAttribute("customer", customer);
        model.addAttribute("card", repoCard.getRemainingCardsForCustomer());

        if (leadId != null) {
            model.addAttribute("block", repoBlock.findByLeadId(leadId));
        }

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("no");                
                if (error != null) {
                    model.addAttribute("noError", error.getDefaultMessage());
                    log.info("Add Customer validation errors.");
                    return "app/admin/customer/add-customer";
                }

                error = bindingResult.getFieldError("cardNo");                
                if (error != null) {
                    model.addAttribute("cardNoError", error.getDefaultMessage());
                    log.info("Add Customer validation errors.");
                    return "app/admin/customer/add-customer";
                }

                error = bindingResult.getFieldError("leadId");                
                if (error != null) {
                    model.addAttribute("leadIdError", error.getDefaultMessage());
                    log.info("Add Customer validation errors.");
                    return "app/admin/customer/add-customer";
                }
            }
            
            if (action.equals("save")) {
                customer.setCreatedBy(userId);
                customer.setCreatedOn(DateTime.now());

                try {
                    log.info("Customer Values: " + customer.toString());
                    saveCustomer = repoCustomer.save(customer);                    
                    log.info("Saved customer to DB: " + saveCustomer.toString());
                    return "redirect:/app/admin/customer?msg=sv";
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the customer.");                    
                }
            }
        }

        return "app/admin/customer/add-customer";
    }
}