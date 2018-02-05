package com.miniseva.app.admin.item;

import com.miniseva.security.account.Account;
import com.miniseva.app.item.Item;
import com.miniseva.app.item.ItemRepository;
import com.miniseva.app.product.ProductRepository;

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
public class AddItemController {
    private static final Logger log = LoggerFactory.getLogger(AddItemController.class);

    private ItemRepository repoItem;
    private AccountService srvAccount;
    private ProductRepository repoProduct;

    public AddItemController(ItemRepository repoItem,
                             AccountService srvAccount,
                             ProductRepository repoProduct) {
        this.repoItem = repoItem;
        this.srvAccount = srvAccount;
        this.repoProduct = repoProduct;
    }

    @GetMapping(value = {"/app/admin/item/add" })
    public String getAddItemPage(HttpServletRequest request, Model model) {
        model.addAttribute("product",repoProduct.findAll());     
        return "app/admin/item/add-item";
    }

    @RequestMapping(value = { "/app/admin/item/add" }, method = POST)
    public String postAddItemPage(HttpServletRequest request, 
            @Valid Item item, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Item saveItem = null;
        model.addAttribute("item", item);
        model.addAttribute("product",repoProduct.findAll());

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("name");                
                if (error != null) {
                    model.addAttribute("nameError", error.getDefaultMessage());                    
                }
                error = bindingResult.getFieldError("price");                
                if (error != null) {
                    model.addAttribute("priceError", error.getDefaultMessage());                    
                }
                error = bindingResult.getFieldError("productId");                
                if (error != null) {
                    model.addAttribute("productIdError", error.getDefaultMessage());                    
                }
                log.info("Add Item validation errors.");
                return "app/admin/item/add-item";
            }
            
            if (action.equals("save")) {
                item.setCreatedBy(userId);
                item.setCreatedOn(DateTime.now());
                try {
                    saveItem = repoItem.save(item);
                    log.info("Saved item to DB: " + saveItem.toString());
                    return "redirect:/app/admin/item?msg=sv";
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the item.");                    
                }
            }
        }

        return "app/admin/item/add-item";
    }
}