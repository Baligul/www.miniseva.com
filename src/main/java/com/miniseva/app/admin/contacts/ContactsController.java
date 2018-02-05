package com.miniseva.app.admin.contacts;

import com.miniseva.website.contact.Contact;
import com.miniseva.website.contact.ContactRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

@Controller
public class ContactsController {
    private static final Logger log = LoggerFactory.getLogger(ContactsController.class);

    private ContactRepository repoContacts;

    public ContactsController(ContactRepository repoContacts) {
        this.repoContacts = repoContacts;
    }

    @GetMapping(value = {"/app/admin/contacts/{pageNumber}", "/app/admin/contacts" })
    public String getContactsPage(HttpServletRequest request,
            @PathVariable Optional<Integer> pageNumber,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            Model model) {

        model.addAttribute("itemName", "Contacts");

        pageNumber = pageNumber.isPresent() ? pageNumber : Optional.of(1);
        
        // Get a page of contacts. Note: page is 0-based, but displayed as 1-based.
        PageRequest pageRequest =
                new PageRequest(pageNumber.get() - 1, PAGE_SIZE, DESC, "createdOn");
        Page<Contact> contacts = repoContacts.findAll(pageRequest);

        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            contacts = repoContacts.findByFullnameContainingIgnoreCase(searchValue, pageRequest);
        }

        model.addAttribute("contacts",contacts);

        // Counts for contacts
        long numContacts = repoContacts.count();
        if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
            repoContacts.countByFullnameContainingIgnoreCase(searchValue);
        }
        model.addAttribute("numContacts", numContacts);

        // First, last, next and previous links
        String previousPage = String.valueOf((pageRequest.previous().getPageNumber() + 1));
        String nextPage = String.valueOf((pageRequest.next().getPageNumber() + 1));
        String firstPage = String.valueOf((pageRequest.first().getPageNumber() + 1));

        // Calculate the last page
        int intLastPage = (int) numContacts/PAGE_SIZE;
        if (numContacts % PAGE_SIZE != 0) {
            intLastPage = intLastPage + 1;        
        } 
        String lastPage = String.valueOf(intLastPage);
        model.addAttribute("hasPrevious", contacts.hasPrevious());
        model.addAttribute("hasNext", contacts.hasNext());
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("range", pageNumber.get() + " of " + lastPage);

        String baseContactUrl = "/app/admin/contacts";

        String previousUrl = baseContactUrl + "/" + previousPage;
        String nextUrl = baseContactUrl + "/" + nextPage;
        String firstUrl = baseContactUrl + "/" + firstPage;
        String lastUrl = baseContactUrl + "/" + lastPage;

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
        
        model.addAttribute("baseUrl", baseContactUrl);

        return "app/admin/contacts/contacts-list";
    }
}