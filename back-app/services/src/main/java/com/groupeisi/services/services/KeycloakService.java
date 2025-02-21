package com.groupeisi.services.services;

import com.groupeisi.services.config.Credentials;
import com.groupeisi.services.dto.User;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import com.groupeisi.services.config.KeycloakConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class KeycloakService {

    public void addUser(User user){
        CredentialRepresentation credential = Credentials.createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setFirstName(user.getFirstname());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmailId());
        userRepresentation.setCredentials(Collections.singletonList(credential));
        userRepresentation.setGroups(Collections.singletonList("USER"));
        userRepresentation.setEnabled(true);

        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        usersResource.create(userRepresentation);
    }

    public List<UserRepresentation> getUser(String userName){
        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;
    }

    public void updateUser(String userId, User user){
        CredentialRepresentation credential = Credentials.createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setFirstName(user.getFirstname());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmailId());
        userRepresentation.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        usersResource.get(userId).update(userRepresentation);
    }

    public void deleteUser(String userId){
        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        usersResource.get(userId).remove();
    }

    public void sendVerificationLink(String userId){
        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        usersResource.get(userId).sendVerifyEmail();
    }

    public void sendResetPassword(String userId){
        UsersResource usersResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
        usersResource.get(userId).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }
}
