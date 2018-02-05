package com.miniseva.errors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HTTPErrorPages {

    @GetMapping("/403")
    public String fourOhThree(HttpServletRequest request, Model model, Principal user) {
        return "error/403";
    }

}
