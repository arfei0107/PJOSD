/**
 * 
 */
package com.example.pjosd;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pjosd.R;

/**
 * @author shengfei.huang
 *
 */
public class DisplaySetup extends Fragment{

	/**
	 * 
	 */
	public DisplaySetup() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_dispset, container,
				false);
	}

}
