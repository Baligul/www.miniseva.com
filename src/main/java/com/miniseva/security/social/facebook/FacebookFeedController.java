//package com.miniseva.security.social.facebook;
//
//import javax.inject.Inject;
//
//import org.springframework.social.facebook.api.Facebook;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class FacebookFeedController {
//
//    private final Facebook facebook;
//
//    @Inject
//    public FacebookFeedController(Facebook facebook) {
//        this.facebook = facebook;
//    }
//
//    @GetMapping("/facebook/feed")
//    public String showFeed(Model model) {
//        model.addAttribute("feed", facebook.feedOperations().getFeed());
//        return "facebook/feed";
//    }
//
//    @PostMapping("/facebook/feed")
//    public String postUpdate(String message) {
//        facebook.feedOperations().updateStatus(message);
//        return "redirect:/facebook/feed";
//    }
//
//}
