package com.miniseva.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miniseva.security.account.Account;

import com.miniseva.website.contact.ContactRepository;
// import com.miniseva.app.admin.orders.OrdersRepository;
import com.miniseva.app.card.CardRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

@Controller
public class MiniSevaHomeController {
    private static final Logger log = LoggerFactory.getLogger(MiniSevaHomeController.class);

    private AccountService srvAccount;
    private ContactRepository repoContacts;
    // private OrdersRepository repoOrders;
    private CardRepository repoCard;
    private ProductRepository repoProduct;

    public MiniSevaHomeController(AccountService srvAccount,
                                  ContactRepository repoContacts,
                                //   OrdersRepository repoOrders,
                                  CardRepository repoCard,
                                  ProductRepository repoProduct) {
        this.srvAccount = srvAccount;
        this.repoContacts = repoContacts;
        // this.repoOrders = repoOrders;
        this.repoCard = repoCard;
        this.repoProduct = repoProduct;
    }

    @RequestMapping(method = GET, path = "/app")
    public String page(HttpServletRequest request, Model model,
                       Authentication authentication) {
        
        model.addAttribute("page", "dashboard");

        model.addAttribute("products",repoProduct.findAll());
        
        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long accountId = srvAccount.getUserId(username);

        Account account = srvAccount.findById(accountId);

        // As soon as account is logged in set the last_login time
        account.setLastLogin(DateTime.now());
        srvAccount.save(account);

        // Counts for accounts (only for root)
        Long numAccounts = 0L;

        // Counts for contacts (only for root)
        Long numContacts = 0L;

        // Counts for orders (only for root)
        Long numOrders = 0L;

        // Counts for card (only for root)
        Long numCard = 0L;

        if (user.isRoot()) { 
            numAccounts = srvAccount.countByRoleRootFalse();
            numContacts = repoContacts.count();
            // numOrders = repoOrders.count();
            numCard = repoCard.count();
        }

        model.addAttribute("numAccounts", numAccounts);
        model.addAttribute("numContacts", numContacts);
        // model.addAttribute("numOrders", numOrders);
        model.addAttribute("numCard", numCard);

        return "app/home";

    }

}
