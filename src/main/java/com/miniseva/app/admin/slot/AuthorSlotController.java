package com.miniseva.app.admin.slot;

import com.miniseva.security.account.Account;
import com.miniseva.app.slot.Slot;
import com.miniseva.app.slot.SlotRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

/**
 * Author (add, edit, delete) slot.
 */
@Controller
public class AuthorSlotController {
    private static final Logger log = LoggerFactory.getLogger(AuthorSlotController.class);

    private SlotRepository repoSlot;
    private AccountService srvAccount;

    public AuthorSlotController(SlotRepository repoSlot,
                                AccountService srvAccount) {
        this.repoSlot = repoSlot;
        this.srvAccount = srvAccount;
    }

    /**
     * Edit an existing slot.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/slot/details/{slotId}" }, method = GET)
    public String authorSlotForm(HttpServletRequest request,
                                    Model model,
                                    @PathVariable Long slotId) {

        Slot slot = repoSlot.findById(slotId);
        model.addAttribute("slot", slot);
        model.addAttribute("slotUrl", "/app/admin/slot");

        return "app/admin/slot/slot-details";
    }

    @RequestMapping(value = { "/app/admin/slot/details/{slotId}" }, method = POST)
    public String slotSubmit(HttpServletRequest request, 
                              @Valid Slot slot, BindingResult bindingResult,
                              Model model, @PathVariable Long slotId, 
                              @RequestParam(value="action", required=false) String action,
                              Authentication authentication) {

        Slot savedSlot = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("slotVal");                
                if (error != null) {
                    model.addAttribute("slotError", error.getDefaultMessage());
                    log.info("Add Slot validation errors.");
                    return "app/admin/slot/slot-details";
                }
            }

            if (action.equals("save")) {
                savedSlot = repoSlot.findById(slotId);
                savedSlot.setUpdatedBy(userId);
                savedSlot.setUpdatedOn(DateTime.now());
                savedSlot.setSlotVal(slot.getSlotVal());
                savedSlot.setEndTime(slot.getEndTime());
                try {
                        savedSlot = repoSlot.save(savedSlot);
                        log.info("Saved slot to DB: " + savedSlot.toString());
                        return "redirect:/app/admin/slot?msg=ed";
                    } catch (Exception e) {
                        log.info("Cannot update slot to DB: " + savedSlot.toString());
                        model.addAttribute("successMsg", "Database Error.");
                }
            }
            model.addAttribute("slot", savedSlot);
        }

        model.addAttribute("slotUrl", "/app/admin/slot");
        
        if (action == null) {
            model.addAttribute("slot", slot);
        }

        return "app/admin/slot/slot-details";
    }
}