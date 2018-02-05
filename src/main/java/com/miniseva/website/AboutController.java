package com.miniseva.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

@Controller
public class AboutController {

    private ProductRepository repoProduct;

    public AboutController(ProductRepository repoProduct) {
        this.repoProduct = repoProduct;
    }

    @GetMapping("/about")
    public String page(Model model) {
        model.addAttribute("page", "about");
        
        model.addAttribute("products",repoProduct.findAll());
        
        return "website/about";
    }

}
