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

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private ProductRepository repoProduct;

    public ProductController(ProductRepository repoProduct) {
        this.repoProduct = repoProduct;
    }

    @GetMapping(value = {"/app/admin/product/{pageNumber}", "/app/admin/product" })
    public String getProductPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Product");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of product. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
        Page<Product> products = repoProduct.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            products = repoProduct.findByNameContainingIgnoreCase(searchValue, pageRequest);
        }

        model.addAttribute("products",products);

        // Counts for product
        long numProduct = repoProduct.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numProduct = repoProduct.countByNameContainingIgnoreCase(searchValue);
        }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numProduct/PAGE_SIZE;
        if (numProduct % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", products.hasPrevious());
        model.addAttribute("hasNext", products.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numProduct", numProduct);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseProductUrl = "/app/admin/product";

        String previousUrl = baseProductUrl + "/" + previousPage;
        String nextUrl = baseProductUrl + "/" + nextPage;
        String firstUrl = baseProductUrl + "/" + firstPage;
        String lastUrl = baseProductUrl + "/" + lastPage;

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
        model.addAttribute("detailsUrl", baseProductUrl + "/details");
        model.addAttribute("addProductUrl", baseProductUrl + "/add");
        model.addAttribute("baseUrl", baseProductUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added product.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated product.");
            }
        }

        return "app/admin/product/product-list";
    }
}