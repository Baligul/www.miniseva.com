package com.miniseva.security;

import com.miniseva.security.account.AccountDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ComponentScan(basePackageClasses = AccountDetailsService.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private ApplicationContext context;
    private DataSource dataSource;

    @Autowired
    @Qualifier("accountDetailsService") // TODO: Can I remove this?
    private AccountDetailsService accountDetailsService;
    //    private UserDetailsService userDetailsService;

    @Inject
    public SecurityConfiguration(ApplicationContext context, DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    private CsrfTokenRepository csrfTokenRepository()
    {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // URLs and files that Spring Security will completely ignore
        // "/test" is a test API
        web
            .ignoring()
                .antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.jpeg", "/**/*.js", "/**/*.woff",
                        "/**/*.ttf", "/*.ico", "/styles/**", "/images/**", "/scripts/**", "/fonts/**", "/**/*.mp4",
                        "/test", "/**/*.woff2", "/**/*.map");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .csrfTokenRepository(csrfTokenRepository())
                .and()
                    .authorizeRequests()
                        .antMatchers("/",
                                "/products",
                                "/package-details/**",
                                "/customers",
                                "/contact",
                                "/about",
                                "/tos",
                                "/privacy",
                                "/register",
                                "/signup",
                                "/signin/**",
                                "/signup/**",
                                "/items/**",
                                "/disconnect/facebook")
                            .permitAll()
                        .antMatchers("/admin/**")
                            .hasRole("ADMIN")
                        .antMatchers("/app/courses/add")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")
                        .antMatchers("/app/courses/author/**")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")
                        .antMatchers("/app/days/add")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")
                        .antMatchers("/app/exams/**/add")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")                        
                        .antMatchers("/app/exams/author")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")
                        .antMatchers("/app/documentation/**/add")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")
                        .antMatchers("/app/documentation/**/author/**")
                            .hasAnyRole("ROOT", "SYSADMIN", "TEACHER")
                        .anyRequest()
                            .authenticated()
                        //.anyRequest().permitAll()
                .and()
                    .formLogin()
                        .loginProcessingUrl("/login")
                        .loginPage("/login")
                        // .loginProcessingUrl("/signin/authenticate") // Social auth
                            .usernameParameter("username")
                            .passwordParameter("password")
                        .defaultSuccessUrl("/app", false)
                        // .failureUrl("/signin?param.error=bad_credentials") // Social auth
                        .failureUrl("/login?error=bad_credentials")
                            .permitAll()
                .and()
                    .logout()
                        .deleteCookies("remember-me")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                            .permitAll()
                //.and()
                    //.requestCache()
                        //.requestCache(new NullRequestCache())
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/403")
                .and()
                    .rememberMe()
                        .tokenRepository(persistentTokenRepository());
    }

    // Autowired with any method name is the same as overriding the base class' configure(AuthenticationManagerBuilder) method
    //@Autowired
    //public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(accountDetailsService).injectedPasswordEncoder(encodePassword());
        //auth.userDetailsService(accountDetailsService).passwordEncoder(passwordEncoder());
        // Default UserDetailsService that provides authentication
        auth.userDetailsService(accountDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Store the RememberMe token in PostgreSQL.
     *
     * @return a JDBC repository in which to store the RememberMe token.
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

}
