package com.miniseva.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);

    private ProductRepository repoProduct;

    public HomeController(ProductRepository repoProduct) {
        this.repoProduct = repoProduct;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page", "home");

        model.addAttribute("products",repoProduct.findAll());
        
        return "website/home";
    }

}
