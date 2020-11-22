package jarg.templates.account_manager.security;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:/security.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    /****************************************************************
     * Define Datasource For Security
     ***************************************************************/
    @Bean
    public DataSource getSecurityDatasource(){
        ComboPooledDataSource ds = new ComboPooledDataSource();
        // Setup the connection
        try {
            ds.setDriverClass(env.getProperty("security.datasource.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        ds.setJdbcUrl(env.getProperty("security.datasource.url"));
        ds.setUser(env.getProperty("security.datasource.username"));
        ds.setPassword(env.getProperty("security.datasource.password"));
        // Setup connection pool
        ds.setInitialPoolSize(Integer.parseInt(
                env.getProperty("security.connection_pool.initialPoolSize")));
        ds.setMinPoolSize(Integer.parseInt(
                env.getProperty("security.connection_pool.minPoolSize")));
        ds.setMaxPoolSize(Integer.parseInt(
                env.getProperty("security.connection_pool.maxPoolSize")));
        ds.setMaxIdleTime(Integer.parseInt(
                env.getProperty("security.connection_pool.maxIdleTime")));
        return ds;
    }

    /***********************************************************************
    * Configure the Authentication here
    ************************************************************************/
//    @Bean
//    public PasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder(12);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(getSecurityDatasource());
 //               .passwordEncoder(getPasswordEncoder());
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
