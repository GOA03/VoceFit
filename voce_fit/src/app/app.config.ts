import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { LUCIDE_ICONS, LucideIconProvider, Target, TrendingUp, Dumbbell, ArrowLeft, Zap, LogOut, EllipsisVertical, Pencil, Trash, Copy, X, Save, Clock, Check, Info, Eye, EyeOff, Mail, Lock, User, LogIn, UserPlus} from 'lucide-angular';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    provideAnimations(),
    { provide: LUCIDE_ICONS, multi: true, useValue: new LucideIconProvider({ Target, TrendingUp, Dumbbell, ArrowLeft, Zap, LogOut, EllipsisVertical, Pencil, Trash, Copy, X, Save, Clock, Check, Info, Eye, EyeOff, Mail, Lock, User, LogIn, UserPlus }) }
  ]
};
