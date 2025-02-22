import {environment} from "./environment/environment";
import {KeycloakService} from "keycloak-angular";

export const initializeKeycloak = (keycloak: KeycloakService) => async () =>
    keycloak.init({
        config: {
            url: environment.keycloak.authority,
            realm: environment.keycloak.realm,
            clientId: environment.keycloak.clientId,
        },
        loadUserProfileAtStartUp: true,
        initOptions: {
          flow: "implicit",
          onLoad: 'check-sso',
          silentCheckSsoRedirectUri:
              window.location.origin + '/assets/silent-check-sso.html',
          checkLoginIframe: false
        },
      bearerExcludedUrls: ['/assets'],
    });
