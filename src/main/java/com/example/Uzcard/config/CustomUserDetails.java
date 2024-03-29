package com.example.Uzcard.config;



import com.example.Uzcard.enums.ProfileRole;
import com.example.Uzcard.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private String id;
    private String email;
    private String password;
    private Status status;
    private ProfileRole role;
    private String name;

    public CustomUserDetails(String id, String email, String password, Status status, ProfileRole role, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new LinkedList<>();
        list.add(new SimpleGrantedAuthority(role.name()));
        return list;

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return status.equals(Status.ACTIVE);
    }

    public String getId() {
        return id;
    }

    public ProfileRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
