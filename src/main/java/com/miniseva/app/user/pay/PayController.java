package com.miniseva.app.user.pay;

import com.miniseva.security.account.Account;
import com.miniseva.app.orders.Orders;
import com.miniseva.app.orders.OrdersRepository;
import com.miniseva.app.schedule.Schedule;
import com.miniseva.app.schedule.ScheduleRepository;
import com.miniseva.app.card.Card;
import com.miniseva.app.card.CardRepository;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;
import com.miniseva.security.account.Account;

@Controller
public class PayController {
    private static final Logger log = LoggerFactory.getLogger(PayController.class);

    private AccountService srvAccount;
    private OrdersRepository repoOrders;
    private ScheduleRepository repoSchedule;
    private CardRepository repoCard; 
    private CustomerRepository repoCustomer;

    public PayController(AccountService srvAccount,
                         OrdersRepository repoOrders,
                         ScheduleRepository repoSchedule,
                         CardRepository repoCard,
                         CustomerRepository repoCustomer) {
        this.srvAccount = srvAccount;         
        this.repoOrders = repoOrders;
        this.repoSchedule = repoSchedule;
        this.repoCard = repoCard;
        this.repoCustomer = repoCustomer;
    }

    @RequestMapping(value = { "/app/pay" }, method = POST)
    public String postPayPage(HttpServletRequest request, Model model,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="orderId") Long orderId,
            @RequestParam(value="amount") Integer amount,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);
        Account account = srvAccount.findById(userId);
        Customer customer = repoCustomer.findById(account.getCustomerId());
        Card card = repoCard.findByCardNo(customer.getCardNo());

        int balance = card.getAmount() - amount;

        if(balance < 0) {
            model.addAttribute("successMsg", "Cannot make payment for the current order using your card no. as '" + card.getCardNo() + "'. Your card balance is Rs. " + card.getAmount() + ".00 which is not sufficient to create this order.");
            return "app/user/pay/pay";
        }

        Orders saveOrder = repoOrders.findById(orderId);
        log.info("Order Found: " + saveOrder.toString());
        saveOrder.setCreatedBy(userId);

        List<Schedule> schedules = repoSchedule.findByOrderId(orderId);        

        if (action != null) {
            try {
                    // Update order with user id
                    saveOrder = repoOrders.save(saveOrder);
                    log.info("Saved order to DB: " + saveOrder.toString());

                    // Update schedules with user id
                    for(Schedule schedule: schedules) {
                        schedule.setCreatedBy(userId);
                        repoSchedule.save(schedule);
                    }

                    // Update card amount
                    card.setAmount(balance);

                    card = repoCard.save(card);
                    log.info("Saved order to DB: " + saveOrder.toString());

                    model.addAttribute("successMsg", "Successfully paid the order using your card no. as '" + card.getCardNo() + "'. Your updated balance is Rs. " + balance + ".00");
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't make payment for current order.");
                }
        }

        return "app/user/pay/pay";
    }

    // @GetMapping(value = {"/app/pay"})
    // public String getPayPage(HttpServletRequest request,
    //         Model model) {

    //     model.addAttribute("itemName", "Block");

    //     pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
    //     // Get a page of block. Note: page is 0-based, but displayed as 1-based.
    //     PageRequest pageRequest =
    //             new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
    //     Page<Block> blocks = repoBlock.findAll(pageRequest);

    //     if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
    //         blocks = repoBlock.findByNameContainingIgnoreCase(searchValue, pageRequest);
    //     }

    //     // Set lead name for each block
    //     for (Block block : blocks) {
    //         block.setLeadName(repoLead.findById(block.getLeadId()).getName());
    //     }

    //     model.addAttribute("blocks",blocks);

    //     // Counts for block
    //     long numBlock = repoBlock.count();

    //     if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
    //         numBlock = repoBlock.countByNameContainingIgnoreCase(searchValue);
    //     }

    //     // First, last, next and previous links
    //     String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
    //     String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
    //     String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

    //     // Calculate the last page
    //     int intLastPage = (int) numBlock/PAGE_SIZE;
    //     if (numBlock % PAGE_SIZE != 0) {
    //         intLastPage = intLastPage + 1;        
    //     } 
    //     String lastPage = String.valueOf(intLastPage);
    //     model.addAttribute("hasPrevious", blocks.hasPrevious());
    //     model.addAttribute("hasNext", blocks.hasNext());
    //     model.addAttribute("previousPage", previousPage);
    //     model.addAttribute("nextPage", nextPage);
    //     model.addAttribute("firstPage", firstPage);
    //     model.addAttribute("lastPage", lastPage);

    //     model.addAttribute("numBlock", numBlock);

    //     model.addAttribute("range", pageNumber.get() + " of " + lastPage);

    //     String baseBlockUrl = "/app/admin/block";

    //     String previousUrl = baseBlockUrl + "/" + previousPage;
    //     String nextUrl = baseBlockUrl + "/" + nextPage;
    //     String firstUrl = baseBlockUrl + "/" + firstPage;
    //     String lastUrl = baseBlockUrl + "/" + lastPage;

    //     if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
    //         previousUrl = previousUrl + "?action=search&searchValue=" + searchValue;
    //         nextUrl = nextUrl + "?action=search&searchValue=" + searchValue;
    //         firstUrl = firstUrl + "?action=search&searchValue=" + searchValue;
    //         lastUrl = lastUrl + "?action=search&searchValue=" + searchValue;
    //         model.addAttribute("action", action);
    //         model.addAttribute("searchValue", searchValue);
    //     }

    //     // First, last, next and previous pages urls
    //     model.addAttribute("previousUrl", previousUrl);
    //     model.addAttribute("nextUrl", nextUrl);
    //     model.addAttribute("firstUrl", firstUrl);
    //     model.addAttribute("lastUrl", lastUrl);

    //     // Base url for Add and Edit
    //     model.addAttribute("detailsUrl", baseBlockUrl + "/details");
    //     model.addAttribute("addBlockUrl", baseBlockUrl + "/add");
    //     model.addAttribute("baseUrl", baseBlockUrl);

    //     if(msg != null) {
    //         if(msg.equals("sv")) {
    //             model.addAttribute("successMsg", "Successfully added block.");
    //         } else if(msg.equals("ed")) {
    //             model.addAttribute("successMsg", "Successfully updated block.");
    //         }
    //     }

    //     return "app/admin/block/block-list";
    // }

    // @GetMapping(value = {"/app/admin/block/{pageNumber}", "/app/admin/block" })
    // public String getBlockPage(HttpServletRequest request,
    //         @PathVariable Optional<Integer> pageNumber,
    //         @RequestParam(value="action", required=false) String action,
    //         @RequestParam(value="searchValue", required=false) String searchValue,
    //         @RequestParam(value="msg", required=false) String msg,
    //         Model model) {

    //     model.addAttribute("itemName", "Block");

    //     pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
    //     // Get a page of block. Note: page is 0-based, but displayed as 1-based.
    //     PageRequest pageRequest =
    //             new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
    //     Page<Block> blocks = repoBlock.findAll(pageRequest);

    //     if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
    //         blocks = repoBlock.findByNameContainingIgnoreCase(searchValue, pageRequest);
    //     }

    //     // Set lead name for each block
    //     for (Block block : blocks) {
    //         block.setLeadName(repoLead.findById(block.getLeadId()).getName());
    //     }

    //     model.addAttribute("blocks",blocks);

    //     // Counts for block
    //     long numBlock = repoBlock.count();

    //     if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
    //         numBlock = repoBlock.countByNameContainingIgnoreCase(searchValue);
    //     }

    //     // First, last, next and previous links
    //     String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
    //     String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
    //     String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

    //     // Calculate the last page
    //     int intLastPage = (int) numBlock/PAGE_SIZE;
    //     if (numBlock % PAGE_SIZE != 0) {
    //         intLastPage = intLastPage + 1;        
    //     } 
    //     String lastPage = String.valueOf(intLastPage);
    //     model.addAttribute("hasPrevious", blocks.hasPrevious());
    //     model.addAttribute("hasNext", blocks.hasNext());
    //     model.addAttribute("previousPage", previousPage);
    //     model.addAttribute("nextPage", nextPage);
    //     model.addAttribute("firstPage", firstPage);
    //     model.addAttribute("lastPage", lastPage);

    //     model.addAttribute("numBlock", numBlock);

    //     model.addAttribute("range", pageNumber.get() + " of " + lastPage);

    //     String baseBlockUrl = "/app/admin/block";

    //     String previousUrl = baseBlockUrl + "/" + previousPage;
    //     String nextUrl = baseBlockUrl + "/" + nextPage;
    //     String firstUrl = baseBlockUrl + "/" + firstPage;
    //     String lastUrl = baseBlockUrl + "/" + lastPage;

    //     if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
    //         previousUrl = previousUrl + "?action=search&searchValue=" + searchValue;
    //         nextUrl = nextUrl + "?action=search&searchValue=" + searchValue;
    //         firstUrl = firstUrl + "?action=search&searchValue=" + searchValue;
    //         lastUrl = lastUrl + "?action=search&searchValue=" + searchValue;
    //         model.addAttribute("action", action);
    //         model.addAttribute("searchValue", searchValue);
    //     }

    //     // First, last, next and previous pages urls
    //     model.addAttribute("previousUrl", previousUrl);
    //     model.addAttribute("nextUrl", nextUrl);
    //     model.addAttribute("firstUrl", firstUrl);
    //     model.addAttribute("lastUrl", lastUrl);

    //     // Base url for Add and Edit
    //     model.addAttribute("detailsUrl", baseBlockUrl + "/details");
    //     model.addAttribute("addBlockUrl", baseBlockUrl + "/add");
    //     model.addAttribute("baseUrl", baseBlockUrl);

    //     if(msg != null) {
    //         if(msg.equals("sv")) {
    //             model.addAttribute("successMsg", "Successfully added block.");
    //         } else if(msg.equals("ed")) {
    //             model.addAttribute("successMsg", "Successfully updated block.");
    //         }
    //     }

    //     return "app/admin/block/block-list";
    // }
}