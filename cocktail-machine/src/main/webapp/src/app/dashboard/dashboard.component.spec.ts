/* tslint:disable:no-unused-variable */

import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { addProviders, async, inject } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { IptpiWebsocketService } from '../services';

describe('Component: Dashboard', () => {

   beforeEach(() => {
    addProviders([IptpiWebsocketService]);
  });

  it('should create an instance', () => {
    inject([IptpiWebsocketService],
    (service: IptpiWebsocketService) => {
      let component = new DashboardComponent(service);
      expect(component).toBeTruthy();
    });
  });

});
