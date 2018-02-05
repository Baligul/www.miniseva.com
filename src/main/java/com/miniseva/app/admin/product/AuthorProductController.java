package com.miniseva.app.admin.product;

import com.miniseva.security.account.Account;
import com.miniseva.app.product.Product;
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

import com.github.slugify.Slugify;

/**
 * Author (add, edit, delete) product.
 */
@Controller
public class AuthorProductController {
    private static final Logger log = LoggerFactory.getLogger(AuthorProductController.class);
    private ProductRepository repoProduct;
    private AccountService srvAccount;

    public AuthorProductController(ProductRepository repoProduct,
                                   AccountService srvAccount) {
        this.repoProduct = repoProduct;
        this.srvAccount = srvAccount;
    }

    /**
     * Edit an existing product.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/product/details/{productId}" }, method = GET)
    public String authorProductForm(HttpServletRequest request,
                                    Model model,
                                    @PathVariable Long productId) {

        Product product = repoProduct.findById(productId);
        model.addAttribute("product", product);
        model.addAttribute("productUrl", "/app/admin/product");

        return "app/admin/product/product-details";
    }

    @RequestMapping(value = { "/app/admin/product/details/{productId}" }, method = POST)
    public String productSubmit(HttpServletRequest request, 
                              @Valid Product product, BindingResult bindingResult,
                              Model model, @PathVariable Long productId, 
                              @RequestParam(value="action", required=false) String action,
                              Authentication authentication) {

        Product savedProduct = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("name");                
                if (error != null) {
                    model.addAttribute("nameError", error.getDefaultMessage());                    
                }
                log.info("Add Product validation errors.");
                return "app/admin/product/product-details";
            }

            if (action.equals("save")) {
                savedProduct = repoProduct.findById(productId);
                Slugify slg = new Slugify();
                savedProduct.setSlug(slg.slugify(product.getName()));
                savedProduct.setUpdatedBy(userId);
                savedProduct.setUpdatedOn(DateTime.now());
                savedProduct.setName(product.getName());
                savedProduct.setDescription(product.getDescription());
                savedProduct.setImgPath(product.getImgPath());
                try {
                        savedProduct = repoProduct.save(savedProduct);
                        log.info("Saved product to DB: " + savedProduct.toString());
                        return "redirect:/app/admin/product?msg=ed";
                    } catch (Exception e) {
                        log.info("Cannot update product to DB: " + savedProduct.toString());
                        model.addAttribute("successMsg", "Database Error.");
                }
            }
            model.addAttribute("product", savedProduct);
        }

        model.addAttribute("productUrl", "/app/admin/product");
        
        if (action == null) {
            model.addAttribute("product", product);
        }

        return "app/admin/product/product-details";
    }
}