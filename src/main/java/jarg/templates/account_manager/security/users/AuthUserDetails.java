package jarg.templates.account_manager.security.users;

import jarg.templates.account_manager.security.users.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides basic user information, that is used for authentication and authorization.
 */
public class AuthUserDetails implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(AuthUserDetails.class);

    private User user;
    private List<GrantedAuthority> authorityList;

    public AuthUserDetails(User user) {
        this.user = user;
        authorityList = Arrays.stream(user.getAuthorities().getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
       logger.debug("User : "+ user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
