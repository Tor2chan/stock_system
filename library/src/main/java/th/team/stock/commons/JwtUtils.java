package th.team.stock.commons;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {

    private final String SECRET = "super_secret_key_123";
    private final long ACCESS_EXP = 1000 * 60 * 15; 
    private final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 7; 

    public String generateAccessToken(String username) {
        return createToken(username, ACCESS_EXP);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, REFRESH_EXP);
    }

    private String createToken(String username, long exp) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
