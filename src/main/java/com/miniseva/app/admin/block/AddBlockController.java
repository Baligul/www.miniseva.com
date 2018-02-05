package com.miniseva.app.admin.block;

import com.miniseva.security.account.Account;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;

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
public class AddBlockController {
    private static final Logger log = LoggerFactory.getLogger(AddBlockController.class);

    private BlockRepository repoBlock;
    private LeadRepository repoLead;
    private AccountService srvAccount;

    public AddBlockController(BlockRepository repoBlock,
                              LeadRepository repoLead,
                              AccountService srvAccount) {
        this.repoBlock = repoBlock;
        this.repoLead = repoLead;
        this.srvAccount = srvAccount;
    }

    @GetMapping(value = {"/app/admin/block/add" })
    public String getAddBlockPage(HttpServletRequest request, Model model) {

        List<Lead> leads = repoLead.findAll();        
        model.addAttribute("leads", leads);

        return "app/admin/block/add-block";
    }

    @RequestMapping(value = { "/app/admin/block/add" }, method = POST)
    public String postAddBlockPage(HttpServletRequest request, 
            @Valid Block block, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Block saveBlock = null;
        log.info("Block Found: " + block.toString());

        List<Lead> leads = repoLead.findAll();        
        model.addAttribute("leads", leads);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("name");                
                if (error != null) {
                    model.addAttribute("nameError", error.getDefaultMessage());                    
                }
                error = bindingResult.getFieldError("leadId");                
                if (error != null) {
                    model.addAttribute("leadIdError", error.getDefaultMessage());
                }
                log.info("Add Block validation errors.");
                return "app/admin/block/add-block";
            }
            
            if (action.equals("save")) {
                block.setCreatedBy(userId);
                block.setCreatedOn(DateTime.now());
                try {
                    saveBlock = repoBlock.save(block);
                    log.info("Saved block to DB: " + saveBlock.toString());
                    return "redirect:/app/admin/block?msg=sv";
                } catch (Exception e) {
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the block.");                    
                }
            }
        }

        return "app/admin/block/add-block";
    }
}