package com.multiplatform.time_management_backend.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public record AlgorithmWithId(
        String id,
        Algorithm algorithm
) {
    public static AlgorithmWithId fromKeyPair(RSAKeyPairConfigurations.KeyPairWithId keyPairWithId) {
        if (keyPairWithId == null || keyPairWithId.keyPair() == null) {
            throw new IllegalArgumentException("Invalid RSA key pair configuration.");
        }

        Algorithm algorithm = Algorithm.RSA256(
                (RSAPublicKey) keyPairWithId.keyPair().getPublic(),
                (RSAPrivateKey) keyPairWithId.keyPair().getPrivate()
        );
        return new AlgorithmWithId(keyPairWithId.keyId(), algorithm);
    }
}
