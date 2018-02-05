package com.miniseva.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.miniseva.app.card.Card;
import com.miniseva.app.card.CardRepository;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.lang.Integer;
import java.util.*;
import javax.validation.Valid;

import com.miniseva.security.account.AccountService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.joda.time.DateTime;

import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

@Controller
public class RegisterController {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private CardRepository repoCard;
    private CustomerRepository repoCustomer;
    private AccountService srvAccount;
    private ProductRepository repoProduct;

    public RegisterController(CardRepository repoCard,
                              AccountService srvAccount,
                              CustomerRepository repoCustomer,
                              ProductRepository repoProduct) {
        this.repoCard = repoCard;
        this.srvAccount = srvAccount;
        this.repoCustomer = repoCustomer;
        this.repoProduct = repoProduct;
    }

    @GetMapping(value = {"/register" })
    public String getRegisterPage(HttpServletRequest request,
                                  Model model) {
        model.addAttribute("products",repoProduct.findAll());

        return "/security/register";
    }

    @RequestMapping(value = { "/register" }, method = POST)
    public String postRegisterPage(HttpServletRequest request,
                                Model model,
                                @RequestParam(value="cardNo", required=false) String cardNo) {

        model.addAttribute("products",repoProduct.findAll());

        if (cardNo != null) {
            if(repoCustomer.countByCardNo(cardNo) == 0){
                model.addAttribute("cardNoError", "This card does not exists in our database.");
            } else {
                Customer customer = repoCustomer.findByCardNo(cardNo);
                if(srvAccount.customerExists(customer.getId())) {
                    model.addAttribute("cardNoError", "The account associated to this card already exists.");
                } else {
                    model.addAttribute("cardNo", cardNo);
                    return "redirect:/signup?cardNo=" + cardNo;
                }
            }
        } else {
            model.addAttribute("cardNoError", "Please enter card no before clicking next button.");
        }

        return "/security/register";
    }
}