package com.miniseva.security.social.twitter;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TwitterProfileController {

//    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    public TwitterProfileController(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @GetMapping("/twitter")
    public String home(Principal currentUser, Model model) {

        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        if (connection == null) {
            return "redirect:/connect/twitter";
        }
        model.addAttribute("profile", connection.getApi().userOperations().getUserProfile());
        return "pages/social/twitter/profile";
    }

}
