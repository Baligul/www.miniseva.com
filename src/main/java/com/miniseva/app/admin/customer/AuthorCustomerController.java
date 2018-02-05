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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
 * Author (add, edit, delete) customer.
 */
@Controller
public class AuthorCustomerController {
    private static final Logger log = LoggerFactory.getLogger(AuthorCustomerController.class);
    private CustomerRepository repoCustomer;
    private AccountService srvAccount;
    private LeadRepository repoLead;
    private BlockRepository repoBlock;
    private CardRepository repoCard;

    public AuthorCustomerController(CustomerRepository repoCustomer,
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

    /**
     * Edit an existing customer.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/customer/details/{customerId}" }, method = GET)
    public String authorCustomerForm(HttpServletRequest request,
                                     Model model,
                                     @PathVariable Long customerId) {
        Customer customer = null;

        customer = repoCustomer.findById(customerId);
        List<Card> card = repoCard.getRemainingCardsForCustomer();
        card.add(repoCard.findByCardNo(customer.getCardNo()));
        model.addAttribute("card", card);
        model.addAttribute("customer", customer);
        model.addAttribute("lead", repoLead.findAll());
        model.addAttribute("customerUrl", "/app/admin/customer");

        if (customer.getLeadId() != null) {
            model.addAttribute("block", repoBlock.findByLeadId(customer.getLeadId()));
        }

        return "app/admin/customer/customer-details";
    }

    @RequestMapping(value = { "/app/admin/customer/details/{customerId}" }, method = POST)
    public String customerSubmit(HttpServletRequest request, 
                              @Valid Customer customer, BindingResult bindingResult,
                              Model model, @PathVariable Long customerId, 
                              @RequestParam(value="action", required=false) String action,
                              Authentication authentication) {

        Customer savedCustomer = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        model.addAttribute("lead", repoLead.findAll());
        List<Card> card = repoCard.getRemainingCardsForCustomer();
        card.add(repoCard.findByCardNo(customer.getCardNo()));
        model.addAttribute("card", card);
        model.addAttribute("customerUrl", "/app/admin/customer");
        model.addAttribute("customer", customer);

        log.info("Customer DETAILS: " + customer.toString());

        if (customer.getLeadId() != null) {
            model.addAttribute("block", repoBlock.findByLeadId(customer.getLeadId()));
        }

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("no");                
                if (error != null) {
                    model.addAttribute("noError", error.getDefaultMessage());
                    log.info("Edit Customer validation errors.");
                    return "app/admin/customer/customer-details";
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
                    log.info("Edit Customer validation errors.");
                    return "app/admin/customer/customer-details";
                }
            }

            if (action.equals("save")) {
                savedCustomer = repoCustomer.findById(customerId);
                savedCustomer.setUpdatedBy(userId);
                savedCustomer.setUpdatedOn(DateTime.now());
                savedCustomer.setCardNo(customer.getCardNo());
                savedCustomer.setNo(customer.getNo());
                savedCustomer.setLeadId(customer.getLeadId());
                savedCustomer.setBlockId(customer.getBlockId());
                try {
                        savedCustomer = repoCustomer.save(savedCustomer);
                        log.info("Saved customer to DB: " + savedCustomer.toString());
                        return "redirect:/app/admin/customer?msg=ed";
                    } catch (Exception e) {
                        log.info("Cannot update customer to DB: " + savedCustomer.toString());
                        model.addAttribute("successMsg", "Database Error.");
                }
            }
            model.addAttribute("customer", savedCustomer);
        }

        return "app/admin/customer/customer-details";
    }
}