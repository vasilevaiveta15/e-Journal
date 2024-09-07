package com.example.ejournal.security;

import com.example.ejournal.JournalDao;
import com.example.ejournal.JournalService;
import com.example.ejournal.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AuthService {
    private final TokenService tokenService;

    private final JournalService journalService;

    @Autowired
    public AuthService(TokenService tokenService,
                       JournalService journalService) {
        this.tokenService = tokenService;
        this.journalService = journalService;
    }

    public String authenticateUser(Principal principal) {
        User user = journalService.findByEmail(principal.getName());

        if (null == user) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return tokenService.generateToken(user);
    }

    public String getCurrentUser() {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return (String) jwt.getClaims().get("sub");
        } catch (ClassCastException ex) {
            return ((org.springframework.security.core.userdetails.User) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        }
    }

}