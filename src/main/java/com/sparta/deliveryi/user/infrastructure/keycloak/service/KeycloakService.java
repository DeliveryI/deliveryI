package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakException;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakMessageCode;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakService implements AuthService {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    public void logout(KeycloakId keycloakId) {
        try {
            UsersResource resource = keycloak.realm(properties.getRealm()).users();
            resource.get(keycloakId.getId().toString()).logout();
        } catch (NotFoundException e) {
            throw new KeycloakException(KeycloakMessageCode.NOT_FOUND);
        } catch (Exception e) {
            // throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);
        }
    }

    @Override
    public void delete(KeycloakId keycloakId) {
        try {
            UsersResource resource = keycloak.realm(properties.getRealm()).users();
            resource.delete(keycloakId.getId().toString());
        } catch (NotFoundException e) {
            throw new KeycloakException(KeycloakMessageCode.NOT_FOUND);
        } catch (Exception e) {
            // throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);
        }
    }

}
