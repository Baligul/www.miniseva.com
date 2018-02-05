package com.miniseva.app.admin.customer;

import com.miniseva.security.account.Account;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.block.BlockRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private CustomerRepository repoCustomer;
    private LeadRepository repoLead;
    private BlockRepository repoBlock;
    private CardRepository repoCard;

    public CustomerController(CustomerRepository repoCustomer,
                              LeadRepository repoLead,
                              BlockRepository repoBlock,
                              CardRepository repoCard) {
        this.repoCustomer = repoCustomer;
        this.repoLead = repoLead;
        this.repoBlock = repoBlock;
        this.repoCard = repoCard;
    }

    @GetMapping(value = {"/app/admin/customer/{pageNumber}", "/app/admin/customer" })
    public String getCustomerPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Customer");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of customer. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "no");
        Page<Customer> customers = repoCustomer.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            customers = repoCustomer.findByNoContainingIgnoreCase(searchValue, pageRequest);
        }

        // Set the Lead name and Block name and card for each Customer
        for (Customer customer : customers) {
            if (customer.getLeadId() != null) {
                customer.setLeadName(repoLead.findById(customer.getLeadId()).getName());
            }

            if (customer.getBlockId() != null) {
                customer.setBlockName(repoBlock.findById(customer.getBlockId()).getName());
            }
        }

        model.addAttribute("customers",customers);

        // Counts for customer
        long numCustomer = repoCustomer.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numCustomer = repoCustomer.countByNoContainingIgnoreCase(searchValue);
        }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numCustomer/PAGE_SIZE;
        if (numCustomer % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", customers.hasPrevious());
        model.addAttribute("hasNext", customers.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numCustomer", numCustomer);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseCustomerUrl = "/app/admin/customer";

        String previousUrl = baseCustomerUrl + "/" + previousPage;
        String nextUrl = baseCustomerUrl + "/" + nextPage;
        String firstUrl = baseCustomerUrl + "/" + firstPage;
        String lastUrl = baseCustomerUrl + "/" + lastPage;

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            previousUrl = previousUrl + "?action=search&searchValue=" + searchValue;
            nextUrl = nextUrl + "?action=search&searchValue=" + searchValue;
            firstUrl = firstUrl + "?action=search&searchValue=" + searchValue;
            lastUrl = lastUrl + "?action=search&searchValue=" + searchValue;
            model.addAttribute("action", action);
            model.addAttribute("searchValue", searchValue);
        }

        // First, last, next and previous pages urls
        model.addAttribute("previousUrl", previousUrl);
        model.addAttribute("nextUrl", nextUrl);
        model.addAttribute("firstUrl", firstUrl);
        model.addAttribute("lastUrl", lastUrl);

        // Base url for Add and Edit
        model.addAttribute("detailsUrl", baseCustomerUrl + "/details");
        model.addAttribute("addCustomerUrl", baseCustomerUrl + "/add");
        model.addAttribute("baseUrl", baseCustomerUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added customer.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated customer.");
            }
        }

        return "app/admin/customer/customer-list";
    }
}