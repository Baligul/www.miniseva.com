package com.miniseva.app.admin.card;

import com.miniseva.security.account.Account;

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

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

import com.miniseva.app.card.CardRepository;
import com.miniseva.app.card.Card;

@Controller
public class CardController {
    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private CardRepository repoCard;

    public CardController(CardRepository repoCard) {
        this.repoCard = repoCard;
    }

    @GetMapping(value = {"/app/admin/card/{pageNumber}", "/app/admin/card" })
    public String getCardPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="msg", required=false) String msg,
            Model model) {

        model.addAttribute("itemName", "Card");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of card. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, DESC, "createdOn");
        Page<Card> cards = repoCard.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            cards = repoCard.findByCardNoContainingIgnoreCase(searchValue, pageRequest);
        }

        model.addAttribute("cards",cards);

        // Counts for card
        long numCard = repoCard.count();

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            numCard = repoCard.countByCardNoContainingIgnoreCase(searchValue);
        }

        model.addAttribute("numCard", numCard);

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numCard/PAGE_SIZE;
        if (numCard % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", cards.hasPrevious());
        model.addAttribute("hasNext", cards.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseCardUrl = "/app/admin/card";

        String previousUrl = baseCardUrl + "/" + previousPage;
        String nextUrl = baseCardUrl + "/" + nextPage;
        String firstUrl = baseCardUrl + "/" + firstPage;
        String lastUrl = baseCardUrl + "/" + lastPage;

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
        model.addAttribute("detailsUrl", baseCardUrl + "/details");
        model.addAttribute("addCardUrl", baseCardUrl + "/add");
        model.addAttribute("baseUrl", baseCardUrl);

        if(msg != null) {
            if(msg.equals("sv")) {
                model.addAttribute("successMsg", "Successfully added card.");
            } else if(msg.equals("ed")) {
                model.addAttribute("successMsg", "Successfully updated card.");
            }
        }

        return "app/admin/card/card-list";
    }
}