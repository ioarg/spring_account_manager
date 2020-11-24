package jarg.templates.account_manager.security;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

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
                .formLogin()
                    .loginPage(registrationPage)
                    .loginProcessingUrl(loginProcessingUrl)
                    .permitAll();

    }
}
