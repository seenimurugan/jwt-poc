package com.seeni.jwtpoc.service;

import com.seeni.jwtpoc.config.JwtConfigProperties;
import com.seeni.jwtpoc.model.request.TokenInfo;
import com.seeni.jwtpoc.model.request.Wc1UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.seeni.jwtpoc.config.CustomJwtAuthenticationConverter.*;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder encoder;
    private final JwtConfigProperties jwtConfigProperties;

    public String generateToken(TokenInfo tokenInfo) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .audience(List.of("5436e3e3-0866-4ac1-b0c0-85c6ec8b863f"))
                .issuedAt(now)
                .notBefore(now)
                .expiresAt(now.plus(15, ChronoUnit.HOURS))
                .subject(tokenInfo.name())
                .claims(stringObjectMap -> {
                    var customClaims = Map.of(
                            "azp", jwtConfigProperties.allowedCw1Instances(),
                            "roles", jwtConfigProperties.roles());
                    stringObjectMap.putAll(customClaims);
                })
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateWc1UserDetailsToken(Wc1UserDetails wc1UserDetails) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .notBefore(now)
                .expiresAt(now.plus(15, ChronoUnit.HOURS))
                .subject(wc1UserDetails.userCode())
                .claims(stringObjectMap -> {
                    var customClaims = Map.of(
                            EBL_DOCUMENT_ID, wc1UserDetails.eblDocumentId(),
                            RID, wc1UserDetails.rid(),
                            LANGUAGE, wc1UserDetails.language(),
                            USER_CODE, wc1UserDetails.userCode(),
                            COMPANY_CODE, wc1UserDetails.companyCode(),
                            TIME_ZONE, wc1UserDetails.timeZone(),
                            SIGNATURE, wc1UserDetails.signature(),
                            ACTION, wc1UserDetails.action());
                    stringObjectMap.putAll(customClaims);
                })
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
