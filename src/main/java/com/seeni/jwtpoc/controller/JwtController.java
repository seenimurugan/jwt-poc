package com.seeni.jwtpoc.controller;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.seeni.jwtpoc.model.request.TokenInfo;
import com.seeni.jwtpoc.model.request.Wc1UserDetails;
import com.seeni.jwtpoc.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/jwt")
@Slf4j
@RequiredArgsConstructor
public class JwtController {

    public static final String KEYS = "keys";
    public static final String JWKS = "jwks";
    public static final String KEY_PAIR_IN_JWK_FORMAT = "rsaKeyPair";
    private final TokenService tokenService;
    private final JWKSet jwkSet;
    private final RSAKey customRsaKey;

    @GetMapping("/ebl")
    public String greeting(Principal principal, @RequestParam String eblDocumentId) {
        log.info("eblDocumentId[{}]", eblDocumentId);
        return "Hello ".concat("principal.getName()").concat(" from JWT Controller");
    }

    @PostMapping("/secureendpoint")
    public String secureEndpoint(Authentication authentication) {
        var wc1UserDetails = (Wc1UserDetails) authentication.getPrincipal();
        log.info("Wc1 User Details[{}]", wc1UserDetails);
        return "Hello ".concat(wc1UserDetails.userCode()).concat(" ").concat(wc1UserDetails.companyCode()).concat(" from JWT Controller");
    }

    @PostMapping("/redirectsecureendpoint")
    public ModelAndView redirectSecureEndpoint(Authentication authentication,
                                               HttpSession session,
                                               HttpServletRequest request,
                                               RedirectAttributes redirectAttributes) {
        var wc1UserDetails = (Wc1UserDetails) authentication.getPrincipal();
        log.info("Wc1 User Details[{}] sessionId: {}", wc1UserDetails, session.getId());
        redirectAttributes.addAttribute("eblDocumentId", wc1UserDetails.eblDocumentId());
        request.setAttribute(
                View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.FOUND);
        return new ModelAndView("redirect:/form/ebl");
    }

    @PostMapping(path = "/token")
    public String token(@RequestBody TokenInfo tokenInfo) {
        log.info("Token requested for user: [{}]", tokenInfo);
        String token = tokenService.generateToken(tokenInfo);
        log.info("Token granted: {}", token);
        return token;
    }

    @PostMapping(path = "/bodytoken")
    public String bodyToken(@RequestBody Wc1UserDetails wc1UserDetails) {
        log.info("Token requested for user: [{}]", wc1UserDetails.userCode());
        String token = tokenService.generateWc1UserDetailsToken(wc1UserDetails);
        log.info("Token granted: {}", token);
        return token;
    }

    @SneakyThrows
    @GetMapping("/keys")
    public Map<String, Object> jwk() {
        var jwks = Map.of(KEYS, List.of(customRsaKey.toPublicJWK().toJSONObject()));
        var rsaKeyPair = Map.of(KEYS, List.of(customRsaKey.toJSONObject()));
        return Map.of(
                JWKS, jwks,
                KEY_PAIR_IN_JWK_FORMAT, rsaKeyPair);
    }
}
