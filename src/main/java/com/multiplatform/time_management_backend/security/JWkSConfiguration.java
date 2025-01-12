package com.multiplatform.time_management_backend.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class JWkSConfiguration {

    private final RSAKeyPairConfigurations rsaKeyPairConfigurations;

    @Bean
    public JWKSet jwkSet() throws NoSuchAlgorithmException {
        return new JWKSet(rsaKeyPairConfigurations.getKeyPairs().stream().map(keyPairWithId -> (JWK) new RSAKey.Builder((RSAPublicKey) keyPairWithId.keyPair().getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(keyPairWithId.keyId()).build()).toList());
    }
}
