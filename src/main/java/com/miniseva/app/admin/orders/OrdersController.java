package com.miniseva.app.admin.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.lang.Boolean;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static com.miniseva.configuration.Configuration.*;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

@Controller
public class OrdersController {
    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);

    private OrdersRepository repo;
    private AccountService srvAccount;

    public OrdersController(OrdersRepository repo,
                            AccountService srvAccount) {
        this.repo = repo;
        this.srvAccount = srvAccount;
    }

    @GetMapping(value = { "app/admin/orders/{pageNumber}", "app/admin/orders" })
    public String getOrdersPage(HttpServletRequest request,
            Model model, @PathVariable Optional<Integer> pageNumber,
            Authentication authentication) {

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        Page<Orders> page = null;
        long numOrders = 0L;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        // Get a page of Orders. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, DESC, "createdOn");

        page = repo.findAll(pageRequest);
        
        model.addAttribute("orders", page);

        numOrders = repo.count();
        
        model.addAttribute("numOrders", numOrders);

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
        model.addAttribute("hasPrevious", page.hasPrevious());
        model.addAttribute("hasNext", page.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("pageNumber", pageNumber.get());

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseOrderUrl = "/app/admin/orders";

        // First, last, next and previous pages urls
        model.addAttribute("previousUrl", baseOrderUrl + "/" + previousPage);
        model.addAttribute("nextUrl", baseOrderUrl + "/" + nextPage);
        model.addAttribute("firstUrl", baseOrderUrl + "/" + firstPage);
        model.addAttribute("lastUrl", baseOrderUrl + "/" + lastPage);

   	    model.addAttribute("baseUrl", baseOrderUrl);
        
        return "app/admin/orders/orders-list";

    }
}