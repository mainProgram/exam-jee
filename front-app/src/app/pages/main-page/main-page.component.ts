import { Component } from '@angular/core';
import {NgIf} from '@angular/common';
import {AuthenticationService} from '../../core/services/authentication.service';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbar} from '@angular/material/toolbar';
import {RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    NgIf,
    MatCardHeader,
    MatCardContent,
    MatCardTitle,
    MatCard,
    MatButtonModule,
    MatToolbar,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent {
  get isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn();
  }
  constructor(private readonly authenticationService: AuthenticationService) {}
  redirectToLoginPage(): void {
    this.authenticationService.redirectToLoginPage();
  }
}
