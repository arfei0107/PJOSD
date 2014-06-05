package com.example.pjosd5;

import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClientService {
    private String serverMessage;
	//private byte serverMessage;
    public static final String SERVERIP = "192.168.1.2"; //your computer IP address
    public static final int SERVERPORT = 20554;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    public OutputStream outToServer;
    public DataOutputStream out;
    public InputStream inFromServer;
    public DataInputStream in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public SocketClientService(OnMessageReceived listener) {
       mMessageListener = listener;
    }
    public SocketClientService() {
       
    }
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void send(byte command[]) throws IOException{
    	
        //if (out != null){ //&& !out.checkError()) {
            //out.writeChar(message);
			//out.writeChars(message);
//        	for(int i = 0; i < 5; i++){
//        		Log.e("SF", "message[" + i + "]=" + Integer.toHexString(command[i]));
//        	}
//        	out.write(command);
			//out.flush();

        //}
        
    }
 
    public void stopClient(){
        mRun = false;
    }

    public void run() {
    	 
        mRun = true;
      
        
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Log.e("SF", "C: Connecting...");
 
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
            
            try {
                //send the message to the server
                //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //out = new PrintWriter(new BufferedWriter(new OutputStream(socket.getOutputStream())), true);
            	
            	outToServer = socket.getOutputStream();
            	out = new DataOutputStream(outToServer);
            	
            	
                Log.e("TCP Client", "C: Sent.");
           	
                Log.e("TCP Client", "C: Done.");
                
//                //receive the message which the server sends back
                //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));                   
                inFromServer = socket.getInputStream();
                in = new DataInputStream(inFromServer);
                
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                        //serverMessage = in.readLine().toString();  //-- Original
                        
                        //Log.e("SF", "serverMessage=" + serverMessage);
                		serverMessage = Integer.toHexString(in.readByte());
                		//Log.e("SF", "serverMessage=" + serverMessage);
                		if (serverMessage != null  && mMessageListener != null) {
                            //call the method messageReceived from MyActivity class
                            
                			mMessageListener.messageReceived(serverMessage);
                        }
                        serverMessage = null;
                }
                
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 



 
            } catch (Exception e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
 
        } catch (Exception e) {
 
            Log.e("TCP", "C: Error", e);
 
        }
 
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
    
}
