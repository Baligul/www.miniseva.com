package com.miniseva.security.controllers;

import com.miniseva.security.account.Account;
import com.miniseva.security.account.AccountService;
import com.miniseva.security.role.RoleService;
import com.miniseva.security.social.message.Message;
import com.miniseva.security.social.message.MessageType;
import com.miniseva.security.social.signin.SignInUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.miniseva.app.card.Card;
import com.miniseva.app.card.CardRepository;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;

import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import static com.miniseva.configuration.Configuration.URL_SIGNUP;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import com.miniseva.app.product.Product;
import com.miniseva.app.product.ProductRepository;

@Controller
public class SignupController {
    private static final Logger log = LoggerFactory.getLogger(SignupController.class);

    private AccountService accountService;
    private final ProviderSignInUtils providerSignInUtils;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private CardRepository repoCard;
    private CustomerRepository repoCustomer;
    private ProductRepository repoProduct;

    /**
     * Create an instance of the LoginController and inject the arguments.
     *
     * @param accountService            query the Account table
     * @param connectionFactoryLocator  lookup the `ConnectionFactory` used to create the `Connection` to the provider
     * @param usersConnectionRepository find the user that has the connection to the provider user attempting to sign in
     * @param roleService               role service
     */
    @Autowired
    public SignupController(AccountService accountService, 
                            ConnectionFactoryLocator connectionFactoryLocator,
                            UsersConnectionRepository usersConnectionRepository,
                            RoleService roleService,
                            PasswordEncoder passwordEncoder,
                            CardRepository repoCard,
                            CustomerRepository repoCustomer,
                            ProductRepository repoProduct) {

        this.accountService = accountService;
        // NOTE: ProviderSignInUtils can be provided a SessionStrategy. I can use SessionStrategy to store sessions in
        //       Redis or PostgreSQL, or to integrate with Spring Boot and Spring Security.
        this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.repoCard = repoCard;
        this.repoCustomer = repoCustomer;
        this.repoProduct = repoProduct;
    }

    /**
     * Display the sign up form. If the user has already signed in via a provider (FB, Twitter, LinkedIn, etc.) then
     * prepopulate the sign up form.
     *
     * @param request    HTTP servlet request
     * @param model      the model to display in the view
     * @param webRequest a generic web request
     * @return the sign up form page
     */
    @GetMapping(URL_SIGNUP)
    public String showSignUpForm(HttpServletRequest request, 
                                 Model model, 
                                 WebRequest webRequest,
                                 @RequestParam(value="action", required=false) String action,
                                 @RequestParam(value="cardNo") String cardNo) {
        model.addAttribute("pageSignup", true);
        model.addAttribute("page", "signup");
        model.addAttribute("action", action);
        model.addAttribute("cardNo", cardNo);
        model.addAttribute("products",repoProduct.findAll());

        // Set the customerId based on card no
        Customer customer = repoCustomer.findByCardNo(cardNo);
        model.addAttribute("customerId", customer.getId());

        // Social Auth
        // ===========

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);

        // TODO: Automate this step. Then prompt the user to update their profile with the missing data points (ex email)
        if (connection != null) {
            // If the user has already created a Social Auth connection, then pre-populate the Sign Up form with the
            // user's social information.

            webRequest.setAttribute("message", new Message(MessageType.INFO,
                    "Your " + StringUtils.capitalize(connection.getKey().getProviderId())
                            + " account is not associated with a MiniSeva account. Please sign up."), SCOPE_REQUEST);

            UserProfile socialUserProfile = connection.fetchUserProfile();
            model.addAttribute("name", socialUserProfile.getFirstName() + " " + socialUserProfile.getLastName());
            model.addAttribute("email", socialUserProfile.getEmail());
        }

        // Errors
        // ======

        Boolean hasErrors = false;
        Map<String, String> errors = new HashMap<>();

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            hasErrors = (Boolean) flashMap.get("hasErrors");

