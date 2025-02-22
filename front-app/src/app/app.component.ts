import {Component, DestroyRef, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {UserIdleService} from 'angular-user-idle';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {AuthenticationService} from './core/services/authentication.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Front-App';

  constructor(
    private readonly authenticationService: AuthenticationService,
    private readonly userIdleService: UserIdleService,
    private destroyRef: DestroyRef
  ) {}

  ngOnInit(): void {
    if (this.authenticationService.isLoggedIn()) {
      this.userIdleService.startWatching();
      this.userIdleService
        .onTimerStart()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe();
      this.userIdleService
        .onTimeout()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          alert('Your session has timed out. Please log in again.');
          this.authenticationService.logout();
          this.userIdleService.resetTimer();
        });
    }
  }
}
