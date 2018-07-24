package com.miniseva.app.admin.block;

import com.miniseva.security.account.Account;
import com.miniseva.security.account.AccountService;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;
import com.miniseva.app.orders.Orders;
import com.miniseva.app.orders.OrdersRepository;

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

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class AdminOrdersController {
    private static final Logger log = LoggerFactory.getLogger(AdminOrdersController.class);

    private BlockRepository repoBlock;
    private LeadRepository repoLead;
    private AccountService srvAccount;
    private CustomerRepository repoCustomer;
    private OrdersRepository repoOrders;


    public AdminOrdersController(BlockRepository repoBlock,
                                LeadRepository repoLead,
                                CustomerRepository repoCustomer,
                                AccountService srvAccount,
                                OrdersRepository repoOrders) {
        this.repoBlock = repoBlock;
        this.repoLead = repoLead;
        this.repoCustomer = repoCustomer;
        this.srvAccount = srvAccount;
        this.repoOrders = repoOrders;
    }

    @GetMapping(value = {"/app/admin/orders/{pageNumber}", "/app/admin/orders" })
    public String getOrdersPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Orders");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of orders. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, DESC, "createdOn");
        Page<Orders> orders = repoOrders.findAll(pageRequest);

        // if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
        //     blocks = repoBlock.findByNameContainingIgnoreCase(searchValue, pageRequest);
        // }

        // Set the orderBy and Address for each order
        for (Orders order : orders) {
            order.setOrderBy(makeOrderBy(order));
            order.setAddress(makeAddress(order));
        }

        model.addAttribute("orders",orders);

        // Counts for orders
        long numOrders = repoOrders.count();

        // if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
        //     numOrders = repoOrders.countByNameContainingIgnoreCase(searchValue);
        // }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numOrders/PAGE_SIZE;
        if (numOrders % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", orders.hasPrevious());
        model.addAttribute("hasNext", orders.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numOrders", numOrders);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseOrderUrl = "/app/admin/orders";

        String previousUrl = baseOrderUrl + "/" + previousPage;
        String nextUrl = baseOrderUrl + "/" + nextPage;
        String firstUrl = baseOrderUrl + "/" + firstPage;
        String lastUrl = baseOrderUrl + "/" + lastPage;

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
        model.addAttribute("detailsUrl", baseOrderUrl + "/details");
        model.addAttribute("addOrderUrl", baseOrderUrl + "/add");
        model.addAttribute("baseUrl", baseOrderUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added block.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated block.");
            }
        }

        return "app/admin/block/block-list";
    }

    public String makeOrderBy(Orders order) {
        String orderBy = "";        
        Long createdById = order.getCreatedBy();
        Account account = srvAccount.findById(createdById);

        orderBy = account.getName() + "\n" + account.getUsername();

        return orderBy;
    }

    public String makeAddress(Orders order) {
        String address = "";
        Long createdById = order.getCreatedBy();
        Account account = srvAccount.findById(createdById);
        Long customerId = account.getCustomerId();
        Customer customer = repoCustomer.findById(customerId);
        Long leadId = customer.getLeadId();
        Lead lead = repoLead.findById(leadId);
        Long blockId = customer.getBlockId();
        if(blockId != null) {
            Block block = repoBlock.findById(blockId);
            address = "Block " + block.getName() + ", ";
        }

        address = address + lead.getName() + ", " +
                    lead.getName() + ", " + "\n" +
                    lead.getAddress1() + ", " + "\n" +
                    lead.getCity() + " - " +
                    lead.getPostalCode() + ", " + "\n" +
                    lead.getState();
                    
        return address;
    }
}