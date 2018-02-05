package com.miniseva.app.admin.lead;

import com.miniseva.security.account.Account;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.state.State;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;

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
 * Author (add, edit, delete) lead.
 */
@Controller
public class AuthorLeadController {
    private static final Logger log = LoggerFactory.getLogger(AuthorLeadController.class);
    private LeadRepository repoLead;
    private AccountService srvAccount;
    private BlockRepository repoBlock;

    public AuthorLeadController(LeadRepository repoLead,
                                AccountService srvAccount,
                                BlockRepository repoBlock) {
        this.repoLead = repoLead;
        this.srvAccount = srvAccount;
        this.repoBlock = repoBlock;
    }

    /**
     * Edit an existing lead.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/lead/details/{leadId}" }, method = GET)
    public String authorLeadForm(HttpServletRequest request, 
                                  Model model, 
                                  @PathVariable Long leadId) {
                                      
        Map<String, String> states = new TreeMap<String, String>();

        for(State state : State.values()) {
            if (!state.name().equals("NULL")) {
                states.put(state.name(),state.toString());
            }
        }
        
        model.addAttribute("states", states);

        Lead lead = repoLead.findById(leadId);
        model.addAttribute("lead", lead);
        model.addAttribute("block", repoBlock.findByLeadId(leadId));
        model.addAttribute("leadUrl", "/app/admin/lead");

        return "app/admin/lead/lead-details";
    }

    @RequestMapping(value = { "/app/admin/lead/details/{leadId}" }, method = POST)
    public String leadSubmit(HttpServletRequest request, 
                              @Valid Lead lead, BindingResult bindingResult,
                              Model model, @PathVariable Long leadId, 
                              @RequestParam(value="action", required=false) String action,
                              @RequestParam(value="blocks", required=false) ArrayList<String> blocks,
                              Authentication authentication) {

        Lead savedLead = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Map<String, String> states = new TreeMap<String, String>();

        for(State state : State.values()) {
            if (!state.name().equals("NULL")) {
                states.put(state.name(),state.toString());
            }
        }
        
        model.addAttribute("states", states);
        model.addAttribute("block", blocks);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("name");
                if (error != null) {
                    model.addAttribute("nameError", error.getDefaultMessage());
                    log.info("Add Lead validation errors.");
                    return "app/admin/lead/add-lead";
                }

                error = bindingResult.getFieldError("address1");
                if (error != null) {
                    model.addAttribute("address1Error", error.getDefaultMessage());
                    log.info("Add Lead validation errors.");
                    return "app/admin/lead/add-lead";
                }
            }
            if (action.equals("save")) {
                savedLead = repoLead.findById(leadId);
                savedLead.setUpdatedBy(userId);
                savedLead.setUpdatedOn(DateTime.now());
                savedLead.setName(lead.getName());
                savedLead.setAddress1(lead.getAddress1());
                savedLead.setCity(lead.getCity());
                savedLead.setState(lead.getState());
                savedLead.setPostalCode(lead.getPostalCode());
                try {
                        savedLead = repoLead.save(savedLead);
                        if(blocks != null && blocks.size() > 0) {                    
                            for(String block: blocks) {
                                Block blk = new Block(null, block, savedLead.getId(), userId, DateTime.now(), null, null);
                                repoBlock.save(blk);
                            }
                        }
                        log.info("Saved lead to DB: " + savedLead.toString());
                        return "redirect:/app/admin/lead?msg=ed";
                    } catch (Exception e) {
                        log.info("Cannot update lead to DB: " + savedLead.toString());
                        model.addAttribute("successMsg", "Database Error." + e.toString());
                }
            }
            model.addAttribute("lead", savedLead);
        }

        model.addAttribute("leadUrl", "/app/admin/lead");
        
        if (action == null) {
            model.addAttribute("lead", lead);
        }

        return "app/admin/lead/lead-details";
    }
}