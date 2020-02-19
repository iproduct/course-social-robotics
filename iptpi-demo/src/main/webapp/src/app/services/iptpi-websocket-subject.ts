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

import {Observer} from 'rxjs/Observer';
import {Subscriber} from 'rxjs/Subscriber';
import {Subject} from 'rxjs/Subject';

export class WebsocketErrorEvent {
    error: any;
    filename: string;
    message: string;
}

export class WebsocketCloseEvent {
    code: number;
    reason: string;
    wasClean: boolean;
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

export class IptpiWebsocketSubject extends Subject<string> {

  private websocket: WebSocket;
  private url: string;
  private openObserver: Observer<Event>;
  private closingObserver: Observer<WebsocketCloseEvent>;
  private serverDestination: Subscriber<string>;

  constructor(url: string, protocol: string,
              openObserver?: Observer<Event>,
              closingObserver?: Observer<WebsocketCloseEvent>) {
    super();

    this.url = url;
    this.openObserver = openObserver;
    this.closingObserver = closingObserver;

    if (!WebSocket) { throw new TypeError('WebSocket not implemented in your runtime.'); }
    this.websocket = new WebSocket(this.url);

    this.websocket.onopen = (event) => {
      this.openObserver.next(event);
      this.openObserver.complete();
    };
    this.websocket.onclose = (event) => {
      this.closingObserver.next(event);
      this.closingObserver.complete();
    };
    this.websocket.onmessage =
      (event) => {
        try {
          super.next(event.data);
        } catch (e) {
          super.error(e);
        }
      };
    this.websocket.onerror = (event) => {
      super.error(event);
    };

    this.serverDestination = Subscriber.create(
      (message: string) => {
        this.websocket.send(message);
      },
      (error: any) => {
        console.log('WebSocket error: ', error);
        this.websocket.close(4011, 'Error processing client data stream.');
        let errorEvent: WebsocketErrorEvent = new WebsocketErrorEvent();
        errorEvent.message = 'Error processing client data stream.';
        errorEvent.error = error;
        super.error(errorEvent);
      },
      () => {
        console.log('WebSocket closing');
        this.websocket.close(1000, 'WS connection closed by client.');
        let closeEvent: WebsocketCloseEvent = new WebsocketCloseEvent();
        closeEvent.code = 1000; // CLOSE_NORMAL
        closeEvent.reason = 'WS connection closed by client.';
        closeEvent.wasClean = true;
        this.closingObserver.next(closeEvent);
        this.closingObserver.complete();
      }
    );
  }

  next(value?: string): void {
    this.serverDestination.next(value);
  }

  error(err?: any): void {
    this.serverDestination.error(err);
  }

  complete(): void {
    this.serverDestination.complete();
  }

}
