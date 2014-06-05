package com.example.pjosd;

import com.example.pjosd.R;

/* Android Package */
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PictureAdjust extends Fragment {
	private final String iTAG = this.getClass().getSimpleName();
	private final String dTAG = "DEBUG:" + iTAG;
	private final String eTAG = "ERR-OCCURRED:";
	public SeekBar sekbarContrast, sekbarBright, sekbarColor, sekbarTint;
	public TextView txtContSekBarVal, txtBrightSekBarVal, txtColorSekBarVal, txtTintSekBarVal;
	private Spinner spinPicMode, spinColTemp;
	byte[] cmd; // For Cedia Command
	private boolean spinFlag;
	
	//== Declare Interface that Implement in MainActivity Class ==//
	public interface PA_onSendCmdListener {
	    public void sendCommand(byte[] command);
	}
	PA_onSendCmdListener sendCommandListener;
	//============================================================//

	
	// Interface has registered on Activity object
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    	Log.i(iTAG, "onAttach");
    	try {
        	sendCommandListener = (PA_onSendCmdListener) activity;
        } catch(ClassCastException e) {
           throw new ClassCastException(activity.toString() + " must implement onSendCommandListener");
        }
    } // End of onAttach
	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(iTAG, "onCreate");
	}  // End of onCreate()
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(iTAG, "onCreateView");
		View view = inflater.inflate(R.layout.fragment_picadj, container, false);
		
		//=== Spinner ======================================= -->>
		// -- Picture Mode -- //
		spinPicMode = (Spinner) view.findViewById(R.id.spinPicMode);
		
		//建立一個ArrayAdapter物件，並且存放下拉式選單的內容
