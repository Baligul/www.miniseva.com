package com.miniseva.app.admin.product;

import com.miniseva.security.account.Account;
import com.miniseva.app.product.Product;
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

import com.github.slugify.Slugify;

@Controller
public class AddProductController {
    private static final Logger log = LoggerFactory.getLogger(AddProductController.class);

    private ProductRepository repoProduct;
    private AccountService srvAccount;

    public AddProductController(ProductRepository repoProduct,
                                AccountService srvAccount) {
        this.repoProduct = repoProduct;
        this.srvAccount = srvAccount;
    }

    @GetMapping(value = {"/app/admin/product/add" })
    public String getAddProductPage(HttpServletRequest request, Model model) {        
        return "app/admin/product/add-product";
    }

    @RequestMapping(value = { "/app/admin/product/add" }, method = POST)
    public String postAddProductPage(HttpServletRequest request, 
            @Valid Product product, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Product saveProduct = null;
        model.addAttribute("product", product);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("name");                
                if (error != null) {
                    model.addAttribute("nameError", error.getDefaultMessage());                    
                }
                log.info("Add Product validation errors.");
                return "app/admin/product/add-product";
            }
            
            if (action.equals("save")) {
                Slugify slg = new Slugify();
                product.setSlug(slg.slugify(product.getName()));
                product.setCreatedBy(userId);
                product.setCreatedOn(DateTime.now());
                try {
                    saveProduct = repoProduct.save(product);
                    log.info("Saved product to DB: " + saveProduct.toString());
                    return "redirect:/app/admin/product?msg=sv";
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the product.");                    
                }
            }
        }

        return "app/admin/product/add-product";
    }
}