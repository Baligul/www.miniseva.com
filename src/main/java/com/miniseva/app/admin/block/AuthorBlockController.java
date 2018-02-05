package com.miniseva.app.admin.block;

import com.miniseva.security.account.Account;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;

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
 * Author (add, edit, delete) block.
 */
@Controller
public class AuthorBlockController {
    private static final Logger log = LoggerFactory.getLogger(AuthorBlockController.class);
    private BlockRepository repoBlock;
    private LeadRepository repoLead;
    private AccountService srvAccount;

    public AuthorBlockController(BlockRepository repoBlock,
                                 LeadRepository repoLead,
                                 AccountService srvAccount) {
        this.repoBlock = repoBlock;
        this.repoLead = repoLead;
        this.srvAccount = srvAccount;
    }

    /**
     * Edit an existing block.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/block/details/{blockId}" }, method = GET)
    public String authorBlockForm(HttpServletRequest request, 
                                  Model model, 
                                  @PathVariable Long blockId) {

        model.addAttribute("leads", repoLead.findAll());

        Block block = repoBlock.findById(blockId);
        model.addAttribute("block", block);
        model.addAttribute("blockUrl", "/app/admin/block");

        return "app/admin/block/block-details";
    }

    @RequestMapping(value = { "/app/admin/block/details/{blockId}" }, method = POST)
    public String blockSubmit(HttpServletRequest request, 
                              @Valid Block block, BindingResult bindingResult,
                              Model model, @PathVariable Long blockId, 
                              @RequestParam(value="action", required=false) String action,
                              Authentication authentication) {

        Block savedBlock = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        model.addAttribute("leads", repoLead.findAll());

        if (action != null) {
            if (action.equals("save")) {
                savedBlock = repoBlock.findById(blockId);
                savedBlock.setUpdatedBy(userId);
                savedBlock.setUpdatedOn(DateTime.now());
                savedBlock.setName(block.getName());
                savedBlock.setLeadId(block.getLeadId());
                try {
                        savedBlock = repoBlock.save(savedBlock);
                        log.info("Saved block to DB: " + savedBlock.toString());
                        return "redirect:/app/admin/block?msg=ed";
                    } catch (Exception e) {
                        log.info("Cannot update block to DB: " + savedBlock.toString());
                        model.addAttribute("successMsg", "Database Error.");
                }
            }
            model.addAttribute("block", savedBlock);
        }

        model.addAttribute("blockUrl", "/app/admin/block");
        
        if (action == null) {
            model.addAttribute("block", block);
        }

        return "app/admin/block/block-details";
    }
}