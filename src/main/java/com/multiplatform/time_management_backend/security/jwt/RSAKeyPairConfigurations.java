package com.multiplatform.time_management_backend.security.jwt;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@Getter
public class RSAKeyPairConfigurations {

    private List<KeyPairWithId> keyPairs = new ArrayList<>();

    public KeyPairWithId generateRSAKeyPair() throws NoSuchAlgorithmException {
        // This Should never be used in production
        // To use in prod Please import the key pair from somewhere secure and use a key pair rotation system for more robust security and never share the private keys
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPairWithId keyPairWithId = new KeyPairWithId(UUID.randomUUID().toString(), kpg.genKeyPair());
        keyPairs.add(keyPairWithId);
        return keyPairWithId;
    }

    public record KeyPairWithId(
            String keyId,
            KeyPair keyPair
    ) {
    }
}
