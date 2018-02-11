package com.miniseva.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

import com.miniseva.app.slot.Slot;
import com.miniseva.app.slot.SlotRepository;

import com.miniseva.app.item.Item;
import com.miniseva.app.item.ItemRepository;

@Controller
public class ItemsController {
    private static final Logger log = LoggerFactory.getLogger(ItemsController.class);

    private ItemRepository repoItem;
    private ProductRepository repoProduct;
    private SlotRepository repoSlot;

    public ItemsController(ItemRepository repoItem,
                           ProductRepository repoProduct,
                           SlotRepository repoSlot) {
        this.repoItem = repoItem;
        this.repoProduct = repoProduct;
        this.repoSlot = repoSlot;
    }


    @GetMapping("/items/{productSlug}/{productId}")
    public String page(Model model,
                       @PathVariable String productSlug,
                       @PathVariable Long productId) {
        model.addAttribute("page", "items");

        model.addAttribute("product", repoProduct.findById(productId));
        model.addAttribute("items",repoItem.findByProductId(productId));
        model.addAttribute("products",repoProduct.findAll());
        model.addAttribute("allSlots", repoSlot.findAll());
        model.addAttribute("todaysSlots", repoSlot.getTodaysSlots());
        
        return "website/item";
    }

}
