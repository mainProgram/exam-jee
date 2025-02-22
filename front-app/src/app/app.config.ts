import {
  ApplicationConfig,
  provideZoneChangeDetection,
  importProvidersFrom,
  APP_INITIALIZER
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {provideHttpClient, withFetch, withXsrfConfiguration} from '@angular/common/http';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "../../keycloak-init";
import {NgIdleModule} from "@ng-idle/core";

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(
      withFetch(),
      withXsrfConfiguration(
        {
          cookieName: 'XSRF-TOKEN',
          headerName: 'X-XSRF-TOKEN',
        })
    ),
    provideAnimationsAsync(),
    importProvidersFrom(KeycloakAngularModule, NgIdleModule.forRoot()),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
  ]
};
