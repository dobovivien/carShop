package dev.carshop.carshop.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private static final long EXPIRATION_TIME = 3_600_000;

    private static final String ENCODED_KEY = "xOS6xd8vssAQ0wS8QrtjD7MYHsqSQzHtKTIDFk2AzxEH0GDDQXizMPlLA38FRzepIKfsSkSs23VnzMACbyeKXrWiWg7VwSeZ9nST4s1wtIrrutZSihqDTcm1kfvloLSb";

    public String generateToken(Authentication authentication) throws Exception {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        try {
            return Jwts.builder()
                    .claim("scope", scope)
                    .setSubject(authentication.getName())
                    .setIssuedAt(Date.from(now))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (JWTCreationException exception){
            throw new Exception("Invalid signing configuration.");
        }
    }

    public boolean validateToken(String token, UserDetails userDetails, HttpServletRequest request) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    public static Key getSecretKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(ENCODED_KEY), "HmacSHA512");
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