//		ArrayAdapter<String> adapterPicMode = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, 
//		  new String[]{"Natural","Dynamic","user1","user2","user3"});
		ArrayAdapter<String> adapterPicMode = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, new String[]{"Natural","Dynamic","user1","user2","user3"});
		//設定Spinner的樣式
		adapterPicMode.setDropDownViewResource(	R.layout.custom_spinner_item);
		//設定adapter 將剛剛的下拉式選單內容 給這個widget
		spinPicMode.setAdapter(adapterPicMode);
		spinPicMode.setOnItemSelectedListener(PM_spinnerListener);
		
		// -- Color Temp. -- //
		spinColTemp = (Spinner) view.findViewById(R.id.spinColTemp);
		
		//建立一個ArrayAdapter物件，並且存放下拉式選單的內容
		ArrayAdapter<String> adapterColTemp = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, 
				  new String[]{"5500K","6500K","9500K","High Bright","Custom 1","Custom 2","Custom 3"});
		//設定Spinner的樣式
		adapterColTemp.setDropDownViewResource(R.layout.custom_spinner_item);
		//設定adapter 將剛剛的下拉式選單內容 給這個widget
		spinColTemp.setAdapter(adapterColTemp);
		spinColTemp.setOnItemSelectedListener(CL_spinnerListener);		
		//=== End ============================================================ <<--
	
		// Set SeekBar & Value for Contrast
		sekbarContrast = (SeekBar)  view.findViewById(R.id.sekbarContrast);
		sekbarContrast.setMax(100);     // Maximum
		sekbarContrast.setProgress(50); // Default position
		txtContSekBarVal = (TextView) view.findViewById(R.id.txtContVal);
		txtContSekBarVal.setText(String.valueOf(sekbarContrast.getProgress() - 50)); // Set default value
		// Set SeekBar & Value for Brightness
		sekbarBright = (SeekBar)  view.findViewById(R.id.sekbarBright);
		sekbarBright.setMax(100);     // Maximum
		sekbarBright.setProgress(50); // Default position
		txtBrightSekBarVal = (TextView) view.findViewById(R.id.txtBrightVal);
		txtBrightSekBarVal.setText(String.valueOf(sekbarBright.getProgress() - 50)); // Set default value
		// Set SeekBar & Value for Color
		sekbarColor = (SeekBar)  view.findViewById(R.id.sekbarColor);
		sekbarColor.setMax(100);     // Maximum
		sekbarColor.setProgress(50); // Default position
		txtColorSekBarVal = (TextView) view.findViewById(R.id.txtColorVal);
		txtColorSekBarVal.setText(String.valueOf(sekbarColor.getProgress() - 50));   // Set default value
		// Set SeekBar & Value for Tint
		sekbarTint = (SeekBar)  view.findViewById(R.id.sekbarTint);
		sekbarTint.setMax(100);     // Maximum
		sekbarTint.setProgress(50); // Default position
		txtTintSekBarVal = (TextView) view.findViewById(R.id.txtTintVal);
		txtTintSekBarVal.setText(String.valueOf(sekbarTint.getProgress() - 50));     // Set default value
		
		// SeekBar ChangeListener for Contrast -->>
		sekbarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		        @Override
		        public void onStopTrackingTouch(SeekBar arg0) {  // seekbar 結束變更
		        }
		        @Override
		        public void onStartTrackingTouch(SeekBar arg0) { // seekbar 開始變更	
		        }
		        @Override
		        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) { // TODO seekbar 變更期間
		        	int val= arg1 - 50;
		        	byte[] hexVal = {0x30, 0x30, 0x30, 0x30};
		        	txtContSekBarVal.setText(String.valueOf(val));
		        	hexVal = GenHexValue(val);
		        	cmd = new byte[]
		    				{CediaCommand.OPR,                                // 0x21
		    				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
		    				 CediaCommand.PM[0],  CediaCommand.PM[1],         // 0x50 0x4D
		    				 CediaCommand.CN[0],  CediaCommand.CN[1],         // 0x43 0x4E
		    				 hexVal[0], hexVal[1], hexVal[2], hexVal[3],      // Contrast
		    				 CediaCommand.TERM};                              // Terminal
		        	sendCommandListener.sendCommand(cmd); // Send command to Projector
		        } // End of onProgressChanged()
        }); // <<--End of setOnSeekBarChangeListener
		
		// SeekBar ChangeListener for Brightness -->>
		sekbarBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		        @Override
		        public void onStopTrackingTouch(SeekBar arg0) {  // seekbar 結束變更
		        }
		        @Override
		        public void onStartTrackingTouch(SeekBar arg0) { // seekbar 開始變更	
		        }
		        @Override
		        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) { // TODO seekbar 變更期間
		        	int val = arg1 - 50;
		        	txtBrightSekBarVal.setText(String.valueOf(val));
		        	byte[] hexVal = GenHexValue(val);
		        	cmd = new byte[]
		    				{CediaCommand.OPR,                                // 0x21
		    				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
		    				 CediaCommand.PM[0],  CediaCommand.PM[1],         // 0x50 0x4D
		    				 CediaCommand.BR[0],  CediaCommand.BR[1],         // 0x43 0x4E
		    				 hexVal[0], hexVal[1], hexVal[2], hexVal[3],      // Brightness
		    				 CediaCommand.TERM};                              // Terminal
		        	sendCommandListener.sendCommand(cmd); // Send command to Projector	        
		        }  // End of onProgressChanged()
        }); // <<-- End of setOnSeekBarChangeListener for Brightness
				
		// SeekBar ChangeListener for Color -->>
		sekbarColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		        @Override
		        public void onStopTrackingTouch(SeekBar arg0) {  // seekbar 結束變更
		        }
		        @Override
		        public void onStartTrackingTouch(SeekBar arg0) { // seekbar 開始變更	
		        }
		        @Override
		        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) { // TODO seekbar 變更期間
		        	int val = arg1 - 50;
		        	txtColorSekBarVal.setText(String.valueOf(val));
		        	byte[] hexVal = GenHexValue(val);   	
		        	cmd = new byte[]
		    				{CediaCommand.OPR,                                // 0x21
		    				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
		    				 CediaCommand.PM[0],  CediaCommand.PM[1],         // 0x50 0x4D
		    				 CediaCommand.CO[0],  CediaCommand.CO[1],         // 0x43 0x4E
		    				 hexVal[0], hexVal[1], hexVal[2], hexVal[3],      // Color
		    				 CediaCommand.TERM};                              // Terminal 	
		        	sendCommandListener.sendCommand(cmd); // Send command to Projector
		        }  // End of onProgressChanged
        }); // <<-- End of setOnSeekBarChangeListener for Color
		

		
		// SeekBar ChangeListener for Tint -->>
		sekbarTint.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		        @Override
		        public void onStopTrackingTouch(SeekBar arg0) {  // seekbar 結束變更
		        }
		        @Override
		        public void onStartTrackingTouch(SeekBar arg0) { // seekbar 開始變更	
		        }
		        @Override
		        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) { // TODO seekbar 變更期間
		        	int val = arg1 - 50;
		        	txtTintSekBarVal.setText(String.valueOf(val));
		        	byte[] hexVal = GenHexValue(val);		        	
		        	cmd = new byte[]
		    				{CediaCommand.OPR,                                // 0x21
		    				 CediaCommand.UID[0], CediaCommand.UID[1],        // 0x89 0x01
		    				 CediaCommand.PM[0],  CediaCommand.PM[1],         // 0x50 0x4D
		    				 CediaCommand.TI[0],  CediaCommand.TI[1],         // 0x43 0x4E
		    				 hexVal[0], hexVal[1], hexVal[2], hexVal[3],      // Tint
		    				 CediaCommand.TERM};                              // Terminal 	
		        	sendCommandListener.sendCommand(cmd); // Send command to Projector
		        }  // End of onProgressChanged
        }); // <<-- End of setOnSeekBarChangeListener for Tint	
		//don't populate listview,create progress dialog or any other activity related task here
        setRetainInstance(true);
		return view;
	}
	

	@Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        Log.i(iTAG, "onActivityCreated");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(iTAG, "onStart");
    }


    @Override
    public void onResume() { 
        super.onResume();
        Log.i(iTAG, "onResume");
    }


    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
        super.onPause();
        Log.i(iTAG, "onPause");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(iTAG, "onStop");
    }


    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        Log.i(iTAG, "onSaveinstanceState");
    }
   
    
   

  
    //=======================================================================
	
    
    
	/* Picture Mode Spinner Widget Listener */
	private Spinner.OnItemSelectedListener PM_spinnerListener = new Spinner.OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?>adapterView, View v, int position, long id) {
			// TODO Auto-generated method stub
			byte[] val = new byte[2];
			String tmpMsg = adapterView.getSelectedItem().toString();
			if(spinPicMode.isDirty() != false){
				Log.i(dTAG, "PM_spinnerListener : position=" + position + "; id=" + id);
				switch (tmpMsg){
					//String[]{"Natural","Dynamic","user1","user2","user3"});
					case "Natural":  val[0] = 0x30; val[1] = 0x33;
					case "Dynamic":	 val[0] = 0x30; val[1] = 0x35;
					case "user1": 	 val[0] = 0x30; val[1] = 0x43;
					case "user2":    val[0] = 0x30; val[1] = 0x44;
					case "user3": 	 val[0] = 0x30; val[1] = 0x45;
				} // End of switch case
				cmd = new byte[]
					{CediaCommand.OPR,                        // 0x21
					 CediaCommand.UID[0], CediaCommand.UID[1],// 0x89 0x01
					 CediaCommand.PM[0],  CediaCommand.PM[1], // 0x50 0x4D
					 CediaCommand.PM[0],  CediaCommand.PM[1], // 0x50 0x4D
					 val[0], val[1],                          // PictureMode
					 CediaCommand.TERM};                      // Terminal
				sendCommandListener.sendCommand(cmd); // Send command to Projector
			}

		} // End of onItemSelected()
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "Picture Mode Nothing Selected.", Toast.LENGTH_LONG).show();
		}	
		
	}; // End of PM_spinnerListener


	/* Color Temp. Spinner Widget Listener */
	private Spinner.OnItemSelectedListener CL_spinnerListener = new Spinner.OnItemSelectedListener(){
		//如果被選擇
		@SuppressLint("NewApi")
		public void onItemSelected(AdapterView<?>adapterView, View v, int position, long id){	
			byte value = 0x00;
			String tmpMsg = adapterView.getSelectedItem().toString();
			if(spinColTemp.isDirty()){
				Log.i(dTAG, "CL_spinnerListener : position=" + position + "; id=" + id);
				switch (tmpMsg){
					case "5500K": 		value = 0x30;
					case "6500K":		value = 0x32;
					case "9500K": 		value = 0x38;
					case "High Bright": value = 0x39;
					case "Custom 1": 	value = 0x41;
					case "Custom 2":	value = 0x42;
					case "Custom 3": 	value = 0x43;
				}
				cmd = new byte[]
					{CediaCommand.OPR,                         // 0x21
					 CediaCommand.UID[0], CediaCommand.UID[1], // 0x89 0x01
					 CediaCommand.PM[0],  CediaCommand.PM[1],  // 0x50 0x4D
					 CediaCommand.CL[0],  CediaCommand.CL[1],  // 0x43 0x4C
					 value,                                    // ColorTemp.
					 CediaCommand.TERM};                       // Terminal
				sendCommandListener.sendCommand(cmd); // Send command to Projector
			}
			spinFlag = true;
		}

		// Nothing Selected
		@SuppressLint("NewApi")
		public void onNothingSelected(AdapterView<?>adapterView){
			Toast.makeText(getActivity(), "Color Temp. Nothing Selected.", Toast.LENGTH_LONG).show();
		}
	}; // End of CL_spinnerListener

	

    
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    
    private byte[] GenHexValue(int val){
		byte[] HexVal = {0x30, 0x30, 0x30, 0x30};
		int len, idx= 0;
    	String hexStr;
    	hexStr = Integer.toHexString(Integer.valueOf(val));
    	len = Integer.toHexString(Integer.valueOf(val)).length();
    	for(int i = len; i > 0; i--){
    		if(len == 1){
    			idx = HexVal.length - i;
    		}
    		else if(len == 2){
    			idx = i + 1;
    		}
    		else if(len > 2){
    			if (i == HexVal.length) break;
    			idx = (i - 1) - HexVal.length;
    		}
    		HexVal[idx] = (byte)(Integer.parseInt(Integer.toHexString(hexStr.charAt(i-1)), 16) > 96? Integer.parseInt(Integer.toHexString(hexStr.charAt(i-1)),16) - 32 : Integer.parseInt(Integer.toHexString(hexStr.charAt(i-1)),16));
    	}
    	return HexVal;  	
    }
    
    


	
    
}


