package com.sparta.deliveryi.user;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface MockUser {
    String subject() default "009f60a3-619f-4cc5-9577-390878c4856e";
    String username() default "test001";
    String[] roles() default {"CUSTOMER"};
    String clientId() default "deliveryI";
    String issuer() default "https://keycloak1.sparta-project.xyz/realms/sparta";
    long issuedAt() default 0L;
    long expiresAt() default 3600L;
}
