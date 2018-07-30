package com.miniseva.app.admin.orders;

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
import com.miniseva.app.schedule.Schedule;
import com.miniseva.app.schedule.ScheduleRepository;

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
import com.miniseva.common.utility.Utility;

@Controller
public class AdminOrdersController {
    private static final Logger log = LoggerFactory.getLogger(AdminOrdersController.class);

    private BlockRepository repoBlock;
    private LeadRepository repoLead;
    private AccountService srvAccount;
    private CustomerRepository repoCustomer;
    private OrdersRepository repoOrders;
    private ScheduleRepository repoSchedule;
    private Utility utility;


    public AdminOrdersController(BlockRepository repoBlock,
                                 LeadRepository repoLead,
                                 CustomerRepository repoCustomer,
                                 AccountService srvAccount,
                                 OrdersRepository repoOrders,
                                 ScheduleRepository repoSchedule) {
        this.repoBlock = repoBlock;
        this.repoLead = repoLead;
        this.repoCustomer = repoCustomer;
        this.srvAccount = srvAccount;
        this.repoOrders = repoOrders;
        this.repoSchedule = repoSchedule;
        this.utility = new Utility(repoBlock, repoLead, repoCustomer, srvAccount, repoSchedule);
    }

    @GetMapping(value = {"/app/admin/orders/{pageNumber}", "/app/admin/orders" })
    public String getOrdersPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            Model model) {

        model.addAttribute("itemName", "Orders");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of orders. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, DESC, "createdOn");
        Page<Orders> orders = repoOrders.findByCreatedByNotNull(pageRequest);

        // if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
        //     blocks = repoBlock.findByNameContainingIgnoreCase(searchValue, pageRequest);
        // }

        // Set the orderBy and Address for each order
        for (Orders order : orders) {
            order.setOrderBy(utility.makeOrderBy(order));
            order.setAddress(utility.makeAddress(order));
        }

        model.addAttribute("orders",orders);

        // Counts for orders
        long numOrders = repoOrders.countByCreatedByNotNull();

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

        return "app/admin/orders/orders-list";
    }

    @GetMapping(value = {"/app/admin/orders/details/{orderId}/{pageNumber}", "/app/admin/orders/details/{orderId}" })
    public String getSchedulePage(HttpServletRequest request,
            @PathVariable Long orderId,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            Model model) {

        model.addAttribute("itemName", "Schedule");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of schedules. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, DESC, "createdOn");
        Page<Schedule> schedules = repoSchedule.findByOrderIdAndCreatedByNotNull(orderId, pageRequest);

        // if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
        //     blocks = repoBlock.findByNameContainingIgnoreCase(searchValue, pageRequest);
        // }
        
        Orders order = repoOrders.findById(orderId);
        String orderBy = utility.makeOrderBy(order);
        String address = utility.makeAddress(order);

        // Set the orderBy and Address for each schedule
        for (Schedule schedule : schedules) {           
            schedule.setOrderBy(orderBy);
            schedule.setAddress(address);
        }

        model.addAttribute("schedules",schedules);

        // Counts for schedules
        long numSchedule = repoSchedule.countByOrderIdAndCreatedByNotNull(orderId);

        // if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
        //     numOrders = repoOrders.countByNameContainingIgnoreCase(searchValue);
        // }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numSchedule/PAGE_SIZE;
        if (numSchedule % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", schedules.hasPrevious());
        model.addAttribute("hasNext", schedules.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numSchedule", numSchedule);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseScheduleUrl = "/app/admin/schedule";
        String baseOrderUrl = "/app/admin/orders";

        String previousUrl = baseScheduleUrl + "/" + previousPage;
        String nextUrl = baseScheduleUrl + "/" + nextPage;
        String firstUrl = baseScheduleUrl + "/" + firstPage;
        String lastUrl = baseScheduleUrl + "/" + lastPage;

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
        model.addAttribute("addScheduleUrl", baseScheduleUrl + "/add");
        model.addAttribute("baseUrl", baseScheduleUrl);

        return "app/admin/schedule/schedule-list";
    }
}