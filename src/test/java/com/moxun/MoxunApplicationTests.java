package com.moxun;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MoxunApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.matches("123456", "$2a$10$IabdDxiiexD1R/mhJB2L/.wM6SOwDLcdVtjGDjSkxOVPph/K/UHGO"));
        System.out.println(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("郑静", "123456")));
    }

}
