package com.miniseva.app.admin.block;

import com.miniseva.security.account.Account;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class BlockController {
    private static final Logger log = LoggerFactory.getLogger(BlockController.class);

    private BlockRepository repoBlock;
    private LeadRepository repoLead;

    public BlockController(BlockRepository repoBlock,
                           LeadRepository repoLead) {
        this.repoBlock = repoBlock;
        this.repoLead = repoLead;
    }

    @GetMapping(value = {"/app/admin/block/{pageNumber}", "/app/admin/block" })
    public String getBlockPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Block");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of block. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
        Page<Block> blocks = repoBlock.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            blocks = repoBlock.findByNameContainingIgnoreCase(searchValue, pageRequest);
        }

        // Set lead name for each block
        for (Block block : blocks) {
            block.setLeadName(repoLead.findById(block.getLeadId()).getName());
        }

        model.addAttribute("blocks",blocks);

        // Counts for block
        long numBlock = repoBlock.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numBlock = repoBlock.countByNameContainingIgnoreCase(searchValue);
        }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numBlock/PAGE_SIZE;
        if (numBlock % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", blocks.hasPrevious());
        model.addAttribute("hasNext", blocks.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numBlock", numBlock);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseBlockUrl = "/app/admin/block";

        String previousUrl = baseBlockUrl + "/" + previousPage;
        String nextUrl = baseBlockUrl + "/" + nextPage;
        String firstUrl = baseBlockUrl + "/" + firstPage;
        String lastUrl = baseBlockUrl + "/" + lastPage;

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            previousUrl = previousUrl + "?action=search&searchValue=" + searchValue;
            nextUrl = nextUrl + "?action=search&searchValue=" + searchValue;
            firstUrl = firstUrl + "?action=search&searchValue=" + searchValue;
            lastUrl = lastUrl + "?action=search&searchValue=" + searchValue;
            model.addAttribute("action", action);
            model.addAttribute("searchValue", searchValue);
        }

        // First, last, next and previous pages urls
        model.addAttribute("previousUrl", previousUrl);
        model.addAttribute("nextUrl", nextUrl);
        model.addAttribute("firstUrl", firstUrl);
        model.addAttribute("lastUrl", lastUrl);

        // Base url for Add and Edit
        model.addAttribute("detailsUrl", baseBlockUrl + "/details");
        model.addAttribute("addBlockUrl", baseBlockUrl + "/add");
        model.addAttribute("baseUrl", baseBlockUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added block.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated block.");
            }
        }

        return "app/admin/block/block-list";
    }
}