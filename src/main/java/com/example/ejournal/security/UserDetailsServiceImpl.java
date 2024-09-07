package com.example.ejournal.security;

import com.example.ejournal.JournalDao;
import com.example.ejournal.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JournalDao journalDao;

    @Autowired
    public UserDetailsServiceImpl(JournalDao journalDao) {
        this.journalDao = journalDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = journalDao.findByEmail(username);
        if (null == user) {
            throw new IllegalArgumentException("No such user found!");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}