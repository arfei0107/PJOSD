/**
 * 
 */
package com.example.pjosd5;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pjosd5.MainActivity.onReplyData;
import com.example.pjosd5.R;



/**
 * @author shengfei.huang
 *
 */
public class Information extends Fragment implements onReplyData {
	private final String iTAG = this.getClass().getSimpleName();
	private final String dTAG = "DEBUG:" + iTAG;
	private final String eTAG = "ERR-OCCURRED:";
	byte[] cmd;
	final static String[][] INPUT_PORT = {{"0x32", "COMP"}, {"0x33", "PC"}, {"0x36", "HDMI-1"}, {"0x37", "HDMI-2"}};
	final static String[][] INPUT_SOURCE = {{"0x41", "1080p 60"}, {"0x42", "No Signal"}, 
											{"0x43", "720p 3D"}, {"0x44", "1080i 30"}, 
											{"0x45", "1080p 3D"}};
	final static String[][] DEEP_COLOR = {{"0x30", "8bit"}, {"0x31", "10bit"}, {"0x32", "12bit"}};
	public static View view;
	public TextView tvInput;
	public static TextView tvInputVal;
	public TextView tvSource;
	public TextView tvSourceVal;
	public TextView tvDeepColor;
	public TextView tvDeepColorVal; 
	public TextView tvLampTime;
	public TextView tvLampTimeVal;
	public TextView tvSoftVer;
	public TextView tvSoftVerVal;
	public TextView tvPSVer;
	public TextView tvPSVerVal;
	public TextView tvUnitID;
	public TextView tvUnitIDVal;

	//== Declare Interface that Implement in MainActivity Class ==//
	public interface IF_onSendCmdListener {
		public void sendCommand(byte[] command);
	}
	IF_onSendCmdListener sendCommandListener;
	//============================================================//
	
	/**
	 *   
	 */
	public Information() {
		// TODO Auto-generated constructor stub
	}

