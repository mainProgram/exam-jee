export const environment = {
    production: false,
    keycloak: {
        authority: 'http://localhost:8081',
        redirectUri: 'http://localhost:4200',
        postLogoutRedirectUri: 'http://localhost:4200/logout',
        realm: 'exam-jee',
        clientId: 'spring-exam-jee',
    },
    idleConfig: { idle: 10, timeout: 60, ping: 10 },
};
