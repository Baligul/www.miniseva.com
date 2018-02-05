package com.miniseva.app.admin.card;

import com.miniseva.security.account.Account;

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

import com.miniseva.app.card.CardRepository;
import com.miniseva.app.card.Card;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Author (add, edit, delete) card.
 */
@Controller
public class AuthorCardController {
    private static final Logger log = LoggerFactory.getLogger(AuthorCardController.class);
    private CardRepository repoCard;
    private AccountService srvAccount;

    public AuthorCardController(CardRepository repoCard,
                                AccountService srvAccount) {
        this.repoCard = repoCard;
        this.srvAccount = srvAccount;
    }

    /**
     * Edit an existing card.
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/app/admin/card/details/{cardId}" }, method = GET)
    public String authorCardForm(HttpServletRequest request, 
                                  Model model, 
                                  @PathVariable Long cardId) {

        Card card = repoCard.findById(cardId);
        model.addAttribute("card", card);
        model.addAttribute("cardUrl", "/app/admin/card");

        return "app/admin/card/card-details";
    }

    @RequestMapping(value = { "/app/admin/card/details/{cardId}" }, method = POST)
    public String cardSubmit(HttpServletRequest request, 
                              @Valid Card card, BindingResult bindingResult,
                              Model model, @PathVariable Long cardId, 
                              @RequestParam(value="action", required=false) String action,
                              Authentication authentication) {

        Card savedCard = null;

        AccountDetails user = new AccountDetails(authentication);
        String username = user.getUsername();
        Long userId = srvAccount.getUserId(username);

        card.setFormattedExpiresOn();
        model.addAttribute("card", card);

        if (action != null) {
            if (bindingResult.hasErrors() && action.equals("save")) {
                FieldError error = bindingResult.getFieldError("card");             
                if (error != null) {
                    // If card is wrong then generate card and send to UI
                    String cardNo = generateRandom(12);
                    model.addAttribute("cardNo", cardNo);

                    model.addAttribute("cardError", error.getDefaultMessage());
                    log.info("Update Card validation errors.");
                    return "app/admin/card/add-card";
                }
                error = bindingResult.getFieldError("expiresOn");             
                if (error != null) {
                    model.addAttribute("expiresOnError", error.getDefaultMessage());
                    log.info("Update Card validation errors.");
                    return "app/admin/card/add-card";
                }
                error = bindingResult.getFieldError("amount");             
                if (error != null) {
                    model.addAttribute("amountError", error.getDefaultMessage());
                    log.info("Update Card validation errors.");
                    return "app/admin/card/add-card";
                }
            }

            if (action.equals("save")) {
                savedCard = repoCard.findById(cardId);
                savedCard.setUpdatedBy(userId);
                savedCard.setUpdatedOn(DateTime.now());
                savedCard.setCardNo(card.getCardNo());
                savedCard.setAmount(card.getAmount());
                savedCard.setExpiresOn(card.getExpiresOn());
                try {
                        savedCard = repoCard.save(savedCard);
                        log.info("Saved card to DB: " + savedCard.toString());
                        return "redirect:/app/admin/card?msg=ed";
                    } catch (DataIntegrityViolationException e) {
                        log.info("Couldn't save the card'");
                        if (e.getMessage().contains("card_no")) {
                            model.addAttribute("cardNoError", "Card '" + card.getCardNo() + "' already exists in our database.");
                        }
                        model.addAttribute("successMsg", "Card can not be saved.");
                    } catch (Exception e) {
                        // If card is duplicate then generate card and send to UI
                        String cardNo = generateRandom(12);
                        model.addAttribute("cardNo", cardNo);
                        log.info("Cannot update card to DB: " + savedCard.toString());
                        model.addAttribute("successMsg", "Database Error.");
                }
            }
        }

        model.addAttribute("cardUrl", "/app/admin/card");

        return "app/admin/card/card-details";
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