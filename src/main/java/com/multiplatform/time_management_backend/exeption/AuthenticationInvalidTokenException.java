package com.multiplatform.time_management_backend.exeption;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class AuthenticationInvalidTokenException extends AuthenticationException {

}
