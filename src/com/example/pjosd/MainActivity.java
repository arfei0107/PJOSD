package com.example.pjosd;
/* OSD Package */
import com.example.pjosd.R;
import com.example.pjosd.PictureAdjust;
import com.example.pjosd.PictureAdjust.PA_onSendCmdListener;
import com.example.pjosd.DisplaySetup;
import com.example.pjosd.Function;
import com.example.pjosd.Information;
import com.example.pjosd.Information.IF_onSendCmdListener;
import com.example.pjosd.InputSignal;
import com.example.pjosd.Installation;



/* Java Package */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;



/* Android Package */
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainActivity extends Activity implements PA_onSendCmdListener, IF_onSendCmdListener {
	private final String iTAG = this.getClass().getSimpleName();
	private final String dTAG = "DEBUG:" + iTAG;
	private final String eTAG = "ERR-OCCURRED:" + iTAG;
	public SocketClientService mTcpClient;
	private ArrayList<String> ReceiveCmd;
	int gItem;

	//== Declare Interface that Implement in MainActivity Class ==//
	public interface onReplyData {
	    public void replydata(int item, String[] data);
	}
	onReplyData RD;
	//============================================================//
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
	 	gItem = 0; // Clear global item number
	 	
		ReceiveCmd = new ArrayList<String>();
		new connectTask().execute("");
		
		//ActionBar gets initiated
		ActionBar bar = this.getActionBar();
		//Tell the ActionBar we want to use Tabs.
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Tab:Picture Adjust
		Tab tab = bar.newTab().setText(R.string.frg1_PicAdj).setTabListener(mTabListener);
		bar.addTab(tab);
		// Tab:Input Signal
		tab = bar.newTab().setText(R.string.frg2_IntSig).setTabListener(mTabListener);		
		bar.addTab(tab);
		// Tab:Installation
		tab = bar.newTab().setText(R.string.frg3_Install).setTabListener(mTabListener);
		bar.addTab(tab);
		// Tab:Display setup
		tab = bar.newTab().setText(R.string.frg4_DispSet).setTabListener(mTabListener);
		bar.addTab(tab);
		// Tab:Function
		tab = bar.newTab().setText(R.string.frg5_Func).setTabListener(mTabListener);
		bar.addTab(tab);
		// Tab:Information
		tab = bar.newTab().setText(R.string.frg6_Info).setTabListener(mTabListener);
		bar.addTab(tab);
		
	} // End of onCreate()

	

	protected void onStart(Fragment fragment){
		super.onStart();
	}
	

	 
	 
	private TabListener mTabListener = new TabListener() {

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			//Log.d(TAG, tab.getText() + " is onTabUnselected");
		}

		@SuppressLint("NewApi")
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Fragment fragment = null;

			if(tab.getText() == getString(R.string.frg1_PicAdj)){
				fragment = new PictureAdjust();
			}
			else if(tab.getText() == getString(R.string.frg2_IntSig)){
				fragment = new InputSignal();
			}
			else if(tab.getText() == getString(R.string.frg3_Install)){
				fragment = new Installation();
			}
			else if(tab.getText() == getString(R.string.frg4_DispSet)){
				fragment = new DisplaySetup();
			}
			else if(tab.getText() == getString(R.string.frg5_Func)){
				fragment = new Function();
			}
			else if(tab.getText() == getString(R.string.frg6_Info)){
				
				fragment = new Information();

			    try {
			    	RD = (onReplyData) fragment;
			    } catch(ClassCastException e) {
			    	throw new ClassCastException(fragment.toString() + " must implement onReplyData");
			    }
	    
			}
			
		
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
			transaction.replace(R.id.layout_fragment, fragment);
            transaction.commit();
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			//Log.d(TAG, tab.getText() + " is onTabReselected");
		}
	};
	
		
	@Override  // For Select Item
	//public void sendCommand(String type, byte[] value) {
	public void sendCommand(byte[] command) {
//		byte[] cmd = {};
		// TODO Auto-generated method stub
		try {
//			if(type.equals("CL")){ // Color Temperature
//				//cmd : 
//				           // 0x0A
//			}
//			else if(type.equals("CS")){
//				
//			}
//			for(int i = 0; i < command.length; i++){
//				Log.e("SF", "COM[" + i + "]=" + (byte)command[i]);
//			}
			
			
			
			mTcpClient.out.write(command);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class connectTask extends AsyncTask<String,String,SocketClientService> {
		  
		@Override
	    protected SocketClientService doInBackground(String... message) {
			//we create a SocketClientService object and
	        mTcpClient = new SocketClientService(new SocketClientService.OnMessageReceived() {
	        	@Override
	            //here the messageReceived method is implemented
	            public void messageReceived(String message) {
	        		//this method calls the onProgressUpdate
	        		publishProgress(message);
	        		
	        	}
	        });
	        mTcpClient.run();
	
	        return null;
		}
	
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			String[] repdata;
			
			//in the arrayList we add the messaged received from server
			//Log.e("SF", "values[0]=" + );
			ReceiveCmd.add(values[0]);
			if(ReceiveCmd.indexOf("50") == 0       //'P'
				&& ReceiveCmd.indexOf("4a") == 1   //'J'
				&& ReceiveCmd.indexOf("5f") == 2   //'_'
				&& ReceiveCmd.indexOf("4f") == 3   //'O'
				&& ReceiveCmd.indexOf("4b") == 4){ //'K'   	   
	    	   try {
	    		   mTcpClient.out.write(CediaCommand.PJREQ); // Reply "PJREQ"
	    		   ReceiveCmd.clear();
	    	   } catch (IOException e) {
	    		   // TODO Auto-generated catch block
	    		   e.printStackTrace();
	    	   }
	    	   ReceiveCmd.clear();
	        }
	        else if(values[0].equals(Integer.toHexString(CediaCommand.TERM))){ // Cmd = 0x0A (Terminal) 	
	        	//Log.e("SF", "ReceiveCmd=" + ReceiveCmd);
	        	if(ReceiveCmd.get(0).equals(Integer.toHexString(CediaCommand.ACK))){
	        		//byte[] t = {0x30, 0x31, 0x32};
	        		//RD.replydata(t);
	        		//Information info = (Information) getFragmentManager().findFragmentById(R.id.fragment_info);
	        	}

	        	if(ReceiveCmd.get(0).equals(Integer.toHexString(CediaCommand.ANS))){  // Receive Information
	        	   if(ReceiveCmd.get(3).equals(Integer.toHexString(CediaCommand.IF[0])) &&
	        		  ReceiveCmd.get(4).equals(Integer.toHexString(CediaCommand.IF[1]))){

	        		   //Log.e("SF", "Cnt = " + (ReceiveCmd.indexOf(Integer.toHexString(CediaCommand.TERM)) - 5));
	        		   repdata = new String[ReceiveCmd.indexOf(Integer.toHexString(CediaCommand.TERM)) - 5];
	        		   
	        		   for(int i = 0; i < ReceiveCmd.indexOf(Integer.toHexString(CediaCommand.TERM)) - 5; i++){
	        			   repdata[i] = ReceiveCmd.get(5+i).toString();
	        			   //-----------------------------------------------------------------//
	        			   //ByteBuffer bb = ByteBuffer.wrap(ReceiveCmd.get(5+i).getBytes()); //
	        			   //Log.e("SF", "getFloat()" + bb.getInt() );
	        			   //repdata[i] = (byte)bb.getInt();                                  //
	        			   //-----------------------------------------------------------------//
	        			   //Log.e("SF", "repdata[" + i + "]=" + repdata[i]); 
	        		   } 
	        		   RD.replydata(gItem++, repdata);  
	        	   }     	
	        	} // End of ANS    	
	        	ReceiveCmd.clear();
	        }
			
	    }
	   
	} // End of connectTask()
	
	
	
}

