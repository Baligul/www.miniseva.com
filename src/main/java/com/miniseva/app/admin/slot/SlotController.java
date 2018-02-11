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

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class SlotController {
    private static final Logger log = LoggerFactory.getLogger(SlotController.class);

    private SlotRepository repoSlot;

    public SlotController(SlotRepository repoSlot) {
        this.repoSlot = repoSlot;
    }

    @GetMapping(value = {"/app/admin/slot/{pageNumber}", "/app/admin/slot" })
    public String getSlotPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("slotName", "Slot");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of slot. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, ASC, "slotVal");
        Page<Slot> slots = repoSlot.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            slots = repoSlot.findBySlotValContainingIgnoreCase(searchValue, pageRequest);
        }

        model.addAttribute("slots",slots);

        // Counts for slot
        long numSlot = repoSlot.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numSlot = repoSlot.countBySlotValContainingIgnoreCase(searchValue);
        }

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numSlot/PAGE_SIZE;
        if (numSlot % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", slots.hasPrevious());
        model.addAttribute("hasNext", slots.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("numSlot", numSlot);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseSlotUrl = "/app/admin/slot";

        String previousUrl = baseSlotUrl + "/" + previousPage;
        String nextUrl = baseSlotUrl + "/" + nextPage;
        String firstUrl = baseSlotUrl + "/" + firstPage;
        String lastUrl = baseSlotUrl + "/" + lastPage;

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
        model.addAttribute("detailsUrl", baseSlotUrl + "/details");
        model.addAttribute("addSlotUrl", baseSlotUrl + "/add");
        model.addAttribute("baseUrl", baseSlotUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added slot.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated slot.");
            }
        }

        return "app/admin/slot/slot-list";
    }
}