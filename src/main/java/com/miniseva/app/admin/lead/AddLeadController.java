package com.miniseva.app.admin.lead;

import com.miniseva.security.account.Account;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.state.State;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;

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
public class AddLeadController {
    private static final Logger log = LoggerFactory.getLogger(AddLeadController.class);

    private LeadRepository repoLead;
    private AccountService srvAccount;
    private BlockRepository repoBlock;

    public AddLeadController(LeadRepository repoLead,
                             AccountService srvAccount,
                             BlockRepository repoBlock) {
        this.repoLead = repoLead;
        this.srvAccount = srvAccount;
        this.repoBlock = repoBlock;
    }

    @GetMapping(value = {"/app/admin/lead/add" })
    public String getAddLeadPage(HttpServletRequest request, Model model) {
        
        Map<String, String> states = new TreeMap<String, String>();

        for(State state : State.values()) {
            if (!state.name().equals("NULL")) {
                states.put(state.name(),state.toString());
            }
        }
        
        model.addAttribute("states", states);

        return "app/admin/lead/add-lead";
    }

    @RequestMapping(value = { "/app/admin/lead/add" }, method = POST)
    public String postAddLeadPage(HttpServletRequest request, 
            @Valid Lead lead, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="blocks", required=false) ArrayList<String> blocks,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Lead saveLead = null;
        log.info("Lead Found: " + lead.toString());

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
                lead.setCreatedBy(userId);
                lead.setCreatedOn(DateTime.now());
                try {
                    log.info("Lead Values: " + lead.toString());
                    saveLead = repoLead.save(lead);
                    if(blocks != null && blocks.size() > 0) {                        
                        for(String block: blocks) {
                            Block blk = new Block(null, block, saveLead.getId(), userId, DateTime.now(), null, null);
                            repoBlock.save(blk);
                        }
                    }
                    log.info("Saved lead to DB: " + saveLead.toString());
                    return "redirect:/app/admin/lead?msg=sv";
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the lead.");                    
                }
            }
        }

        return "app/admin/lead/add-lead";
    }
}