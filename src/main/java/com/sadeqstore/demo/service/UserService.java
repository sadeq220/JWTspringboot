package com.sadeqstore.demo.service;

import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.repository.UsersRepository;
import com.sadeqstore.demo.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class UserService {

    private JWTTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private UsersRepository usersRepository;

    @Autowired
    public UserService(JWTTokenProvider tokenProvider,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       UsersRepository usersRepository){
        this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
        this.tokenProvider=tokenProvider;
        this.usersRepository=usersRepository;
    }
    public String get_jwt(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return tokenProvider.createToken(username, usersRepository.findByName(username).getRole());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password supplied");
        }
    }
        public String signup(User user) {
            if (!usersRepository.existsByName(user.getName())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                usersRepository.save(user);
                return tokenProvider.createToken(user.getName(), user.getRole());
            } else {
                throw new ResponseStatusException( HttpStatus.UNPROCESSABLE_ENTITY,"Username is already in use");
            }
        }

        public Integer delete(String username){
        return usersRepository.deleteByName(username);
        }
    }

