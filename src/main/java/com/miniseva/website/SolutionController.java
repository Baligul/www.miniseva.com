package com.miniseva.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SolutionController {

    @GetMapping("/solution")
    public String page(Model model) {
        model.addAttribute("page", "solution");
        return "website/solution";
    }

}
