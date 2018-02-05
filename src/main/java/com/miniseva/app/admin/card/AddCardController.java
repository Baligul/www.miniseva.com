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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;
import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.miniseva.configuration.Configuration.PAGE_SIZE;

import org.joda.time.DateTime;

import com.miniseva.security.account.AccountService;
import com.miniseva.security.custom.AccountDetails;
import org.springframework.security.core.Authentication;

import com.miniseva.app.card.CardRepository;
import com.miniseva.app.card.Card;

import org.springframework.dao.DataIntegrityViolationException;

@Controller
public class AddCardController {
    private static final Logger log = LoggerFactory.getLogger(AddCardController.class);

    private CardRepository repoCard;
    private AccountService srvAccount;

    public AddCardController(CardRepository repoCard,
                             AccountService srvAccount) {
        this.repoCard = repoCard;
        this.srvAccount = srvAccount;
    }

    @GetMapping(value = {"/app/admin/card/add" })
    public String getAddCardPage(HttpServletRequest request, Model model) {
        
        // Generate card and send to UI
        String cardNo = generateRandom(12);
        model.addAttribute("cardNo", cardNo);
        
        Card card = new Card(null, null, 0, DateTime.now().plusMonths(12),
            null, null, null, null);
        card.setFormattedExpiresOn();

        model.addAttribute("card", card);

        return "app/admin/card/add-card";
    }

    @RequestMapping(value = { "/app/admin/card/add" }, method = POST)
    public String postAddCardPage(HttpServletRequest request, 
            @Valid Card card, 
            BindingResult bindingResult, Model model, 
            @RequestParam(value="action", required=false) String action,
            Authentication authentication) {

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        Card saveCard = null;
        card.setFormattedExpiresOn();
        model.addAttribute("card", card);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("card");             
                if (error != null) {
                    // If card is wrong then generate card and send to UI
                    String cardNo = generateRandom(12);
                    model.addAttribute("cardNo", cardNo);

                    model.addAttribute("cardNoError", error.getDefaultMessage());
                    log.info("Add Card validation errors.");
                    return "app/admin/card/add-card";
                }
                error = bindingResult.getFieldError("expiresOn");             
                if (error != null) {
                    model.addAttribute("expiresOnError", error.getDefaultMessage());
                    log.info("Add Card validation errors.");
                    return "app/admin/card/add-card";
                }
                error = bindingResult.getFieldError("amount");             
                if (error != null) {
                    model.addAttribute("amountError", error.getDefaultMessage());
                    log.info("Add Card validation errors.");
                    return "app/admin/card/add-card";
                }
            }
            
            if (action.equals("save")) {
                card.setCreatedBy(userId);
                card.setCreatedOn(DateTime.now());
                try {
                    saveCard = repoCard.save(card);
                    log.info("Saved card to DB: " + saveCard.toString());
                    return "redirect:/app/admin/card?msg=sv";
                } catch (DataIntegrityViolationException e) {
                    log.info("Couldn't add the card'");
                    if (e.getMessage().contains("card_no")) {
                        model.addAttribute("cardNoError", "Card '" + card.getCardNo() + "' already exists in our database.");
                    }
                    model.addAttribute("successMsg", "Card can not be saved.");
                } catch (Exception e) {
                    // If card is duplicate then generate card and send to UI
                    String cardNo = generateRandom(12);
                    model.addAttribute("cardNo", cardNo);
                    model.addAttribute("successMsg", e.getMessage());
                    log.info("Couldn't add the card'");
                }
            }
        }

        return "app/admin/card/add-card";
    }

    public static String generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }
}