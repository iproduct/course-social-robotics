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

import { Component, OnInit } from '@angular/core';
import { IptpiWebsocketService } from '../services';

export interface Position {
  x: number;
  y: number;
  heading: number;
  timestamp: number;
}

export interface MovementCommand {
  deltaX: number;
  deltaY: number;
  deltaHeading: number;
  velocity: number;
  angularVelocity: number;
  acceleration: number;
  angularAcceleration: number;
}

const DEFAULT_COMMAND: MovementCommand = {
  deltaX: 400,
  deltaY: 0,
  deltaHeading: 0,
  velocity: 50,
  angularVelocity: 0,
  acceleration: 0,
  angularAcceleration: 0
};


@Component({
  selector: 'iptpi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public title = 'Command your IPTPI Robot';
  public position: Position = { 'x': 0, 'y': 0, 'heading': 0, timestamp: 0 };
  public positions: Position[] = [];
  public command: MovementCommand = DEFAULT_COMMAND;

  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private robotImage = new Image();
  private compassImage = new Image();
  private needleImage = new Image();

  constructor(private wsService: IptpiWebsocketService) {

  }

  ngOnInit() {
    this.wsService.subscribe(
      event => {
        let data: string = event;
        console.log(`data: ${data}`);
        this.position = JSON.parse(data);
        console.log(this.position);
        this.updateCanvasDrawing();
      },
      err => {
        console.log('Error:', event);
      },
      () => {
        console.log('Complete:', event);
      });

    this.canvas = <HTMLCanvasElement>document.getElementById('robot-canvas');
    this.ctx = this.canvas.getContext('2d');

    this.robotImage.src = '../../assets/img/iptpi.png';
    this.robotImage.onload = () => this.updateCanvasDrawing();
    this.compassImage.src = '../../assets/img/compass.png';
    this.compassImage.onload = () => this.updateCanvasDrawing();
    this.needleImage.src = '../../assets/img/needle.png';
    this.needleImage.onload = () => this.updateCanvasDrawing();
  }


submitToServer(move: MovementCommand) {
    console.log(move);
    this.wsService.next(JSON.stringify(move));
  }

updateCanvasDrawing() {
    let width = document.body.clientWidth;
    // let height = document.body.clientHeight;
    let canvasWidth = Math.min(Math.round(0.8 * width), 500);
    let canvasHeight = canvasWidth;
    this.canvas.width = canvasWidth;
    this.canvas.height = canvasHeight;
    let robotWidth = Math.round(0.6 * canvasWidth);
    let robotHeight = Math.round(1.2 * robotWidth);
    let compassWidth = Math.round(0.3 * canvasWidth);
    let needleWidth = Math.round(1 * compassWidth);
    let centerX = canvasWidth * 0.6;
    let centerY = canvasHeight * 0.5;

    this.ctx.fillStyle = '#55FF55';
    this.ctx.fillRect(0, 0, canvasWidth, canvasHeight);

    // Draw robot
    this.ctx.save();
    this.ctx.translate(centerX, centerY);
    this.ctx.rotate(- this.position.heading);
    this.ctx.drawImage(this.robotImage, -robotWidth / 2, -robotHeight / 2, robotWidth, robotHeight);
    this.ctx.restore();
    // Draw compass
    this.ctx.drawImage(this.compassImage, 0, 0, compassWidth, compassWidth);
    // Draw compass needle
    this.ctx.save();
    this.ctx.translate(compassWidth / 2, compassWidth / 2);
    this.ctx.rotate(-this.position.heading);
    this.ctx.drawImage(this.needleImage, -compassWidth / 2, -compassWidth / 2, needleWidth, needleWidth);
    this.ctx.restore();
  }

  onGoUp() {
    this.command = {
      deltaX: 400,
      deltaY: 0,
      deltaHeading: 0,
      velocity: 50,
      angularVelocity: 0,
      acceleration: 0,
      angularAcceleration: 0
    };
    this.submitToServer(this.command);
  }

  onGoLeft() {
    this.command = {
      deltaX: 400,
      deltaY: 0,
      deltaHeading: 1.6,
      velocity: 40,
      angularVelocity: 0,
      acceleration: 0,
      angularAcceleration: 0
    };
    this.submitToServer(this.command);
  }

  onGoRight() {
    this.command = {
      deltaX: 400,
      deltaY: 0,
      deltaHeading: -1.6,
      velocity: 40,
      angularVelocity: 0,
      acceleration: 0,
      angularAcceleration: 0
    };
    this.submitToServer(this.command);
  }

  onGoDown() {
    this.command = {
      deltaX: -200,
      deltaY: 0,
      deltaHeading: 0,
      velocity: -50,
      angularVelocity: 0,
      acceleration: 0,
      angularAcceleration: 0
    };
    this.submitToServer(this.command);
  }

  onGoCustom() {
    this.command.deltaX = +this.command.deltaX;
    this.command.velocity = +this.command.velocity;
    this.command.deltaHeading = +this.command.deltaHeading;
    this.submitToServer(this.command);
  }

}

