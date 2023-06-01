package com.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.model.User;
import com.springboot.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Generate and return JWT token
            String token = generateToken(user.getId(), user.getUsername());
            return token;
        }
        return null;
    }

    private String generateToken(Long userId, String username) {
    	
        // Generate JWT token using userId and username
        // You can use any JWT library here, like JJWT
        // Example implementation:
    	
        String secretKey = "your-secret-key";
        String token = Jwts.builder()
                .setId(userId.toString())
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }
}
