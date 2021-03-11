package org.lejos.ev3.sample.graphicssample;

import java.io.File;
import java.io.FileInputStream;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.UARTPort;
import lejos.utility.Delay;

/**
 * Demonstrate various leJOS graphics techniques.
 */
public class GraphicsSample extends Thread
{

    GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
    final int SW = g.getWidth();
    final int SH = g.getHeight();
    final int DELAY = 2000;
    final int TITLE_DELAY = 1000;
    Image duke = new Image(100, 64, new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x1c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x1e, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1e, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x60, (byte) 0x3e, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xbe, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xbe, (byte) 0x07, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xfd, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x01, (byte) 0xe0, (byte) 0xff, (byte) 0x07, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, 
            (byte) 0xe0, (byte) 0xff, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0xc0, (byte) 0xff, 
            (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x1f, (byte) 0xc0, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3f, (byte) 0x80, 
            (byte) 0xff, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0xff, (byte) 0x07, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xe1, (byte) 0xff, 
            (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0xf3, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xf7, 
            (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0xef, (byte) 0xfe, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xf8, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x1f, (byte) 0xe0, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x3f, (byte) 0xc0, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x7f, 
            (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x7f, (byte) 0xc0, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0xff, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xc1, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0xff, (byte) 0xc0, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xfd, 
            (byte) 0xc3, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x3f, (byte) 0xea, (byte) 0x7f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5f, 
            (byte) 0x55, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x80, (byte) 0x8f, (byte) 0xf8, (byte) 0x3c, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x80, 
            (byte) 0x57, (byte) 0x55, (byte) 0x3c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0xaf, (byte) 0xea, 
            (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x80, (byte) 0x55, (byte) 0x55, (byte) 0x38, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0xad, 
            (byte) 0x7a, (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xc0, (byte) 0x5d, (byte) 0x15, (byte) 0x30, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0, 
            (byte) 0xb9, (byte) 0x1e, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xf0, (byte) 0x07, 
            (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0x80, (byte) 0x00, (byte) 0xe0, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf8, (byte) 0x00, 
            (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf8, (byte) 0x00, (byte) 0x00, (byte) 0xc0, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x6c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x6c, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x6c, (byte) 0x00, 
            (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x6c, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x78, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x38, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3c, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x1e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0x00, 
            (byte) 0x0c, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x1f, (byte) 0x80, (byte) 0x7f, (byte) 0x00, 
            (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, 
            (byte) 0xe0, (byte) 0xff, (byte) 0x00, (byte) 0x06, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0xf0, (byte) 0x80, 
            (byte) 0x01, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x18, (byte) 0x38, (byte) 0x00, (byte) 0x03, (byte) 0x06, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x1c, 
            (byte) 0x00, (byte) 0x06, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x18, (byte) 0x07, (byte) 0x00, (byte) 0x0e, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x98, 
            (byte) 0x03, (byte) 0x00, (byte) 0x0c, (byte) 0x03, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf8, (byte) 0x01, (byte) 0x00, 
            (byte) 0x18, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xe0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, });
    
    Image logo = new Image(52, 64, new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x33, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x33, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, 
            (byte) 0x33, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x3f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xc0, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0xff, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xfc, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x03, (byte) 0xfc, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0x03, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0x00, (byte) 0x00, });
    
    void splash()
    {
        g.clear();
        g.setFont(Font.getLargeFont());
        g.drawString("Lego EV3", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.drawString("+", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.drawString("Java", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.drawString("=", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();       
        //g.setFont(Font.getDefaultFont());
        g.drawRegion(logo, 0, 0, logo.getWidth(), logo.getHeight(), 0, SW / 2, SH / 4+10, GraphicsLCD.HCENTER | GraphicsLCD.VCENTER);
        g.drawString("leJOS/EV3", SW/2, 3*SH/4+10, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        //g.drawString("Preview", SW/2, 3*SH/4+24, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY*2);
        g.setFont(Font.getDefaultFont());
        g.clear();
    }
   
    
    void titles()
    {
        g.setContrast(0);
        g.setFont(Font.getLargeFont());
        //g.drawString("leJOS", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        g.drawString("Graphics", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        g.refresh();
        g.setFont(Font.getDefaultFont());
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.refresh();
        g.setContrast(0x60);
    }
    
    void buttons()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Buttons", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        g.setFont(Font.getDefaultFont());
        for(;;)
        {
            int but = Button.waitForAnyPress(TITLE_DELAY);
            g.clear();
            String pressed = "";
            if (but == 0)
                pressed = "None";
            if ((but & Button.ID_ENTER) != 0)
                pressed += "Enter ";
            if ((but & Button.ID_LEFT) != 0)
                pressed += "Left ";
            if ((but & Button.ID_RIGHT) != 0)
                pressed += "Right ";
            if ((but & Button.ID_UP) != 0)
                pressed += "Up ";
            if ((but & Button.ID_DOWN) != 0)
                pressed += "Down ";
            if ((but & Button.ID_ESCAPE) != 0)
                pressed += "Escape ";
            g.drawString(pressed, SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
            if (but == Button.ID_ESCAPE)
                break;
            
        }
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.refresh();
    }    
    
    void leds()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("LEDS", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.setFont(Font.getDefaultFont());
        for(int i = 1; i < 10; i++)
        {
            g.clear();
            g.drawString("Pattern " + i, SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
            g.refresh();
            Button.LEDPattern(i);
            Button.waitForAnyPress(DELAY*2);
            
        }
        Button.LEDPattern(0);
        g.clear();
        g.refresh();
    }

    void displaySensorValues(UARTPort p)
    {
        g.setFont(Font.getLargeFont());
        g.drawString(p.getModeName(0), SW/2, SH/4, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        for(int i = 0; i < 20; i++)
        {
            g.clear();
            g.setFont(Font.getDefaultFont());
            g.drawString(p.getModeName(0), SW/2, SH/4, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
            g.setFont(Font.getLargeFont());
            g.drawString(p.toString(), SW/2, 3*SH/4, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
            g.refresh();
            Button.waitForAnyPress(500);
        }
        
    }
    

    public void run()
    {
        //System.out.println("Thread running");
        File f = new File("popcorn.wav");
        Sound.playSample(f, 100);
        //g.clear();
    }
    
    void sound()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Sound", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.setFont(Font.getDefaultFont());
        g.clear();
        g.drawString("Tones", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        g.refresh();
        Button.waitForAnyPress(DELAY/2);
        Sound.setVolume(50);
        Sound.beepSequenceUp();
        Button.waitForAnyPress(DELAY/2);
        Sound.beepSequence();
        Button.waitForAnyPress(DELAY/2);
        Sound.twoBeeps();
        Button.waitForAnyPress(DELAY/2);
        Sound.setVolume(100);
        g.clear();
        g.drawString("Wav files", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        //this.setDaemon(true);
        this.start();
        Button.waitForAnyPress(DELAY*2);
        g.clear();
        g.drawString("Popcorn anyone?", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(DELAY*2);
        
        g.clear();
        g.refresh();
    }

    void displayTacho(NXTRegulatedMotor m)
    {
        g.clear();
        while(m.isMoving())
        {
            g.clear();
            g.drawString("Position: " + m.getTachoCount(), SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);            
            g.refresh();
        }
        Button.waitForAnyPress(DELAY);
        g.clear();
    }
    
    void motors()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("Motors", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
        g.setFont(Font.getDefaultFont());
        NXTRegulatedMotor ma = Motor.A;
        ma.resetTachoCount();
        ma.setAcceleration(600);
        ma.setSpeed(500);
        g.drawString("Forward 720", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(720, true);
        displayTacho(ma);
        g.drawString("Backward 720", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(-720, true);
        displayTacho(ma);
        ma.setSpeed(80);
        g.drawString("Slow Forward 360", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(360, true);
        displayTacho(ma);
        ma.setSpeed(800);
        g.drawString("Fast Backward 360", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(DELAY);
        ma.rotate(-360, true);
        displayTacho(ma);        
    }
    
    void credits()
    {
        g.setFont(Font.getLargeFont());
        g.drawString("leJOS/EV3", SW/2, SH/2, GraphicsLCD.BOTTOM|GraphicsLCD.HCENTER);
        Button.waitForAnyPress(TITLE_DELAY*100);
        g.setFont(Font.getDefaultFont());        
    }

    void displayTitle(String text)
    {
        g.clear();
        g.drawString(text, SW / 2, SH / 2, GraphicsLCD.HCENTER | GraphicsLCD.BASELINE);
        Button.waitForAnyPress(TITLE_DELAY);
        g.clear();
    }

    void characterSet()
    {
        displayTitle("Character Set");
        int chHeight = g.getFont().getHeight();
        int chWidth = g.getFont().stringWidth("M");
        for(int l = 0; l < 8; l++)
            for(int c = 0; c < 16; c++)
                g.drawChar((char)(l*16 + c), c*chWidth, l*chHeight - 8, 0);
        Button.waitForAnyPress(DELAY);
    }

    void textAnchors()
    {
        displayTitle("Text Anchors");
        int chHeight = g.getFont().getHeight();
        g.drawString("Left", SW / 2, 0, GraphicsLCD.LEFT);
        g.drawString("Center", SW / 2, chHeight, GraphicsLCD.HCENTER);
        g.drawString("Right", SW / 2, chHeight * 2, GraphicsLCD.RIGHT);
        g.drawString("Left", SW / 2, chHeight * 4, GraphicsLCD.LEFT, true);
        g.drawString("Center", SW / 2, chHeight * 5, GraphicsLCD.HCENTER, true);
        g.drawString("Right", SW / 2, chHeight * 6, GraphicsLCD.RIGHT, true);
        Button.waitForAnyPress(DELAY);
    }

    void fonts()
    {
        displayTitle("Fonts");
        g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
        g.drawString("Small", SW / 2, 16, GraphicsLCD.HCENTER | GraphicsLCD.BASELINE);
        g.setFont(Font.getFont(0, 0, Font.SIZE_MEDIUM));
        g.drawString("Medium", SW / 2, 48, GraphicsLCD.HCENTER | GraphicsLCD.BASELINE);
        g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
        g.drawString("Large", SW / 2, 96, GraphicsLCD.HCENTER | GraphicsLCD.BASELINE);
        g.setFont(Font.getDefaultFont());
        Button.waitForAnyPress(DELAY);
    }

    void rotatedText()
    {
        displayTitle("Rotated Text");
        Font large = Font.getFont(0, 0, Font.SIZE_LARGE);
        Image base = Image.createImage(SW, large.getHeight());
        GraphicsLCD bg = base.getGraphics();
        bg.setFont(large);
        bg.drawString("Top", SW / 2, 0, GraphicsLCD.HCENTER);
        g.drawImage(base, 0, 0, 0);
        bg.clear();
        bg.drawString("Bottom", SW / 2, 0, GraphicsLCD.HCENTER);
        Image rotImage = Image.createImage(base, 0, 0, SW, base.getHeight(), GraphicsLCD.TRANS_ROT180);
        g.drawImage(rotImage, 0, SH - 1, GraphicsLCD.BOTTOM);
        bg.clear();
        bg.drawString("Left", SH / 2, 0, GraphicsLCD.HCENTER);
        rotImage = Image.createImage(base, 0, 0, SH, base.getHeight(), GraphicsLCD.TRANS_ROT270);
        g.drawImage(rotImage, 0, 0, 0);
        bg.clear();
        bg.drawString("Right", SH / 2, 0, GraphicsLCD.HCENTER);
        rotImage = Image.createImage(base, 0, 0, SH, base.getHeight(), GraphicsLCD.TRANS_ROT90);
        g.drawImage(rotImage, SW - 1, 0, GraphicsLCD.RIGHT);
        Button.waitForAnyPress(DELAY);
    }

    void fileImage() throws Exception
    {
        displayTitle("File image");
        Image img = Image.createImage(new FileInputStream(new File("arm.lni")));
        g.drawRegion(img, 0, 0, SW, SH, GraphicsLCD.TRANS_NONE, SW / 2, SH / 2, GraphicsLCD.HCENTER | GraphicsLCD.VCENTER);
        Button.waitForAnyPress(DELAY);
    }

    void lines()
    {
        displayTitle("Lines");
        for (int i = 1; i < SH / 2; i += 4)
        {
            g.drawLine(i - 4, i, SW - i, i);
            g.drawLine(SW - i, i, SW - i, SH - i);
            g.drawLine(i, SH - i, SW - i, SH - i);
            g.drawLine(i, i + 4, i, SH - i);
        }
        g.drawLine(0, 0, SW, SH);
        g.drawLine(SW, 0, 0, SH);
        Button.waitForAnyPress(DELAY);
    }

    void rectangles()
    {
        displayTitle("Rectangles");
        for (int i = 1; i < 7; i++)
            g.drawRect(i * 25 - 10, 20 - 2 * i, i * 4, i * 4);
        for (int i = 1; i < 7; i++)
            g.fillRect(i * 25 - 10, 80 - 2 * i, i * 4, i * 4);
        Button.waitForAnyPress(DELAY);
    }

    void circles()
    {
        displayTitle("Circles");
        for (int i = 1; i < 7; i++)
            g.drawArc(i * 25 - 10, 20 - 2 * i, i * 4, i * 4, 0, 360);
        for (int i = 1; i < 7; i++)
            g.fillArc(i * 25 - 10, 80 - 2 * i, i * 4, i * 4, 0, 360);
        Button.waitForAnyPress(DELAY);
    }

    void scroll()
    {
        displayTitle("Scrolling");
        int line = g.getFont().getHeight();
        g.drawString("Hello from leJOS", SW / 2, SH - line, GraphicsLCD.HCENTER);
        g.setColor(GraphicsLCD.WHITE);
        for (int i = 0; i < 7; i++)
        {
            Delay.msDelay(250);
            g.copyArea(0, SH - (i+1) * line, SW, line, 0, SH - (i+2)*line, 0);
            g.fillRect(0, SH - (i+1) * line, SW, line);
        }
        for (int i = 6; i >= 0; i--)
        {
            Delay.msDelay(250);
            g.copyArea(0, SH - (i + 2) * line, SW, line, 0, SH - (i + 1) * line, 0);
            g.fillRect(0, SH - (i + 2) * line, SW, line);
        }
        Button.waitForAnyPress(DELAY);
        g.setAutoRefresh(false);
        for (int i = 0; i < 7*line; i++)
        {
            Delay.msDelay(10);
            g.copyArea(0, SH - line - i, SW, line, 0, SH - line - (i + 1), 0);
            g.fillRect(0, SH - i, SW, 1);
            g.refresh();
        }
        for (int i = 7*line - 1; i >= 0; i--)
        {
            Delay.msDelay(10);
            g.copyArea(0, SH - line - (i + 1), SW, line, 0, SH - line - i, 0);
            g.fillRect(0, SH - line - (i + 1), SW, 1);
            g.refresh();
        }
        g.setAutoRefresh(true);
        g.refresh();
        Button.waitForAnyPress(DELAY);
        g.setColor(GraphicsLCD.BLACK);
    }

    void image(int transform, String title)
    {
        displayTitle(title);
        g.drawRegion(duke, 0, 0, duke.getWidth(), duke.getHeight(), transform, SW / 2, SH / 2, GraphicsLCD.HCENTER | GraphicsLCD.VCENTER);
        Button.waitForAnyPress(DELAY);
    }

    void images()
    {
        displayTitle("Image Display");
        image(GraphicsLCD.TRANS_NONE, "Normal");
        image(GraphicsLCD.TRANS_ROT90, "Rotate 90");
        image(GraphicsLCD.TRANS_ROT180, "Rotate 180");
        //image(GraphicsLCD.TRANS_ROT270, "Rotate 270");
        image(GraphicsLCD.TRANS_MIRROR, "Mirror");
        //image(GraphicsLCD.TRANS_MIRROR_ROT90, "Mirror 90");
        //image(GraphicsLCD.TRANS_MIRROR_ROT180, "Mirror 180");
        //image(GraphicsLCD.TRANS_MIRROR_ROT270, "Mirror 270");
    }

    void animation()
    {
        displayTitle("Animation");
        Image arms = new Image(216, 33, new byte[] {(byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, 
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, 
                (byte) 0x3c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x3e, (byte) 0x0f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x3c, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0x38, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3e, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3e, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0x79, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x60, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0xbe, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x3e, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x3d, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xbd, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3e, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0x3e, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3c, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, 
                (byte) 0xff, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x9f, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x70, 
                (byte) 0x3e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf8, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xe0, (byte) 0x3d, (byte) 0x0f, (byte) 0x00, 
                (byte) 0x00, (byte) 0x78, (byte) 0x1f, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x78, (byte) 0x1e, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x9f, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x78, (byte) 0x1f, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7c, (byte) 0x3f, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, 
                (byte) 0xc0, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0xe0, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf8, (byte) 0x9f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xfc, (byte) 0x1f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x78, (byte) 0x00, (byte) 0x80, (byte) 0xff, (byte) 0x03, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf8, (byte) 0xaf, (byte) 0x03, 
                (byte) 0x00, (byte) 0x00, (byte) 0xbc, (byte) 0x0f, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x7c, (byte) 0x00, (byte) 0x80, 
                (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xe0, 
                (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0xe2, 
                (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x80, (byte) 0xf8, 
                (byte) 0xef, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xfe, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3e, 
                (byte) 0x02, (byte) 0xf8, (byte) 0xff, (byte) 0x03, (byte) 0x00, 
                (byte) 0x00, (byte) 0xfe, (byte) 0xff, (byte) 0x01, (byte) 0x00, 
                (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00, 
                (byte) 0xe0, (byte) 0xfd, (byte) 0xff, (byte) 0x03, (byte) 0x00, 
                (byte) 0x70, (byte) 0xfe, (byte) 0x47, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x9e, (byte) 0x0f, (byte) 0xfc, (byte) 0xff, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
                (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0xff, (byte) 0xff, 
                (byte) 0x01, (byte) 0x00, (byte) 0xf0, (byte) 0xfe, (byte) 0x73, 
                (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0xdf, (byte) 0x0f, 
                (byte) 0xfc, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0xfe, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0xc0, 
                (byte) 0xff, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x1c, 
                (byte) 0xff, (byte) 0x07, (byte) 0xb8, (byte) 0xff, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0xfc, (byte) 0xff, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0xfa, (byte) 0x7f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xc0, (byte) 0xff, (byte) 0x3f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x01, 
                (byte) 0x00, (byte) 0xbc, (byte) 0xff, (byte) 0x03, (byte) 0x81, 
                (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0xe0, 
                (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xf8, 
                (byte) 0x3f, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0xff, 
                (byte) 0x1f, (byte) 0x00, (byte) 0x01, (byte) 0xc0, (byte) 0xff, 
                (byte) 0xff, (byte) 0x10, (byte) 0x00, (byte) 0xfc, (byte) 0xff, 
                (byte) 0x03, (byte) 0x01, (byte) 0x7e, (byte) 0x00, (byte) 0x00, 
                (byte) 0x10, (byte) 0xc0, (byte) 0x7f, (byte) 0x00, (byte) 0x00, 
                (byte) 0x01, (byte) 0xf0, (byte) 0x3f, (byte) 0x00, (byte) 0x10, 
                (byte) 0x00, (byte) 0xfe, (byte) 0x0f, (byte) 0x00, (byte) 0x01, 
                (byte) 0xc0, (byte) 0xff, (byte) 0x1f, (byte) 0x10, (byte) 0x00, 
                (byte) 0xfc, (byte) 0xff, (byte) 0x00, (byte) 0x03, (byte) 0x3c, 
                (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x80, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xe0, (byte) 0x5f, 
                (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0xfe, (byte) 0x0f, 
                (byte) 0x00, (byte) 0x03, (byte) 0x80, (byte) 0xff, (byte) 0x0f, 
                (byte) 0x30, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x1f, 
                (byte) 0x07, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x70, 
                (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x07, 
                (byte) 0xe0, (byte) 0x1f, (byte) 0x00, (byte) 0x70, (byte) 0x00, 
                (byte) 0xfc, (byte) 0x07, (byte) 0x00, (byte) 0x07, (byte) 0x80, 
                (byte) 0xff, (byte) 0x03, (byte) 0x70, (byte) 0x00, (byte) 0xf8, 
                (byte) 0xff, (byte) 0x3f, (byte) 0x0f, (byte) 0x18, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0x03, (byte) 0x00, 
                (byte) 0x00, (byte) 0x0f, (byte) 0xc0, (byte) 0x01, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x00, (byte) 0xfc, (byte) 0x03, (byte) 0x00, 
                (byte) 0x0f, (byte) 0x80, (byte) 0xff, (byte) 0x01, (byte) 0xf0, 
                (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x3f, (byte) 0x0f, 
                (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x00, 
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0xc0, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0x9c, 
                (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x80, (byte) 0xff, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
                (byte) 0x1f, (byte) 0x1f, (byte) 0x18, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0x1f, (byte) 0xe0, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x01, (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x1f, 
                (byte) 0x80, (byte) 0xff, (byte) 0x00, (byte) 0xf0, (byte) 0x01, 
                (byte) 0xf0, (byte) 0xff, (byte) 0x01, (byte) 0x3f, (byte) 0x18, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x83, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0x3f, (byte) 0x60, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x03, (byte) 0x0e, (byte) 0x00, 
                (byte) 0x00, (byte) 0x3f, (byte) 0xc0, (byte) 0x01, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x03, (byte) 0xf0, (byte) 0xff, (byte) 0x00, 
                (byte) 0x1f, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x81, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x1f, 
                (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x01, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0xe0, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x01, (byte) 0xf8, 
                (byte) 0x3f, (byte) 0x00, (byte) 0x7f, (byte) 0x0c, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x87, (byte) 0x01, (byte) 0x00, 
                (byte) 0x00, (byte) 0x7f, (byte) 0x30, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0x87, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
                (byte) 0x7f, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
                (byte) 0x07, (byte) 0xbc, (byte) 0x1f, (byte) 0x00, (byte) 0x7d, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xd0, (byte) 0xf7, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7d, (byte) 0x1e, 
                (byte) 0x00, (byte) 0x00, (byte) 0xd0, (byte) 0xf7, (byte) 0x01, 
                (byte) 0x00, (byte) 0x00, (byte) 0xfd, (byte) 0x1f, (byte) 0x00, 
                (byte) 0x00, (byte) 0xd0, (byte) 0x77, (byte) 0x0e, (byte) 0x00, 
                (byte) 0x00, (byte) 0x6a, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
                (byte) 0xa0, (byte) 0xf6, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0xea, (byte) 0x1e, (byte) 0x00, (byte) 0x00, (byte) 0xa0, 
                (byte) 0xfe, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xea, 
                (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0xfe, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x9f, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x79, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x9f, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xf0, (byte) 0x79, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x9f, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
                (byte) 0xf0, (byte) 0xf9, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
                (byte) 0x8a, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xa0, 
                (byte) 0x78, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8a, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x78, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8a, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x78, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x1d, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xd0, (byte) 0x71, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, (byte) 0x1d, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
                (byte) 0xd0, (byte) 0x71, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                (byte) 0x1d, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xd0, 
                (byte) 0x71, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, 
                (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x70, 
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x07, 
                (byte) 0x00, (byte) 0x00, (byte) 0xa0, (byte) 0x70, (byte) 0x00, 
                (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x07, (byte) 0x00, 
                (byte) 0x00, (byte) 0xa0, (byte) 0x70, (byte) 0x00, (byte) 0x00, 
                (byte) 0x00, });
        final int AW = 36;
        final int AH = 33;
        final int XPOS = (SW - duke.getWidth())/2;
        final int YPOS = (SH - duke.getHeight())/2;
        g.setAutoRefresh(false);
        for (int i = 0; i <= SH; i++)
        {
            g.clear();
            g.drawImage(duke, XPOS, YPOS + i - SH, 0);
            g.refresh();
            Delay.msDelay(20);
        }
        Delay.msDelay(1000);
        for (int wavecnt = 0; wavecnt < 3; wavecnt++)
        {
            for (int i = 0; i < 6; i++)
            {
                g.drawRegion(arms, AW * i, 0, AW, AH, 0, XPOS+51, YPOS, 0);
                g.refresh();
                Delay.msDelay(50);
            }
            for (int i = 7 - 1; i >= 0; i--)
            {
                g.drawRegion(arms, AW * i, 0, AW, AH, 0, XPOS+51, YPOS, 0);
                g.refresh();
                Delay.msDelay(50);
            }
            g.drawRegion(duke, 51, 0, AW, AH, 0, XPOS+51, YPOS, 0);
            g.refresh();
            Delay.msDelay(50);
        }

        Delay.msDelay(1000);
        // Remove the image using a split display...
        for (int i = 0; i < SW; i++)
        {
            g.drawRegionRop(duke, 0, 0, SW, SH, XPOS-i, YPOS, 0, 0x55aa00);
            g.drawRegionRop(duke, 0, 0, SW, SH, XPOS+i, YPOS, 0, 0xaa5500);
            g.refresh();
            //Delay.msDelay(20);
        }
        g.setAutoRefresh(true);
        g.refresh();
        Button.waitForAnyPress(DELAY);
    }

	public static void introMessage() {
	
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Graphics Demo", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		g.drawString("Plug a motor into port A and ", 2, 20, 0);
		g.drawString("get ready for a demonstration", 2, 30, 0);
		g.drawString("of various abilities of the ", 2, 40, 0);
		g.drawString("LEGO EV3 brick.", 2, 50, 0);
		//g.drawString("independently controlled", 2, 50, 0);
		//g.drawString("motors connected to motor", 2, 60, 0);
		//g.drawString("ports B and C, and an", 2, 70, 0); 
		//g.drawString("ultrasonic sensor connected", 2, 80, 0);
		//g.drawString("to port 4.", 2, 90, 0);
		  
		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit/2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit+7, 0);
		g.drawLine(0, y_quit,  45, y_quit); // top line
		g.drawLine(0, y_quit,  0, y_quit+height_quit-arc_diam/2); // left line
		g.drawLine(width_quit, y_quit,  width_quit, y_quit+height_quit/2); // right line
		g.drawLine(0+arc_diam/2, y_quit+height_quit,  width_quit-10, y_quit+height_quit); // bottom line
		g.drawLine(width_quit-10, y_quit+height_quit, width_quit, y_quit+height_quit/2); // diagonal
		g.drawArc(0, y_quit+height_quit-arc_diam, arc_diam, arc_diam, 180, 90);
		
		// Enter GUI button:
		g.fillRect(width_quit+10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit+15, y_quit+7, 0,true);
		
		Button.waitForAnyPress();
		if(Button.ESCAPE.isDown()) System.exit(0);
		g.clear();
	}
    
    public static void main(String[] options) throws Exception
    {
    	introMessage();
        GraphicsSample sample = new GraphicsSample();
        sample.splash();
        sample.buttons();
        sample.motors();
        sample.sound();
        sample.leds();
        sample.titles();
        sample.characterSet();
        sample.textAnchors();
        sample.fonts();
        sample.rotatedText();
        //sample.fileImage();
        sample.lines();
        sample.rectangles();
        sample.circles();
        sample.scroll();
        sample.images();
        sample.animation();
        sample.credits();
    }
}
