package com.miniseva.website.home;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeRestController {

    @RequestMapping("/test")
    public String test() {
        return "The test works";
    }

}
