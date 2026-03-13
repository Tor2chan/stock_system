package th.team.stock.api.controllers;

import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
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
            // รับ Authentication object กลับมาเพื่อดึง user info
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            // ดึง UserDetails จาก principal
            org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication
                    .getPrincipal();

            String username = userDetails.getUsername();

            // ดึง role แรกจาก authorities
            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("user")
                    .replace("ROLE_", "");

            String accessToken = jwtUtils.generateAccessToken(username);
            String refreshToken = jwtUtils.generateRefreshToken(username);

            return ResponseEntity.ok(Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken,
                    "username", username,
                    "role", role));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}