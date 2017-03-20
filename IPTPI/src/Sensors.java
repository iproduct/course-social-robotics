
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Sensors {
	public static final int GYRO_NUM_BYTES_READ = 6; // X, Y, Z axes - 2 bytes each
	
	public static final byte 
		D_OUT_X_L_M       = 0x08,
		D_OUT_X_H_M       = 0x09,
		D_OUT_Y_L_M       = 0x0A,
		D_OUT_Y_H_M       = 0x0B,
		D_OUT_Z_L_M       = 0x0C,
		D_OUT_Z_H_M       = 0x0D;

    
    I2CBus bus;
    I2CDevice gyro, compassAccel;
    byte[] bytes;
    float accelX_G;
    float accelY_G;
    float accelZ_G;
    double gyroXdeg;
    double gyroYdeg;
    double gyroZdeg;
    static int SENSITIVITY = 16384;  //sensor sencitivity
    
    /**
     * @param args
     * @throws IOException 
     */
    public Sensors() throws IOException {
        System.out.println("Starting sensors reading:");
        
        // get I2C bus instance
        
        //...
        //get i2c bus
        bus = I2CFactory.getInstance(I2CBus.BUS_1);
        System.out.println("Connected to bus OK!");

        //get device itself
        gyro = bus.getDevice(0x6b); //Gyroscope L3G4200D
        System.out.println("Connected to Gyroscope device OK!");

        //start sensing, using config registries 0x23 and 0x20    
        gyro.write(0x23, (byte)0x20); // sensitivity 2000dps
        gyro.write(0x20, (byte)0x0f); // normal mode, enable X, Y, Z
        System.out.println("Configuring Gyroscope device OK!");
        
        compassAccel = bus.getDevice(0x1d); //Compass and Accelerometer LSM303D
        System.out.println("Connected to Compass and Accelerometer devices OK!");
        
        //Accelerometer

        //start sensing, using config registries 0x23 and 0x20    
        // 0x00 = 0b00000000
        // AFS = 0 (+/- 2 g full scale)
        compassAccel.write(0x21, (byte)0x00); // AFS = 0 (+/-2g)
        
        // 0x57 = 0b01010111
        // AODR = 0101 (50 Hz ODR); AZEN = AYEN = AXEN = 1 (all axes enabled)
        compassAccel.write(0x20, (byte)0x57); // normal mode, enable X, Y, Z
        
        System.out.println("Configuring Compass device OK!");


        // Magnetometer

        // 0x64 = 0b01100100
        // M_RES = 11 (high resolution mode); M_ODR = 001 (6.25 Hz ODR)
        compassAccel.write(0x24, (byte)0x64);

        // 0x20 = 0b00100000
        // MFS = 01 (+/- 4 gauss full scale)
        compassAccel.write(0x25, (byte)0x20);

        // 0x00 = 0b00000000
        // MLP = 0 (low power mode off); MD = 00 (continuous-conversion mode)
        compassAccel.write(0x26, (byte)0x00);
        
        System.out.println("Configuring Accelerometer device OK!");

//            //config gyro
//            device.write(0x1B, (byte) 0b00011000);
//            //config accel    
//            device.write(0x1C, (byte) 0b00000100);
//            System.out.println("Configuring sensors OK!");

     
    }
    
    public void readMag() throws IOException{// чете данните за X, Y и Z от жироскопа
    	int magX, magY, magZ;
    	short accelX, accelY, accelZ;
        float tempX, tempY, tempZ;
        short temp;

        
        compassAccel.write(D_OUT_X_L_M, (byte) (1 << 7)); // autoincrement address
    	
//    	bytes = new byte[GYRO_NUM_BYTES_READ]; // X, Y, Z axes - 2 bytes each
//    	int r = compassAccel.read(D_OUT_X_L_M, bytes, 0, bytes.length);
//
//        if (r != GYRO_NUM_BYTES_READ) {   //6 registries to be read for gyro
//            System.out.println("Error reading data, < " + bytes.length + " bytes");
//        }
//        System.out.println("Magnetometer bytes read: " + Arrays.toString(bytes));
    	
//    	DataInputStream gyroIn = new DataInputStream(new ByteArrayInputStream(bytes));
    	
//    	accelX = gyroIn.readShort();
//        accelY = gyroIn.readShort();
//        accelZ = gyroIn.readShort();
//        temp = gyroIn.readShort();
//        gyroX = (gyroIn.read() | (gyroIn.read() << 8));
//        gyroY = gyroIn.read(); gyroIn.read();
//        gyroZ = gyroIn.read(); gyroIn.read();
//        System.out.println("Gyro (X, Y , Z) = (" + gyroX + ", " + gyroY + ", "+ gyroZ + ")");
//       
//    	

        int xlm, xhm, ylm, yhm, zlm, zhm;
        // D: X_L, X_H, Y_L, Y_H, Z_L, Z_H
        xlm = compassAccel.read(D_OUT_X_L_M);
        xhm = compassAccel.read(D_OUT_X_H_M);
        ylm = compassAccel.read(D_OUT_Y_L_M);
        yhm = compassAccel.read(D_OUT_Y_H_M);
        zlm = compassAccel.read(D_OUT_Z_L_M);
        zhm = compassAccel.read(D_OUT_Z_H_M);
        
    	if (xlm >= 0 && xhm >= 0) {
    		magX = ((xhm << 8) | xlm);// 16 бита за X
    		System.out.println("Gyro X = " + magX + ", byres: " + xhm + " " + xlm );
    	} else {
    		System.out.println("Error reading Giro bytes: " + xhm + " " + xlm);
    	}
//
//    	int yMSB = gyro.read(0x2B);
//    	int yLSB = gyro.read(0x2A);
//    	if (yMSB >= 0 && yLSB >= 0) {
//    		gyroY = ((yMSB << 8) | yLSB);// 16 бита за Y
//    		System.out.println("Gyro Y = " + gyroY + ", byres: " + yMSB + " " + yLSB );
//    	} else {
//    		System.out.println("Error reading Giro bytes: " + yMSB + " " + yLSB);
//    	}
//
//    	int zMSB = gyro.read(0x2D);
//    	int zLSB = gyro.read(0x2C);
//    	if (zMSB >= 0 && zLSB >= 0) {
//    		gyroZ = ((zMSB << 8) | zLSB);// 16 бита за Z
//    		System.out.println("Gyro Z = " + gyroZ + ", byres: " + zMSB + " " + zLSB );
//    	} else {
//    		System.out.println("Error reading Giro bytes: " + zMSB + " " + zLSB);
//    	}
//
//        gyroXdeg = gyroX * (180d / (double) Short.MAX_VALUE);
//        gyroYdeg = gyroY * (180d / (double) Short.MAX_VALUE);
//        gyroZdeg = gyroZ * (180d / (double) Short.MAX_VALUE);
//        System.out.println("Gyro [Deg] (X, Y , Z) = (" + gyroXdeg + ", " + gyroYdeg + ", "+ gyroZdeg + ")");
//
    }
    
    public void readGyro() throws IOException{// чете данните за X, Y и Z от жироскопа
    	short accelX, accelY, accelZ;
        float tempX, tempY, tempZ;
        short temp;
        int gyroX, gyroY, gyroZ;
        
    	gyro.write((byte)0x28, (byte) (1 << 7)); // autoincrement address ad Gyro
    	
    	bytes = new byte[GYRO_NUM_BYTES_READ]; // X, Y, Z axes - 2 bytes each
    	int r = gyro.read((byte)0x28, bytes, 0, bytes.length);

        if (r != GYRO_NUM_BYTES_READ) {   //6 registries to be read for gyro
            System.out.println("Error reading data, < " + bytes.length + " bytes");
        }
        System.out.println("Bytes read: " + Arrays.toString(bytes));
    	
    	DataInputStream gyroIn = new DataInputStream(new ByteArrayInputStream(bytes));
    	
//    	accelX = gyroIn.readShort();
//        accelY = gyroIn.readShort();
//        accelZ = gyroIn.readShort();
//        temp = gyroIn.readShort();
        gyroX = (gyroIn.read() | (gyroIn.read() << 8));
        gyroY = gyroIn.read(); gyroIn.read();
        gyroZ = gyroIn.read(); gyroIn.read();
        System.out.println("Gyro (X, Y , Z) = (" + gyroX + ", " + gyroY + ", "+ gyroZ + ")");
       
    	
    	gyro.write((byte)0x28, (byte) (1 << 7));
    	int xMSB = gyro.read(0x29);
    	int xLSB = gyro.read(0x28);
    	if (xMSB >= 0 && xLSB >= 0) {
    		gyroX = ((xMSB << 8) | xLSB);// 16 бита за X
    		System.out.println("Gyro X = " + gyroX + ", byres: " + xMSB + " " + xLSB );
    	} else {
    		System.out.println("Error reading Giro bytes: " + xMSB + " " + xLSB);
    	}

    	int yMSB = gyro.read(0x2B);
    	int yLSB = gyro.read(0x2A);
    	if (yMSB >= 0 && yLSB >= 0) {
    		gyroY = ((yMSB << 8) | yLSB);// 16 бита за Y
    		System.out.println("Gyro Y = " + gyroY + ", byres: " + yMSB + " " + yLSB );
    	} else {
    		System.out.println("Error reading Giro bytes: " + yMSB + " " + yLSB);
    	}

    	int zMSB = gyro.read(0x2D);
    	int zLSB = gyro.read(0x2C);
    	if (zMSB >= 0 && zLSB >= 0) {
    		gyroZ = ((zMSB << 8) | zLSB);// 16 бита за Z
    		System.out.println("Gyro Z = " + gyroZ + ", byres: " + zMSB + " " + zLSB );
    	} else {
    		System.out.println("Error reading Giro bytes: " + zMSB + " " + zLSB);
    	}

        gyroXdeg = gyroX * (180d / (double) Short.MAX_VALUE);
        gyroYdeg = gyroY * (180d / (double) Short.MAX_VALUE);
        gyroZdeg = gyroZ * (180d / (double) Short.MAX_VALUE);
        System.out.println("Gyro [Deg] (X, Y , Z) = (" + gyroXdeg + ", " + gyroYdeg + ", "+ gyroZdeg + ")");

    	}

    public void startReading() {
//        Task task = new Task<Void>() {
//            @Override
//            public Void call() {
//                try {
//                    readingSensors();
//                } catch (IOException e) {
//                }
//                return null;
//            }
//        };
//        new Thread(task).start();
    }

    private void readingSensors() throws IOException {
        bytes = new byte[6 + 2 + 6];
        DataInputStream gyroIn;
        short accelX, accelY, accelZ;
        float tempX, tempY, tempZ;
        short temp;
        short gyroX, gyroY, gyroZ;

        while (true) {
            int r = gyro.read(0x3B, bytes, 0, bytes.length);
     
            if (r != 14) {   //14 registries to be read, 6 for gyro, 6 for accel and 2 for temp
                System.out.println("Error reading data, < " + bytes.length + " bytes");
            }
            gyroIn = new DataInputStream(new ByteArrayInputStream(bytes));
            accelX = gyroIn.readShort();
            accelY = gyroIn.readShort();
            accelZ = gyroIn.readShort();
            temp = gyroIn.readShort();
            gyroX = gyroIn.readShort();
            gyroY = gyroIn.readShort();
            gyroZ = gyroIn.readShort();
     
            tempX = (float) accelX / SENSITIVITY;
            //Anything higher than 1 or lower than -1 is ignored
            accelX_G = (tempX > 1) ? 1 : ((tempX < -1) ? -1 : tempX);
            tempY = (float) accelY / SENSITIVITY;
            //Anything higher than 1, or lower than 01 is ignored
            accelY_G = (tempY > 1) ? 1 : ((tempY < -1) ? -1 : tempY);
            tempZ = ((float) accelZ / SENSITIVITY) * (-1); //sensor upsidedown, opposite value used
            accelZ_G = (tempZ > 1) ? 1 : ((tempZ < -1) ? -1 : tempZ);

//use accel data as desired...            
            
            gyroXdeg = gyroX * (2000d / (double) Short.MAX_VALUE);
            gyroYdeg = gyroY * (2000d / (double) Short.MAX_VALUE);
            gyroZdeg = gyroZ * (2000d / (double) Short.MAX_VALUE);

//Use the gyro values as desired..            

            double tempC = ((double) temp / 340d) + 35d;

            try {
                Thread.sleep(700);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sensors.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
    	Sensors s = new Sensors();
    	for (int i = 1; i < 100; i ++){
//    		s.readGyro();
    		s.readMag();
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
		
	}
//...

}