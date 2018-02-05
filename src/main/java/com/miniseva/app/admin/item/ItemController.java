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

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private ItemRepository repoItem;
    private ProductRepository repoProduct;

    public ItemController(ItemRepository repoItem,
                          ProductRepository repoProduct) {
        this.repoItem = repoItem;
        this.repoProduct = repoProduct;
    }

    @GetMapping(value = {"/app/admin/item/{pageNumber}", "/app/admin/item" })
    public String getItemPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Item");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of item. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
        Page<Item> items = repoItem.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            items = repoItem.findByNameContainingIgnoreCase(searchValue, pageRequest);
        }

        model.addAttribute("items",items);

        // Set the product name for each item
        for(Item item : items) {
            item.setProductName(repoProduct.findById(item.getProductId()).getName());
        }

        // Counts for item
        long numItem = repoItem.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numItem = repoItem.countByNameContainingIgnoreCase(searchValue);
        }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numItem/PAGE_SIZE;
        if (numItem % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", items.hasPrevious());
        model.addAttribute("hasNext", items.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numItem", numItem);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseItemUrl = "/app/admin/item";

        String previousUrl = baseItemUrl + "/" + previousPage;
        String nextUrl = baseItemUrl + "/" + nextPage;
        String firstUrl = baseItemUrl + "/" + firstPage;
        String lastUrl = baseItemUrl + "/" + lastPage;

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
        model.addAttribute("detailsUrl", baseItemUrl + "/details");
        model.addAttribute("addItemUrl", baseItemUrl + "/add");
        model.addAttribute("baseUrl", baseItemUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added item.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated item.");
            }
        }

        return "app/admin/item/item-list";
    }
}