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
import th.team.stock.models.Users;
import th.team.stock.repositories.UsersRepo;
import java.util.*;

@RestController
@RequestMapping("/stock-api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UsersRepo usersRepo; // ⭐ inject repo มาเช็ค active โดยตรง

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {

            // ⭐ step 1 - หา user จาก DB
            Users user = usersRepo.findByUsername(request.getUsername()).orElse(null);

            // ⭐ step 2 - ไม่เจอ user
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }

            // ⭐ step 3 - เช็ค active ถ้า false ปฏิเสธเลย
            if (user.getActive() == null || !user.getActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "ACCOUNT_DISABLED"));
            }

            // ⭐ step 4 - active แล้วค่อย authenticate password
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            org.springframework.security.core.userdetails.UserDetails userDetails =
                    (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();

            String username = userDetails.getUsername();
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

        } catch (DisabledException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "ACCOUNT_DISABLED"));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}