package dev.oguzhanercelik.config.security;

import dev.oguzhanercelik.exception.TokenGenerateException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class JwtSecurityTokenConfiguration {

    @Value("${jwt.token.secret}")
    private String secret;

    public SecretKeySpec getSecretKeySpec() {
        try {
            return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
        } catch (Exception ex) {
            log.error("Exception occurred : ", ex);
            throw new TokenGenerateException(ex.getMessage());
        }
    }
}