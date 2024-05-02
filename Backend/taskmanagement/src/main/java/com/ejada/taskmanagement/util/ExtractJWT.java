package com.ejada.taskmanagement.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ejada.taskmanagement.enums.RoleEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class ExtractJWT {



    private final SecurityProperties secProperties;

    // algorithm that hash with

    Algorithm _algorithm ;
    @PostConstruct
    private void setAlgorithms(){
        _algorithm= Algorithm.HMAC256(secProperties.getSecret_key().getBytes());
    }






    public String creatAccessToken(String userId, String username, RoleEnum role , boolean isVerified,
                                   HttpServletRequest request) {

        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) //expire after 30 minutes
                .withIssuer(request.getRequestURL().toString())
                .withClaim("id",  Long.valueOf(userId))
                .withClaim("role",  role.toString())
                .withClaim("isVerified",  isVerified)
                .withClaim("username", username)
                .sign(_algorithm);
    }

    public String createRefreshToken(String userId,
                                     String username,
                                     HttpServletRequest request) {

        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 24 * 7 * 60 * 1000)) //expire after a week
                .withIssuer(request.getRequestURL().toString())
                .withClaim("id", userId)
                .sign(_algorithm);
    }




    public  Long refreshAccessToken(String refreshToken) {

        String token = refreshToken.substring((secProperties.getBearer() + " ").length());
        // check algorithm that hash with
        JWTVerifier verifier = JWT.require(_algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("id").asLong();
    }

    public Map<String, Object> getTokensAsJson(String access_token, String refresh_token) {
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        return tokens;
    }



    public boolean checkIfTokenIsExpired(String token){
        String btoken = token.substring((secProperties.getBearer() + " ").length());

        JWTVerifier verifier = JWT.require(_algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(btoken);
        if (decodedJWT.getExpiresAt().before(new Date())){
            return true;
        }
        return false;
    }

    public Long getUserIdFromToken(String accessToken) {
        String token = accessToken.substring((secProperties.getBearer() + " ").length());
        // check algorithm that hash with
        JWTVerifier verifier = JWT.require(_algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("id").asLong();
    }
}