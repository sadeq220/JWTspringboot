package com.sadeqstore.demo.security;

import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MyUserDetails implements UserDetailsService {
    private UsersRepository usersRepository;
    @Autowired
    public MyUserDetails(UsersRepository usersRepository){
        this.usersRepository=usersRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=usersRepository.findByName(s);
        if(user==null)throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"username not found!");
        return org.springframework.security.core.userdetails.User//
                .withUsername(user.getName())//
                .password(user.getPassword())//
                .authorities(user.getRole())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }
}
