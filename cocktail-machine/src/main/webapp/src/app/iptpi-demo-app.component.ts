import { Component } from '@angular/core';
// import { APP_SHELL_DIRECTIVES } from '@angular/app-shell';
import {MdToolbar} from '@angular2-material/toolbar';
import {MdButton} from '@angular2-material/button';
import {MdIcon, MdIconRegistry} from '@angular2-material/icon';
// import { DogsComponent } from './+dogs';

@Component({
  selector: 'iptpi-demo-app',
  template: `
    <md-sidenav-layout fullscreen>
      <md-sidenav #sidenav>
        <md-nav-list >
          <a md-list-item *ngFor="let view of views" routerLink="{{view.routerLink}}" (click)="sidenav.close()">
            <md-icon md-list-icon>{{view.icon}}</md-icon>
            <span md-line>{{view.name}}</span>
            <span md-line>{{view.description}}</span>
          </a>
        </md-nav-list>
      </md-sidenav>
      <md-toolbar color="primary">
        <button md-icon-button (click)="sidenav.open()" ripple="false">
          <svg style="width:24px;height:24px" viewBox="0 0 24 24">
            <path fill="currentColor" d="M3,6H21V8H3V6M3,11H21V13H3V11M3,16H21V18H3V16Z" />
          </svg>
        </button>
        IPTPI Reactive Robotics Demo
      </md-toolbar>
      <router-outlet ></router-outlet>
    </md-sidenav-layout>
  `,
  // templateUrl: 'iptpi-demo-app.component.html',
  styles: [`
    md-sidenav-layout {
      background: rgba(0,0,0,0.08);
    }
  `],
  providers: [MdIconRegistry],
})
export class IptpiDemoAppComponent {
  title = 'IPTPI Reactive Robotics Demo';
  views: Object[] = [
    {
      name: 'Commands',
      description: 'Command your robot',
      icon: 'explore',
      routerLink: '/dashboard'
    },
    {
      name: 'Robot Settings',
      description: 'Edit robot settings',
      icon: 'pets',
      routerLink: '/settings'
    }
  ];
}
