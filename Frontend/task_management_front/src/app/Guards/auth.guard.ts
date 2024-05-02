import { inject } from '@angular/core';
import { CanActivateChildFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';

export const authGuard: CanActivateChildFn = (childRoute, state) => {
  const authService = inject(UserService);
  const router = inject(Router);
  if (!authService.isUserLogged) {
    router.navigate(['/Login']);
    return false;
  }
  // logged in, so return true
  authService.isUserLogged;
  return true;
};
