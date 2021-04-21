package org.iproduct.demos.spring.streamingdemos.udp;

// UDPChatClient.java
// Chat client using UDP communication protocol
// (c) Copyright IPT - Intellectual Products & Technologies Ltd., 2004-2006.
// All rights reserved. This software program can be compiled and modified only as a part of the 
// "Programming in Java" course provided by IPT - Intellectual Products & Technologies Ltd.,
// for educational purposes only, and provided that this copyright notice is kept unchanged 
// with the program. The program is provided "as is", without express or implied warranty of any 
// kind, including any implied warranty of merchantability, fitness for a particular purpose or 
// non-infringement. Should the Source Code or any resulting software prove defective, the user
// assumes the cost of all necessary servicing, repair, or correction. In no event shall 
// IPT - Intellectual Products & Technologies Ltd. be liable to any party under any legal theory 
// for direct, indirect, special, incidental, or consequential damages, including lost profits, 
// business interruption, loss of business information, or any other pecuniary loss, or for
// personal injuries, arising out of the use of this source code and its documentation, or arising 
// out of the inability to use any resulting program, even if IPT - Intellectual Products & 
// Technologies Ltd. has been advised of the possibility of such damage. 
// Contact information: www.iproduct.org, e-mail: office@iproduct.org 

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.Charset;

public class DatagramUtility {
  public static DatagramPacket getDatagramPacket(String s, InetAddress destIA, int destPort) {
    byte[] buf = new byte[0];
    try{
    	buf = s.getBytes("US-ASCII");
    } catch(UnsupportedEncodingException e) {
    	System.err.println("Unsupported encoding");
    }
    return new DatagramPacket(buf, buf.length, 
      destIA, destPort);
  }
  public static String getString(DatagramPacket p){
    return new String(p.getData(), 0, p.getLength(), 
    		Charset.forName("US-ASCII") );
  }
}