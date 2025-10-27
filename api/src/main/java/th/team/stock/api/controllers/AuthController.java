package th.team.stock.api.controllers;

import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;

import th.team.stock.dto.LoginRequest;
import th.team.stock.commons.JwtUtils;
import java.util.*;

@RestController
@RequestMapping("/stock-api/auth")  
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String accessToken = jwtUtils.generateAccessToken(request.getUsername());
            String refreshToken = jwtUtils.generateRefreshToken(request.getUsername());

            return ResponseEntity.ok(Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            ));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}