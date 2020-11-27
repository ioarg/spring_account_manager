package jarg.templates.account_manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@PropertySource("security.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;
    @Autowired
    private UserDetailsService userDetailsService;

    /***********************************************************************
    * Configure the Authentication here
    ************************************************************************/
    @Bean
    public PasswordEncoder getPasswordEncoder(){

//        // No-op encoder for testing
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                return charSequence.toString();
//            }
//
//            @Override
//            public boolean matches(CharSequence charSequence, String s) {
//                return s.equals(charSequence.toString());
//            }
//        };
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /***********************************************************************
     * Configure the Authorization here
     ************************************************************************/
    /* Remember :
    *   * Roles require the prefix "ROLE_" in the database
    *   * Users without a role are considered not existent
    *   * formLogin needs to also permit the custom login page
    *   * CSRF is automatically enabled and allows only POST logout
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String registrationPage = env.getProperty("security.web_pages.registrationPage");
        String loginProcessingUrl = env.getProperty("security.web_pages.loginProcessingUrl");
        String logoutProcessingUrl = env.getProperty("security.web_pages.logoutProcessingUrl");

        http.authorizeRequests()
                .antMatchers("/styles/**", "/scripts/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                    .sessionManagement()
                    .sessionFixation()
                    .newSession()
                .and()
                .formLogin()
                    .loginPage(registrationPage)
                    .loginProcessingUrl(loginProcessingUrl)
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}
