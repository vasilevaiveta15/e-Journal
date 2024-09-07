package com.example.ejournal.security;

import com.example.ejournal.bean.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class TokenService {
    private final JwtEncoder encoder;

    public final String ROLE = "roles";

    public final String ISSUER = "self";

    public final String USER_ROLE_START = "ROLE_";

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(User user) {
        List<String> roleOfLoggedUser = Collections.singletonList(USER_ROLE_START + user.getRole().name());

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getEmail())
                .claim(ROLE, roleOfLoggedUser)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}