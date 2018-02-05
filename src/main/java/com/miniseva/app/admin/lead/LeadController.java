package com.miniseva.app.admin.lead;

import com.miniseva.security.account.Account;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class LeadController {
    private static final Logger log = LoggerFactory.getLogger(LeadController.class);

    private LeadRepository repoLead;

    public LeadController(LeadRepository repoLead) {
        this.repoLead = repoLead;
    }

    @GetMapping(value = {"/app/admin/lead/{pageNumber}", "/app/admin/lead" })
    public String getLeadPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Lead");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of lead. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "name");
        Page<Lead> leads = repoLead.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            leads = repoLead.findByNameContainingIgnoreCase(searchValue, pageRequest);
        }
        
        model.addAttribute("leads",leads);

        // Counts for lead
        long numLead = repoLead.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numLead = repoLead.countByNameContainingIgnoreCase(searchValue);
        }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numLead/PAGE_SIZE;
        if (numLead % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", leads.hasPrevious());
        model.addAttribute("hasNext", leads.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numLead", numLead);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseLeadUrl = "/app/admin/lead";

        String previousUrl = baseLeadUrl + "/" + previousPage;
        String nextUrl = baseLeadUrl + "/" + nextPage;
        String firstUrl = baseLeadUrl + "/" + firstPage;
        String lastUrl = baseLeadUrl + "/" + lastPage;

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
        model.addAttribute("detailsUrl", baseLeadUrl + "/details");
        model.addAttribute("addLeadUrl", baseLeadUrl + "/add");
        model.addAttribute("baseUrl", baseLeadUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added lead.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated lead.");
            }
        }

        return "app/admin/lead/lead-list";
    }
}