            // Convert generic map to a typed map while avoiding unchecked cast warning
            Map<?, ?> untypedErrorsMap = (HashMap<?, ?>) flashMap.get("errors");
            for (Map.Entry<?, ?> entry : untypedErrorsMap.entrySet()) {
                errors.put((String) entry.getKey(), (String) entry.getValue());         // All errors in a map
                model.addAttribute((String) entry.getKey(), (String) entry.getValue()); // Individual errors
            }
        }

        model.addAttribute("hasErrors", hasErrors);
        model.addAttribute("errors", errors);

        // Set the form values to previously entered data
        if (hasErrors) {
            Account account = (Account) flashMap.get("account");
            // model.addAttribute("cardNo", repoCard.findById(account.getCardId()).getCardNo());
            model.addAttribute("name", account.getName());
            model.addAttribute("email", account.getEmail());
            model.addAttribute("mobilePhone", account.getMobilePhone());
        }

        // URLs
        // ====
        model.addAttribute("urlSignup", URL_SIGNUP + "?cardNo="+cardNo);

        return "security/signup-form";
    }

    @PostMapping(URL_SIGNUP)
    public String registerAccount(@Valid Account account,
                                  Model model,                                  
                                  BindingResult bindingResult, 
                                  WebRequest webRequest,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam String cardNo,
                                  @RequestParam String confirmPassword) {

        // Validation
        // ==========
        int numValidationErrors = 0;
        Customer customer = null;
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("products",repoProduct.findAll());


        if (cardNo != null) {
            if(repoCustomer.countByCardNo(cardNo) == 0){
                log.error("VALIDATION ERROR: This card does not exists in our database.");
                errors.put("cardNoError", "This card does not exists in our database.");
                numValidationErrors++;
            } else {
                customer = repoCustomer.findByCardNo(cardNo);
                if(accountService.customerExists(customer.getId())) {
                    log.error("VALIDATION ERROR: The account associated to this card already exists.");
                    errors.put("cardNoError", "The account associated to this card already exists.");
                    numValidationErrors++;
                }
            }
        } else {
            log.error("VALIDATION ERROR: Card no. cannot be null.");
            errors.put("cardNoError", "Please enter card no.");
            numValidationErrors++;
        }

        // Check that passwords are the same
        if (!account.getPassword().equals(confirmPassword)) {
            log.info("Password and confirm password do not match.");
            errors.put("passwordError", "Password and confirm password do not match.");
            numValidationErrors++;
        }

        for (FieldError error : bindingResult.getFieldErrors()) {
            String field = error.getField();
            if(!field.equals("customerId")) {
                log.error("VALIDATION ERROR " + field + " " + error.getDefaultMessage());
                errors.put(field + "Error", fieldToLabel(field) + " " + error.getDefaultMessage());
                numValidationErrors++;
            }
        }

        if (numValidationErrors > 0) {
            log.error("Signup validation error(s). Redirect back to Signup form.");
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("hasErrors", true);
            redirectAttributes.addFlashAttribute("account", account);
            return "redirect:" + URL_SIGNUP + "?cardNo="+cardNo;
        }

        log.debug("Setting Username and other fields");
        account.setEnabled(true); // Enable the new account
        account.setRoles(roleService.findByRole("ROLE_CUSTOMER")); // Set the role as customer for the new account

        // Encode the password
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setUsername(account.getMobilePhone()); // Set the username for new account

        // save the Account in the DB via JPA
        try {
            log.debug("trying to save account");
                accountService.save(account);
                log.info("Saved account to DB: " + account.toString());
            } catch (DataIntegrityViolationException e) {
                log.info("Cannot save account to DB: " + account.toString());
                errors.put("emailError", "Email '" + account.getEmail() + "' already exists in our database.");
                log.error("Signup validation error(s). Redirect back to Signup form.");
                redirectAttributes.addFlashAttribute("errors", errors);
                redirectAttributes.addFlashAttribute("hasErrors", true);
                redirectAttributes.addFlashAttribute("account", account);
                return "redirect:" + URL_SIGNUP + "?cardNo="+cardNo;
            } catch (Exception e) {
                errors.put("exceptionError", "Some unknown database error occurs. Please copy the error text and send to sales@miniseva.com\n Error: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errors", errors);
                redirectAttributes.addFlashAttribute("hasErrors", true);
                redirectAttributes.addFlashAttribute("account", account);
                log.info("Cannot save account to DB: " + account.toString());
                return "redirect:" + URL_SIGNUP + "?cardNo="+cardNo;
        }

        // Create an authentication (does not have roles set)
        // Delixus.com uses ROLE_USER
        //Set<GrantedAuthority> authorities = roleService.rolesToGrantedAuthorities(roleService.findByRole("ROLE_USER"));
        Set<GrantedAuthority> authorities = roleService.rolesToGrantedAuthorities(roleService.findByRole("ROLE_CUSTOMER"));
        User user = new User(account.getUsername(), account.getPassword(), authorities);

        SignInUtils.signin(user, authorities);

        // Add a connection to a ProviderSignInAttempt
        providerSignInUtils.doPostSignUp(account.getUsername(), webRequest);        
        log.debug("Returning to app");
        return "redirect:/app";
        //return "redirect:/welcome"; // Welcome screen

    }

    private String fieldToLabel(String field) {
        if (field.equals("cardNo"))
            return "Card No";
        if (field.equals("name"))
            return "Name";
        if (field.equals("email"))
            return "Email";
        if (field.equals("password"))
            return "Password";
        if (field.equals("mobilePhone"))
            return "Mobile Phone";
        return "";
    }
}