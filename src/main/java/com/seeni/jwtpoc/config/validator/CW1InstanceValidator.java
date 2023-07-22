package com.seeni.jwtpoc.config.validator;

import com.seeni.jwtpoc.config.JwtConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CW1InstanceValidator {

    public static final String AZP = "azp";
    public static final String ERROR_MSG_PREFIX = "An error occurred while attempting to decode the Jwt: ";
    public static final String ERROR_MSG = "CW1 Instance not allowed";
    private final JwtConfigProperties jwtConfigProperties;

    public void validate(Jwt jwt) {
        var allowedCw1Instances = jwtConfigProperties.allowedCw1Instances();
        if (!allowedCw1Instances.contains(jwt.getClaimAsString(AZP))) {
            throw new InvalidBearerTokenException(ERROR_MSG_PREFIX + ERROR_MSG);
        }
    }
}
