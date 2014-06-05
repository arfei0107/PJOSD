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
public class Installation extends Fragment {

	/**
	 * 
	 */
	public Installation() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_install, container,
				false);
	}
}
