package com.multiplatform.time_management_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class RSAKeyPairConfigurations {

    @Bean(name = "AccessTokenKeyPair")
    public KeyPairWithId getAccessTokenKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair();
    }

    @Bean(name = "RefreshTokenKeyPair")
    public KeyPairWithId getRefreshTokenKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair();
    }

    public List<KeyPairWithId> getKeyPairs() throws NoSuchAlgorithmException {
        return List.of(getAccessTokenKeyPair(), getRefreshTokenKeyPair());
    }

    private KeyPairWithId generateKeyPair() throws NoSuchAlgorithmException {
        // This Should never be used in production
        // To use in prod Please import the key pair from somewhere secure and use a key pair rotation system for more robust security and never share the private keys
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return new KeyPairWithId(UUID.randomUUID().toString(), kpg.genKeyPair());
    }

    public record KeyPairWithId(
            String keyId,
            KeyPair keyPair
    ) {
    }
}
