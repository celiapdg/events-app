import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './components/homepage/homepage.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './auth/auth.guard';
import { BulletinBoardComponent } from './components/bulletin-board/bulletin-board.component';
import { EventDetailsComponent } from './components/events/event-details/event-details.component';
import { ProfileComponent } from './components/profile/profile.component';
import { MyEventsComponent } from './components/my-events/my-events.component';

const routes: Routes = [
  { path: 'home', component: HomepageComponent, canActivate: [AuthGuard] },
  { path: 'signup', component: SignupComponent, canActivate: [AuthGuard]  },
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard]  },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]  },
  { path: 'my-events', component: MyEventsComponent, canActivate: [AuthGuard]  },
  { path: 'bulletin-board', component: BulletinBoardComponent, canActivate: [AuthGuard] },
  { path: 'bulletin-board/:eventId', component: EventDetailsComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: 'home' }

];


@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
