package com.miniseva.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

// import com.miniseva.app.product.Product;
// import com.miniseva.app.product.ProductRepository;

// import com.miniseva.app.slot.Slot;
// import com.miniseva.app.slot.SlotRepository;

// import com.miniseva.app.checkout.Checkout;
// import com.miniseva.app.checkout.CheckoutRepository;

@Controller
public class CheckoutController {
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    // private CheckoutRepository repoCheckout;
    // private ProductRepository repoProduct;
    // private SlotRepository repoSlot;

    // public CheckoutController(CheckoutRepository repoCheckout,
    //                        ProductRepository repoProduct,
    //                        SlotRepository repoSlot) {
    //     this.repoCheckout = repoCheckout;
    //     this.repoProduct = repoProduct;
    //     this.repoSlot = repoSlot;
    // }

    public CheckoutController() {

    }
    
    @PostMapping("/checkout")
    public String page(Model model) {
        model.addAttribute("page", "checkout");

        return "website/checkout";
    }

    @GetMapping("/checkout")
    public String getCheckout(Model model) {
        model.addAttribute("page", "checkout");

        return "website/checkout";
    }

}