	// Interface has registered on Activity object
    public void onAttach(Activity activity){
    	super.onAttach(activity);
        try {
        	sendCommandListener = (IF_onSendCmdListener) activity;
        } catch(ClassCastException e) {
           throw new ClassCastException(activity.toString() + " must implement onSendCommandListener");
        }
    } // End of onAttach	
    
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        if (null != saved) {
            // Restore state here
        }
        //Log.i(TAG, "onCreate");
    } // End of onCreate()
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/* Input Port */
		//Log.e(TAG, "Send Information - Input Port Command!");
		view = inflater.inflate(R.layout.fragment_info, container, false);
		tvInput = (TextView) view.findViewById(R.id.Input);
		tvInputVal = (TextView) view.findViewById(R.id.InputVal);
		//-- Input
		cmd = new byte[]
				{CediaCommand.REF,                                // 0x3F
				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
				 CediaCommand.IF[0],  CediaCommand.IF[1],         // 0x49 0x46
				 CediaCommand.IN[0],  CediaCommand.IN[1],         // 0x49 0x4E
				 CediaCommand.TERM};                              // Terminal
    	try {
			Thread.sleep(100);
			sendCommandListener.sendCommand(cmd);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/* Input Source */
    	//Log.e(TAG, "Send Information - Source Command!");
		tvSource = (TextView) view.findViewById(R.id.Source);
		tvSourceVal = (TextView) view.findViewById(R.id.SourceVal);
		cmd = new byte[]
				{CediaCommand.REF,                                // 0x3F
				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
				 CediaCommand.IF[0],  CediaCommand.IF[1],         // 0x49 0x46
				 CediaCommand.IS[0],  CediaCommand.IS[1],         // 0x49 0x53
				 CediaCommand.TERM};                              // Terminal
    	try {
			Thread.sleep(100);
			sendCommandListener.sendCommand(cmd);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/* Deep Color */
    	//Log.e(TAG, "Send Information - Deep Color Command!");
		tvDeepColor = (TextView) view.findViewById(R.id.DeppColor);
		tvDeepColorVal = (TextView) view.findViewById(R.id.DeppColorVal);
		cmd = new byte[]
				{CediaCommand.REF,                                // 0x3F
				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
				 CediaCommand.IF[0],  CediaCommand.IF[1],         // 0x49 0x46
				 CediaCommand.DC[0],  CediaCommand.DC[1],         // 0x44 0x43
				 CediaCommand.TERM};                              // Terminal
    	try {
			Thread.sleep(100);
			sendCommandListener.sendCommand(cmd);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Log.e(TAG, "Send Information - Lamp Time Command!");
		tvLampTime = (TextView) view.findViewById(R.id.LampTime);
		tvLampTimeVal = (TextView) view.findViewById(R.id.LampTimeVal);
		cmd = new byte[]
				{CediaCommand.REF,                                // 0x3F
				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
				 CediaCommand.IF[0],  CediaCommand.IF[1],         // 0x49 0x46
				 CediaCommand.LT[0],  CediaCommand.LT[1],         // 0x4C 0x54
				 CediaCommand.TERM};                              // Terminal
    	
    	try {
			Thread.sleep(100);
			sendCommandListener.sendCommand(cmd);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/* Software Version */
    	//Log.e(TAG, "Send Information - Soft Ver. Command!"); 
		tvSoftVer = (TextView) view.findViewById(R.id.SoftVer);
		tvSoftVerVal = (TextView) view.findViewById(R.id.SoftVerVal);
		cmd = new byte[]
				{CediaCommand.REF,                                // 0x3F
				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
				 CediaCommand.IF[0],  CediaCommand.IF[1],         // 0x49 0x46
				 CediaCommand.SV[0],  CediaCommand.SV[1],         // 0x53 0x56
				 CediaCommand.TERM};                              // Terminal
    		
    	try {
			Thread.sleep(100);
			sendCommandListener.sendCommand(cmd);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/* PS version */
		tvPSVer = (TextView) view.findViewById(R.id.PSVer);
		tvPSVerVal = (TextView) view.findViewById(R.id.PSVerVal);
		/* Unit ID */
		tvUnitID = (TextView) view.findViewById(R.id.UnitID);
		tvUnitIDVal = (TextView) view.findViewById(R.id.UnitIDVal);
		// To gain Infomation Data
		
		return view;
	}



    
	public void replydata(int item, String[] data) {
		// TODO Auto-generated method stub
		int i;
		String ascii = "";
		//Log.e(TAG, "item = " + item%5);
		
		switch (item%5){
			case 0:{  // Input Value
				//Log.e(TAG, "0x" + data[0]);
				for(i = 0; i < INPUT_PORT.length; i++){
					if(INPUT_PORT[i][0].equals("0x" + data[0])){
						tvInputVal.setText(INPUT_PORT[i][1]);
					}
				}
				break;
			}
			case 1:{  // Source
				for(i = 0; i < INPUT_SOURCE.length; i++){
					if(INPUT_SOURCE[i][0].equals("0x" + data[0])){
						tvSourceVal.setText(INPUT_SOURCE[i][1]);
					}
				}
				break;
			}
			case 2:{ // Deep Color
				//Log.e(TAG, "0x" + data[0]);
				for(i = 0; i < DEEP_COLOR.length; i++){
					if(DEEP_COLOR[i][0].equals("0x" + data[0])){
						tvDeepColorVal.setText(DEEP_COLOR[i][1]);
						break;
					}
				}
				break;
			}
			
			case 3:{  // Lamp Time
				for(i = 0; i < data.length; i++){
					//Log.e(TAG, "Lamp Time Data[" + i + "]=" + data[i]);				
					ascii = ascii + utility.hexToASCII(data[i]);
				}
				tvLampTimeVal.setText(String.valueOf(Integer.parseInt(ascii, 16)) + " H");
				break;
			}
			case 4:{  // Soft Ver.			
				for(i = 0; i < data.length; i++){
					ascii = ascii + utility.hexToASCII(data[i]);
				}
				tvSoftVerVal.setText(ascii + "31");
				break;
			}
		}  // End of switch (item%5);
	} // End of replydata
    

}
