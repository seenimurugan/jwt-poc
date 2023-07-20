package com.seeni.jwtpoc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.seeni.jwtpoc.config.JwtConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/jwks/v1.0")
@Slf4j
@RequiredArgsConstructor
public class JwksController {

    private final JWKSet jwkSet;
    private final ObjectMapper objectMapper;
    private final JwtConfigProperties jwtConfigProperties;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return this.jwkSet.toJSONObject();
    }

    @SneakyThrows
    @GetMapping(path = "/.well-known/openid-configuration", produces = APPLICATION_JSON_VALUE)
    public String openIdConfiguration() {
        return objectMapper.readTree(jwtConfigProperties.openidConfiguration().toString()).toPrettyString();
    }

}
