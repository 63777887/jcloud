package com.jwk.security;

import com.jwk.security.security.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwkSecurityApplicationTests {

    @Autowired
    TokenService tokenService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
        String token = tokenService.generateToken("jiwk");
        System.out.println(tokenService.validateToken(token));
    }

}
