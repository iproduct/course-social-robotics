/* tslint:disable:no-unused-variable */

import { addProviders, async, inject } from '@angular/core/testing';
import { IptpiWebsocketService } from './iptpi-websocket.service';

describe('Service: IptpiWebsocket', () => {
  beforeEach(() => {
    addProviders([IptpiWebsocketService]);
  });

  it('should ...',
    inject([IptpiWebsocketService],
      (service: IptpiWebsocketService) => {
        expect(service).toBeTruthy();
      }));
});
