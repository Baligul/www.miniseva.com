package com.miniseva.app.admin.users;

import com.miniseva.security.account.Account;
import com.miniseva.security.role.Role;
import com.miniseva.app.customer.CustomerRepository;

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

import com.miniseva.security.account.AccountService;

@Controller
public class UsersController {
    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

   private AccountService srvAccount;
   private CustomerRepository repoCustomer;

    public UsersController(AccountService srvAccount,
                           CustomerRepository repoCustomer) {
        this.srvAccount = srvAccount;
        this.repoCustomer = repoCustomer;
    }

    @GetMapping(value = {"/app/admin/users/{pageNumber}", "/app/admin/users" })
    public String getUsersPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            Model model) {

        model.addAttribute("itemName", "Accounts");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);

        String nameToSearch = searchValue;

        // Get a page of users. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
        Page<Account> users = srvAccount.getByRoleRootFalse(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            if (nameToSearch.trim().contains(" ")) {
                nameToSearch = nameToSearch.trim().split(" ")[0];
            }
            nameToSearch = "%" + nameToSearch + "%";
            users = srvAccount.getByRoleRootFalseAndName(nameToSearch, pageRequest);
        }

        // Populate customer no for each user
        for(Account user : users){
            user.setCardNo(repoCustomer.findById(user.getCustomerId()).getNo());
        }

        model.addAttribute("users", users);

        // Counts for users
        long numUsers = srvAccount.countByRoleRootFalse();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numUsers = srvAccount.countByRoleRootFalseAndName(nameToSearch);
        }

        model.addAttribute("numUsers", numUsers);

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numUsers/PAGE_SIZE;
        if (numUsers % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", users.hasPrevious());
        model.addAttribute("hasNext", users.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseUserUrl = "/app/admin/users";

        // First, last, next and previous pages urls
        String previousUrl = baseUserUrl + "/" + previousPage;
        String nextUrl = baseUserUrl + "/" + nextPage;
        String firstUrl = baseUserUrl + "/" + firstPage;
        String lastUrl = baseUserUrl + "/" + lastPage;

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            previousUrl = previousUrl + "?action=search&searchValue=" + searchValue;
            nextUrl = nextUrl + "?action=search&searchValue=" + searchValue;
            firstUrl = firstUrl + "?action=search&searchValue=" + searchValue;
            lastUrl = lastUrl + "?action=search&searchValue=" + searchValue;
            model.addAttribute("action", action);
            model.addAttribute("searchValue", searchValue);
        }

        model.addAttribute("previousUrl", previousUrl);
        model.addAttribute("nextUrl", nextUrl);
        model.addAttribute("firstUrl", firstUrl);
        model.addAttribute("lastUrl", lastUrl);

        // Base url for Add and Edit
        model.addAttribute("detailsUrl", baseUserUrl + "/details");
        model.addAttribute("formPostUrl", baseUserUrl + "/add");
        model.addAttribute("baseUserUrl", baseUserUrl);

        return "app/admin/users/users-list";
    }
}