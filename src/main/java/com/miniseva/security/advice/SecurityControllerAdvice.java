package com.miniseva.security.advice;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.miniseva.app.slot.SlotRepository;
import com.miniseva.app.item.ItemRepository;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ControllerAdvice
@Controller
public class SecurityControllerAdvice {
    private ItemRepository repoItem;
    private SlotRepository repoSlot;

    public SecurityControllerAdvice(ItemRepository repoItem,
                                    SlotRepository repoSlot) {
        this.repoItem = repoItem;
        this.repoSlot = repoSlot;
    }

    @ModelAttribute
    public void enrichModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Exit immediately if we don't have access to the auth object as this indicates that the method was called
        // after the template has been rendered.
        if (auth == null)
            return;

        // Defaults
        boolean loggedIn = false;
        boolean enabled = false;
        Collection<GrantedAuthority> authorities;
        //boolean isUserRole = false;
        //boolean isAdminRole = false;
        boolean isRootRole = false;
        boolean isCustomerRole = false;
        boolean isDeliveryBoyRole = false;
        String username = "";

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            if (auth instanceof UsernamePasswordAuthenticationToken || auth instanceof RememberMeAuthenticationToken) {
                if (auth.getPrincipal() instanceof UserDetails) {
                    loggedIn = true;
                    User user = (User) auth.getPrincipal();
                    if (null != user) {
                        username = user.getUsername();
                        enabled = user.isEnabled();
                        authorities = user.getAuthorities();
                        for (GrantedAuthority authority : authorities) {
                            String role = authority.getAuthority();
                            // ROLE_USER and ROLE_ADMIN are Delixus.com roles
                            //if (role != null && role.equals("ROLE_USER"))
                            //    isUserRole = true;
                            //if (role != null && role.equals("ROLE_ADMIN"))
                            //    isAdminRole = true;
                            if (role != null && role.equals("ROLE_ROOT"))
                                isRootRole = true;
                            if (role != null && role.equals("ROLE_CUSTOMER"))
                                isCustomerRole = true;
                            if (role != null && role.equals("ROLE_DELIVERY_BOY"))
                                isDeliveryBoyRole = true;
                        }
                    }
                } else if (auth.getPrincipal() instanceof String) {
                    loggedIn = true;
                    username = (String) auth.getPrincipal();
                    enabled = true;
                }
            }

            // Social Authentication
            // ---------------------

            // There's no need to check for `auth instanceof AnonymousAuthenticationToken` because the default values
            // satisfy this auth type.
        }

        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("enabled", enabled);
        //model.addAttribute("isUserRole", isUserRole);
        //model.addAttribute("isAdminRole", isAdminRole);
        model.addAttribute("isRootRole", isRootRole);
        model.addAttribute("isCustomerRole", isCustomerRole);
        model.addAttribute("isDeliveryBoyRole", isDeliveryBoyRole);
        // The name displayUsername is required to avoid conflicting with controller specific model attributes
        // (i.e. username is already used in the LogInController and the SignUpController)
        model.addAttribute("displayUsername", username);
        model.addAttribute("allSlots", repoSlot.findAll());
        model.addAttribute("allItems",repoItem.findAll());
        
        if(repoSlot.getTodaysSlots().size() > 0) {
            model.addAttribute("todaysSlots", repoSlot.getTodaysSlots());
        }
    }
}
