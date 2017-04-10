/* tslint:disable:no-unused-variable */

import { addProviders, async, inject } from '@angular/core/testing';
import { IptpiDemoAppComponent } from './iptpi-demo-app.component';

describe('App: IptpiDemo', () => {
  beforeEach(() => {
    addProviders([IptpiDemoAppComponent]);
  });

  it('should create the app',
    inject([IptpiDemoAppComponent], (app: IptpiDemoAppComponent) => {
      expect(app).toBeTruthy();
    }));

  it('should have as title \'app works!\'',
    inject([IptpiDemoAppComponent], (app: IptpiDemoAppComponent) => {
      expect(app.title).toEqual('IPTPI Reactive Robotics Demo');
    }));
});
