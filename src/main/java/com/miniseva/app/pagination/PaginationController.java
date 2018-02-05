package com.miniseva.app.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaginationController {
    private static final Logger log = LoggerFactory.getLogger(PaginationController.class);

    public PaginationController() {
    }

    @RequestMapping(value = { "/app/pagination" }, method = POST)
    public String goToPage(HttpServletRequest request,
            Model model,
            @RequestParam String baseUrl,
            @RequestParam Integer pageHash,
            @RequestParam(value="unapproved", required=false) String unapproved,
            @RequestParam(value="action", required=false) String action,
            @RequestParam(value="searchValue", required=false) String searchValue,
            @RequestParam(value="group", required=false) Long group) {

            Boolean paramAlreadyExists = false;

            String redirectUrl = baseUrl + "/" + pageHash;
            if (unapproved != null && unapproved.equals("true")) {
                redirectUrl = redirectUrl + "?unapproved=true";
                paramAlreadyExists = true;
            }

            if(action != null && action.equals("search") && searchValue != null && !searchValue.trim().equals("")) {
                if (paramAlreadyExists) {
                    redirectUrl = redirectUrl + "&action=search&searchValue=" + searchValue;
                } else {
                    redirectUrl = redirectUrl + "?action=search&searchValue=" + searchValue;
                }
                paramAlreadyExists = true;
            } else if (group != null && group != 0) {
                if (paramAlreadyExists) {
                    redirectUrl = redirectUrl + "&group=" + group;
                } else {
                    redirectUrl = redirectUrl + "?group=" + group;
                }
                paramAlreadyExists = true;
            }

        return "redirect:" + redirectUrl;
    }
}