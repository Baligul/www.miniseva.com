package com.miniseva.app.admin.item;

import com.miniseva.security.account.Account;
import com.miniseva.app.item.Item;
import com.miniseva.app.item.ItemRepository;
import com.miniseva.app.product.ProductRepository;

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
 * Author (add, edit, delete) item.
 */
@Controller
public class AuthorItemController {
    private static final Logger log = LoggerFactory.getLogger(AuthorItemController.class);

    private ItemRepository repoItem;
    private AccountService srvAccount;
    private ProductRepository repoProduct;

    public AuthorItemController(ItemRepository repoItem,
                                AccountService srvAccount,
                                ProductRepository repoProduct) {
        this.repoItem = repoItem;
        this.srvAccount = srvAccount;
        this.repoProduct = repoProduct;
    }

    /**
     * Edit an existing item.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/item/details/{itemId}" }, method = GET)
    public String authorItemForm(HttpServletRequest request,
                                    Model model,
                                    @PathVariable Long itemId) {

        Item item = repoItem.findById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemUrl", "/app/admin/item");
        model.addAttribute("product",repoProduct.findAll());

        return "app/admin/item/item-details";
    }

    @RequestMapping(value = { "/app/admin/item/details/{itemId}" }, method = POST)
    public String itemSubmit(HttpServletRequest request, 
                              @Valid Item item, BindingResult bindingResult,
                              Model model, @PathVariable Long itemId, 
                              @RequestParam(value="action", required=false) String action,
                              Authentication authentication) {

        Item savedItem = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

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
                return "app/admin/item/item-details";
            }

            if (action.equals("save")) {
                savedItem = repoItem.findById(itemId);
                savedItem.setUpdatedBy(userId);
                savedItem.setUpdatedOn(DateTime.now());
                savedItem.setName(item.getName());
                savedItem.setMrp(item.getMrp());
                savedItem.setPrice(item.getPrice());
                savedItem.setDescription(item.getDescription());
                savedItem.setImgPath(item.getImgPath());
                savedItem.setProductId(item.getProductId());
                try {
                        savedItem = repoItem.save(savedItem);
                        log.info("Saved item to DB: " + savedItem.toString());
                        return "redirect:/app/admin/item?msg=ed";
                    } catch (Exception e) {
                        log.info("Cannot update item to DB: " + savedItem.toString());
                        model.addAttribute("successMsg", "Database Error.");
                }
            }
            model.addAttribute("item", savedItem);
        }

        model.addAttribute("itemUrl", "/app/admin/item");
        
        if (action == null) {
            model.addAttribute("item", item);
        }

        return "app/admin/item/item-details";
    }
}