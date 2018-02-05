package com.miniseva.security.social.signin;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniseva.security.account.Account;
import com.miniseva.security.account.AccountService;
import com.miniseva.security.role.RoleService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Set;

/**
 * Spring Social specific functionality for provider sign in. Logs a user into the application when a matching
 * connection is found.
 */
public class SimpleSignInAdapter implements SignInAdapter {

    // Continue an existing log in flow after redirecting to a 3rd party provider (i.e. Linked, FB, Twitter, etc)
    private final RequestCache requestCache;

    @Inject
    private RoleService srvRole;

    @Inject
    private AccountService srvAccount;

    /**
     * This constructor is called in `MiniSevaApplication.java` so you cannot use constructor DI for srvRole and
     * srvAccount.
     *
     * @param requestCache
     */
    public SimpleSignInAdapter(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    /**
     * Log in programmatically and return the URL the user visited prior to starting the log in process.
     *
     * @param localUsername
     * @param connection  OAuth connection
     * @param request     native web request
     * @return
     */
    @Override
    public String signIn(String localUsername, Connection<?> connection, NativeWebRequest request) {
        // Create an authentication for the current user (i.e. programmatically log in the user). In other words, a user
        // is logged in when they have an authentication with their authenticated account details.
        Account localAccount = srvAccount.findByUsername(localUsername);
        Set<GrantedAuthority> authorities = srvRole.rolesToGrantedAuthorities(localAccount.getRoles());
        User localUser = new User(localUsername, "", authorities);
        SignInUtils.signin(localUser, authorities);
        //SignInUtils.signin(localUsername);

        return extractOriginalUrl(request);
    }

    /**
     * Get the URL of the most recently saved request (i.e. the page the user view before starting the log in process).
     *
     * @param request a web request
     * @return the URL of the most recently requested page
     */
    private String extractOriginalUrl(NativeWebRequest request) {
        HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
        // Get the most recent saved (i.e. cached) request
        SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
        if (saved == null) {
            return null;
        }
        // Remove the saved request from the cache (so we don't get it again)
        requestCache.removeRequest(nativeReq, nativeRes);
        // Remove the most recent auth failure exception from the session
        removeLastAuthExceptionFromSession(nativeReq.getSession(false));
        // Return the URL of the most recently saved request
        return saved.getRedirectUrl();
    }

    /**
     * Remove the most recent authentication-failure exception from the session.
     *
     * @param session the current http session
     */
    private void removeLastAuthExceptionFromSession(HttpSession session) {
        if (session == null)
            return;

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
