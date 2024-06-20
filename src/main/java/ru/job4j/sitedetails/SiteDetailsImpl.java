package ru.job4j.sitedetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.job4j.model.Site;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class SiteDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Site site;

    public SiteDetailsImpl(Site site) {
        this.site = site;
    }

    public static SiteDetailsImpl build(Site site) {
        return new SiteDetailsImpl(site);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return site.getPassword();
    }

    @Override
    public String getUsername() {
        return site.getLogin();
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
        return true;
    }
}
