/* Copyright 2015 IPT – Intellectual Products & Technologies Ltd.
   Author: Trayan Iliev, IPT (http://iproduct.org)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   */

import { Injectable } from '@angular/core';
import {Subscriber} from 'rxjs/Subscriber';
import {IptpiWebsocketSubject} from './iptpi-websocket-subject';

export class WebsocketErrorEvent {
  error: any;
  filename: string;
  message: string;
}


/**
   * Creates a reactive WebSocket Subject with a given URL, protocol and an optional observer for open and close events.
   *
   * @example
   *  var socketSubject = = new IPTRxWebSocketSubject('ws://127.0.0.1/ws', 'wish-protocol', openObserver, closingObserver);
   *
   * @param {String} url The URL of the WebSocket endpoint.
   * @param {String} protocol The WebSocket protocol.
   * @param {Observer} [openObserver] Optional Observer call after WebSocket open event.
   * @param {Observer} [closingObserver] Optional Observer to call before underlying WebSocket is closed.
   * @returns {Subject} An observable sequence wrapping WebSocket.
   */

@Injectable()
export class IptpiWebsocketService extends IptpiWebsocketSubject {

  constructor() {
    super('ws://' + window.location.host + '/ws', null,
      Subscriber.create(event => console.log('socket open')),
      Subscriber.create(event => console.log('socket is about to close'))
    );
  }

}
