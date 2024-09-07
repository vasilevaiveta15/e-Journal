package com.example.ejournal.bean;

import com.example.ejournal.enums.Role;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class User implements GrantedAuthority {
    public final String USER_ROLE_START = "ROLE_";

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    public User() {
    }

    public User(String mail,
                ASN1ObjectIdentifier userPassword,
                List<GrantedAuthority> student) {
    }

    @Override
    @Transient
    public String getAuthority() {
        return USER_ROLE_START + role.toString();
    }

    public String getUSER_ROLE_START() {
        return USER_ROLE_START;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}