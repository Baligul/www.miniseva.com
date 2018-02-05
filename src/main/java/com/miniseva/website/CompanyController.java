package com.miniseva.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {

    @GetMapping("/company")
    public String page(Model model) {
        model.addAttribute("page", "company");
        return "website/company";
    }

}
