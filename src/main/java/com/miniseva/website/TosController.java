package com.miniseva.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TosController {

    @GetMapping("/tos")
    public String page(Model model) {
        model.addAttribute("page", "tos");
        return "website/tos";
    }

}
