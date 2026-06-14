package com.myeasybudget.user.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myeasybudget.user.infrastructure.persistence.AppUserEntity;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int MIN_SECRET_BYTES = 32;
    private static final String INSECURE_DEFAULT_SECRET = "change-me-in-production-with-at-least-32-characters";
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final byte[] secret;
    private final Duration expiration;

    @Autowired
    public JwtTokenService(
            ObjectMapper objectMapper,
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration}") Duration expiration
    ) {
        this(objectMapper, Clock.systemUTC(), secret, expiration);
    }

    JwtTokenService(ObjectMapper objectMapper, Clock clock, String secret, Duration expiration) {
        this.objectMapper = objectMapper;
        this.clock = clock;
        this.secret = validateSecret(secret).getBytes(StandardCharsets.UTF_8);
        this.expiration = expiration;
    }

    private static String validateSecret(String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "app.security.jwt.secret must be configured with at least " + MIN_SECRET_BYTES + " bytes");
        }
        if (INSECURE_DEFAULT_SECRET.equals(secret)) {
            throw new IllegalStateException(
                    "app.security.jwt.secret is still the insecure default; set JWT_SECRET to a strong value");
        }
        return secret;
    }

    public JwtToken createToken(AppUserEntity user) {
        Instant now = clock.instant();
        Instant expiresAt = now.plus(expiration);

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", user.getId().toString());
        payload.put("email", user.getEmailNormalized());
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());

        String headerPart = encodeJson(header);
        String payloadPart = encodeJson(payload);
        String unsignedToken = headerPart + "." + payloadPart;
        String signature = base64Url(hmac(unsignedToken));

        return new JwtToken(unsignedToken + "." + signature, expiresAt);
    }

    public Optional<UUID> validateAndExtractUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }

            String unsignedToken = parts[0] + "." + parts[1];
            String expectedSignature = base64Url(hmac(unsignedToken));
            if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8))) {
                return Optional.empty();
            }

            Map<String, Object> payload = objectMapper.readValue(Base64.getUrlDecoder().decode(parts[1]), MAP_TYPE);
            long expiresAt = ((Number) payload.get("exp")).longValue();
            if (clock.instant().getEpochSecond() >= expiresAt) {
                return Optional.empty();
            }

            return Optional.of(UUID.fromString((String) payload.get("sub")));
        } catch (RuntimeException | java.io.IOException ex) {
            return Optional.empty();
        }
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return base64Url(objectMapper.writeValueAsBytes(value));
        } catch (java.io.IOException ex) {
            throw new IllegalStateException("Could not serialize JWT", ex);
        }
    }

    private byte[] hmac(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (java.security.GeneralSecurityException ex) {
            throw new IllegalStateException("Could not sign JWT", ex);
        }
    }

    private static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
