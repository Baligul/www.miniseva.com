package com.miniseva.app.admin.slot;

import com.miniseva.security.account.Account;
import com.miniseva.app.slot.Slot;
import com.miniseva.app.slot.SlotRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;
import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

@Controller
public class AddSlotController {
    private static final Logger log = LoggerFactory.getLogger(AddSlotController.class);

    private SlotRepository repoSlot;
    private AccountService srvAccount;
    public AddSlotController(SlotRepository repoSlot,
                             AccountService srvAccount) {
        this.repoSlot = repoSlot;
        this.srvAccount = srvAccount;
    }

    @GetMapping(value = {"/app/admin/slot/add" })
    public String getAddSlotPage(HttpServletRequest request) {
        return "app/admin/slot/add-slot";
    }

    @RequestMapping(value = { "/app/admin/slot/add" }, method = POST)
    public String postAddSlotPage(HttpServletRequest request, 
            @Valid Slot slot, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Slot saveSlot = null;
        model.addAttribute("slot", slot);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("slotVal");                
                if (error != null) {
                    model.addAttribute("slotError", error.getDefaultMessage());
                    log.info("Add Slot validation errors.");
                    return "app/admin/slot/add-slot";
                }
            }
            
            if (action.equals("save")) {
                slot.setCreatedBy(userId);
                slot.setCreatedOn(DateTime.now());
                try {
                    saveSlot = repoSlot.save(slot);
                    log.info("Saved slot to DB: " + saveSlot.toString());
                    return "redirect:/app/admin/slot?msg=sv";
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the slot.");                    
                }
            }
        }

        return "app/admin/slot/add-slot";
    }
}