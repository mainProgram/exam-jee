import { Component } from '@angular/core';
import {NgIf} from '@angular/common';
import {AuthenticationService} from '../../core/services/authentication.service';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    NgIf
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
