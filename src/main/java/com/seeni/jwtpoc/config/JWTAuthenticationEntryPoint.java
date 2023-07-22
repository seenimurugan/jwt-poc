package com.seeni.jwtpoc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(UNAUTHORIZED.value());
        var errorMap = Map.of(
                "timestamp", Calendar.getInstance().getTime(),
                "err_msg", authException.getMessage(),
                "status", response.getStatus());
        var outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, errorMap);
        outputStream.flush();
    }
}
